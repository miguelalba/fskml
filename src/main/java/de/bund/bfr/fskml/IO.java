package de.bund.bfr.fskml;

import de.bund.bfr.fskml.sedml.SourceScript;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom2.JDOMException;
import org.jlibsedml.*;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class IO {

    static final URI SEDML_URI = URI.create("http://identifiers.org/combine.specifications/sed-ml");

    static FSKXArchive readArchive(File file) throws CombineArchiveException, ParseException, IOException, XMLException, org.jdom2.JDOMException {

        Simulations sim = null;

        try (CombineArchive archive = new CombineArchive(file)) {
            if (archive.hasEntriesWithFormat(SEDML_URI)) {
                ArchiveEntry simulationEntry = archive.getEntriesWithFormat(SEDML_URI).get(0);
                File tempFile = File.createTempFile("simulation", ".sedml");
                simulationEntry.extractFile(tempFile);

                sim = readSimulations(tempFile);

                tempFile.delete();
            }
        }

        return new FSKXArchiveImpl(sim);
    }

    static void writeArchive(FSKXArchive archive, File file, String scriptExtension) throws JDOMException, CombineArchiveException, ParseException, IOException, TransformerException {

        try (CombineArchive combineArchive = new CombineArchive(file)) {

            // Add simulations as SEDML
            File sedmlFile = File.createTempFile("simulation", ".sedml");
            SEDMLDocument doc = createSedml(archive.getSimulations(), scriptExtension);
            doc.writeDocument(sedmlFile);

            combineArchive.addEntry(sedmlFile, "simulation.sedml", SEDML_URI);

            sedmlFile.delete();

            combineArchive.pack();
        }
    }

    private static Simulations readSimulations(File sedmlFile) throws XMLException {

        SedML sedml = Libsedml.readDocument(sedmlFile).getSedMLModel();

        // Get selected index
        final int selectedIndex;
        if (sedml.getAnnotation().size() == 1) {
            Annotation annotation = sedml.getAnnotation().get(0);
            Text e = (Text) annotation.getAnnotationElement().getContent().get(0);
            selectedIndex = Integer.parseInt(e.getText());
        } else {
            selectedIndex = 0;
        }

        List<String> outputs = sedml.getDataGenerators().stream().map(DataGenerator::getId).collect(Collectors.toList());

        Map<String, Map<String, String>> values = new HashMap<>(sedml.getModels().size());
        for (Model model : sedml.getModels()) {
            Map<String, String> simulation = model.getListOfChanges().stream().filter(change -> change.getChangeKind().equals(SEDMLTags.CHANGE_ATTRIBUTE_KIND))
                    .map(change -> (ChangeAttribute) change).collect(Collectors.toMap(change -> change.getTargetXPath().toString(), ChangeAttribute::getNewValue));
            values.put(model.getId(), simulation);
        }

        return new SimulationsImpl(selectedIndex, outputs, values);
    }

    private static SEDMLDocument createSedml(Simulations simulations, String scriptExtension) {

        SEDMLDocument doc = Libsedml.createDocument();
        SedML sedml = doc.getSedMLModel();

        final String uri = "https://iana.org/assignments/mediatypes/text/x-" + scriptExtension;

        // Add outputs as data generators
        for (String id : simulations.getOutputs()) {
            sedml.addDataGenerator(new DataGenerator(id, "", Libsedml.parseFormulaString(id)));
        }

        // Add simulation
        SteadyState simulation = new SteadyState("steadyState", "", new Algorithm(" "));
        simulation.addAnnotation(new Annotation(new SourceScript(uri, "model." + scriptExtension)));
        sedml.addSimulation(simulation);

        // Add selected simulation index
        Element selectedSimulationElement = new Element("selectedSimulation");
        selectedSimulationElement.setAttribute("id", Integer.toString(simulations.getSelectedIndex()));
        sedml.addAnnotation(new Annotation(selectedSimulationElement));

        for (Map.Entry<String, Map<String, String>> simulationEntry : simulations.getInputValues().entrySet()) {

            // Add model
            Model sedmlModel = new Model(simulationEntry.getKey(), "", uri, "model." + scriptExtension);
            sedml.addModel(sedmlModel);

            // Add task
            sedml.addTask(new Task("task" + sedml.getTasks().size(), "", sedmlModel.getId(), simulation.getId()));

            // Add changes to model
            for (Map.Entry<String, String> entry : simulationEntry.getValue().entrySet()) {
                String parameterName = entry.getKey();
                String parameterValue = entry.getValue();

                ChangeAttribute change = new ChangeAttribute(new XPathTarget(parameterName), parameterValue);
                sedmlModel.addChange(change);
            }
        }

        // Add plot
        {
            SourceScript ss = new SourceScript(uri, "visualization." + scriptExtension);
            Plot2D plot = new Plot2D("plot1", "");
            plot.addAnnotation(new Annotation(ss));
            sedml.addOutput(plot);
        }

        return doc;
    }
}

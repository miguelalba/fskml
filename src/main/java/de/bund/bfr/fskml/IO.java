package de.bund.bfr.fskml;

import de.bund.bfr.fskml.sedml.SelectedSimulation;
import de.bund.bfr.fskml.sedml.SourceScript;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jlibsedml.*;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class IO {

    static final URI SEDML_URI = URI.create("http://identifiers.org/combine.specifications/sed-ml");
    static final URI JSON_URI = URI.create("https://www.iana.org/assignments/media-types/application/json");
    static final URI PLAIN_URI = URI.create("http://purl.org/NET/mediatypes/text-xplain");

    static FSKXArchive readArchive(File file) throws CombineArchiveException, ParseException, IOException, XMLException, org.jdom2.JDOMException, DataConversionException {

        Simulations sim = null;
        Packages pack = null;
        String readme = "";
        String version = "";

        try (CombineArchive archive = new CombineArchive(file)) {

            if (archive.hasEntriesWithFormat(SEDML_URI)) {
                ArchiveEntry simulationEntry = archive.getEntriesWithFormat(SEDML_URI).get(0);
                File tempFile = File.createTempFile("simulation", ".sedml");
                simulationEntry.extractFile(tempFile);

                sim = readSimulations(tempFile);
                tempFile.delete();
            }

            if (archive.hasEntriesWithFormat(JSON_URI)) {
                ArchiveEntry packageEntry = archive.getEntriesWithFormat(JSON_URI).get(0);
                File tempFile = File.createTempFile("packages", ".json");
                packageEntry.extractFile(tempFile);

                pack = readPackages(tempFile);
                tempFile.delete();
            }

            if (archive.hasEntriesWithFormat(PLAIN_URI)) {
                for (ArchiveEntry entry : archive.getEntriesWithFormat(PLAIN_URI)) {
                    // README entry is the only entry with PLAIN_URI and a description
                    if (entry.getDescriptions().size() > 0) {
                        Path tempFile = Files.createTempFile("README", ".txt");
                        try {
                            entry.extractFile(tempFile.toFile());
                            readme = Files.readAllLines(tempFile).get(0);
                        } finally {
                            Files.delete(tempFile);
                        }

                        break;
                    }
                }
            }

            if (archive.getDescriptions().size() > 0) {
                version = readVersion(archive);
            }
        }

        return new FSKXArchiveImpl(sim, pack, readme, version);
    }

    static void writeArchive(FSKXArchive archive, File file, String scriptExtension) throws JDOMException, CombineArchiveException, ParseException, IOException, TransformerException {

        try (CombineArchive combineArchive = new CombineArchive(file)) {

            // Add simulations as SEDML
            File sedmlFile = File.createTempFile("simulation", ".sedml");
            SEDMLDocument doc = createSedml(archive.getSimulations(), scriptExtension);
            doc.writeDocument(sedmlFile);
            combineArchive.addEntry(sedmlFile, "simulation.sedml", SEDML_URI);
            sedmlFile.delete();

            // Add packages as Json file
            File jsonFile = createPackagesFile(archive.getPackages());
            combineArchive.addEntry(jsonFile, "packages.json", JSON_URI);
            jsonFile.delete();

            // Add readme
            if (archive.getReadme() != null && !archive.getReadme().isEmpty()) {
                // Create temporary file with README to be added to archive
                Path readmeFile = Files.createTempFile("README", ".txt");
                Files.write(readmeFile, archive.getReadme().getBytes());

                // Copy temporary file to archive
                ArchiveEntry readmeEntry = combineArchive.addEntry(readmeFile.toFile(), "README.txt", PLAIN_URI);

                Files.delete(readmeFile);
            }

            addVersion(combineArchive, archive.getVersion());

            combineArchive.pack();
        }
    }

    private static Simulations readSimulations(File sedmlFile) throws XMLException, DataConversionException {

        SedML sedml = Libsedml.readDocument(sedmlFile).getSedMLModel();

        // Get selected index
        final int selectedIndex;
        if (sedml.getAnnotation().size() == 1) {
            Annotation annotation = sedml.getAnnotation().get(0);
            Element element = annotation.getAnnotationElementsList().get(0);
            selectedIndex = new SelectedSimulation(element).getIndex();
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
        sedml.addAnnotation(new Annotation(new SelectedSimulation(simulations.getSelectedIndex())));

        for (Map.Entry<String, Map<String, String>> simulationEntry : simulations.getInputValues().entrySet()) {

            // Add model
            Model sedmlModel = new Model(simulationEntry.getKey(), "", uri, "model." + scriptExtension);
            sedml.addModel(sedmlModel);

            // Add task
            sedml.addTask(new Task("task" + sedml.getTasks().size(), "", sedmlModel.getId(), simulation.getId()));

            // Add changes to model: keys are parameter names and values are parameter values
            for (Map.Entry<String, String> entry : simulationEntry.getValue().entrySet()) {
                ChangeAttribute change = new ChangeAttribute(new XPathTarget(entry.getKey()), entry.getValue());
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

    private static Packages readPackages(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonFile, PackagesImpl.class);
    }

    private static File createPackagesFile(Packages packages) throws IOException {

        File file = File.createTempFile("packages", ".json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, packages);

        return file;
    }

    private static void addVersion(CombineArchive archive, String version) {
        DefaultJDOMFactory factory = new DefaultJDOMFactory();
        Namespace dcTermsNamespace = Namespace.getNamespace("dcterms", "http://purl.org/dc/terms/");

        org.jdom2.Element conformsToNode = factory.element("conformsTo", dcTermsNamespace);
        conformsToNode.setText(version);

        org.jdom2.Element element = factory.element("element");
        element.addContent(conformsToNode);

        archive.addDescription(new DefaultMetaDataObject(element));
    }

    private static String readVersion(CombineArchive archive) {

        MetaDataObject metaDataObject = archive.getDescriptions().get(0);
        org.jdom2.Element element = metaDataObject.getXmlDescription();

        return "";
    }
}

package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import org.jdom.Text;
import org.jlibsedml.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class IO {

    private static final URI SEDML_URI = URI.create("http://identifiers.org/combine.specifications/sed-ml");

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
}

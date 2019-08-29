package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IOTest {

    @Test
    public void testReadArchive() throws Exception {

        File file = createSampleArchive();
        FSKXArchive archive = IO.readArchive(file);
        Simulations simulations = archive.getSimulations();

        assertNotNull(simulations);
        assertEquals(3, simulations.getSelectedIndex());
        assertEquals("result", simulations.getOutputs().get(0));
        assertEquals(4, simulations.getInputValues().size());
    }

    private File createSampleArchive() throws Exception {

        // Create CombineArchive at a temporary file that must be deleted after
        File tempFile = File.createTempFile("model", ".fskx");
        tempFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(tempFile)) {
            // Add simulation file
            File sedmlFile = new File(IOTest.class.getResource("sim.sedml").getFile());
            ArchiveEntry entry = archive.addEntry(sedmlFile, "sim.sedml", URI.create("http://identifiers.org/combine.specifications/sed-ml"));

            archive.pack();
        }

        return tempFile;
    }

    @Test
    public void testWriteArchive() throws Exception {

        FSKXArchive archive = new FSKXArchiveImpl(createExampleSimulations());

        File tempFile = File.createTempFile("archive", ".fskx");
        tempFile.deleteOnExit();

        IO.writeArchive(archive, tempFile, "r");
        assert tempFile.exists();

        try (CombineArchive ca = new CombineArchive(tempFile)) {
            assert ca.hasEntriesWithFormat(IO.SEDML_URI);
        }
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;

        Map<String, String> defaultSimulation = new HashMap<>();
        defaultSimulation.put("n_iter", "200");
        defaultSimulation.put("Npos", "30");
        defaultSimulation.put("Ntotal", "100");

        Map<String, Map<String, String>> values = new HashMap<>();
        values.put("defaultSimulation", defaultSimulation);

        return new SimulationsImpl(selected, Collections.singletonList("output"), values);
    }
}

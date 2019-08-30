package de.bund.bfr.fskml;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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


        //packages
        assertNotNull(archive.getPackages());
        assertEquals("R",archive.getPackages().getLanguage());
        assertTrue(archive.getPackages().getPackages().containsKey("triangle"));
        assertTrue(archive.getPackages().getPackages().containsValue("0.12"));
    }

    private File createSampleArchive() throws Exception {

        // Create CombineArchive at a temporary file that must be deleted after
        File tempFile = File.createTempFile("model", ".fskx");
        tempFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(tempFile)) {
            // Add simulation file
            File sedmlFile = new File(IOTest.class.getResource("sim.sedml").getFile());
            ArchiveEntry entry = archive.addEntry(sedmlFile, "sim.sedml", URI.create("http://identifiers.org/combine.specifications/sed-ml"));

            //add packages file
            File packagesFile = new File(IOTest.class.getResource("jsonPackages.json").getFile());
            archive.addEntry(packagesFile,"jsonPackages.json",URI.create("https://www.iana.org/assignments/media-types/application/json"));

            archive.pack();
        }

        return tempFile;
    }

    @Test
    public void testWriteArchive() throws Exception {

        FSKXArchive archive = new FSKXArchiveImpl(createExampleSimulations(),createExamplePackages());

        File tempFile = File.createTempFile("archive", ".fskx");
        tempFile.deleteOnExit();

        IO.writeArchive(archive, tempFile, "r");
        assert tempFile.exists();

        try (CombineArchive ca = new CombineArchive(tempFile)) {
            assert ca.hasEntriesWithFormat(IO.SEDML_URI);
            assert ca.hasEntriesWithFormat(IO.JSON_PKG_URI);
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
    private PackagesImpl createExamplePackages(){
        String language = "R";
        Map<String,String> packages = new HashMap<>();
        packages.put("triangle","0.12");
        packages.put("ggplot","1.23");
        return new PackagesImpl(language,packages);
    }

}

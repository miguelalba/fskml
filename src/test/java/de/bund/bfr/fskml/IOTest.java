package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.junit.Test;

import java.io.File;
import java.net.URI;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IOTest {

    @Test
    public void testReadArchive() throws Exception {

        File file = createSampleArchive();
        FSKXArchive archive = IO.readArchive(file);
        assertNotNull(archive.getSimulations());
        assertEquals(3, archive.getSimulations().getSelectedIndex());
        assertEquals(4, archive.getSimulations().getInputValues().size());
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
}

package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ScriptEntryTest {

    @Test
    public void test() throws IOException, JDOMException, ParseException, CombineArchiveException {
        File archiveFile = File.createTempFile("archive", "fskx");
        archiveFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(archiveFile)) {
            File scriptFile = File.createTempFile("model", "r");
            scriptFile.deleteOnExit();

            // Test constructor with CombineArchive, targetName, script and ResourceType
            ScriptEntry se = new ScriptEntry(archive, "model.r", "print('Hallo')", FskMetaDataObject.ResourceType.modelScript);
            assertNotNull(archive.getEntry("/model.r"));

            // Test constructor with ArchiveEntry
            ScriptEntry se2 = new ScriptEntry(se.entry);
            assertEquals("print('Hallo')", se2.script);
        }
    }
}

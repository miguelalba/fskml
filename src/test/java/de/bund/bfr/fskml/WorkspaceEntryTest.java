package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import org.apache.commons.io.FileUtils;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class WorkspaceEntryTest {

    @Test
    public void test() throws IOException, JDOMException, ParseException, CombineArchiveException {
        File archiveFile = File.createTempFile("archive", "fskx");
        archiveFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(archiveFile)) {
            File workspaceFile = File.createTempFile("workspace", "r");
            workspaceFile.deleteOnExit();

            // Test constructor with CombineArchive, targetName and file
            WorkspaceEntry we = new WorkspaceEntry(archive, "workspace.r", workspaceFile);
            assertNotNull(archive.getEntry("./workspace.r"));

            // Test constructor with ArchiveEntry
            WorkspaceEntry we2 = new WorkspaceEntry(we.entry);
            assertTrue(FileUtils.contentEquals(workspaceFile, we2.workspaceFile));
        }
    }
}

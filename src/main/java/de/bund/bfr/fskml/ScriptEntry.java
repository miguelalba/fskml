package de.bund.bfr.fskml;

import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScriptEntry {

    public ArchiveEntry entry;
    public String script;

    public ScriptEntry(CombineArchive archive, String targetName, String script, ResourceType resourceType)
            throws IOException {

        // Create temporary file with script
        File f = Files.createTempFile("script", "r").toFile();
        FileUtils.writeStringToFile(f, script, "UTF-8");
        f.deleteOnExit();

        // Create and add entry to archive
        this.entry = archive.addEntry(f, targetName, FSKML.getURIS(1, 0, 11).get("r"));
        this.entry.addDescription(new FskMetaDataObject(resourceType).metaDataObject);
    }

    public ScriptEntry(final ArchiveEntry entry) throws IOException {

        // Create temporary file with entry contents
        File f = Files.createTempFile("script", "r").toFile();
        f.deleteOnExit();
        entry.extractFile(f);

        // Copy entry and script
        this.entry = entry;
        this.script = FileUtils.readFileToString(f, "UTF-8");
    }
}

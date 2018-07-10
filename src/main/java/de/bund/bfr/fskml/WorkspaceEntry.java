package de.bund.bfr.fskml;

import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class WorkspaceEntry {

    public ArchiveEntry entry;
    public File workspaceFile;

    public WorkspaceEntry(CombineArchive archive, String targetName, File workspaceFile) throws IOException {
        this.entry = archive.addEntry(workspaceFile, targetName, FSKML.getURIS(1, 0, 11).get("r"));

        MetaDataObject mdo = new FskMetaDataObject(ResourceType.workspace).metaDataObject;
        this.entry.addDescription(mdo);
    }

    public WorkspaceEntry(ArchiveEntry entry) throws IOException {
        this.workspaceFile = Files.createTempFile("workspace", "r").toFile();
        entry.extractFile(this.workspaceFile);
    }
}


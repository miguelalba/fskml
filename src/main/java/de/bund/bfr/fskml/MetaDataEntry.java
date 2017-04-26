package de.bund.bfr.fskml;

import de.bund.bfr.fskml.FskMetaData.DataType;
import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class MetaDataEntry {

    public ArchiveEntry entry;
    public FskMetaData metaData;

    public MetaDataEntry(CombineArchive archive, String targetName, FskMetaData metaData)
            throws IOException, SBMLException, XMLStreamException {

        SBMLDocument doc = new MetadataDocument(metaData).doc;

        // Creates temporary file with metadata
        File f = Files.createTempFile("metadata", "pmf").toFile();
        f.deleteOnExit();
        new SBMLWriter().write(doc, f);

        this.entry = archive.addEntry(f, targetName, URIS.pmf);
        this.entry.addDescription(new FskMetaDataObject(ResourceType.metaData).metaDataObject);
        this.metaData = metaData;
    }

    public MetaDataEntry(ArchiveEntry entry) throws IOException, XMLStreamException {
        try (InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ)) {
            SBMLDocument doc = new SBMLReader().readSBMLFromStream(stream);

            this.entry = entry;

            this.metaData = new MetadataDocument(doc).getMetaData();
            this.metaData.software = FskMetaData.Software.R;
            for (int i = 0; i < this.metaData.dependentVariables.size(); i++) {
                this.metaData.dependentVariables.get(i).type = DataType.numeric;
            }
        }
    }
}

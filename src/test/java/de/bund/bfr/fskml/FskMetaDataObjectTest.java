/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.fskml;

import de.bund.bfr.fskml.FskMetaDataObject.ResourceType;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FskMetaDataObjectTest {

    @Test
    public void test() throws IOException {
        File archiveFile = File.createTempFile("archive", ".fskx");
        archiveFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(archiveFile)) {

            File dummyFile = File.createTempFile("entry", "txt");
            dummyFile.deleteOnExit();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 11);
            URI rURI = rawUris.get("r");
            URI pmfURI = rawUris.get("pmf");

            // Test model script
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "model.r", rURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.modelScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/model.r", fmdo.metaDataObject.getAbout());
                assertEquals(ResourceType.modelScript, fmdo.getResourceType());

            }

            // Test param script
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "param.r", rURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.parametersScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/param.r", fmdo.metaDataObject.getAbout());
                assertEquals(ResourceType.parametersScript, fmdo.getResourceType());
            }

            // Test visualization script
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "visualization.r", rURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.visualizationScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/visualization.r", fmdo.metaDataObject.getAbout());
                assertEquals(ResourceType.visualizationScript, fmdo.getResourceType());
            }

            // Test metadata file
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "metadata.pmf", pmfURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.metaData);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/metadata.pmf", fmdo.metaDataObject.getAbout());
                assertEquals(ResourceType.metaData, fmdo.getResourceType());
            }

            // Test workspace file
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "workspace.r", rURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.workspace);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/workspace.r", fmdo.metaDataObject.getAbout());
                assertEquals(ResourceType.workspace, fmdo.getResourceType());
            }

            // Test clone
            {
                ArchiveEntry entry = archive.addEntry(dummyFile, "workspace.r", rURI);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.workspace);
                entry.addDescription(fmdo.metaDataObject);

                // Test clone
                FskMetaDataObject clone = new FskMetaDataObject(fmdo.metaDataObject);
                assertEquals("/workspace.r", clone.metaDataObject.getAbout());
                assertEquals(ResourceType.workspace, fmdo.getResourceType());
            }

        } catch (ParseException | CombineArchiveException | JDOMException e) {
            e.printStackTrace();
        }
    }
}

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
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class FskMetaDataObjectTest {

    @Test
    public void test() throws IOException {
        File archiveFile = File.createTempFile("archive", "fskx");
        archiveFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(archiveFile)) {

            File dummyArchiveFile = File.createTempFile("entry", "txt");
            dummyArchiveFile.deleteOnExit();

            // Test model script
            {
                File file = File.createTempFile("model", "r");
                file.deleteOnExit();
                ArchiveEntry entry = archive.addEntry(dummyArchiveFile, "model.r", URIS.r);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.modelScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/model.r", fmdo.metaDataObject.getAbout());

                String resourceTypeString = fmdo.metaDataObject.getXmlDescription().getChildText("type",
                        FskMetaDataObject.dcNamespace);
                assertEquals(ResourceType.modelScript, ResourceType.valueOf(resourceTypeString));
            }

            // Test param script
            {
                File file = File.createTempFile("param", "r");
                file.deleteOnExit();
                ArchiveEntry entry = archive.addEntry(dummyArchiveFile, "param.r", URIS.r);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.parametersScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/param.r", fmdo.metaDataObject.getAbout());

                String resourceTypeString = fmdo.metaDataObject.getXmlDescription().getChildText("type",
                        FskMetaDataObject.dcNamespace);
                assertEquals(ResourceType.parametersScript, ResourceType.valueOf(resourceTypeString));
            }

            // Test visualization script
            {
                File file = File.createTempFile("visualization", "r");
                file.deleteOnExit();
                ArchiveEntry entry = archive.addEntry(dummyArchiveFile, "visualization.r", URIS.r);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.visualizationScript);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/visualization.r", fmdo.metaDataObject.getAbout());

                String resourceTypeString = fmdo.metaDataObject.getXmlDescription().getChildText("type",
                        FskMetaDataObject.dcNamespace);
                assertEquals(ResourceType.visualizationScript, ResourceType.valueOf(resourceTypeString));
            }

            // Test metadata file
            {
                File file = File.createTempFile("metadata", ".pmf");
                file.deleteOnExit();
                ArchiveEntry entry = archive.addEntry(dummyArchiveFile, "metadata.pmf", URIS.pmf);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.metaData);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/metadata.pmf", fmdo.metaDataObject.getAbout());

                String resourceTypeString = fmdo.metaDataObject.getXmlDescription().getChildText("type",
                        FskMetaDataObject.dcNamespace);
                assertEquals(ResourceType.metaData, ResourceType.valueOf(resourceTypeString));
            }

            // Test workspace file
            {
                File file = File.createTempFile("workspace", ".r");
                file.deleteOnExit();
                ArchiveEntry entry = archive.addEntry(dummyArchiveFile, "workspace.r", URIS.r);
                FskMetaDataObject fmdo = new FskMetaDataObject(ResourceType.workspace);
                entry.addDescription(fmdo.metaDataObject);

                assertEquals("/workspace.r", fmdo.metaDataObject.getAbout());

                String resourceTypeString = fmdo.metaDataObject.getXmlDescription().getChildText("type",
                        FskMetaDataObject.dcNamespace);
                assertEquals(ResourceType.workspace, ResourceType.valueOf(resourceTypeString));
            }

        } catch (ParseException | CombineArchiveException | JDOMException e) {
            e.printStackTrace();
        }
    }
}

/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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

import de.unirostock.sems.cbarchive.ArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

// Stuff that should be moved into separated classes
public class Util {

	public SBMLDocument readMetaData(final ArchiveEntry entry) throws IOException, XMLStreamException {

		// Create temporary file
		File tempFile = Files.createTempFile("metadata", ".pmf").toFile();
		entry.extractFile(tempFile);

		SBMLDocument doc;
		try {
			doc = SBMLReader.read(tempFile);
		} finally {
			tempFile.delete();
		}

		return doc;
	}

	public List<String> readLibraryNames(final List<ArchiveEntry> entries) {
		return entries.stream().map(entry -> entry.getFileName().split("\\_")[0]).collect(Collectors.toList());
	}

	private static String loadScriptFromEntry(final ArchiveEntry entry) throws IOException {
		// Create temporary file
		File tempFile = Files.createTempFile("script", ".r").toFile();
		entry.extractFile(tempFile);

		// Read script from f and return script
		String script;
		try (FileInputStream fis = new FileInputStream(tempFile)) {
			script = IOUtils.toString(fis, "UTF-8");
		} finally {
			tempFile.delete();
		}

		return script;
	}
}

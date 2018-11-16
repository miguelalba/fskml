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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

/**
 * @author Miguel Alba
 */
public class PythonScriptTest extends TestCase {

	public void testScript() throws IOException {
		
		// Creates temporary file. Fails the test if an error occurs.
		File f = File.createTempFile("temp", "");
		f.deleteOnExit();

		String origScript = "Hello World";
		FileUtils.writeStringToFile(f, origScript, "UTF-8");
		PythonScript pyScript = new PythonScript(f);

		assertEquals(origScript, pyScript.getScript());
	}
}
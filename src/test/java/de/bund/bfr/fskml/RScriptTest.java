package de.bund.bfr.fskml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.bund.bfr.fskml.RScript;
import junit.framework.TestCase;

/**
 * @author Miguel Alba
 */
public class RScriptTest extends TestCase {

	private final String origScript = "# This is a comment line: It should not appear in the simplified version\n"
			+ "library(triangle)\n" + "source(other.R)\n"
			+ "hist(result, breaks=50, main=\"PREVALENCE OF PARENT FLOCKS\")\n";
	private RScript rScript;

	public void testScript() throws IOException {
		
		// Creates temporary file. Fails the test if an error occurs.
		File f = File.createTempFile("temp", "");
		f.deleteOnExit();

		try (FileWriter fw = new FileWriter(f)) {
			fw.write(origScript);
			fw.close();
			rScript = new RScript(f);
		}

		assertEquals(origScript, rScript.getScript());

		List<String> expectedLibraries = Arrays.asList("triangle");
		List<String> obtainedLibraries = rScript.getLibraries();
		assertEquals(expectedLibraries, obtainedLibraries);

		List<String> expectedSources = Arrays.asList("other.R");
		List<String> obtainedSources = rScript.getSources();
		assertEquals(expectedSources, obtainedSources);
	}
}
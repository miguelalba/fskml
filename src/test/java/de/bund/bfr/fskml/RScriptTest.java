package de.bund.bfr.fskml;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Miguel Alba
 */
public class RScriptTest extends TestCase {

	public void testScript() throws IOException {
		
		// Creates temporary file. Fails the test if an error occurs.
		File f = File.createTempFile("temp", "");
		f.deleteOnExit();

		String origScript = "# This is a comment line: It should not appear in the simplified version\n"
				+ "library(triangle)\n" + "source(other.R)\n"
				+ "hist(result, breaks=50, main=\"PREVALENCE OF PARENT FLOCKS\")\n";
		FileUtils.writeStringToFile(f, origScript, "UTF-8");
		RScript rScript = new RScript(f);

		assertEquals(origScript, rScript.getScript());
		assertEquals(Collections.singletonList("triangle"), rScript.getLibraries());
		assertEquals(Collections.singletonList("other.R"), rScript.getSources());
	}
}
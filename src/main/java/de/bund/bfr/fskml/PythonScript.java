package de.bund.bfr.fskml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class PythonScript extends Script {
	/**
	 * Process R script.
	 */
	public PythonScript(final String script) {

		// If no errors are thrown, proceed to extract libraries and sources
		final String[] lines = script.split("\\r?\\n");

				
		//look for lines starting with import
		//split lines by whitespaces and collect the second word (name of library) 
		//add all the found items to List of libraries 
		Arrays.stream(lines).filter(id -> id.startsWith("import")).map(id -> id.split(" ")[1]).forEach(libraries::add);

		this.script = script;// sb.toString();
	}

	/**
	 * Process the script.
	 * 
	 * @param file
	 * @throws IOException
	 *             if the file specified by path cannot be read.
	 */
	public PythonScript(final File file) throws IOException {
		this(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
	}

}

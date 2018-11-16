package de.bund.bfr.fskml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class PythonScript extends Script {
	/**
	 * Process R script.
	 */
	public PythonScript(final String script) {

		// If no errors are thrown, proceed to extract libraries and sources
		final String[] lines = script.split("\\r?\\n");


		StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			sb.append(line).append('\n');
		}

		this.script = script;//sb.toString();
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

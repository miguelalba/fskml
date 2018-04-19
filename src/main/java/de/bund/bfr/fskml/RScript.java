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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RScript {
	private final String script;
	private final List<String> libraries = new LinkedList<>();
	private final List<String> sources = new LinkedList<>();

	/**
	 * Process R script.
	 */
	public RScript(final String script) {

		// If no errors are thrown, proceed to extract libraries and sources
		final String[] lines = script.split("\\r?\\n");

		final Pattern noQuotesPattern = Pattern.compile("\\((.*?)\\)");
		final Pattern doubleQuotePattern = Pattern.compile("\\((\".*?\")\\)");
		final Pattern simpleQuotePattern = Pattern.compile("\\(('.*?')\\)");

		StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			sb.append(line).append('\n');

			if (line.startsWith("library") || line.startsWith("require")) {

				// Check for library commands using simple quotes. E.g. library('triangle')
				Matcher simpleQuoteMatcher = simpleQuotePattern.matcher(line);
				if (simpleQuoteMatcher.find(1)) {
					String libraryName = simpleQuoteMatcher.group(1).replace("'", "");
					libraries.add(libraryName);
					continue;
				}

				// Check for library commands using double quotes. E.g. library("triangle")
				Matcher doubleQuoteMatcher = doubleQuotePattern.matcher(line);
				if (doubleQuoteMatcher.find(1)) {
					String libraryName = doubleQuoteMatcher.group(1).replace("\"", "");
					libraries.add(libraryName);
					continue;
				}

				// Check for library commands not using quotes. E.g. library(triangle).
				Matcher noQuoteMatcher = noQuotesPattern.matcher(line);
				if (noQuoteMatcher.find(1)) {
					String libraryName = noQuoteMatcher.group(1);
					libraries.add(libraryName);
				}

			} else if (line.startsWith("source")) {
				// Check for source commands using simple quotes. E.g. library('triangle')
				Matcher simpleQuoteMatcher = simpleQuotePattern.matcher(line);
				if (simpleQuoteMatcher.find(1)) {
					String sourceName = simpleQuoteMatcher.group(1).replace("'", "");
					sources.add(sourceName);
					continue;
				}

				// Check for source commands using double quotes. E.g. library("triangle")
				Matcher doubleQuoteMatcher = doubleQuotePattern.matcher(line);
				if (doubleQuoteMatcher.find(1)) {
					String sourceName = doubleQuoteMatcher.group(1).replace("\"", "");
					sources.add(sourceName);
				}
			}


		}

		this.script = sb.toString();
	}

	/**
	 * Process R script.
	 * 
	 * @param file
	 * @throws IOException
	 *             if the file specified by path cannot be read.
	 */
	public RScript(final File file) throws IOException {
		this(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
	}

	/**
	 * Gets the R script.
	 * 
	 * @return the R script.
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Gets the names of the source files linked in the R script.
	 * 
	 * @return the names of the source files linked in the R script.
	 */
	public List<String> getSources() {
		return sources;
	}

	/**
	 * Gets the names of the libraries imported in the R script.
	 * 
	 * @return the names of the libraries imported in the R script.
	 */
	public List<String> getLibraries() {
		return libraries;
	}
}

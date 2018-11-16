package de.bund.bfr.fskml;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;


public class ScriptFactory {

	public static Script createScript(final File file)throws IOException {
		Script script = null;
		
		String language = FilenameUtils.getExtension(file.getPath());
		if(language.toLowerCase().startsWith("r"))
		{
			script = new RScript(file);
		}
		if(language.toLowerCase().startsWith("py"))
		{
			script = new PythonScript(file);
		}
		
		return script;
	}
}

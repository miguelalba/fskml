package de.bund.bfr.fskml;

import java.util.Map;

public interface Packages {

    /**
     * Returns the language of the script.
     * Example: R, Python
     * @return Language the script is written in.
     *
     */
    String getLanguage();

    /**
     * Return map of packages with name as key and version as value
     * Example:
     * <pre>
     * "triangle" : "0.12"
     * </pre>
     *
     * @return Map of
     */
    Map<String,String> getPackages();

}

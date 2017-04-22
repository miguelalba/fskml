package de.bund.bfr.fskml;

/**
 * Keeps the paths to the scripts of an FSK model in a COMBINE archive.
 */
public class FskModel {

    public final String modelScript;
    public final String parametersScript;
    public final String visualizationScript;

    public FskModel(final String modelScript, final String parametersScript,
                    final String visualizationScript) {
        this.modelScript = modelScript;
        this.parametersScript = parametersScript;
        this.visualizationScript = visualizationScript;
    }
}

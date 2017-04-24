package de.bund.bfr.fskml;

/**
 * Keeps the paths to the scripts of an FSK model in a COMBINE archive.
 */
public class FskModel {

    /**
     * Model script.
     */
    public String model;

    /**
     * Parameters script.
     */
    public String param;

    /**
     * Visualization script.
     */
    public String viz;

    /**
     * Model meta data.
     */
    public FskMetaData template;

    public FskModel(final String model, final String param,
                    final String viz, final FskMetaData template) {
        this.model = model;
        this.param = param;
        this.viz = viz;
        this.template = template;
    }

    @Override
    public String toString() {
        return template == null ? "" : template.modelId;
    }
}

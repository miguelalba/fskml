package de.bund.bfr.fskml;

import org.jdom2.Element;

import java.util.List;

/**
 * Keeps the names of the R scripts:
 * <ul>
 * <li>main script</li>
 * <li>visualization script</li>
 * <li>parameters script</li>
 * <li>workspace file</li>
 * </ul>
 *
 * @author Miguel de Alba
 */
public interface OmexMetaDataHandlerI {

    /** @return null if the model script is not set. */
    String getModelScript();

    /** @param script R model script. */
    void setModelScript(final String script);

    /** @return null if the parameters script is not set. */
    String getParametersScript();

    /** @param script R parameters script. */
    void setParametersScript(final String script);

    /** @return null if the visualization script is not set. */
    String getVisualizationScript();

    /** @param script R visualization script. */
    void setVisualizationScript(final String script);

    /** @return null if the workspace file is not set. */
    String getWorkspaceFile();

    /** @param file R workspace file. */
    void setWorkspaceFile(final String file);

    List<Element> getElements();
}

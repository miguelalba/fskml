package de.bund.bfr.fskml;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;

import java.util.Collections;
import java.util.List;

/**
 * Legacy OMEX annotation.
 *
 * @author Miguel de Alba
 */
public class LegacyOmexMetaDataHandler implements OmexMetaDataHandlerI {

    private static final String MAIN_SCRIPT_TAG = "mainScript";
    private static final String PARAM_SCRIPT_TAG = "parametersScript";
    private static final String VIZ_SCRIPT_TAG = "visualizationScript";
    private static final String WORKSPACE_TAG = "workspace";

    private final Element node;

    private final DefaultJDOMFactory factory = new DefaultJDOMFactory();

    public LegacyOmexMetaDataHandler() { node = factory.element("metaParent"); }

    public LegacyOmexMetaDataHandler(final Element node) { this.node = node; }

    @Override
    public String getModelScript() {
        return node.getChildText(MAIN_SCRIPT_TAG);
    }

    @Override
    public void setModelScript(String script) {
        setText(MAIN_SCRIPT_TAG, script);
    }

    @Override
    public String getParametersScript() {
        return node.getChildText(PARAM_SCRIPT_TAG);
    }

    @Override
    public void setParametersScript(String script) {
        setText(PARAM_SCRIPT_TAG, script);
    }

    @Override
    public String getVisualizationScript() {
        return node.getChildText(VIZ_SCRIPT_TAG);
    }

    @Override
    public void setVisualizationScript(String script) {
        setText(VIZ_SCRIPT_TAG, script);
    }

    @Override
    public String getWorkspaceFile() {
        return node.getChildText(WORKSPACE_TAG);
    }

    @Override
    public void setWorkspaceFile(String file) {
        setText(WORKSPACE_TAG, file);
    }

    @Override
    public List<Element> getElements() {
        return Collections.singletonList(node);
    }

    private void setText(final String cname, final String value) {
        Element child = node.getChild(cname);

        // if the script is not set, create a new Element and add it to the node
        if (child == null) {
            child = factory.element(cname);
            child.addContent(value);
            node.addContent(child);
        }
        // otherwise just update it
        else {
            child.setText(value);
        }
    }
}

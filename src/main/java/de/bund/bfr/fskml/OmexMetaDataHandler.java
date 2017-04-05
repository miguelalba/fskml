package de.bund.bfr.fskml;

import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the names of the R scripts:
 *
 * <ul>
 *     <li>main script</li>
 *     <li>visualization script</li>
 *     <li>parameters script</li>
 *     <li>workspace file</li>
 * </ul>
 *
 * @author Miguel de Alba
 */
public class OmexMetaDataHandler {

    private Element modelElement;
    private Element paramElement;
    private Element vizElement;
    private Element workspaceElement;

    private static final Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
    private static final DefaultJDOMFactory factory = new DefaultJDOMFactory();

    private enum ResourceType {modelScript, parametersScript, visualizationScript, workspace}

    public OmexMetaDataHandler() {
    }

    public OmexMetaDataHandler(final List<Element> elements) {
        for (final Element element : elements) {
            ResourceType type = ResourceType.valueOf(element.getChildText("type", dcNamespace));

            switch (type) {
                case modelScript:
                    modelElement = element;
                    break;
                case parametersScript:
                    paramElement = element;
                    break;
                case visualizationScript:
                    vizElement = element;
                    break;
                case workspace:
                    workspaceElement = element;
                    break;
            }
        }
    }

    /** @return null if not set */
    public String getModelScript() {
        return modelElement == null ? null : modelElement.getChildText("source", dcNamespace);
    }

    /** @param script R model script. */
    public void setModelScript(final String script) {
        if (modelElement == null) {
            Element typeNode = factory.element("type", dcNamespace);
            typeNode.setText(ResourceType.modelScript.name());

            Element srcNode = factory.element("source", dcNamespace);
            srcNode.setText(script);

            modelElement = factory.element("element");
            modelElement.addContent(typeNode);
            modelElement.addContent(srcNode);
        } else {
            modelElement.getChild("source", dcNamespace).setText(script);
        }
    }

    /** @return null if the parameters script is not set. */
    public String getParametersScript() {
        return paramElement == null ? null : paramElement.getChildText("source", dcNamespace);
    }

    /** @param script R parameters script. */
    public void setParametersScript(final String script) {
        if (paramElement == null) {
            Element typeNode = factory.element("type", dcNamespace);
            typeNode.setText(ResourceType.parametersScript.name());

            Element srcNode = factory.element("source", dcNamespace);
            srcNode.setText(script);

            paramElement = factory.element("element");
            paramElement.addContent(typeNode);
            paramElement.addContent(srcNode);
        } else {
            paramElement.getChild("source", dcNamespace).setText(script);
        }
    }

    /** @return null if the visualization script is not set. */
    public String getVisualizationScript() {
        return vizElement == null ? null : vizElement.getChildText("source", dcNamespace);
    }

    /** @param script R visualization script. */
    public void setVisualizationScript(final String script) {
        if (vizElement == null) {
            Element typeNode = factory.element("type", dcNamespace);
            typeNode.setText(ResourceType.visualizationScript.name());

            Element srcNode = factory.element("source", dcNamespace);
            srcNode.setText(script);

            vizElement = factory.element("element");
            vizElement.addContent(typeNode);
            vizElement.addContent(srcNode);
        } else {
            vizElement.getChild("source", dcNamespace).setText(script);
        }
    }

    /** @return null if the workspace file is not set. */
    public String getWorkspaceFile() {
        return workspaceElement == null ? null : workspaceElement.getChildText("source", dcNamespace);
    }

    /** @param file R workspace file. */
    public void setWorkspaceFile(final String file) {
        if (workspaceElement == null) {
            Element typeNode = factory.element("type", dcNamespace);
            typeNode.setText(ResourceType.workspace.name());

            Element srcNode = factory.element("source", dcNamespace);
            srcNode.setText(file);

            workspaceElement = factory.element("element");
            workspaceElement.addContent(typeNode);
            workspaceElement.addContent(srcNode);
        } else {
            workspaceElement.getChild("source", dcNamespace).setText(file);
        }
    }

    public List<Element> getElements() {
        List<Element> elements = new ArrayList<>(4);
        if (modelElement != null) {
            elements.add(modelElement);
        }
        if (paramElement != null) {
            elements.add(paramElement);
        }
        if (vizElement != null) {
            elements.add(vizElement);
        }
        if (workspaceElement != null) {
            elements.add(workspaceElement);
        }

        return elements;
    }
}

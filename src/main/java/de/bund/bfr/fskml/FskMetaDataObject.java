package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Keeps the names of the R scripts for a number of models.
 */
public class FskMetaDataObject {

    public enum ResourceType {
        /** Model script */
        modelScript,

        /** Parameters script */
        parametersScript,

        /** Visualization script */
        visualizationScript,

        /** Model metadata encoded in fskml */
        metaData,

        /** Binary file with workspace */
        workspace
    }

    public static final Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
    private static final DefaultJDOMFactory factory = new DefaultJDOMFactory();

    public final MetaDataObject metaDataObject;

    public FskMetaDataObject(ResourceType resourceType) {

        Element typeNode = factory.element("type", dcNamespace);
        typeNode.setText(resourceType.name());

        Element element = factory.element("element");
        element.addContent(typeNode);

        metaDataObject = new DefaultMetaDataObject(element);
    }
}

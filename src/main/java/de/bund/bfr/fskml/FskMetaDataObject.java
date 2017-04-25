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

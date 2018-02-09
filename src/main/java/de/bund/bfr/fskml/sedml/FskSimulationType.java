/*
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 * Department Biological Safety - BfR
 */
package de.bund.bfr.fskml.sedml;

import org.jdom.Element;

/**
 * Extension class for FSK SED-ML that describes the kind of simulation.
 *
 * <p>
 * <li>type: Mandatory attribute. Cannot be null.</li>
 * </p>
 *
 * @author Miguel de Alba, BfR.
 */
public class FskSimulationType extends Element {

    enum Type {deterministic, statistic, probabilistic };

    /**
     * Build new FskSimulationType.
     *
     * @param type Mandatory.
     */
    public FskSimulationType(Type type) {
        super("fskSimulationType");

        setAttribute("type", type.name());
    }

    public FskSimulationType(Element element) {
        this(Type.valueOf(element.getAttributeValue("type")));
    }

    /**
     * @return type of simulation. Non-null.
     */
    public Type getType() {
        return Type.valueOf(getAttributeValue("type"));
    }

    /**
     * @return true if type is changed
     */
    public boolean setType(Type type) {
        if (type != getType()) {
            setAttribute("type", type.name());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "fskSimulationType [";
        s += "type=" + getAttributeValue("type");
        s += "]";
        return s;
    }

    // hashCode and equals are implemented final in org.jdom.Element.
}

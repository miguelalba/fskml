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
 * Extension class for FSK SED-ML that allows to reference external scripts in supported scripting
 * languages. It contains the path to a script and the scripting language.
 * <p>
 * <ul>
 * <li>Language: Mandatory attribute of string type. Refers to the scripting language.</li>
 * <li>Src: Optional attribute of string type. Refers to path to a script.</li>
 * </ul>
 *
 * @author Miguel de Alba, BfR.
 */
public class SourceScript extends Element {

    /**
     * Build new SourceScript
     *
     * @param language Mandatory, cannot be null or empty string.
     * @param src      Optional, cannot be null but empty.
     */
    public SourceScript(String language, String src) {
        super("sourceScript");

        assert language != null && !language.isEmpty();
        assert src != null;

        setAttribute("language", language);
        setAttribute("src", src);
    }

    public SourceScript(Element element) {
        this(element.getAttributeValue("language"), element.getAttributeValue("src"));
    }

    /**
     * @return Scripting language. Non-null and non-empty string.
     */
    public String getLanguage() {
        return getAttributeValue("language");
    }

    /**
     * @param language non-null and non-empty string.
     * @return true if language is changed.
     */
    public boolean setLanguage(String language) {
        if (language != null && !language.isEmpty()) {
            setAttribute("language", language);
            return true;
        }
        return false;
    }

    /**
     * @return path to script. Non-null string. Empty string if not set.
     */
    public String getSrc() {
        return getAttributeValue("src");
    }

    /**
     * @param src non-null string. Can be empty.
     * @return boolean if src is changed.
     */
    public boolean setSrc(String src) {
        if (src != null) {
            setAttribute("src", src);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "SourceScript [";
        s += "language=" + getAttributeValue("language");
        s += "src=" + getAttributeValue("src");
        s += "]";
        return s;
    }

    // hashCode and equals are implemented final in org.jdom.Element.
}

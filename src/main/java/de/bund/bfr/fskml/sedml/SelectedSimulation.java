package de.bund.bfr.fskml.sedml;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class SelectedSimulation extends Element {

    public SelectedSimulation(int index) {
        super("selectedSimulation");
        setAttribute("index", Integer.toString(index));
    }

    public SelectedSimulation(Element element) throws DataConversionException {
        this(element.getAttribute("index").getIntValue());
    }

    public int getIndex() throws DataConversionException {
        return getAttribute("index").getIntValue();
    }
}

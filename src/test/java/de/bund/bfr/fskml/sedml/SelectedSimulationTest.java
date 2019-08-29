package de.bund.bfr.fskml.sedml;

import org.jdom.DataConversionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectedSimulationTest  {

    @Test
    public void testConstructor() throws DataConversionException {
        SelectedSimulation ss = new SelectedSimulation(1);
        assertEquals(1, ss.getIndex());

        SelectedSimulation ssCopy = new SelectedSimulation(ss);
        assertEquals(1, ssCopy.getIndex());
    }
}

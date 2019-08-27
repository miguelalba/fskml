package de.bund.bfr.fskml;

import java.util.List;
import java.util.Map;

public interface Simulations {

    /**
     * @return Index of selected simulation.
     */
    int getSelectedIndex();

    /**
     * @return Ids of input and constant parameters.
     */
    List<String> getInputIds();

    /**
     * @return Map of simulation names and list of input and constant parameter values for every simulation.
     */
    Map<String, List<String>> getInputValues();
}

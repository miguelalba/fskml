package de.bund.bfr.fskml;

import java.util.List;
import java.util.Map;

public interface Simulations {

    /**
     * @return Index of selected simulation.
     */
    int getSelectedIndex();

    /**
     * @return Ids of output parameters.
     */
    List<String> getOutputs();

    /**
     * Return nested map of simulation names and parameter values. The nested map keeps parameter ids:values.
     * Example:
     * <pre>
     * {
     *   'defaultSimulation': {'a': '1', 'b': '2', 'c': '3'},
     *   'otherSimulation': {'a': '4', 'b': '5', 'c': '6'}
     * }
     * </pre>
     *
     * @return Map of simulation names and list of input and constant parameter values for every simulation.
     */
    Map<String, Map<String, String>> getInputValues();
}

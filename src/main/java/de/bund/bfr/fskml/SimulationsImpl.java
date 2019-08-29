package de.bund.bfr.fskml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SimulationsImpl implements Simulations {

    private final int selected;
    private final List<String> outputs;
    private final Map<String, Map<String, String>> values;

    SimulationsImpl(int selected, List<String> outputs, Map<String, Map<String, String>> values) {
        this.selected = selected;
        this.outputs = Collections.unmodifiableList(outputs);
        this.values = Collections.unmodifiableMap(values);
    }

    public int getSelectedIndex() {
        return selected;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public Map<String, Map<String, String>> getInputValues() {
        return values;
    }
}

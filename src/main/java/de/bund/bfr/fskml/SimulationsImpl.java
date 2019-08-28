package de.bund.bfr.fskml;

import java.util.Collections;
import java.util.Map;

public final class SimulationsImpl implements Simulations {

    private final int selected;
    private final Map<String, Map<String, String>> values;

    SimulationsImpl(int selected, Map<String, Map<String, String>> values) {
        this.selected = selected;
        this.values = Collections.unmodifiableMap(values);
    }

    public int getSelectedIndex() {
        return selected;
    }

    public Map<String, Map<String, String>> getInputValues() {
        return values;
    }
}

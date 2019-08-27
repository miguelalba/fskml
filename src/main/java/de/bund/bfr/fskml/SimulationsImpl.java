package de.bund.bfr.fskml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SimulationsImpl implements Simulations {

    private final int selected;
    private final List<String> parameterIds;
    private final Map<String, List<String>> values;

    SimulationsImpl(int selected, List<String> parameterIds, Map<String, List<String>> values) {
        this.selected = selected;
        this.parameterIds = Collections.unmodifiableList(parameterIds);
        this.values = Collections.unmodifiableMap(values);
    }

    public int getSelectedIndex() {
        return selected;
    }

    public List<String> getInputIds() {
        return parameterIds;
    }

    public Map<String, List<String>> getInputValues() {
        return values;
    }
}

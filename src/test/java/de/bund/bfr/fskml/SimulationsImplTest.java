package de.bund.bfr.fskml;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SimulationsImplTest {

    @Test
    public void test() {
        SimulationsImpl sim = createExampleSimulations();
        assertEquals(0, sim.getSelectedIndex());
        assertEquals(Arrays.asList("n_iter", "Npos", "Ntotal"), sim.getInputIds());

        assertTrue(sim.getInputValues().containsKey("defaultSimulation"));
        assertEquals(Arrays.asList("200", "30", "100"), sim.getInputValues().get("defaultSimulation"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableIds() {
        SimulationsImpl sim = createExampleSimulations();
        sim.getInputIds().add("something");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableValues() {
        SimulationsImpl sim = createExampleSimulations();
        sim.getInputValues().clear();
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;
        List<String> inputIds = Arrays.asList("n_iter", "Npos", "Ntotal");
        Map<String, List<String>> inputValues = new HashMap<>();
        inputValues.put("defaultSimulation", Arrays.asList("200", "30", "100"));

        return new SimulationsImpl(selected, inputIds, inputValues);
    }
}

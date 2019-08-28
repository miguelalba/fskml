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

        assertTrue(sim.getInputValues().containsKey("defaultSimulation"));

        Map<String, String> defaultSimulation = sim.getInputValues().get("defaultSimulation");
        assertEquals("200", defaultSimulation.get("n_iter"));
        assertEquals("30", defaultSimulation.get("Npos"));
        assertEquals("100", defaultSimulation.get("Ntotal"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableValues() {
        SimulationsImpl sim = createExampleSimulations();
        sim.getInputValues().clear();
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;

        Map<String, String> defaultSimulation = new HashMap<>();
        defaultSimulation.put("n_iter", "200");
        defaultSimulation.put("Npos", "30");
        defaultSimulation.put("Ntotal", "100");

        Map<String, Map<String, String>> values = new HashMap<>();
        values.put("defaultSimulation", defaultSimulation);

        return new SimulationsImpl(selected, values);
    }
}

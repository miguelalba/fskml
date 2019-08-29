package de.bund.bfr.fskml;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertNotNull;

public class FSKXArchiveImplTest {

    @Test
    public void test() {
        FSKXArchive archive = new FSKXArchiveImpl(createExampleSimulations());
        assertNotNull(archive.getSimulations());
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;

        Map<String, String> defaultSimulation = new HashMap<>();
        defaultSimulation.put("n_iter", "200");
        defaultSimulation.put("Npos", "30");
        defaultSimulation.put("Ntotal", "100");

        Map<String, Map<String, String>> values = new HashMap<>();
        values.put("defaultSimulation", defaultSimulation);

        return new SimulationsImpl(selected, Collections.singletonList("output"), values);
    }
}
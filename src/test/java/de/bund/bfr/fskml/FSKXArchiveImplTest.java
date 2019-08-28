package de.bund.bfr.fskml;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class FSKXArchiveImplTest {

    @Test
    public void test() {
        FSKXArchive archive = new FSKXArchiveImpl(createExampleSimulations());
        assertNotNull(archive.getSimulations());
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;
        List<String> inputIds = Arrays.asList("n_iter", "Npos", "Ntotal");
        Map<String, List<String>> inputValues = new HashMap<>();
        inputValues.put("defaultSimulation", Arrays.asList("200", "30", "100"));

        return new SimulationsImpl(selected, inputIds, inputValues);
    }
}
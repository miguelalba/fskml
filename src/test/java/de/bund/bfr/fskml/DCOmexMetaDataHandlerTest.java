package de.bund.bfr.fskml;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DCOmexMetaDataHandlerTest {

    @Test
    public void testModelScript() {
        // Creates new handler with model script name
        DCOmexMetaDataHandler handler = new DCOmexMetaDataHandler();
        handler.setModelScript("model.r");

        // Checks getModelScript
        assertEquals("model.r", handler.getModelScript());

        // Modifies model script and checks again
        handler.setModelScript("other.r");
        assertEquals("other.r", handler.getModelScript());
    }

    @Test
    public void testParamScript() {
        // Creates new handler with param script name
        DCOmexMetaDataHandler handler = new DCOmexMetaDataHandler();
        handler.setParametersScript("param.r");

        // Checks getParametersScript
        assertEquals("param.r", handler.getParametersScript());

        // Modifies parameters script and checks again
        handler.setParametersScript("other.r");
        assertEquals("other.r", handler.getParametersScript());
    }

    @Test
    public void testVisualizationScript() {
        // Creates new handler with visualization script name
        DCOmexMetaDataHandler handler = new DCOmexMetaDataHandler();
        handler.setVisualizationScript("viz.r");

        // Checks getVisualizationScript
        assertEquals("viz.r", handler.getVisualizationScript());

        // Modifies visualization script and checks again
        handler.setVisualizationScript("other.r");
        assertEquals("other.r", handler.getVisualizationScript());
    }

    @Test
    public void testWorkspace() {
        // Creates new handler with workspace
        DCOmexMetaDataHandler handler = new DCOmexMetaDataHandler();
        handler.setWorkspaceFile("workspace.r");

        // Checks getWorkspace
        assertEquals("workspace.r", handler.getWorkspaceFile());

        // Modifies workspace and checks again
        handler.setWorkspaceFile("other.r");
        assertEquals("other.r", handler.getWorkspaceFile());
    }
}
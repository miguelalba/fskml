package de.bund.bfr.fskml.sedml;
import de.bund.bfr.fskml.sedml.SourceScript;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSourceScript {

    @Test
    public void testConstructor() {

        // null language should throw AssertionError
        try {
            new SourceScript(null, "");
            fail("SourceScript should not accept null for language");
        } catch (AssertionError error) {
            // fails as expected
        }

        // "" language should throw AssertionError
        try {
            new SourceScript("", "");
            fail("SourceScript should not accept empty string for language");
        } catch (AssertionError error) {
            // fails as expected
        }

        // Non-null and non-empty language should pass
        new SourceScript("r", "");

        // Non-null and non-empty language and src should pass
        new SourceScript("r", "model.r");

        // Copy constructor
        SourceScript ss = new SourceScript(new SourceScript("R", "model.r"));
        assertEquals("R", ss.getLanguage());
        assertEquals("model.r", ss.getSrc());
    }

    @Test
    public void testLanguage() {
        SourceScript ss = new SourceScript("R", "model.r");
        assertEquals("R", ss.getLanguage());

        // null for language should be ignored by setLanguage
        assertFalse(ss.setLanguage(null));
        assertEquals("R", ss.getLanguage());

        // "" for language should be ignored by setLanguage
        assertFalse(ss.setLanguage(""));
        assertEquals("R", ss.getLanguage());

        assertTrue(ss.setLanguage("Matlab"));
        assertEquals("Matlab", ss.getLanguage());
    }

    @Test
    public void testSrc() {
        SourceScript ss = new SourceScript("R", "model.r");
        assertEquals("model.r", ss.getSrc());

        // null for src should be ignored by setSrc
        assertFalse(ss.setSrc(null));
        assertEquals("model.r", ss.getSrc());

        assertTrue(ss.setSrc(""));
        assertTrue(ss.getSrc().isEmpty());

        assertTrue(ss.setSrc("model2.r"));
        assertEquals("model2.r", ss.getSrc());
    }
}

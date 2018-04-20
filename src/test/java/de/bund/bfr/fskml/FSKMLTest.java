package de.bund.bfr.fskml;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FSKMLTest {

    /**
     * Test FSKML#getURIS for 1.0.0.
     */
    @Test
    public void testGetURIS100() {

        Map<String, String> expectedUris = new HashMap<>(2);
        expectedUris.put("r", "http://www-r-project.org");
        expectedUris.put("zip", "http://www.iana.org/assignments/media-types/application/zip");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 0);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.2.
     */
    @Test
    public void testGetURIS102() {

        Map<String, String> expectedUris = new HashMap<>(2);
        expectedUris.put("r", "http://www-r-project.org");
        expectedUris.put("zip", "http://www.iana.org/assignments/media-types/application/zip");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 2);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.3.
     */
    @Test
    public void testGetURIS103() {

        Map<String, String> expectedUris = new HashMap<>(2);
        expectedUris.put("r", "http://www-r-project.org");
        expectedUris.put("zip", "http://www.iana.org/assignments/media-types/application/zip");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 3);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.4.
     */
    @Test
    public void testGetURIS104() {

        Map<String, String> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 4);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.5.
     */
    @Test
    public void testGetURIS105() {

        Map<String, String> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 5);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.6.
     */
    @Test
    public void testGetURIS106() {

        Map<String, String> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 6);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.7.
     */
    @Test
    public void testGetURIS107() {

        Map<String, String> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 7);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.8.
     */
    @Test
    public void testGetURIS108() {

        Map<String, String> expectedUris = new HashMap<>();
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
        expectedUris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
        expectedUris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 8);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.9.
     */
    @Test
    public void testGetURIS109() {

        Map<String, String> expectedUris = new HashMap<>();
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
        expectedUris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
        expectedUris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 9);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.10.
     */
    @Test
    public void testGetURIS10_10() {

        Map<String, String> expectedUris = new HashMap<>();
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
        expectedUris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
        expectedUris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");
        expectedUris.put("csv", "https://www.iana.org/assignments/media-types/text/csv");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 10);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.11.
     */
    @Test
    public void testGetURIS10_11() {

        Map<String, String> expectedUris = new HashMap<>();
        expectedUris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
        expectedUris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
        expectedUris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
        expectedUris.put("r", "http://purl.org/NET/mediatypes/application/r");
        expectedUris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
        expectedUris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
        expectedUris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
        expectedUris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
        expectedUris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
        expectedUris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
        expectedUris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");
        expectedUris.put("csv", "https://www.iana.org/assignments/media-types/text/csv");
        expectedUris.put("sedml", "https://identifiers.org/combine.specifications/sed-ml");

        Map<String, String> obtainedUris = FSKML.getURIS(1, 0, 11);
        assertEquals(expectedUris, obtainedUris);
    }
}

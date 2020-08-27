package de.bund.bfr.fskml;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class URISTest {

    @Test
    public void test() throws URISyntaxException {
        assertEquals(new URI("http://purl.org/NET/mediatypes/application/zip"), URIS.zip);
        assertEquals(new URI("http://purl.org/NET/mediatypes/application/r"), URIS.r);
        assertEquals(new URI("http://purl.org/NET/mediatypes/application/x-pmf"), URIS.pmf);
        assertEquals(new URI("http://purl.org/NET/mediatypes/application/sbml+xml"), URIS.sbml);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/x-matlab"), URIS.matlab);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/x-php"), URIS.php);
    }
}
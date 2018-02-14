/*******************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
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
        assertEquals(new URI("http://identifiers.org/combine.specifications/sed-ml"), URIS.sedml);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/x-matlab"), URIS.matlab);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/x-php"), URIS.php);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/plain"), URIS.plainText);
        assertEquals(new URI("http://purl.org/NET/mediatypes/text/x-RData"), URIS.rData);
        assertEquals(new URI("https://www.iana.org/assignments/media-types/application/json"), URIS.json);
        assertEquals(new URI("https://www.iana.org/assignments/media-types/text/csv"), URIS.csv);
    }
}
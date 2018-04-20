package de.bund.bfr.fskml;

import java.util.HashMap;
import java.util.Map;

public class FSKML {

    public static Map<String, String> getURIS(int major, int minor, int patch) {

        Map<String, String> uris = new HashMap<>();

        if (major == 1 && minor == 0 && (patch == 0 || patch == 2 || patch == 3)) {
            uris.put("r", "http://www-r-project.org");
            uris.put("zip", "http://www.iana.org/assignments/media-types/application/zip");
        } else if (major == 1 && minor == 0 && (patch == 4 || patch == 5 || patch == 6 || patch == 7)) {
            uris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
            uris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
            uris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
            uris.put("r", "http://purl.org/NET/mediatypes/application/r");
            uris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
            uris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
            uris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
            uris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
        } else if (major == 1 && minor == 0 && (patch == 8 || patch == 9)) {
            uris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
            uris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
            uris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
            uris.put("r", "http://purl.org/NET/mediatypes/application/r");
            uris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
            uris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
            uris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
            uris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
            uris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
            uris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
            uris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");
        } else if (major == 1 && minor == 0 && patch == 10) {
            uris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
            uris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
            uris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
            uris.put("r", "http://purl.org/NET/mediatypes/application/r");
            uris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
            uris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
            uris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
            uris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
            uris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
            uris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
            uris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");
            uris.put("csv", "https://www.iana.org/assignments/media-types/text/csv");
        } else if (major == 1 && minor == 0 && patch == 11) {
            uris.put("zip", "http://purl.org/NET/mediatypes/application/zip");
            uris.put("tgz", "http://purl.org/NET/mediatypes/application/x-tgz");
            uris.put("tar_gz", "http://purl.org/NET/mediatypes/application/x-tar.gz");
            uris.put("r", "http://purl.org/NET/mediatypes/application/r");
            uris.put("pmf", "http://purl.org/NET/mediatypes/application/x-pmf");
            uris.put("sbml", "http://purl.org/NET/mediatypes/application/sbml+xml");
            uris.put("json", "https://wwww.iana.org/assignments/media-types/application/json");
            uris.put("matlab", "http://purl.org/NET/mediatypes/text/x-matlab");
            uris.put("php", "http://purl.org/NET/mediatypes/text/x-php");
            uris.put("plain", "http://purl.org/NET/mediatypes/text-xplain");
            uris.put("rdata", "http://purl.org/NET/mediatypes/x-RData");
            uris.put("csv", "https://www.iana.org/assignments/media-types/text/csv");
            uris.put("sedml", "https://identifiers.org/combine.specifications/sed-ml");
        }

        return uris;
    }
}

package de.bund.bfr.fskml;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collections;
import java.util.Map;

/*
this class should only contain the package information so that a json of the packages can be built
{
    "language":"R",
    "packages":{
        "triangle" : "0.12"
    }
}
 */
@JsonAutoDetect
public class PackagesImpl implements Packages {

    private final String language;
    private final Map<String, String> packages;

    PackagesImpl() {
        this("", Collections.emptyMap());
    }

    PackagesImpl(String language, Map<String, String> packages) {
        this.language = language;
        this.packages = packages;
    }

    public String getLanguage(){ return this.language;}
    public Map<String,String> getPackages(){return this.packages;}


}

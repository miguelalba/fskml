package de.bund.bfr.fskml;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

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
public class PackagesImpl implements Packages {

    private final String language;
    private final Map<String, String> packages;


    @JsonCreator
    PackagesImpl(@JsonProperty("language")String language, @JsonProperty("packages")Map<String, String> packages) {
        this.language = language;
        this.packages = packages;
    }

    public String getLanguage(){ return this.language;}
    public Map<String,String> getPackages(){return this.packages;}


}

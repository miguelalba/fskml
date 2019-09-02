package de.bund.bfr.fskml;

public class FSKXArchiveImpl implements FSKXArchive {

    private final Simulations simulations;
    private final Packages packages;
    private final String readme;
    private final String version;

    FSKXArchiveImpl(Simulations simulations, Packages packages, String readme, String version) {
        this.simulations = simulations;
        this.packages = packages;
        this.readme = readme;
        this.version = version;
    }

    @Override
    public Simulations getSimulations() {
        return simulations;
    }

    @Override
    public Packages getPackages() {
        return packages;
    }

    @Override
    public String getReadme() {
        return readme;
    }

    @Override
    public String getVersion() {
        return version;
    }
}

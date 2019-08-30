package de.bund.bfr.fskml;

public class FSKXArchiveImpl implements FSKXArchive {

    private final Simulations simulations;
    private final Packages packages;
    private final String readme;

    FSKXArchiveImpl(Simulations simulations, Packages packages, String readme) {
        this.simulations = simulations;
        this.packages = packages;
        this.readme = readme;
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
}

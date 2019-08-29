package de.bund.bfr.fskml;

public class FSKXArchiveImpl implements FSKXArchive {

    private final Simulations simulations;
    private final Packages packages;

    FSKXArchiveImpl(Simulations simulations, Packages packages)
    {
        this.simulations = simulations;
        this.packages = packages;
    }

    @Override
    public Simulations getSimulations() {
        return simulations;
    }

    @Override
    public Packages getPackages(){
        return packages;
    }

}

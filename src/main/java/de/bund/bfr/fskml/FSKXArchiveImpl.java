package de.bund.bfr.fskml;

public class FSKXArchiveImpl implements FSKXArchive {

    private final Simulations simulations;

    FSKXArchiveImpl(Simulations simulations) {
        this.simulations = simulations;
    }

    @Override
    public Simulations getSimulations() {
        return simulations;
    }
}

package de.bund.bfr.fskml;

public interface FSKXArchive {

    /**
     * @return simulations.
     */
    Simulations getSimulations();

    /**
     * @return packages information.
     */
    Packages getPackages();

    /**
     * @return readme
     */
    String getReadme();

    /**
     * @return archive version.
     */
    String getVersion();
}

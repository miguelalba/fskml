package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.ArchiveEntry;

import java.util.Set;

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

package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class IOTest {

    @Test
    public void testReadArchive() throws Exception {

        File file = createSampleArchive();
        FSKXArchive archive = IO.readArchive(file);
        Simulations simulations = archive.getSimulations();

        assertNotNull(simulations);
        assertEquals(3, simulations.getSelectedIndex());
        assertEquals("result", simulations.getOutputs().get(0));
        assertEquals(4, simulations.getInputValues().size());

        // Packages
        assertNotNull(archive.getPackages());
        assertEquals("R", archive.getPackages().getLanguage());
        assertTrue(archive.getPackages().getPackages().containsKey("triangle"));
        assertTrue(archive.getPackages().getPackages().containsValue("0.12"));

        // Readme
        assertNotNull(archive.getReadme());
        assertFalse(archive.getReadme().isEmpty());
    }

    @Test
    public void testWriteArchive() throws Exception {

        FSKXArchive archive = new FSKXArchiveImpl(createExampleSimulations(), createExamplePackages(), "readme", "2.0");

        File tempFile = File.createTempFile("archive", ".fskx");
        tempFile.deleteOnExit();
        IO.writeArchive(archive, tempFile, createSampleResources(),"r");
        assert tempFile.exists();

        try (CombineArchive ca = new CombineArchive(tempFile)) {
            assert ca.hasEntriesWithFormat(IO.SEDML_URI);
            assert ca.hasEntriesWithFormat(IO.JSON_URI);
            assert ca.hasEntriesWithFormat(IO.PLAIN_URI);
        }
    }
    private List<File> createSampleResources() throws Exception{
        List<File> resList = new ArrayList<File>();

        // Adds resources
        File pngFile = new File(IOTest.class.getResource("resource.png").getFile());
        resList.add(pngFile);

        File jpegFile = new File(IOTest.class.getResource("resource.jpeg").getFile());
        resList.add(jpegFile);

        File tiffFile = new File(IOTest.class.getResource("resource.tiff").getFile());
        resList.add(tiffFile);

        File svgFile = new File(IOTest.class.getResource("resource.svg").getFile());
        resList.add(svgFile);

        File rDataFile = new File(IOTest.class.getResource("resource.rData").getFile());
        resList.add(rDataFile);

        File bmpFile = new File(IOTest.class.getResource("resource.bmp").getFile());
        resList.add(bmpFile);

        File xlsFile = new File(IOTest.class.getResource("resource.xls").getFile());
        resList.add(xlsFile);

        File xlsxFile = new File(IOTest.class.getResource("resource.xlsx").getFile());
        resList.add(xlsxFile);
        return resList;

    }
    private File createSampleArchive() throws Exception {

        // Create CombineArchive at a temporary file that must be deleted after
        File tempFile = File.createTempFile("model", ".fskx");
        tempFile.deleteOnExit();

        try (CombineArchive archive = new CombineArchive(tempFile)) {
            // Add simulation file
            File sedmlFile = new File(IOTest.class.getResource("sim.sedml").getFile());
            ArchiveEntry entry = archive.addEntry(sedmlFile, "sim.sedml", IO.SEDML_URI);

            // Add packages file
            File packagesFile = new File(IOTest.class.getResource("packages.json").getFile());
            archive.addEntry(packagesFile, "packages.json", IO.JSON_URI);

            // Add readme
            File readmeFile = new File(IOTest.class.getResource("README.txt").getFile());
            ArchiveEntry readmeEntry = archive.addEntry(readmeFile, "README.txt", IO.PLAIN_URI);
            readmeEntry.addDescription(new FskMetaDataObject(FskMetaDataObject.ResourceType.readme).metaDataObject);

            // Adds resources
            File pngFile = new File(IOTest.class.getResource("resource.png").getFile());
            ArchiveEntry pngEntry = archive.addEntry(pngFile, "resource.png", IO.ResourceType.png.uri);

            File jpegFile = new File(IOTest.class.getResource("resource.jpeg").getFile());
            ArchiveEntry jpegEntry = archive.addEntry(jpegFile, "resource.jpeg", IO.ResourceType.jpeg.uri);

            File tiffFile = new File(IOTest.class.getResource("resource.tiff").getFile());
            ArchiveEntry tiffEntry = archive.addEntry(tiffFile, "resource.tiff", IO.ResourceType.tiff.uri);

            File svgFile = new File(IOTest.class.getResource("resource.svg").getFile());
            ArchiveEntry svgEntry = archive.addEntry(svgFile, "resource.svg", IO.ResourceType.svg.uri);

            File rDataFile = new File(IOTest.class.getResource("resource.rData").getFile());
            ArchiveEntry rDataEntry = archive.addEntry(rDataFile, "resource.rData", IO.ResourceType.rdata.uri);

            File bmpFile = new File(IOTest.class.getResource("resource.bmp").getFile());
            ArchiveEntry bmpEntry = archive.addEntry(bmpFile, "resource.bmp", IO.ResourceType.bmp.uri);

            File xlsFile = new File(IOTest.class.getResource("resource.xls").getFile());
            ArchiveEntry xlsEntry = archive.addEntry(xlsFile, "resource.xls", IO.ResourceType.xls.uri);

            File xlsxFile = new File(IOTest.class.getResource("resource.xlsx").getFile());
            ArchiveEntry xlsxntry = archive.addEntry(xlsxFile, "resource.xlsx", IO.ResourceType.xlsx.uri);

            archive.pack();
        }

        return tempFile;
    }

    private SimulationsImpl createExampleSimulations() {
        int selected = 0;

        Map<String, String> defaultSimulation = new HashMap<>();
        defaultSimulation.put("n_iter", "200");
        defaultSimulation.put("Npos", "30");
        defaultSimulation.put("Ntotal", "100");

        Map<String, Map<String, String>> values = new HashMap<>();
        values.put("defaultSimulation", defaultSimulation);

        return new SimulationsImpl(selected, Collections.singletonList("output"), values);
    }

    private PackagesImpl createExamplePackages() {
        String language = "R";
        Map<String, String> packages = new HashMap<>();
        packages.put("triangle", "0.12");
        packages.put("ggplot", "1.23");
        return new PackagesImpl(language, packages);
    }

}

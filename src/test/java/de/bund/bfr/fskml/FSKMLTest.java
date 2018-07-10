package de.bund.bfr.fskml;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import org.apache.commons.io.FileUtils;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FSKMLTest {

    /**
     * Test FSKML#getURIS for 1.0.0.
     */
    @Test
    public void testGetURIS100() {

        Map<String, URI> expectedUris = new HashMap<>(2);
        expectedUris.put("r", URI.create("http://www-r-project.org"));
        expectedUris.put("zip", URI.create("http://www.iana.org/assignments/media-types/application/zip"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 0);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.2.
     */
    @Test
    public void testGetURIS102() {

        Map<String, URI> expectedUris = new HashMap<>(2);
        expectedUris.put("r", URI.create("http://www-r-project.org"));
        expectedUris.put("zip", URI.create("http://www.iana.org/assignments/media-types/application/zip"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 2);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.3.
     */
    @Test
    public void testGetURIS103() {

        Map<String, URI> expectedUris = new HashMap<>(2);
        expectedUris.put("r", URI.create("http://www-r-project.org"));
        expectedUris.put("zip", URI.create("http://www.iana.org/assignments/media-types/application/zip"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 3);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.4.
     */
    @Test
    public void testGetURIS104() throws URISyntaxException {

        Map<String, URI> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", new URI("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 4);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.5.
     */
    @Test
    public void testGetURIS105() {

        Map<String, URI> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 5);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.6.
     */
    @Test
    public void testGetURIS106() {

        Map<String, URI> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 6);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.7.
     */
    @Test
    public void testGetURIS107() {

        Map<String, URI> expectedUris = new HashMap<>(8);
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 7);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.8.
     */
    @Test
    public void testGetURIS108() {

        Map<String, URI> expectedUris = new HashMap<>();
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("json", URI.create("https://wwww.iana.org/assignments/media-types/application/json"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));
        expectedUris.put("plain", URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        expectedUris.put("rdata", URI.create("http://purl.org/NET/mediatypes/x-RData"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 8);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.9.
     */
    @Test
    public void testGetURIS109() {

        Map<String, URI> expectedUris = new HashMap<>();
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("json", URI.create("https://wwww.iana.org/assignments/media-types/application/json"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));
        expectedUris.put("plain", URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        expectedUris.put("rdata", URI.create("http://purl.org/NET/mediatypes/x-RData"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 9);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.10.
     */
    @Test
    public void testGetURIS10_10() {

        Map<String, URI> expectedUris = new HashMap<>();
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("json", URI.create("https://wwww.iana.org/assignments/media-types/application/json"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));
        expectedUris.put("plain", URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        expectedUris.put("rdata", URI.create("http://purl.org/NET/mediatypes/x-RData"));
        expectedUris.put("csv", URI.create("https://www.iana.org/assignments/media-types/text/csv"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 10);
        assertEquals(expectedUris, obtainedUris);
    }

    /**
     * Test FSKML#getURIS for 1.0.11.
     */
    @Test
    public void testGetURIS10_11() {

        Map<String, URI> expectedUris = new HashMap<>();
        expectedUris.put("zip", URI.create("http://purl.org/NET/mediatypes/application/zip"));
        expectedUris.put("tgz", URI.create("http://purl.org/NET/mediatypes/application/x-tgz"));
        expectedUris.put("tar_gz", URI.create("http://purl.org/NET/mediatypes/application/x-tar.gz"));
        expectedUris.put("r", URI.create("http://purl.org/NET/mediatypes/application/r"));
        expectedUris.put("pmf", URI.create("http://purl.org/NET/mediatypes/application/x-pmf"));
        expectedUris.put("sbml", URI.create("http://purl.org/NET/mediatypes/application/sbml+xml"));
        expectedUris.put("json", URI.create("https://wwww.iana.org/assignments/media-types/application/json"));
        expectedUris.put("matlab", URI.create("http://purl.org/NET/mediatypes/text/x-matlab"));
        expectedUris.put("php", URI.create("http://purl.org/NET/mediatypes/text/x-php"));
        expectedUris.put("plain", URI.create("http://purl.org/NET/mediatypes/text-xplain"));
        expectedUris.put("rdata", URI.create("http://purl.org/NET/mediatypes/x-RData"));
        expectedUris.put("csv", URI.create("https://www.iana.org/assignments/media-types/text/csv"));
        expectedUris.put("sedml", URI.create("https://identifiers.org/combine.specifications/sed-ml"));

        Map<String, URI> obtainedUris = FSKML.getURIS(1, 0, 11);
        assertEquals(expectedUris, obtainedUris);
    }

    @Test
    public void testFindVersion1_0_0() throws Exception {
        File tempFile = File.createTempFile("archive", null);
        CombineArchive archive = create1_0_0_archive(tempFile);

        assertEquals(new Version(1, 0, 0), FSKML.findVersion(archive));

        tempFile.delete();
    }

    @Test
    public void testFindVersion1_0_2() throws Exception {
        File tempFile = File.createTempFile("archive", null);
        CombineArchive archive = create1_0_2_archive(tempFile);

        assertEquals(new Version(1, 0, 2), FSKML.findVersion(archive));

        tempFile.delete();
    }

    @Test
    public void testFindVersion1_0_4() throws Exception {
        File tempFile = File.createTempFile("archive", null);
        CombineArchive archive = create1_0_4_archive(tempFile);

        assertEquals(new Version(1, 0, 4), FSKML.findVersion(archive));

        tempFile.delete();
    }

    @Test
    public void testFindVersion1_0_10() throws Exception {
        File tempFile = File.createTempFile("archive", null);
        CombineArchive archive = create1_0_10_archive(tempFile);

        assertEquals(new Version(1, 0, 10), FSKML.findVersion(archive));
        tempFile.delete();
    }

    @Test
    public void testFindVersion1_0_11() throws Exception {
        File tempFile = File.createTempFile("archive", null);
        CombineArchive archive = create1_0_11_archive(tempFile);

        assertEquals(new Version(1, 0, 11), FSKML.findVersion(archive));
        tempFile.delete();
    }

    private static CombineArchive create1_0_0_archive(File file) throws Exception {

        try (CombineArchive archive = new CombineArchive(file)) {

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 0);
            URI rURI = rawUris.get("r");

            Element rdfElement = new Element("metaParent");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            Element modelScriptElement = new Element("mainScript");
            modelScriptElement.addContent("model.R");
            rdfElement.addContent(modelScriptElement);

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            Element paramScriptElement = new Element("parametersScript");
            paramScriptElement.addContent("param.R");
            rdfElement.addContent(paramScriptElement);

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");

            Element vizScriptElement = new Element("visualizationScript");
            vizScriptElement.addContent("viz.R");
            rdfElement.addContent(vizScriptElement);

            archive.addEntry(vizScriptFile, "viz.R", rURI);
            vizScriptFile.delete();

            archive.addDescription(new DefaultMetaDataObject(rdfElement));

            return archive;
        }
    }

    private static CombineArchive create1_0_2_archive(File file) throws Exception {

        try (CombineArchive archive = new CombineArchive(file)) {

            Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 2);
            URI rURI = rawUris.get("r");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("modelScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("model.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("parametersScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("param.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");
            archive.addEntry(vizScriptFile, "viz.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("visualizationScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("viz.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            vizScriptFile.delete();

            return archive;
        }
    }

    private static CombineArchive create1_0_4_archive(File file) throws Exception {
        try (CombineArchive archive = new CombineArchive(file)) {

            Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 4);
            URI rURI = rawUris.get("r");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("modelScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("model.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("parametersScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("param.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");
            archive.addEntry(vizScriptFile, "viz.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("visualizationScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("viz.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            vizScriptFile.delete();

            return archive;
        }
    }

    private static CombineArchive create1_0_8_archive(File file) throws Exception {
        try (CombineArchive archive = new CombineArchive(file)) {

            Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 8);
            URI rURI = rawUris.get("r");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("modelScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("model.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("parametersScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("param.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");
            archive.addEntry(vizScriptFile, "viz.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("visualizationScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("viz.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            vizScriptFile.delete();

            // Add JSON file
            File jsonFile = File.createTempFile("json", null);
            FileUtils.writeStringToFile(jsonFile, "", "UTF-8");
            archive.addEntry(jsonFile, "metadata.json", rawUris.get("json"));

            return archive;
        }
    }

    private static CombineArchive create1_0_10_archive(File file) throws Exception {
        try (CombineArchive archive = new CombineArchive(file)) {

            Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 10);
            URI rURI = rawUris.get("r");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("modelScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("model.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("parametersScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("param.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");
            archive.addEntry(vizScriptFile, "viz.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("visualizationScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("viz.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            vizScriptFile.delete();

            // Add CSV
            File csvFile = File.createTempFile("csv", null);
            FileUtils.writeStringToFile(csvFile, "", "UTF-8");
            archive.addEntry(csvFile, "data.csv", rawUris.get("csv"));

            csvFile.delete();

            return archive;
        }
    }

    private static CombineArchive create1_0_11_archive(File file) throws Exception {
        try (CombineArchive archive = new CombineArchive(file)) {

            Namespace dcNamespace = Namespace.getNamespace("dc", "http://purl.org/dc/elements/1.1/");
            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Map<String, URI> rawUris = FSKML.getURIS(1, 0, 11);
            URI rURI = rawUris.get("r");

            // Model script
            File modelScriptFile = File.createTempFile("model", ".R");
            FileUtils.writeStringToFile(modelScriptFile, "", "UTF-8");
            archive.addEntry(modelScriptFile, "model.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("modelScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("model.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            modelScriptFile.delete();

            // Parameters script
            File paramScriptFile = File.createTempFile("param", ".R");
            FileUtils.writeStringToFile(paramScriptFile, "", "UTF-8");
            archive.addEntry(paramScriptFile, "param.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("parametersScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("param.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            paramScriptFile.delete();

            // Visualization script
            File vizScriptFile = File.createTempFile("visualization", ".R");
            FileUtils.writeStringToFile(vizScriptFile, "", "UTF-8");
            archive.addEntry(vizScriptFile, "viz.R", rURI);

            {
                Element typeNode = factory.element("type", dcNamespace);
                typeNode.setText("visualizationScript");

                Element srcNode = factory.element("source", dcNamespace);
                srcNode.setText("viz.R");

                Element modelElement = factory.element("element");
                modelElement.addContent(typeNode);
                modelElement.addContent(srcNode);

                archive.addDescription(new DefaultMetaDataObject(modelElement));
            }

            vizScriptFile.delete();

            // Add SEDML
            File sedmlFile = File.createTempFile("sedml", null);
            FileUtils.writeStringToFile(sedmlFile, "", "UTF-8");
            archive.addEntry(sedmlFile, "sim.sedml", rawUris.get("sedml"));

            sedmlFile.delete();

            return archive;
        }
    }
}

package de.bund.bfr.fskml;

import de.bund.bfr.fskml.MetadataDocument.MetadataAnnotation;
import de.bund.bfr.fskml.MetadataDocument.RuleAnnotation;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MetadataDocumentTest {

    @Test
    public void testMetadataAnnotation() {

        // With empty metadata
        FskMetaData expected = new FskMetaData();
        MetadataAnnotation ma1 = new MetadataAnnotation("", "", "", "", "", "", "", "", "");
        MetadataAnnotation ma2 = new MetadataAnnotation(ma1.annotation);

        assertTrue(ma2.getGivenName().isEmpty());
        assertTrue(ma2.getFamilyName().isEmpty());
        assertTrue(ma2.getContact().isEmpty());
        assertTrue(ma2.getCreatedDate().isEmpty());
        assertTrue(ma2.getModifiedDate().isEmpty());
        assertTrue(ma2.getModelType().isEmpty());
        assertTrue(ma2.getRights().isEmpty());
        assertTrue(ma2.getReferenceDescription().isEmpty());
        assertTrue(ma2.getReferenceDescriptionLink().isEmpty());

        // With filled metadata
        String creator = "Creator";
        String familyName = "Family name";
        String contact = "Contact";
        String createdDate = "02.21.1989";  // my birthday
        String modifiedDate = "03.29.2017";  // current date
        String type = ModelType.EXPERIMENTAL_DATA.name();
        String rights = "Rights";
        String referenceDescription = "Reference description";
        String referenceDescriptionLink = "https://myresearch.com";

        ma1 = new MetadataAnnotation(creator, familyName, contact, createdDate, modifiedDate, type, rights,
                referenceDescription, referenceDescriptionLink);
        ma2 = new MetadataAnnotation(ma1.annotation);

        assertEquals(creator, ma2.getGivenName());
        assertEquals(familyName, ma2.getFamilyName());
        assertEquals(contact, ma2.getContact());
        assertEquals(createdDate, ma2.getCreatedDate());
        assertEquals(modifiedDate, ma2.getModifiedDate());
        assertEquals(type, ma2.getModelType());
        assertEquals(rights, ma2.getRights());
        assertEquals(referenceDescription, ma2.getReferenceDescription());
        assertEquals(referenceDescriptionLink, ma2.getReferenceDescriptionLink());
    }

    @Test
    public void testRuleAnnotation() {
        RuleAnnotation annot1 = new RuleAnnotation(ModelClass.UNKNOWN);
        RuleAnnotation annot2 = new RuleAnnotation(annot1.annotation);
        assertEquals(ModelClass.UNKNOWN, annot2.modelClass);
    }
}

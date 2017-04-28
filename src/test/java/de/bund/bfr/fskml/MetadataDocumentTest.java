package de.bund.bfr.fskml;

import de.bund.bfr.fskml.MetadataDocument.MetadataAnnotation;
import de.bund.bfr.fskml.MetadataDocument.ParameterArray;
import de.bund.bfr.fskml.MetadataDocument.RuleAnnotation;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import org.junit.Test;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;

import static org.junit.Assert.*;


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
        RuleAnnotation annot1 = new RuleAnnotation(ModelClass.UNKNOWN.fullName());
        RuleAnnotation annot2 = new RuleAnnotation(annot1.annotation);
        assertEquals(ModelClass.UNKNOWN, ModelClass.fromName(annot2.getModelClass()));
    }

    @Test
    public void testParameterArray() {
        Model model = new Model(3, 2);
        Parameter parameter = model.createParameter();
        double[] values = {0.0, 1.0, 2.0, 3.0, 4.0};
        ParameterArray parameterArray = new ParameterArray(parameter, "my_param", values);

        // Check that Parameter has an initial assignment
        assertTrue(model.getNumInitialAssignments() == 1);

        // Check variable in initial assignment
        assertEquals("my_param", parameterArray.initialAssignment.getVariable());

        // Check Real nodes in initial assignment
        ASTNode vectorNode = parameterArray.initialAssignment.getMath().getChild(0);
        assertEquals(ASTNode.Type.VECTOR, vectorNode.getType());
        double[] obtainedValues = vectorNode.getChildren().stream().mapToDouble(ASTNode::getReal).toArray();
        assertArrayEquals(values, obtainedValues, .0);

        // Check ParameterArray#getValues
        ParameterArray parameterArray2 = new ParameterArray(parameterArray.initialAssignment);
        assertArrayEquals(values, parameterArray2.getValues(), .0);
    }
}

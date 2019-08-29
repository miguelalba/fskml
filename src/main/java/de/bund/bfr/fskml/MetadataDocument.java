/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.fskml;

import de.bund.bfr.fskml.FskMetaData.DataType;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.sbml.*;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.sbml.jsbml.*;
import org.sbml.jsbml.ext.arrays.ArraysConstants;
import org.sbml.jsbml.ext.arrays.ArraysSBasePlugin;
import org.sbml.jsbml.ext.arrays.Dimension;
import org.sbml.jsbml.ext.arrays.Index;
import org.sbml.jsbml.ext.comp.*;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class MetadataDocument {

    public SBMLDocument doc;

    public MetadataDocument(FskMetaData template, Map<String, String> replacements) {
        // Creates SBMLDocument for the primary model
        this.doc = new SBMLDocument(3, 1);

        addNamespaces(this.doc);  // Adds namespaces to the SBMLDocument

        addDocumentAnnotation(template);

        // Creates model and names it
        Model model;
        if (StringUtils.isEmpty(template.modelId)) {
            model = this.doc.createModel();
        } else {
            model = this.doc.createModel(PMFUtil.createId(template.modelId));
        }

        if (StringUtils.isNotEmpty(template.modelName)) {
            model.setName(template.modelName);
        }

        // Sets model notes
        if (StringUtils.isNotEmpty(template.notes)) {
            try {
                model.setNotes(template.notes);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }

        // Creates and adds compartment to the model
        addCompartment(model, template.matrix, template.matrixDetails);

        // TODO: Creates and adds species to the model

        // Creates and adds species to the model if:
        // - template.organism is specified
        // - template.dependentVariable.unit is specified
        // - model has a compartment
        // if (!Strings.isNullOrEmpty(template.organism) &&
        // template.dependentVariable != null
        // && !Strings.isNullOrEmpty(template.dependentVariable.unit) &&
        // model.getNumCompartments() > 0) {
        // String speciesId = PMFUtil.createId(template.organism);
        // String speciesName = template.organism;
        // String speciesUnit =
        // PMFUtil.createId(template.dependentVariable.unit);
        // PMFSpecies species =
        // SBMLFactory.createPMFSpecies(model.getCompartment(0).getId(),
        // speciesId, speciesName,
        // speciesUnit);
        // model.addSpecies(species.getSpecies());
        // }

        // Add unit definitions here (before parameters)
        addUnitDefintions(model, template.dependentVariables, template.independentVariables);

        // Adds dep parameter
        template.dependentVariables.forEach(v -> addDependentVariable(model, v));

        // Adds independent parameters
        template.independentVariables.forEach(v -> addIndependentVariable(model, v));

        // Add rule
        if (model.getNumParameters() > 0 && StringUtils.isNotEmpty(model.getParameter(0).getId())) {
            AssignmentRule rule = new AssignmentRule(3, 1);
            // Assigns the id of the dependent parameter which happens to be the
            // first parameter of the model
            rule.setVariable(model.getParameter(0).getId());

            String modelClass = template.subject == null ? ModelClass.UNKNOWN.fullName() : template.subject.fullName();
            rule.setAnnotation(new RuleAnnotation(modelClass).annotation);
            model.addRule(rule);
        }


        // TODO: work in progress:
        // The ExternalModelDefinition and Submodel are requisites of the SBML replacements

        // ExternalModelDefinition
        CompSBMLDocumentPlugin docPlugin = (CompSBMLDocumentPlugin) this.doc.getPlugin(CompConstants.shortLabel);
        ExternalModelDefinition emd = docPlugin.createExternalModelDefinition("model_id");
        emd.setSource("a_file.sbml");
        emd.setModelRef("model_id");

        // Submodel
        CompModelPlugin modelPlugin = (CompModelPlugin) model.getPlugin(CompConstants.shortLabel);
        Submodel submodel = modelPlugin.createSubmodel("submodel_id");
        submodel.setModelRef("model_id");

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            Parameter parameter = model.getParameter(entry.getKey());

            CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
            ReplacedBy replacedBy = plugin.createReplacedBy();
            replacedBy.setIdRef(entry.getValue());
            replacedBy.setSubmodelRef(submodel.getId());
        }
    }

    public MetadataDocument(final SBMLDocument doc) {
        this.doc = doc;
    }

    public FskMetaData getMetaData() {
        FskMetaData template = new FskMetaData();

        Model model = this.doc.getModel();

        // caches limits
        List<Limits> limits = model.getListOfConstraints().stream().map(LimitsConstraint::new)
                .map(LimitsConstraint::getLimits).collect(Collectors.toList());

        template.modelId = model.getId();
        template.modelName = model.getName();

        // organism data
        if (model.getNumSpecies() > 0) {
            PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
            template.organism = species.getName();
            if (species.isSetDetail()) {
                template.organismDetails = species.getDetail();
            }
        }

        // matrix data
        if (model.getNumCompartments() > 0) {
            PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
            template.matrix = compartment.getName();
            if (compartment.isSetDetail()) {
                template.matrixDetails = compartment.getDetail();
            }
        }

        // creator
        MetadataAnnotation annot = new MetadataAnnotation(this.doc.getAnnotation());
        template.creator = annot.getGivenName();
        template.familyName = annot.getFamilyName();
        template.contact = annot.getContact();

        String createdDateString = annot.getCreatedDate();
        if (!createdDateString.isEmpty()) {
            try {
                template.createdDate = FskMetaData.dateFormat.parse(createdDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String modifiedDateString = annot.getModifiedDate();
        if (!modifiedDateString.isEmpty()) {
            try {
                template.modifiedDate = FskMetaData.dateFormat.parse(modifiedDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String typeString = annot.getModelType();
        if (!typeString.isEmpty()) {
            template.type = ModelType.valueOf(typeString);
        }

        template.rights = annot.getRights();
        template.referenceDescription = annot.getReferenceDescription();
        template.referenceDescriptionLink = annot.getReferenceDescriptionLink();

        if (model.getNumRules() > 0) {
            AssignmentRule rule = (AssignmentRule) model.getRule(0);
            String modelClassString = new RuleAnnotation(rule.getAnnotation()).getModelClass();
            template.subject = ModelClass.fromName(modelClassString);
        }

        // model notes
        if (model.isSetNotes()) {
            try {
                template.notes = Jsoup.parse(model.getNotesString()).text();
            } catch (XMLStreamException e) {
                System.err.println("Error accesing the notes of " + model);
                e.printStackTrace();
            }
        }

        for (Parameter param : model.getListOfParameters()) {
            Variable var = new Variable();
            var.name = param.getName();

            var.unit = "";
            if (param.isSetUnits() && !param.getUnits().equals("dimensionless")) {
                UnitDefinition unitDef = model.getUnitDefinition(param.getUnits());
                if (unitDef != null) {
                    var.unit = unitDef.getName();
                }
            }

            Limits paramLimits = limits.stream().filter(lim -> lim.getVar().equals(param.getId())).findFirst().get();
            var.min = paramLimits.getMin().toString();
            var.max = paramLimits.getMax().toString();

            // If param has value then var is an independent variable, otherwise
            // dependent
            if (param.isSetValue()) {
                if (param.getNumPlugins() > 0) {
                    var.type = DataType.array;
                    InitialAssignment ia = model.getInitialAssignment(var.name);
                    double[] array = new ParameterArray(ia).getValues();
                    var.value = "c(" + StringUtils.join(array, ",") + ")";
                } else {
                    var.type = param.getValue() % 1 == 0 ? DataType.integer : DataType.numeric;
                    var.value = Double.toString(param.getValue());
                }

                template.independentVariables.add(var);
            } else {
                var.type = null;
                var.value = "";

                template.dependentVariables.add(var);
            }
        }

        template.hasData = false;

        return template;
    }

    /**
     * Creates a {@link MetadataDocument} with no replacements.
     *
     * @param template FSK model metadata
     */
    public MetadataDocument(FskMetaData template) {

        // Creates SBMLDocument for the primary model
        this.doc = new SBMLDocument(3, 1);

        addNamespaces(this.doc);  // Adds namespaces to the SBMLDocument

        addDocumentAnnotation(template);

        // Creates model and names it
        Model model;
        if (StringUtils.isEmpty(template.modelId)) {
            model = this.doc.createModel();
        } else {
            model = this.doc.createModel(PMFUtil.createId(template.modelId));
        }

        if (StringUtils.isNotEmpty(template.modelName)) {
            model.setName(template.modelName);
        }

        // Set model notes
        if (StringUtils.isNotEmpty(template.notes)) {
            try {
                model.setNotes(template.notes);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }

        // Creates and add compartment to the model
        addCompartment(model, template.matrix, template.matrixDetails);

        // Add unit definitions here (before parameters)
        addUnitDefintions(model, template.dependentVariables, template.independentVariables);

        // Adds dependent parameters
        template.dependentVariables.forEach(v -> addDependentVariable(model, v));

        // Add independent parameters
        template.independentVariables.forEach(v -> addIndependentVariable(model, v));

        // Add rule
        if (model.getNumParameters() > 0 && StringUtils.isNotEmpty(model.getParameter(0).getId())) {
            AssignmentRule rule = new AssignmentRule(3, 1);
            // Assigns the id of the dependent parameter which happens to be the first parameter of the model
            rule.setVariable(model.getParameter(0).getId());

            String modelClass = template.subject == null ? ModelClass.UNKNOWN.fullName() : template.subject.fullName();
            rule.setAnnotation(new RuleAnnotation(modelClass).annotation);
            model.addRule(rule);
        }
    }

    public Map<String, String> getReplacements(final SBMLDocument doc) {

        Map<String, String> replacements = new HashMap<>();

        for (Parameter parameter : doc.getModel().getListOfParameters()) {
            if (parameter.getNumPlugins() == 0) continue;

            CompSBasePlugin plugin = (CompSBasePlugin) parameter.getPlugin(CompConstants.shortLabel);
            replacements.put(parameter.getId(), plugin.getReplacedBy().getIdRef());
        }

        return replacements;
    }

    private void addNamespaces(final SBMLDocument doc) {
        doc.addDeclaredNamespace("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
        doc.addDeclaredNamespace("xmlns:pmf", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
        doc.addDeclaredNamespace("xmlns:dc", "http://purl.org/dc/elements/1.1");
        doc.addDeclaredNamespace("xmlns:dcterms", "http://purl.org/dc/terms/");
        doc.addDeclaredNamespace("xmlns:pmmlab",
                "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
        doc.addDeclaredNamespace("xmlns:numl", "http://www.numl.org/numl/level1/version1");
        doc.addDeclaredNamespace("xmlns:xlink", "http://www.w3.org/1999/xlink");
    }

    private void addCompartment(final Model model, final String matrix, final String matrixDetails) {
        if (StringUtils.isNotEmpty(matrix)) {
            PMFCompartment compartment = SBMLFactory.createPMFCompartment(PMFUtil.createId(matrix),
                    matrix);
            if (StringUtils.isNotEmpty(matrixDetails)) {
                compartment.setDetail(matrixDetails);
            }
            model.addCompartment(compartment.getCompartment());
        }
    }

    /**
     * Add {@link UnitDefinition} of the units used in dependent and independent variables to the SBML model.
     *
     * @param model  SBML model
     * @param deps   List of dependent variables
     * @param indeps List of independent variables
     */
    private void addUnitDefintions(final Model model, final List<Variable> deps, final List<Variable> indeps) {
        Set<String> unitsSet = new LinkedHashSet<>();
        deps.forEach(v -> unitsSet.add(v.unit.trim()));
        indeps.forEach(v -> unitsSet.add(v.unit.trim()));
        for (String unit : unitsSet) {
            UnitDefinition ud = model.createUnitDefinition(PMFUtil.createId(unit));
            ud.setName(unit);
        }
    }

    /**
     * @param template FSK model metadata
     */
    private void addDocumentAnnotation(final FskMetaData template) {

        // null is replaces with empty string
        String givenName = StringUtils.defaultString(template.creator);
        String familyName = StringUtils.defaultString(template.familyName);
        String contact = StringUtils.defaultString(template.contact);
        String createdDate = template.createdDate == null ? "" : FskMetaData.dateFormat.format(template
                .createdDate);
        String modifiedDate = template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template
                .modifiedDate);
        String type = template.type == null ? "" : template.type.name();
        String rights = StringUtils.defaultString(template.rights);
        String referenceDescription = StringUtils.defaultString(template.referenceDescription);
        String referenceDescriptionLink = StringUtils.defaultString(template.referenceDescriptionLink);

        Annotation annotation = new MetadataAnnotation(givenName, familyName, contact, createdDate, modifiedDate,
                type, rights, referenceDescription, referenceDescriptionLink).annotation;
        this.doc.setAnnotation(annotation);
    }

    private void addDependentVariable(final Model model, final Variable v) {
        if (StringUtils.isEmpty(v.name))
            return;

        Parameter param = model.createParameter(PMFUtil.createId(v.name));
        param.setName(v.name);

        // Write unit if v.unit is not null
        if (StringUtils.isNotEmpty(v.unit)) {
            try {
                param.setUnits(PMFUtil.createId(v.unit));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        // Write min and max values if not null
        if (StringUtils.isNoneEmpty(v.min, v.max)) {
            try {
                double min = Double.parseDouble(v.min);
                double max = Double.parseDouble(v.max);
                LimitsConstraint lc = new LimitsConstraint(v.name.replaceAll("\\.", "\\_"), min, max);
                if (lc.getConstraint() != null) {
                    model.addConstraint(lc.getConstraint());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void addIndependentVariable(final Model model, final Variable v) {
        if (StringUtils.isEmpty(v.name))
            return;

        Parameter param = model.createParameter(PMFUtil.createId(v.name));
        param.setName(v.name);

        // Write value if v.type and v.value are not null
        if (v.type != null && StringUtils.isNotEmpty(v.value)) {

            if (v.type == DataType.integer) {
                param.setValue(Double.valueOf(v.value).intValue());
            } else if (v.type == DataType.numeric) {
                param.setValue(Double.parseDouble(v.value));
            } else if (v.type == DataType.array) {
                param.setValue(0);

                // Remove "c(" and ")" around the values in the array
                String cleanArray = v.value.substring(2, v.value.length() - 1);
                // Split values into tokens using the comma as splitter
                String[] tokens = cleanArray.split(",");
                double[] values = Arrays.stream(tokens).mapToDouble(Double::parseDouble).toArray();

                new ParameterArray(param, v.name, values);
            }
        } else if (v.type == DataType.character) {
            // TODO: Add character
            return;
        }

        // Write unit if v.unit is not null
        if (StringUtils.isNotEmpty(v.unit)) {
            try {
                param.setUnits(PMFUtil.createId(v.unit));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        // Write min and max values if not null
        if (StringUtils.isNoneEmpty(v.min, v.max)) {
            try {
                double min = Double.parseDouble(v.min);
                double max = Double.parseDouble(v.max);
                LimitsConstraint lc = new LimitsConstraint(param.getId(), min, max);
                if (lc.getConstraint() != null) {
                    model.addConstraint(lc.getConstraint());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    static class MetadataAnnotation {

        Annotation annotation;

        /**
         * Creates an {@link Annotation} with metadata encoded with Dublin Core.
         *
         * @param creator                  Empty string if missing
         * @param familyName               Empty string if missing
         * @param contact                  Empty string if missing
         * @param createdDate              Empty string if missing
         * @param modifiedDate             Empty string if missing
         * @param type                     Empty string if missing
         * @param rights                   Empty string if missing
         * @param referenceDescription     Empty string missing
         * @param referenceDescriptionLink Empty string missing
         */
        MetadataAnnotation(final String creator, final String familyName, final String contact,
                           final String createdDate, final String modifiedDate, final String type,
                           final String rights, final String referenceDescription, final String referenceDescriptionLink) {

            XMLTriple pmfTriple = new XMLTriple("metadata", "", "pmf");
            XMLNode pmfNode = new XMLNode(pmfTriple);

            // Builds creator node
            if (!creator.isEmpty() || !familyName.isEmpty() || !contact.isEmpty()) {
                XMLNode creatorNode = new XMLNode(new XMLTriple("creator", "", "dc"));
                creatorNode.addChild(new XMLNode(creator + "." + familyName + "." + contact));
                pmfNode.addChild(creatorNode);
            }

            // Builds created date node
            if (!createdDate.isEmpty()) {
                XMLNode createdNode = new XMLNode(new XMLTriple("created", "", "dcterms"));
                createdNode.addChild(new XMLNode(createdDate));
                pmfNode.addChild(createdNode);
            }

            // Builds modified date node
            if (!modifiedDate.isEmpty()) {
                XMLNode modifiedNode = new XMLNode(new XMLTriple("modified", "", "dcterms"));
                modifiedNode.addChild(new XMLNode(modifiedDate));
                pmfNode.addChild(modifiedNode);
            }

            // Builds type node
            if (!type.isEmpty()) {
                XMLNode typeNode = new XMLNode(new XMLTriple("type", "", "dc"));
                typeNode.addChild(new XMLNode(type));
                pmfNode.addChild(typeNode);
            }

            // Builds rights node
            if (!rights.isEmpty()) {
                XMLNode rightsNode = new XMLNode(new XMLTriple("rights", "", "dc"));
                rightsNode.addChild(new XMLNode(rights));
                pmfNode.addChild(rightsNode);
            }

            // Builds reference description node
            if (!referenceDescription.isEmpty()) {
                XMLNode refdescNode = new XMLNode(new XMLTriple("description", "", "dc"));
                refdescNode.addChild(new XMLNode(referenceDescription));
                pmfNode.addChild(refdescNode);
            }

            // Builds reference description link node
            if (!referenceDescriptionLink.isEmpty()) {
                XMLNode refdescLinkNode = new XMLNode(new XMLTriple("source", "", "dc"));
                refdescLinkNode.addChild(new XMLNode(referenceDescriptionLink));
                pmfNode.addChild(refdescLinkNode);
            }

            // Create annotation
            this.annotation = new Annotation();
            this.annotation.setNonRDFAnnotation(pmfNode);
        }

        MetadataAnnotation(final Annotation annotation) {
            this.annotation = annotation;
        }

        /**
         * @return given name or empty string if missing.
         */
        String getGivenName() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("creator", "");
            return node == null ? "" : node.getChild(0).getCharacters().split("\\.", 3)[0];
        }

        /**
         * Return family name or empty string if missing.
         */
        String getFamilyName() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("creator", "");
            return node == null ? "" : node.getChild(0).getCharacters().split("\\.", 3)[1];
        }

        /**
         * @return contact or empty string if missing.
         */
        String getContact() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("creator", "");
            return node == null ? "" : node.getChild(0).getCharacters().split("\\.", 3)[2];
        }

        /**
         * @return created date or empty string if missing.
         */
        String getCreatedDate() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("created", "");
            return node == null ? "" : node.getChild(0).getCharacters();
        }

        /**
         * @return modified date or empty string if missing.
         */
        String getModifiedDate() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("modified", "");
            return node == null ? "" : node.getChild(0).getCharacters();
        }

        /**
         * @return model type or empty string if missing.
         */
        String getModelType() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("type", "");
            return node == null ? "" : node.getChild(0).getCharacters();

        }

        /**
         * @return model rights or empty string if missing.
         */
        String getRights() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("rights", "");
            return node == null ? "" : node.getChild(0).getCharacters();
        }

        /**
         * @return the reference description or empty string if missing.
         */
        String getReferenceDescription() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("description", "");
            return node == null ? "" : node.getChild(0).getCharacters();
        }

        /**
         * @return the reference description link or empty string if missing.
         */
        String getReferenceDescriptionLink() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "")
                    .getChildElement("source", "");
            return node == null ? "" : node.getChild(0).getCharacters();
        }
    }

    /**
     * Reduced version of PMF-ML ModelRuleAnnotation with only the model class.
     */
    static class RuleAnnotation {

        final Annotation annotation;

        /**
         * @param modelClass Empty string if missing
         */
        RuleAnnotation(final String modelClass) {
            // Builds metadata node
            XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
            XMLNode pmfNode = new XMLNode(pmfTriple);

            // Builds model class node
            XMLNode modelClassNode = new XMLNode(new XMLTriple("subject", null, "pmmlab"));
            modelClassNode.addChild(new XMLNode(modelClass));
            pmfNode.addChild(modelClassNode);

            // Create annotation
            this.annotation = new Annotation();
            this.annotation.setNonRDFAnnotation(pmfNode);
        }

        RuleAnnotation(final Annotation annotation) throws IllegalArgumentException {
            this.annotation = annotation;
        }

        /**
         * @return model class
         */
        String getModelClass() {
            XMLNode node = annotation.getNonRDFannotation().getChildElement("metadata", "").getChildElement
                    ("subject", "");
            return node.getChild(0).getCharacters();
        }
    }

    /**
     * Creates a {@link Dimension} and an {@link InitialAssignment} containing a selector node
     * with all the values of an array.
     * <p>
     * Structure:
     * <pre>
     * {@code
     * apply>
     *     <selector>
     *     <vector>
     *         <cn>0</cn>
     *         <cn>1</cn>
     *         ...
     *         <cn>n</cn>
     *      </vector>
     * </apply>
     * }
     * </pre>
     */
    static class ParameterArray {

        final InitialAssignment initialAssignment;

        ParameterArray(final Parameter parameter, final String var, final double[] values) {
            // Create dimension within parameter
            ArraysSBasePlugin parameterPlugin = (ArraysSBasePlugin) parameter.getPlugin(ArraysConstants.shortLabel);
            Dimension dimension = parameterPlugin.createDimension("d0");
            dimension.setSize(Integer.toString(values.length));
            dimension.setArrayDimension(0);

            // Create initial assignment
            this.initialAssignment = parameter.getModel().createInitialAssignment();
            this.initialAssignment.setVariable(var);

            // Create math of initial assignment with a selector function
            ASTNode node = new ASTNode(ASTNode.Type.FUNCTION_SELECTOR, this.initialAssignment);
            ASTNode vectorNode = new ASTNode(ASTNode.Type.VECTOR, this.initialAssignment);
            for (Double d : values) {
                vectorNode.addChild(new ASTNode(d));
            }
            node.addChild(vectorNode);
            this.initialAssignment.setMath(node);

            ArraysSBasePlugin iaPlugin = (ArraysSBasePlugin) this.initialAssignment.getPlugin(ArraysConstants.shortLabel);

            // Add index to initial assignment
            Index index = iaPlugin.createIndex();
            index.setReferencedAttribute("symbol");
            index.setArrayDimension(0);
            index.setMath(new ASTNode("d0"));
        }

        ParameterArray(InitialAssignment assignment) {
            this.initialAssignment = assignment;
        }

        double[] getValues() {
            return this.initialAssignment.getMath().getChild(0).getChildren().stream().mapToDouble(ASTNode::getReal)
                    .toArray();
        }
    }
}

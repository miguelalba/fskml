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
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class MetadataDocument {

    public SBMLDocument doc;

    public MetadataDocument(FskMetaData template) {
        // Creates SBMLDocument for the primary model
        this.doc = new SBMLDocument(3, 1);

        // Adds namespaces to the sbmlDocument
        this.doc.addDeclaredNamespace("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        this.doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
        this.doc.addDeclaredNamespace("xmlns:pmf", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
        this.doc.addDeclaredNamespace("xmlns:dc", "http://purl.org/dc/elements/1.1");
        this.doc.addDeclaredNamespace("xmlns:dcterms", "http://purl.org/dc/terms/");
        this.doc.addDeclaredNamespace("xmlns:pmmlab",
                "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
        this.doc.addDeclaredNamespace("xmlns:numl", "http://www.numl.org/numl/level1/version1");
        this.doc.addDeclaredNamespace("xmlns:xlink", "http//www.w3.org/1999/xlink");

        // Adds document annotation
        {
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

            Annotation annotation = new MetadataAnnotation(givenName, familyName, contact, createdDate,
                    modifiedDate, type, rights, referenceDescription, referenceDescriptionLink).annotation;
            this.doc.setAnnotation(annotation);
        }

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
        if (StringUtils.isNotEmpty(template.matrix)) {
            PMFCompartment compartment = SBMLFactory.createPMFCompartment(PMFUtil.createId(template.matrix),
                    template.matrix);
            if (StringUtils.isNotEmpty(template.matrixDetails)) {
                compartment.setDetail(template.matrixDetails);
            }
            model.addCompartment(compartment.getCompartment());
        }

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
        Set<String> unitsSet = new LinkedHashSet<>();
        template.dependentVariables.forEach(v -> unitsSet.add(v.unit.trim()));
        template.independentVariables.forEach(v -> unitsSet.add(v.unit.trim()));
        for (String unit : unitsSet) {
            UnitDefinition ud = model.createUnitDefinition(PMFUtil.createId(unit));
            ud.setName(unit);
        }

        // Adds dep parameter
        for (Variable v : template.dependentVariables) {
            if (StringUtils.isEmpty(v.name))
                continue;

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

        // Adds independent parameters
        for (Variable v : template.independentVariables) {
            if (StringUtils.isEmpty(v.name))
                continue;

            Parameter param = model.createParameter(PMFUtil.createId(v.name));
            param.setName(v.name);

            // Write value if v.type and v.value are not null
            if (v.type != null && StringUtils.isNotEmpty(v.value)) {
                switch (v.type) {
                    case integer:
                        param.setValue(Double.valueOf(v.value).intValue());
                        break;
                    case numeric:
                        param.setValue(Double.parseDouble(v.value));
                        break;
                    case array:
                        // TODO: Add array
                        try {
                            param.setValue(0);
                            addArrayToParameter(param, v.value, v.name);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case character:
                        // TODO: Add character
                        break;
                }
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
    }

    private static void addArrayToParameter(Parameter parameter, String value, String var) throws ParseException {
        String cleanArray = value.substring(2, value.length() - 1);
        String[] tokens = cleanArray.split(",");
        List<Double> array = Arrays.stream(tokens).map(Double::parseDouble).collect(Collectors.toList());
        int size = tokens.length;

        // Create dimension within parameter
        ArraysSBasePlugin arrayPlugin = (ArraysSBasePlugin) parameter.getPlugin(ArraysConstants.shortLabel);
        Dimension dim = arrayPlugin.createDimension("d0");
        dim.setSize(Integer.toString(size));
        dim.setArrayDimension(0);

        // Create initial assignment
        InitialAssignment ia = parameter.getModel().createInitialAssignment();
        ia.setVariable(var);

        // Create math of initial assignment with a selector function
        ia.setMath(new SelectorNode(array, ia).node);

        ArraysSBasePlugin iaPlugin = (ArraysSBasePlugin) ia.getPlugin(ArraysConstants.shortLabel);

        // Add dimension to initial assignment
        iaPlugin.addDimension(dim.clone());

        // Add index to initial assignment
        Index index = iaPlugin.createIndex();
        index.setReferencedAttribute("symbol");
        index.setArrayDimension(0);
        index.setMath(new ASTNode("d0"));
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
            template.subject = ModelClass.valueOf(modelClassString);
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
                    List<Double> array = new SelectorNode(ia.getMath()).getArray();
                    var.value = "c(" + array.stream().map(d -> Double.toString(d)).collect(Collectors.joining(","))
                            + ")";
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
}

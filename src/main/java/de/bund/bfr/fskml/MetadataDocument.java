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
        this.doc.setAnnotation(new MetadataAnnotation(template).annotation);

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

            ModelClass modelClass = template.subject == null ? ModelClass.UNKNOWN : template.subject;
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
        template.creator = annot.givenName;
        template.familyName = annot.familyName;
        template.contact = annot.contact;
        template.createdDate = annot.createdDate;
        template.modifiedDate = annot.modifiedDate;
        template.type = annot.type;
        template.rights = annot.rights;
        template.referenceDescription = annot.referenceDescription;
        template.referenceDescriptionLink = annot.referenceDescriptionLink;

        if (model.getNumRules() > 0) {
            AssignmentRule rule = (AssignmentRule) model.getRule(0);
            template.subject = new RuleAnnotation(rule.getAnnotation()).modelClass;
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

    public static class MetadataAnnotation {

        public Annotation annotation;

        // Fields with metadata
        public String givenName;
        public String familyName;
        public String contact;
        public Date createdDate;
        public Date modifiedDate;
        public ModelType type;
        public String rights;
        public String referenceDescription;
        public String referenceDescriptionLink;

        public MetadataAnnotation(final FskMetaData metadata) {

            XMLTriple pmfTriple = new XMLTriple("metadata", "", "pmf");
            XMLNode pmfNode = new XMLNode(pmfTriple);

            // Builds creator node
            if (StringUtils.isNotEmpty(metadata.creator) || StringUtils.isNotEmpty(metadata.familyName)
                    || StringUtils.isNotEmpty(metadata.contact)) {
                this.givenName = StringUtils.defaultString(metadata.creator);
                this.familyName = StringUtils.defaultString(metadata.familyName);
                this.contact = StringUtils.defaultString(metadata.contact);

                String creator = this.givenName + "." + this.familyName + "." + this.contact;
                XMLNode creatorNode = new XMLNode(new XMLTriple("creator", null, "dc"));
                creatorNode.addChild(new XMLNode(creator));
                pmfNode.addChild(creatorNode);
            }

            // Builds created date node
            if (metadata.createdDate != null) {
                XMLNode createdNode = new XMLNode(new XMLTriple("created", "", "dcterms"));
                createdNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.createdDate)));
                pmfNode.addChild(createdNode);
            }

            // Builds modified date node
            if (metadata.modifiedDate != null) {
                XMLNode modifiedNode = new XMLNode(new XMLTriple("modified", "", "dcterms"));
                modifiedNode.addChild(new XMLNode(FskMetaData.dateFormat.format(metadata.modifiedDate)));
                pmfNode.addChild(modifiedNode);
            }

            // Builds type node
            if (metadata.type != null) {
                XMLNode typeNode = new XMLNode(new XMLTriple("type", "", "dc"));
                typeNode.addChild(new XMLNode(metadata.type.name()));
                pmfNode.addChild(typeNode);
            }

            // Builds rights node
            if (StringUtils.isNotEmpty(metadata.rights)) {
                XMLNode rightsNode = new XMLNode(new XMLTriple("rights", "", "dc"));
                rightsNode.addChild(new XMLNode(metadata.rights));
                pmfNode.addChild(rightsNode);
            }

            // Builds reference description node
            if (StringUtils.isNotEmpty(metadata.referenceDescription)) {
                XMLNode refdescNode = new XMLNode(new XMLTriple("description", "", "dc"));
                refdescNode.addChild(new XMLNode(metadata.referenceDescription));
                pmfNode.addChild(refdescNode);
            }

            // Builds reference description link node
            if (StringUtils.isNotEmpty(metadata.referenceDescriptionLink)) {
                XMLNode refdescLinkNode = new XMLNode(new XMLTriple("source", "", "dc"));
                refdescLinkNode.addChild(new XMLNode(metadata.referenceDescriptionLink));
                pmfNode.addChild(refdescLinkNode);
            }

            // Create annotation
            this.annotation = new Annotation();
            this.annotation.setNonRDFAnnotation(pmfNode);
        }

        public MetadataAnnotation(final Annotation annotation) {
            XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement("metadata", "");

            // Reads creatorNode
            XMLNode creatorNode = pmfNode.getChildElement("creator", "");
            if (creatorNode != null) {
                String[] tempStrings = creatorNode.getChild(0).getCharacters().split("\\.", 3);
                this.givenName = StringUtils.defaultString(tempStrings[0]);
                this.familyName = StringUtils.defaultString(tempStrings[1]);
                this.contact = StringUtils.defaultString(tempStrings[2]);
            }

            // Reads created date
            XMLNode createdNode = pmfNode.getChildElement("created", "");
            if (createdNode != null) {
                try {
                    this.createdDate = FskMetaData.dateFormat.parse(createdNode.getChild(0).getCharacters());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Reads modified date
            XMLNode modifiedNode = pmfNode.getChildElement("modified", "");
            if (modifiedNode != null) {
                try {
                    this.modifiedDate = FskMetaData.dateFormat.parse(modifiedNode.getChild(0).getCharacters());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Reads model type
            XMLNode typeNode = pmfNode.getChildElement("type", "");
            if (typeNode != null) {
                this.type = ModelType.valueOf(typeNode.getChild(0).getCharacters());
            }

            // Reads rights
            XMLNode rightsNode = pmfNode.getChildElement("rights", "");
            if (rightsNode != null) {
                this.rights = rightsNode.getChild(0).getCharacters();
            }

            // Reads reference description
            XMLNode refdescNode = pmfNode.getChildElement("description", "");
            if (refdescNode != null) {
                this.referenceDescription = refdescNode.getChild(0).getCharacters();
            }

            // Reads reference description link
            XMLNode refdescLinkNode = pmfNode.getChildElement("source", "");
            if (refdescLinkNode != null) {
                this.referenceDescriptionLink = refdescLinkNode.getChild(0).getCharacters();
            }

            // Copies annotation
            this.annotation = annotation;
        }
    }

    /**
     * Reduced version of PMF-ML ModelRuleAnnotation with only the model class.
     */
    public static class RuleAnnotation {

        public static final String SUBJECT_NAME = "subject";
        public static final String SUBJECT_URI = "pmmlab";

        public ModelClass modelClass;
        public Annotation annotation;

        public RuleAnnotation(final ModelClass modelClass) {
            // Builds metadata node
            XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
            XMLNode pmfNode = new XMLNode(pmfTriple);

            // Builds model class node
            XMLTriple modelClassTriple = new XMLTriple(SUBJECT_NAME, null, SUBJECT_URI);
            XMLNode modelClassNode = new XMLNode(modelClassTriple);
            modelClassNode.addChild(new XMLNode(modelClass.fullName()));
            pmfNode.addChild(modelClassNode);

            // Create annotation
            this.annotation = new Annotation();
            this.annotation.setNonRDFAnnotation(pmfNode);
        }

        public RuleAnnotation(final Annotation annotation) {

            XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement("metadata", "");

            // Reads model class node
            XMLNode modelClassNode = pmfNode.getChildElement(SUBJECT_NAME, "");
            if (modelClassNode != null) {
                this.modelClass = ModelClass.fromName(modelClassNode.getChild(0).getCharacters());
            }

            // Copies annotation
            this.annotation = annotation;
        }
    }
}

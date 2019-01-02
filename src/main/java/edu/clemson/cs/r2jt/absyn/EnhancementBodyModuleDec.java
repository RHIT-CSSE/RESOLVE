/*
 * EnhancementBodyModuleDec.java
 * ---------------------------------
 * Copyright (c) 2019
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.Iterator;
import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;

public class EnhancementBodyModuleDec extends AbstractParameterizedModuleDec {

    // Variables

    /** The name member. */
    private PosSymbol name;

    /** The performance profile name member. */
    private PosSymbol profileName;

    /** The enhancementName member. */
    private PosSymbol enhancementName;

    /** The conceptName member. */
    private PosSymbol conceptName;

    /** The enhancementBodies member. */
    private List<EnhancementBodyItem> enhancementBodies;

    /** The requires member. */
    private Exp requires;

    /** The conventions member. */
    private List<Exp> conventions;

    /** The corrs member. */
    private List<Exp> corrs;

    /** The facilityInit member. */
    private InitItem facilityInit;

    /** The facilityFinal member. */
    private FinalItem facilityFinal;

    /** The decs member. */
    private List<Dec> decs;

    // Constructors

    public EnhancementBodyModuleDec() {};

    public EnhancementBodyModuleDec(PosSymbol name, PosSymbol profileName,
            List<ModuleParameterDec> parameters, PosSymbol enhancementName,
            PosSymbol conceptName, List<EnhancementBodyItem> enhancementBodies,
            List<UsesItem> usesItems, Exp requires, List<Exp> conventions,
            List<Exp> corrs, InitItem facilityInit, FinalItem facilityFinal,
            List<Dec> decs) {
        this.name = name;
        this.profileName = profileName;
        this.parameters = parameters;
        this.enhancementName = enhancementName;
        this.conceptName = conceptName;
        this.enhancementBodies = enhancementBodies;
        this.usesItems = usesItems;
        this.requires = requires;
        this.conventions = conventions;
        this.corrs = corrs;
        this.facilityInit = facilityInit;
        this.facilityFinal = facilityFinal;
        this.decs = decs;
    }

    // Accessor Methods

    // -----------------------------------------------------------
    // Get Methods
    // -----------------------------------------------------------

    /** Returns the value of the name variable. */
    public PosSymbol getName() {
        return name;
    }

    /** Returns the value of the profileName variable. */
    public PosSymbol getProfileName() {
        return profileName;
    }

    /** Returns the value of the enhancementName variable. */
    public PosSymbol getEnhancementName() {
        return enhancementName;
    }

    /** Returns the value of the conceptName variable. */
    public PosSymbol getConceptName() {
        return conceptName;
    }

    /** Returns the value of the enhancementBodies variable. */
    public List<EnhancementBodyItem> getEnhancementBodies() {
        return enhancementBodies;
    }

    /** Returns the value of the usesItems variable. */
    public List<UsesItem> getUsesItems() {
        return usesItems;
    }

    /** Returns the value of the requires variable. */
    public Exp getRequires() {
        return requires;
    }

    /** Returns the value of the conventions variable. */
    public List<Exp> getConventions() {
        return conventions;
    }

    /** Returns the value of the corrs variable. */
    public List<Exp> getCorrs() {
        return corrs;
    }

    /** Returns the value of the facilityInit variable. */
    public InitItem getFacilityInit() {
        return facilityInit;
    }

    /** Returns the value of the facilityFinal variable. */
    public FinalItem getFacilityFinal() {
        return facilityFinal;
    }

    /** Returns the value of the decs variable. */
    public List<Dec> getDecs() {
        return decs;
    }

    /** Returns a list of procedures in this realization. */
    public List<Symbol> getLocalProcedureNames() {
        List<Symbol> retval = new List<Symbol>();
        Iterator<Dec> it = decs.iterator();
        while (it.hasNext()) {
            Dec d = it.next();
            if (d instanceof ProcedureDec) {
                retval.add(d.getName().getSymbol());
            }
        }
        return retval;
    }

    // -----------------------------------------------------------
    // Set Methods
    // -----------------------------------------------------------

    /** Sets the name variable to the specified value. */
    public void setName(PosSymbol name) {
        this.name = name;
    }

    /** Sets the profileName variable to the specified value. */
    public void setProfileName(PosSymbol name) {
        this.profileName = name;
    }

    /** Sets the enhancementName variable to the specified value. */
    public void setEnhancementName(PosSymbol enhancementName) {
        this.enhancementName = enhancementName;
    }

    /** Sets the conceptName variable to the specified value. */
    public void setConceptName(PosSymbol conceptName) {
        this.conceptName = conceptName;
    }

    /** Sets the enhancementBodies variable to the specified value. */
    public void setEnhancementBodies(List<EnhancementBodyItem> enhancementBodies) {
        this.enhancementBodies = enhancementBodies;
    }

    /** Sets the usesItems variable to the specified value. */
    public void setUsesItems(List<UsesItem> usesItems) {
        this.usesItems = usesItems;
    }

    /** Sets the requires variable to the specified value. */
    public void setRequires(Exp requires) {
        this.requires = requires;
    }

    /** Sets the conventions variable to the specified value. */
    public void setConventions(List<Exp> conventions) {
        this.conventions = conventions;
    }

    /** Sets the corrs variable to the specified value. */
    public void setCorrs(List<Exp> corrs) {
        this.corrs = corrs;
    }

    /** Sets the facilityInit variable to the specified value. */
    public void setFacilityInit(InitItem facilityInit) {
        this.facilityInit = facilityInit;
    }

    /** Sets the facilityFinal variable to the specified value. */
    public void setFacilityFinal(FinalItem facilityFinal) {
        this.facilityFinal = facilityFinal;
    }

    /** Sets the decs variable to the specified value. */
    public void setDecs(List<Dec> decs) {
        this.decs = decs;
    }

    // Public Methods

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitEnhancementBodyModuleDec(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {

        StringBuffer sb = new StringBuffer();

        printSpace(indent, sb);
        sb.append("EnhancementBodyModuleDec\n");

        if (name != null) {
            sb.append(name.asString(indent + increment, increment));
        }

        if (parameters != null) {
            sb.append(parameters.asString(indent + increment, increment));
        }

        if (enhancementName != null) {
            sb.append(enhancementName.asString(indent + increment, increment));
        }

        if (conceptName != null) {
            sb.append(conceptName.asString(indent + increment, increment));
        }

        if (enhancementBodies != null) {
            sb
                    .append(enhancementBodies.asString(indent + increment,
                            increment));
        }

        if (usesItems != null) {
            sb.append(usesItems.asString(indent + increment, increment));
        }

        if (requires != null) {
            sb.append(requires.asString(indent + increment, increment));
        }

        if (conventions != null) {
            sb.append(conventions.asString(indent + increment, increment));
        }

        if (corrs != null) {
            sb.append(corrs.asString(indent + increment, increment));
        }

        if (facilityInit != null) {
            sb.append(facilityInit.asString(indent + increment, increment));
        }

        if (facilityFinal != null) {
            sb.append(facilityFinal.asString(indent + increment, increment));
        }

        if (decs != null) {
            sb.append(decs.asString(indent + increment, increment));
        }

        return sb.toString();
    }
}

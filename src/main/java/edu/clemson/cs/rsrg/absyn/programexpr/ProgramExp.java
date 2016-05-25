/**
 * ProgramExp.java
 * ---------------------------------
 * Copyright (c) 2016
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.cs.rsrg.absyn.programexpr;

import edu.clemson.cs.r2jt.typeandpopulate2.programtypes.PTType;
import edu.clemson.cs.rsrg.absyn.Exp;
import edu.clemson.cs.rsrg.errorhandling.exception.NullProgramTypeException;
import edu.clemson.cs.rsrg.parsing.data.Location;

/**
 * <p>This is the abstract base class for all the programming expression objects
 * that the compiler builds using the ANTLR4 AST nodes.</p>
 *
 * @version 2.0
 */
public abstract class ProgramExp extends Exp {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /** <p>The program type representation for this programming expression.</p> */
    private PTType myProgramType;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * <p>An helper constructor that allow us to store the location
     * of any objects created from a class that inherits from
     * {@code ProgramExp}.</p>
     *
     * @param l A {@link Location} representation object.
     */
    protected ProgramExp(Location l) {
        super(l);
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * <p>This method overrides the default clone method implementation
     * for all the classes that extend from {@link ProgramExp}.</p>
     *
     * @return A deep copy of the object.
     */
    @Override
    public final ProgramExp clone() {
        ProgramExp newExp = (ProgramExp) super.clone();
        newExp.setProgramType(myProgramType);

        return newExp;
    }

    /**
     * <p>This method gets the programming type associated
     * with this object.</p>
     *
     * @return The {@link PTType} type object.
     */
    public final PTType getProgramType() {
        return myProgramType;
    }

    /**
     * <p>This method sets the programming type associated
     * with this object.</p>
     *
     * @param progType The {@link PTType} type object.
     */
    public final void setProgramType(PTType progType) {
        if (progType == null) {
            throw new NullProgramTypeException("Null Program Type on: "
                    + this.getClass() + ". The causing expression is: "
                    + this.toString());
        }

        myProgramType = progType;
    }

}
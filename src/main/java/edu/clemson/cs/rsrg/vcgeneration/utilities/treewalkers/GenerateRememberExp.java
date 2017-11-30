/*
 * GenerateRememberExp.java
 * ---------------------------------
 * Copyright (c) 2017
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.cs.rsrg.vcgeneration.utilities.treewalkers;

import edu.clemson.cs.rsrg.absyn.expressions.Exp;
import edu.clemson.cs.rsrg.absyn.expressions.mathexpr.MathExp;
import edu.clemson.cs.rsrg.absyn.expressions.mathexpr.OldExp;
import edu.clemson.cs.rsrg.absyn.expressions.programexpr.ProgramExp;
import edu.clemson.cs.rsrg.statushandling.exception.MiscErrorException;
import edu.clemson.cs.rsrg.treewalk.TreeWalkerVisitor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>This class is an helper class that helps generate the resulting {@link Exp}
 * after applying the {@code Remember} rule. This visitor logic is implemented
 * as a {@link TreeWalkerVisitor}.</p>
 *
 * @author Yu-Shan Sun
 * @version 1.0
 */
public class GenerateRememberExp extends TreeWalkerVisitor {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /**
     * <p>This contains a mapping of the original {@link Exp} to
     * the resulting {@link Exp} after applying the {@code Remember} rule.</p>
     */
    private final Map<Exp, Exp> myGeneratedExpMap;

    /**
     * <p>This is the original {@link Exp} that we are applying
     * the rule to.</p>
     */
    private final Exp myOriginalExp;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * <p>This creates an object that that applies the {@code Remember} rule
     * to anything that descends from {@link MathExp}.</p>
     *
     * @param originalExp The expression we want to apply the
     *                    {@code Remember} rule to.
     */
    public GenerateRememberExp(Exp originalExp) {
        myGeneratedExpMap = new LinkedHashMap<>();
        myOriginalExp = originalExp;
    }

    // ===========================================================
    // Visitor Methods
    // ===========================================================

    // -----------------------------------------------------------
    // Expression-Related
    // -----------------------------------------------------------

    /**
     * <p>Code that gets executed after visiting an {@link Exp}.</p>
     *
     * @param exp An expression.
     */
    @Override
    public final void postExp(Exp exp) {
        // YS: If there isn't a specific walk method that generated
        //     a modified Exp, simply make a copy.
        if (myGeneratedExpMap.containsKey(exp)) {
            myGeneratedExpMap.put(exp, exp.clone());
        }
    }

    // -----------------------------------------------------------
    // Math Expression-Related
    // -----------------------------------------------------------

    /**
     * <p>This method redefines how n {@link OldExp} should be walked.</p>
     *
     * @param exp An {@code old} expression.
     *
     * @return {@code true}
     */
    @Override
    public final boolean walkOldExp(OldExp exp) {
        preAny(exp);
        preExp(exp);
        preMathExp(exp);
        preOldExp(exp);

        // YS: We only want to get rid of the outermost
        //     "#". The expression could be "##y", so the
        //     resulting expression should be "#y". Therefore,
        //     we don't walk the children of OldExp.
        myGeneratedExpMap.put(exp, exp.getExp().clone());

        postOldExp(exp);
        postMathExp(exp);
        postExp(exp);
        postAny(exp);

        return true;
    }

    // -----------------------------------------------------------
    // Program Expression-Related
    // -----------------------------------------------------------

    /**
     * <p>Code that gets executed before visiting a {@link ProgramExp}.</p>
     *
     * @param exp A programming expression.
     */
    @Override
    public final void preProgramExp(ProgramExp exp) {
        // This is an error! We should have converted all ProgramExp
        // to their math counterparts.
        throw new MiscErrorException("[VCGenerator] Encountered ProgramExp: "
                + exp + " in " + myOriginalExp
                + " while applying the Remember Rule.", new RuntimeException());
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * <p>This method returns the modified expression generated by
     * this tree walker visitor.</p>
     *
     * @return The resulting {@link Exp}.
     */
    public final Exp getResultingExp() {
        return myGeneratedExpMap.get(myOriginalExp);
    }

}
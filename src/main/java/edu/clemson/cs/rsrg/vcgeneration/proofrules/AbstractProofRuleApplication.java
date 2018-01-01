/*
 * AbstractProofRuleApplication.java
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
package edu.clemson.cs.rsrg.vcgeneration.proofrules;

import edu.clemson.cs.rsrg.absyn.expressions.Exp;
import edu.clemson.cs.rsrg.vcgeneration.sequents.Sequent;
import edu.clemson.cs.rsrg.vcgeneration.utilities.AssertiveCodeBlock;
import edu.clemson.cs.rsrg.vcgeneration.utilities.VerificationCondition;
import java.util.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * <p>This is the abstract base class for all the {@code Proof Rules}.</p>
 *
 * @author Yu-Shan Sun
 * @version 1.0
 */
public abstract class AbstractProofRuleApplication
        implements
            ProofRuleApplication {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /**
     * <p>The current {@link AssertiveCodeBlock} that the {@code Proof Rules}
     * will operate on.</p>
     */
    protected final AssertiveCodeBlock myCurrentAssertiveCodeBlock;

    /**
     * <p>A map that stores string template models for generated
     * {@link AssertiveCodeBlock AssertiveCodeBlocks}.</p>
     */
    protected final Map<AssertiveCodeBlock, ST> myNewAssertiveCodeBlockModels;

    /**
     * <p>A double ended queue that contains all the assertive code blocks
     * that was either passed in or generated by the {@code Proof Rule}.</p>
     */
    protected final Deque<AssertiveCodeBlock> myResultingAssertiveCodeBlocks;

    /** <p>String template groups for storing all the VC generation details.</p> */
    protected final STGroup mySTGroup;

    /** <p>String template model for the {@link AssertiveCodeBlock}.</p> */
    protected final ST myBlockModel;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * <p>An helper constructor that creates a double ended queue to
     * store the {@link AssertiveCodeBlock} that was passed in as well
     * as any generated from a class that inherits from
     * {@code AbstractProofRuleApplication}.</p>
     *
     * @param block The assertive code block that the subclasses are
     *              applying the rule to.
     * @param stGroup The string template group we will be using.
     * @param blockModel The model associated with {@code block}.
     */
    protected AbstractProofRuleApplication(AssertiveCodeBlock block, STGroup stGroup, ST blockModel) {
        myResultingAssertiveCodeBlocks = new LinkedList<>();
        myCurrentAssertiveCodeBlock = block;
        myNewAssertiveCodeBlockModels = new HashMap<>();
        mySTGroup = stGroup;
        myBlockModel = blockModel;
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * <p>This method returns the a {@link Deque} of {@link AssertiveCodeBlock AssertiveCodeBlock(s)}
     * that resulted from applying the {@code Proof Rule}.</p>
     *
     * @return A {@link Deque} containing all the {@link AssertiveCodeBlock AssertiveCodeBlock(s)}.
     */
    @Override
    public final Deque<AssertiveCodeBlock> getAssertiveCodeBlocks() {
        Deque<AssertiveCodeBlock> blocks = new LinkedList<>(myResultingAssertiveCodeBlocks);
        blocks.addFirst(myCurrentAssertiveCodeBlock);

        return blocks;
    }

    /**
     * <p>This method returns the string template associated with the incoming
     * {@link AssertiveCodeBlock}.</p>
     *
     * @return A {@link ST} object.
     */
    @Override
    public final ST getBlockModel() {
        return myBlockModel;
    }

    /**
     * <p>This method returns the a map containing the new string template
     * block models associated with any {@link AssertiveCodeBlock AssertiveCodeBlock(s)}
     * that resulted from applying the {@code Proof Rule}.</p>
     *
     * @return A map from {@link AssertiveCodeBlock AssertiveCodeBlock(s)} to
     * {@link ST} block models.
     */
    @Override
    public final Map<AssertiveCodeBlock, ST> getNewAssertiveCodeBlockModels() {
        return myNewAssertiveCodeBlockModels;
    }

    // ===========================================================
    // Protected Methods
    // ===========================================================

    /**
     * <p>An helper method that performs the substitution on all the
     * {@link Exp} in each {@link VerificationCondition}.</p>
     *
     * @param vcs The original list of {@link VerificationCondition}.
     * @param substitutions A map of substitutions.
     *
     * @return A modified list of {@link VerificationCondition}.
     */
    protected final List<VerificationCondition> createReplacementVCs(
            List<VerificationCondition> vcs, Map<Exp, Exp> substitutions) {
        List<VerificationCondition> newVCs = new ArrayList<>(vcs.size());
        for (VerificationCondition vc : vcs) {
            newVCs.add(new VerificationCondition(vc.getLocation(), vc.getName(),
                    createReplacementSequent(vc.getSequent(), substitutions),
                    vc.getHasImpactingReductionFlag(), vc.getLocationDetailModel()));
        }

        return newVCs;
    }

    // ===========================================================
    // Private Methods
    // ===========================================================

    /**
     * <p>An helper method that performs the substitution on all the
     * {@link Exp} in the {@link Sequent}.</p>
     *
     * @param s The original {@link Sequent}.
     * @param substitutions A map of substitutions.
     *
     * @return A modified {@link Sequent}.
     */
    private Sequent createReplacementSequent(Sequent s, Map<Exp, Exp> substitutions) {
        List<Exp> newAntecedents = new ArrayList<>();
        List<Exp> newConsequents = new ArrayList<>();

        for (Exp antencedent : s.getAntecedents()) {
            newAntecedents.add(antencedent.substitute(substitutions));
        }

        for (Exp consequent : s.getConcequents()) {
            newConsequents.add(consequent.substitute(substitutions));
        }

        return new Sequent(s.getLocation(), newAntecedents, newConsequents);
    }
}
/*
 * AbstractProofRuleApplication.java
 * ---------------------------------
 * Copyright (c) 2021
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.rsrg.vcgeneration.proofrules;

import edu.clemson.rsrg.absyn.clauses.AffectsClause;
import edu.clemson.rsrg.absyn.declarations.mathdecl.MathDefVariableDec;
import edu.clemson.rsrg.absyn.declarations.sharedstatedecl.SharedStateDec;
import edu.clemson.rsrg.absyn.declarations.typedecl.TypeFamilyDec;
import edu.clemson.rsrg.absyn.declarations.variabledecl.MathVarDec;
import edu.clemson.rsrg.absyn.expressions.Exp;
import edu.clemson.rsrg.absyn.expressions.mathexpr.*;
import edu.clemson.rsrg.parsing.data.Location;
import edu.clemson.rsrg.parsing.data.LocationDetailModel;
import edu.clemson.rsrg.parsing.data.PosSymbol;
import edu.clemson.rsrg.vcgeneration.sequents.Sequent;
import edu.clemson.rsrg.vcgeneration.utilities.AssertiveCodeBlock;
import edu.clemson.rsrg.vcgeneration.utilities.Utilities;
import edu.clemson.rsrg.vcgeneration.utilities.VerificationCondition;
import edu.clemson.rsrg.vcgeneration.utilities.VerificationContext;
import edu.clemson.rsrg.vcgeneration.utilities.formaltoactual.FormalActualLists;
import edu.clemson.rsrg.vcgeneration.utilities.formaltoactual.InstantiatedFacilityDecl;
import java.util.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * <p>
 * This is the abstract base class for all the {@code Proof Rules}.
 * </p>
 *
 * @author Yu-Shan Sun
 *
 * @version 1.0
 */
public abstract class AbstractProofRuleApplication implements ProofRuleApplication {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /**
     * <p>
     * The current {@link AssertiveCodeBlock} that the {@code Proof Rules} will operate on.
     * </p>
     */
    protected final AssertiveCodeBlock myCurrentAssertiveCodeBlock;

    /**
     * <p>
     * The {@link VerificationContext} where all the information for the current {@code Assertive Code Block} is
     * located.
     * </p>
     */
    protected final VerificationContext myCurrentVerificationContext;

    /**
     * <p>
     * A map that stores string template models for generated {@link AssertiveCodeBlock AssertiveCodeBlocks}.
     * </p>
     */
    protected final Map<AssertiveCodeBlock, ST> myNewAssertiveCodeBlockModels;

    /**
     * <p>
     * A double ended queue that contains all the assertive code blocks that was either passed in or generated by the
     * {@code Proof Rule}.
     * </p>
     */
    protected final Deque<AssertiveCodeBlock> myResultingAssertiveCodeBlocks;

    /**
     * <p>
     * String template groups for storing all the VC generation details.
     * </p>
     */
    protected final STGroup mySTGroup;

    /**
     * <p>
     * String template model for the {@link AssertiveCodeBlock}.
     * </p>
     */
    protected final ST myBlockModel;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * <p>
     * An helper constructor that creates a double ended queue to store the {@link AssertiveCodeBlock} that was passed
     * in as well as any generated from a class that inherits from {@code AbstractProofRuleApplication}.
     * </p>
     *
     * @param block
     *            The assertive code block that the subclasses are applying the rule to.
     * @param context
     *            The verification context that contains all the information we have collected so far.
     * @param stGroup
     *            The string template group we will be using.
     * @param blockModel
     *            The model associated with {@code block}.
     */
    protected AbstractProofRuleApplication(AssertiveCodeBlock block, VerificationContext context, STGroup stGroup,
            ST blockModel) {
        myResultingAssertiveCodeBlocks = new LinkedList<>();
        myCurrentAssertiveCodeBlock = block;
        myCurrentVerificationContext = context;
        myNewAssertiveCodeBlockModels = new HashMap<>();
        mySTGroup = stGroup;
        myBlockModel = blockModel;
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * <p>
     * This method returns the a {@link Deque} of {@link AssertiveCodeBlock AssertiveCodeBlock(s)} that resulted from
     * applying the {@code Proof Rule}.
     * </p>
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
     * <p>
     * This method returns the string template associated with the incoming {@link AssertiveCodeBlock}.
     * </p>
     *
     * @return A {@link ST} object.
     */
    @Override
    public final ST getBlockModel() {
        return myBlockModel;
    }

    /**
     * <p>
     * This method returns the a map containing the new string template block models associated with any
     * {@link AssertiveCodeBlock AssertiveCodeBlock(s)} that resulted from applying the {@code Proof Rule}.
     * </p>
     *
     * @return A map from {@link AssertiveCodeBlock AssertiveCodeBlock(s)} to {@link ST} block models.
     */
    @Override
    public final Map<AssertiveCodeBlock, ST> getNewAssertiveCodeBlockModels() {
        return myNewAssertiveCodeBlockModels;
    }

    // ===========================================================
    // Protected Methods
    // ===========================================================

    /**
     * <p>
     * An helper method for creating the modified {@code ensures} clause that modifies the shared variables
     * appropriately.
     * </p>
     *
     * <p>
     * Note that this helper method also does all the appropriate substitutions to the {@code VCs} in the assertive code
     * block.
     * </p>
     *
     * @param loc
     *            Location where we are creating a replacement for.
     * @param originalExp
     *            The original expression.
     * @param facQualifier
     *            A facility qualifier (if any).
     * @param affectsClause
     *            The {@code affects} clause associated with the ensures clause.
     *
     * @return The modified {@code ensures} clause expression.
     */
    protected final Exp createEnsuresExpWithModifiedSharedVars(Location loc, Exp originalExp, PosSymbol facQualifier,
            AffectsClause affectsClause) {
        // Create a replacement maps
        // 1) substitutions: Contains all the replacements for the originalExp
        // 2) substitutionsForSeq: Contains all the replacements for the VC's sequents.
        Map<Exp, Exp> substitutions = new LinkedHashMap<>();
        Map<Exp, Exp> substitutionsForSeq = new LinkedHashMap<>();

        // Create replacements for any affected variables (if needed)
        if (affectsClause != null) {
            for (Exp affectedExp : affectsClause.getAffectedExps()) {
                // Replace any #originalAffectsExp with the facility qualified modifiedAffectsExp
                VarExp originalAffectsExp = (VarExp) affectedExp;
                VarExp modifiedAffectsExp = Utilities.createVarExp(loc.clone(), facQualifier,
                        originalAffectsExp.getName(), affectedExp.getMathType(), affectedExp.getMathTypeValue());
                substitutions.put(new OldExp(originalAffectsExp.getLocation(), originalAffectsExp), modifiedAffectsExp);

                // Replace any originalAffectsExp with NQV(modifiedAffectsExp)
                VCVarExp vcVarExp = Utilities.createVCVarExp(myCurrentAssertiveCodeBlock, modifiedAffectsExp);
                myCurrentAssertiveCodeBlock.addFreeVar(vcVarExp);
                substitutions.put(originalAffectsExp, vcVarExp);

                // Add modifiedAffectsExp with NQV(modifiedAffectsExp) as a substitution for VC's sequents
                substitutionsForSeq.put(modifiedAffectsExp.clone(), vcVarExp.clone());
            }

            // Retrieve the list of VCs and use the sequent
            // substitution map to do replacements.
            List<VerificationCondition> newVCs = createReplacementVCs(myCurrentAssertiveCodeBlock.getVCs(),
                    substitutionsForSeq);

            // Store the new list of vcs
            myCurrentAssertiveCodeBlock.setVCs(newVCs);
        }

        return originalExp.substitute(substitutions);
    }

    /**
     * <p>
     * An helper method that performs the substitution on all the {@link Exp} in each {@link VerificationCondition}.
     * </p>
     *
     * @param vcs
     *            The original list of {@link VerificationCondition}.
     * @param substitutions
     *            A map of substitutions.
     *
     * @return A modified list of {@link VerificationCondition}.
     */
    protected final List<VerificationCondition> createReplacementVCs(List<VerificationCondition> vcs,
            Map<Exp, Exp> substitutions) {
        List<VerificationCondition> newVCs = new ArrayList<>(vcs.size());
        for (VerificationCondition vc : vcs) {
            newVCs.add(new VerificationCondition(vc.getLocation(), vc.getName(),
                    createReplacementSequent(vc.getSequent(), substitutions), vc.getHasImpactingReductionFlag(),
                    vc.getLocationDetailModel()));
        }

        return newVCs;
    }

    /**
     * <p>
     * An helper method for generating a conjunction of {@code Def Var} with their definitions.
     * </p>
     *
     * @param facilityName
     *            Name of the facility we are processing.
     * @param typeFamilyDec
     *            A type family declaration.
     * @param instantiatedFacilityDecl
     *            The instantiating facility declaration.
     *
     * @return If there are {@code Def Vars}, it returns a conjuncted expression containing the {@code Def Vars} with
     *         their definitions. Otherwise, it returns an expression representing {@code true}.
     */
    protected final Exp generateDefVarExps(PosSymbol facilityName, TypeFamilyDec typeFamilyDec,
            InstantiatedFacilityDecl instantiatedFacilityDecl) {
        Exp retExp = VarExp.getTrueVarExp(typeFamilyDec.getLocation(), myCurrentAssertiveCodeBlock.getTypeGraph());

        // Create a replacement map
        Map<Exp, Exp> substitutionMap = new LinkedHashMap<>();

        for (MathDefVariableDec defVariableDec : typeFamilyDec.getDefinitionVarList()) {
            // Convert the definition variable into VarExps
            VarExp defVarAsVarExp = Utilities.createVarExp(defVariableDec.getLocation().clone(), null,
                    defVariableDec.getName(), defVariableDec.getMathType(), null);
            VarExp qualifiedDefVarAsVarExp = (VarExp) defVarAsVarExp.clone();
            qualifiedDefVarAsVarExp.setQualifier(facilityName);

            // Generate a proper EqualsExp with the definition
            if (defVariableDec.getDefinitionAsExp() != null) {
                EqualsExp definitionExp = new EqualsExp(defVariableDec.getLocation().clone(), qualifiedDefVarAsVarExp,
                        null, EqualsExp.Operator.EQUAL, defVariableDec.getDefinitionAsExp().clone());
                definitionExp.setMathType(myCurrentAssertiveCodeBlock.getTypeGraph().BOOLEAN);
                definitionExp.setLocationDetailModel(new LocationDetailModel(definitionExp.getLocation().clone(),
                        definitionExp.getLocation().clone(),
                        "Def Var: " + defVariableDec.getVariable().getName().getName()));

                if (VarExp.isLiteralTrue(retExp)) {
                    retExp = definitionExp;
                } else {
                    retExp = MathExp.formConjunct(retExp.getLocation().clone(), retExp, definitionExp);
                }
            }

            // Substitute any definition variables with ones
            // containing the proper facility qualifier
            substitutionMap.put(defVarAsVarExp, qualifiedDefVarAsVarExp);
        }

        // Substitute any <type>.Receptacles and <type>.Val_in with ones
        // containing the proper facility qualifier.
        VarExp typeAsVarExp = Utilities.createVarExp(typeFamilyDec.getLocation().clone(), null, typeFamilyDec.getName(),
                typeFamilyDec.getModel().getMathType(), typeFamilyDec.getModel().getMathTypeValue());
        VarExp qualifiedTypeAsVarExp = (VarExp) typeAsVarExp.clone();
        qualifiedTypeAsVarExp.setQualifier(facilityName);
        substitutionMap.put(typeAsVarExp, qualifiedTypeAsVarExp);

        // Substituting shared variables with ones containing the proper facility qualifier.
        for (SharedStateDec stateDec : instantiatedFacilityDecl.getConceptSharedStates()) {
            for (MathVarDec mathVarDec : stateDec.getAbstractStateVars()) {
                VarExp stateVarExp = Utilities.createVarExp(facilityName.getLocation().clone(), null,
                        mathVarDec.getName(), mathVarDec.getMathType(), null);
                VarExp qualifiedVarExp = Utilities.createVarExp(facilityName.getLocation().clone(), facilityName,
                        mathVarDec.getName(), mathVarDec.getMathType(), null);
                substitutionMap.put(stateVarExp, qualifiedVarExp);
            }
        }

        // Substitute any formal concept arguments with its actual
        FormalActualLists conceptParamArgs = instantiatedFacilityDecl.getConceptParamArgLists();
        Iterator<VarExp> formalArgsIt = conceptParamArgs.getFormalParamList().iterator();
        Iterator<Exp> actualArgsIt = conceptParamArgs.getActualArgList().iterator();
        while (formalArgsIt.hasNext() && actualArgsIt.hasNext()) {
            substitutionMap.put(formalArgsIt.next(), actualArgsIt.next());
        }

        return retExp.substitute(substitutionMap);
    }

    // ===========================================================
    // Private Methods
    // ===========================================================

    /**
     * <p>
     * An helper method that performs the substitution on all the {@link Exp} in the {@link Sequent}.
     * </p>
     *
     * @param s
     *            The original {@link Sequent}.
     * @param substitutions
     *            A map of substitutions.
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

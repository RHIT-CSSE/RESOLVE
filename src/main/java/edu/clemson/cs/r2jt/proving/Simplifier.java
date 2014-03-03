/**
 * Simplifier.java
 * ---------------------------------
 * Copyright (c) 2014
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.cs.r2jt.proving;

import java.util.Iterator;

/**
 * <p>A <code>Simplifier</code> always returns a single new version of the VC: 
 * the VC with redundant expressions removed, obviously true conjuncts removed, 
 * and conjuncts from the consequent that appear directly in the antecedent 
 * removed.</p>
 */
public class Simplifier implements VCTransformer {

    @Override
    public Iterator<VC> transform(VC original) {
        return new SingletonIterator<VC>(original.simplify());
    }

    @Override
    public String toString() {
        return "simplification";
    }

    @Override
    public Antecedent getPattern() {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    public Consequent getReplacementTemplate() {
        throw new UnsupportedOperationException("Not applicable.");
    }

    @Override
    public boolean introducesQuantifiedVariables() {
        return false;
    }
}

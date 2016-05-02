/**
 * AbstractPipeline.java
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
package edu.clemson.cs.rsrg.init.pipeline;

import edu.clemson.cs.rsrg.init.CompileEnvironment;
import edu.clemson.cs.r2jt.typeandpopulate2.MathSymbolTableBuilder;
import edu.clemson.cs.rsrg.typeandpopulate.ModuleIdentifier;

/**
 * TODO: Description for this class
 */
public abstract class AbstractPipeline {

    protected final CompileEnvironment myCompileEnvironment;
    protected final MathSymbolTableBuilder mySymbolTable;

    protected AbstractPipeline(CompileEnvironment ce,
            MathSymbolTableBuilder symbolTable) {
        myCompileEnvironment = ce;
        mySymbolTable = symbolTable;
    }

    public abstract void process(ModuleIdentifier currentTarget);

}
/**
 * SourceErrorException.java
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
package edu.clemson.cs.rsrg.errorhandling.exception;

import edu.clemson.cs.rsrg.parsing.data.PosSymbol;

/**
 * <p>The default source error exception for the compiler.</p>
 *
 * @author Yu-Shan Sun
 * @version 2.0
 */
public class SourceErrorException extends CompilerException {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /** <p>Serial version for Serializable objects</p> */
    private static final long serialVersionUID = 1L;

    // ==========================================================
    // Constructors
    // ==========================================================

    /**
     * <p>This constructor takes in a throwable cause and a message for the
     * symbol that caused an source exception to be thrown.</p>
     *
     * @param message Message to be displayed when the exception is thrown.
     * @param symbol Offending symbol
     * @param cause Cause of the exception.
     */
    protected SourceErrorException(String message, PosSymbol symbol,
            Throwable cause) {
        super(message, symbol, cause);
    }

    /**
     * <p>This constructor takes in a message for the
     * symbol that caused an source exception to be thrown.</p>
     *
     * @param message Message to be displayed when the exception is thrown.
     * @param symbol Offending symbol
     */
    protected SourceErrorException(String message, PosSymbol symbol) {
        super(message, symbol, null);
    }

}
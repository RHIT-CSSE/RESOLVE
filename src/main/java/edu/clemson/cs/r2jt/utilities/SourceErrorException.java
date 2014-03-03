/**
 * SourceErrorException.java
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
package edu.clemson.cs.r2jt.utilities;

import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.PosSymbol;

public class SourceErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Location myErrorLocation;

    public SourceErrorException(String message, PosSymbol symbol,
            Throwable cause) {

        this(message, symbol.getLocation(), cause);
    }

    public SourceErrorException(String message, PosSymbol symbol) {
        this(message, symbol.getLocation());
    }

    public SourceErrorException(String message, Location location,
            Throwable cause) {

        super(message, cause);
        myErrorLocation = location;
    }

    public SourceErrorException(String message, Location location) {
        this(message, location, null);
    }

    public Location getErrorLocation() {
        return myErrorLocation;
    }
}

/**
 * DebugIterator.java
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
 *
 * @author hamptos
 */
public class DebugIterator implements Iterator {

    private final Iterator myBaseIterator;
    private final String myStartMessage;
    private final String myEndMessage;

    private boolean myGotFirstFlag = false;
    private boolean myFoundEndFlag = false;

    public DebugIterator(Iterator i, String start, String end) {
        myBaseIterator = i;
        myStartMessage = start;
        myEndMessage = end;
    }

    @Override
    public boolean hasNext() {
        boolean result = myBaseIterator.hasNext();

        if (!result && !myFoundEndFlag) {
            System.out.println(myEndMessage);
            myFoundEndFlag = true;
        }

        return myBaseIterator.hasNext();
    }

    @Override
    public Object next() {
        if (!myGotFirstFlag) {
            System.out.println(myStartMessage);
            myGotFirstFlag = true;
        }

        return myBaseIterator.next();
    }

    @Override
    public void remove() {
        myBaseIterator.remove();
    }
}

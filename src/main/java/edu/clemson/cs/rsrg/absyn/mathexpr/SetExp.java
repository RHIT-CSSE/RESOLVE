/**
 * SetExp.java
 * ---------------------------------
 * Copyright (c) 2015
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.cs.rsrg.absyn.mathexpr;

import edu.clemson.cs.rsrg.absyn.Exp;
import edu.clemson.cs.rsrg.absyn.variables.MathVarDec;
import edu.clemson.cs.rsrg.parsing.data.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>This is the class for all the mathematical set expressions
 * of the kind {x : Z | x < y} that the compiler builds from
 * the ANTLR4 AST tree.</p>
 *
 * @version 2.0
 */
public class SetExp extends MathExp {

    // ===========================================================
    // Member Fields
    // ===========================================================

    /** <p>The mathematical variable in this set expression.</p> */
    private final MathVarDec myVar;

    /** <p>The set expression's where part.</p> */
    private final Exp myWhereExp;

    /** <p>The set expression's body.</p> */
    private final Exp myBodyExp;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * <p>This constructs a set expression.</p>
     *
     * @param l A {@link Location} representation object.
     * @param var A {@link MathVarDec} representing the expression's variable.
     * @param where A {@link Exp} representing the where clause.
     * @param body A {@link Exp} representing the body of the expression.
     */
    public SetExp(Location l, MathVarDec var, Exp where, Exp body) {
        super(l);
        myVar = var;
        myWhereExp = where;
        myBodyExp = body;
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * <p>This method creates a special indented
     * text version of the class as a string.</p>
     *
     * @param indentSize The base indentation to the first line
     *                   of the text.
     * @param innerIndentSize The additional indentation increment
     *                        for the subsequent lines.
     *
     * @return A formatted text string of the class.
     */
    @Override
    public final String asString(int indentSize, int innerIndentSize) {
        StringBuffer sb = new StringBuffer();
        printSpace(indentSize, sb);
        sb.append("SetExp\n");
        sb
                .append(myVar.asString(indentSize + innerIndentSize,
                        innerIndentSize));

        if (myWhereExp != null) {
            sb.append(myWhereExp.asString(indentSize + innerIndentSize,
                    innerIndentSize));
        }

        sb.append(myBodyExp.asString(indentSize + innerIndentSize,
                innerIndentSize));

        return sb.toString();
    }

    /**
     * <p>This method attempts to find the provided expression in our
     * subexpressions.</p>
     *
     * @param exp The expression we wish to locate.
     *
     * @return True if there is an instance of <code>exp</code>
     * within this object's subexpressions. False otherwise.
     */
    @Override
    public final boolean containsExp(Exp exp) {
        boolean found = myWhereExp.containsExp(exp);
        if (!found) {
            found = myBodyExp.containsExp(exp);
        }

        return found;
    }

    /**
     *  <p>This method attempts to find an expression with the given name in our
     * subexpressions.</p>
     *
     * @param varName Expression name.
     * @param IsOldExp Flag to indicate if the given name is of the form
     *                 "#[varName]"
     *
     * @return True if there is a {@link Exp} within this object's
     * subexpressions that matches <code>varName</code>. False otherwise.
     */
    @Override
    public boolean containsVar(String varName, boolean IsOldExp) {
        boolean found = myWhereExp.containsVar(varName, IsOldExp);
        if (!found) {
            found = myBodyExp.containsVar(varName, IsOldExp);
        }

        return found;
    }

    /**
     * <p>This method overrides the default equals method implementation
     * for the {@link SetExp} class.</p>
     *
     * @param o Object to be compared.
     *
     * @return True if all the fields are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof InfixExp) {
            SetExp eAsSetExp = (SetExp) o;
            result = myLoc.equals(eAsSetExp.myLoc);

            if (result) {
                result = myVar.equals(eAsSetExp.myVar);

                if (result) {
                    result = myWhereExp.equals(eAsSetExp.myWhereExp);

                    if (result) {
                        result = myBodyExp.equals(eAsSetExp.myBodyExp);
                    }
                }
            }
        }

        return result;
    }

    /**
     * <p>Shallow compare is too weak for many things, and equals() is too
     * strict. This method returns <code>true</code> <strong>iff</code> this
     * expression and the provided expression, <code>e</code>, are equivalent
     * with respect to structure and all function and variable names.</p>
     *
     * @param e The expression to compare this one to.
     *
     * @return True <strong>iff</strong> this expression and the provided
     *         expression are equivalent with respect to structure and all
     *         function and variable names.
     */
    @Override
    public boolean equivalent(Exp e) {
        boolean retval = e instanceof SetExp;
        if (retval) {
            SetExp eAsSetExp = (SetExp) e;
            retval =
                    myVar.getName().equals(eAsSetExp.myVar.getName())
                            && myVar.getTy().equals(eAsSetExp.myVar.getTy());
            retval &= myWhereExp.equivalent(eAsSetExp.myWhereExp);
            retval &= myBodyExp.equivalent(eAsSetExp.myBodyExp);
        }

        return retval;
    }

    /**
     * <p>This method returns a deep copy of the body expression.</p>
     *
     * @return The {@link Exp} representation object.
     */
    public Exp getBody() {
        return myBodyExp.clone();
    }

    /**
     * <p>This method returns a deep copy of the list of
     * subexpressions.</p>
     *
     * @return A list containing subexpressions ({@link Exp}s).
     */
    @Override
    public List<Exp> getSubExpressions() {
        List<Exp> list = new ArrayList<>();
        list.add(myWhereExp.clone());
        list.add(myBodyExp.clone());

        return list;
    }

    /**
     * <p>This method returns a deep copy of the variable.</p>
     *
     * @return The {@link MathVarDec} representation object.
     */
    public MathVarDec getVar() {
        return myVar.clone();
    }

    /**
     * <p>This method returns a deep copy of the where expression.</p>
     *
     * @return The {@link Exp} representation object.
     */
    public Exp getWhere() {
        return myWhereExp.clone();
    }

    /**
     * <p>This method applies VC Generator's remember rule.
     * For all inherited programming expression classes, this method
     * should throw an exception.</p>
     *
     * @return The resulting {@link SetExp} from applying the remember rule.
     */
    @Override
    public SetExp remember() {
        Exp newWhere = ((MathExp) myWhereExp).remember();
        Exp newBody = ((MathExp) myBodyExp).remember();

        return new SetExp(new Location(myLoc), myVar.clone(), newWhere, newBody);
    }

    /**
     * <p>This method adds a new expression to our list of subexpressions.</p>
     *
     * @param index The index in our subexpression list.
     * @param e The new {@link Exp} to be added.
     */
    // TODO: See the message in Exp.
    /*public void setSubExpression(int index, Exp e) {
        switch (index) {
        case 0:
            where = e;
            break;
        case 1:
            body = e;
            break;
        }
    }*/

    /**
     * <p>This method applies the VC Generator's simplification step.</p>
     *
     * @return The resulting {@link MathExp} from applying the simplification step.
     */
    @Override
    public MathExp simplify() {
        return this.clone();
    }

    /**
     * <p>Returns the expression in string format.</p>
     *
     * @return Expression as a string.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{ ");
        sb.append(myVar.toString());
        sb.append(", ");

        if (myWhereExp != null) {
            sb.append(myWhereExp.toString());
            sb.append(", ");
        }

        sb.append(myBodyExp.toString());
        sb.append(" }");

        return sb.toString();
    }

    // ===========================================================
    // Protected Methods
    // ===========================================================

    /**
     * <p>Implemented by this concrete subclass of {@link Exp} to manufacture
     * a copy of themselves.</p>
     *
     * @return A new {@link Exp} that is a deep copy of the original.
     */
    @Override
    protected Exp copy() {
        Exp newWhere = null;
        if (myWhereExp != null) {
            newWhere = myWhereExp.clone();
        }

        return new SetExp(new Location(myLoc), myVar.clone(), newWhere,
                myBodyExp.clone());
    }

    /**
     * <p>Implemented by this concrete subclass of {@link Exp} to manufacture
     * a copy of themselves where all subexpressions have been appropriately
     * substituted. This class is assuming that <code>this</code>
     * does not match any key in <code>substitutions</code> and thus need only
     * concern itself with performing substitutions in its children.</p>
     *
     * @param substitutions A mapping from {@link Exp}s that should be
     *                      substituted out to the {@link Exp} that should
     *                      replace them.
     *
     * @return A new {@link Exp} that is a deep copy of the original with
     *         the provided substitutions made.
     */
    @Override
    protected Exp substituteChildren(Map<Exp, Exp> substitutions) {
        return new SetExp(new Location(myLoc), myVar.clone(), substitute(
                myWhereExp, substitutions),
                substitute(myBodyExp, substitutions));
    }

}
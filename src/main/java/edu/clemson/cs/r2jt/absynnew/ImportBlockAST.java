/**
 * ImportBlockAST.java
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
package edu.clemson.cs.r2jt.absynnew;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * <p>A <code>ImportBlockAST</code> classifies and maintains a complete
 * collection of module imports ranging from implicitly referenced/imported
 * modules, to those explicitly requested via the <tt>uses</tt> list.</p>
 */
public class ImportBlockAST extends ResolveAST {

    public static enum ImportType {
        EXPLICIT, IMPLICIT, EXTERNAL
    }

    protected final Map<ImportType, Set<Token>> myImports;

    private ImportBlockAST(ImportCollectionBuilder builder) {
        super(builder.getStart(), builder.getStop());
        myImports = builder.usesItems;
    }

    /**
     * <p>Retrieves a set of all imports except those of <code>type</code>.</p>
     * 
     * @param type The
     * @return
     */
    public Set<Token> getImportsExcluding(ImportType... type) {
        Set<Token> result = new HashSet<Token>();
        List<ImportType> typesToExclude = Arrays.asList(type);

        for (ImportType s : myImports.keySet()) {
            if (!typesToExclude.contains(s)) {
                result.addAll(myImports.get(s));
            }
        }
        return result;
    }

    public Set<Token> getImportsOfType(ImportType type) {
        return myImports.get(type);
    }

    /**
     * <p>Returns all imports, regardless of their <code>ImportType</code>, in a
     * single set.</p>
     * 
     * @return <strong>All</strong> imports.
     */
    public Set<Token> getImports() {
        Set<Token> aggregateImports = new HashSet<Token>();

        for (Set<Token> typeSet : myImports.values()) {
            aggregateImports.addAll(typeSet);
        }
        return aggregateImports;
    }

    /**
     * <p>Useful for collecting <em>all</em> imports over the course of the
     * construction of a given {@link ModuleAST}.</p>
     */
    public static class ImportCollectionBuilder
            extends
                AbstractNodeBuilder<ImportBlockAST> {

        protected final Map<ImportType, Set<Token>> usesItems =
                new HashMap<ImportType, Set<Token>>();

        public ImportCollectionBuilder() {
            this(null, null);
        }

        public ImportCollectionBuilder(Token start, Token stop) {
            super(start, stop);

            //Initialize the uses/import map to empty sets
            for (int i = 0; i < ImportType.values().length; i++) {
                ImportType curType = ImportType.values()[i];
                if (usesItems.get(curType) == null) {
                    usesItems.put(curType, new HashSet<Token>());
                }
            }
        }

        public ImportCollectionBuilder imports(ImportType type, Token... t) {
            addTokenSet(type, Arrays.asList(t));
            return this;
        }

        public ImportCollectionBuilder imports(ImportType type,
                TerminalNode... t) {
            imports(type, Arrays.asList(t));
            return this;
        }

        public ImportCollectionBuilder imports(ImportType type,
                List<TerminalNode> terminals) {
            List<Token> convertedTerms = new ArrayList<Token>();
            for (TerminalNode t : terminals) {
                convertedTerms.add(t.getSymbol());
            }
            addTokenSet(type, convertedTerms);
            return this;
        }

        private void addTokenSet(ImportType type,
                Collection<? extends Token> newToks) {
            Set<Token> tokSet = usesItems.get(type);
            if (tokSet == null) {
                tokSet = new HashSet<Token>();
            }
            tokSet.addAll(newToks);
        }

        @Override
        public ImportBlockAST build() {
            return new ImportBlockAST(this);
        }
    }
}
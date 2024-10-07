/*
 * ParameterModeTest.java
 * ---------------------------------
 * Copyright (c) 2024
 * RESOLVE Software Research Group
 * School of Computing
 * Clemson University
 * All rights reserved.
 * ---------------------------------
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package edu.clemson.rsrg.parsing.sanitychecking;

import edu.clemson.rsrg.absyn.clauses.AssertionClause;
import edu.clemson.rsrg.init.CompileEnvironment;
import edu.clemson.rsrg.init.ResolveCompiler;
import edu.clemson.rsrg.init.file.ModuleType;
import edu.clemson.rsrg.init.file.ResolveFile;
import edu.clemson.rsrg.init.file.ResolveFileBasicInfo;
import edu.clemson.rsrg.parsing.data.Location;
import edu.clemson.rsrg.parsing.data.LocationDetailModel;
import edu.clemson.rsrg.statushandling.SystemStdHandler;
import edu.clemson.rsrg.statushandling.exception.MiscErrorException;
import edu.clemson.rsrg.typeandpopulate.typereasoning.TypeGraph;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ParameterModeTest {
    private final Location FAKE_LOCATION_1;
    private final Location FAKE_LOCATION_2;

    private final LocationDetailModel FAKE_LOCATION_DETAIL_MODEL_1;
    private final LocationDetailModel FAKE_LOCATION_DETAIL_MODEL_2;

    private final TypeGraph FAKE_TYPEGRAPH;

    {
        try {
            FAKE_LOCATION_1 = new Location(
                    new ResolveFile(new ResolveFileBasicInfo("ExpEquivalenceTest", ""), ModuleType.THEORY,
                            new UnbufferedCharStream(new StringReader("")), null, new ArrayList<String>(), ""),
                    0, 0);

            FAKE_LOCATION_DETAIL_MODEL_1 = new LocationDetailModel(FAKE_LOCATION_1.clone(), FAKE_LOCATION_1.clone(),
                    "Fake Location 1");

            FAKE_LOCATION_2 = new Location(
                    new ResolveFile(new ResolveFileBasicInfo("ExpEquivalenceTest", ""), ModuleType.THEORY,
                            new UnbufferedCharStream(new StringReader("")), null, new ArrayList<String>(), ""),
                    1, 0);

            FAKE_LOCATION_DETAIL_MODEL_2 = new LocationDetailModel(FAKE_LOCATION_2.clone(), FAKE_LOCATION_2.clone(),
                    "Fake Location 2");

            // Create a fake typegraph
            // YS: We need to create a ResolveCompiler instance to instantiate
            // the flag manager...
            new ResolveCompiler(new String[0]);
            FAKE_TYPEGRAPH = new TypeGraph(
                    new CompileEnvironment(new String[0], "TestCompiler", new SystemStdHandler()));
        } catch (IOException e) {
            throw new MiscErrorException("Error creating a fake location", e);
        }
    }

    // @Test
    // public void checkParameterMode_checkEnsuresClause_throwsNoWarnings() {
    // AssertionClause assertionClause1 = new AssertionClause(FAKE_LOCATION_1);
    // }
}

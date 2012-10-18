/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.maven.analyzer.database.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Test cases
 */
public class DependencyRelationTest {
    private final VersionNode startNode = mock(VersionNode.class);
    private final VersionNode endNode = mock(VersionNode.class);
    private final DependencyRelation relation = new DependencyRelation(Scope.Provided, startNode, endNode);

    @Test
    public void testGetNodeId() throws Exception {
        assertNull(relation.getNodeId());
    }

    @Test
    public void testGetScope() throws Exception {
        assertEquals(Scope.Provided, relation.getScope());
    }

    @Test
    public void testGetStartNode() throws Exception {
        assertEquals(startNode, relation.getStartNode());
    }

    @Test
    public void testGetEndNode() throws Exception {
        assertEquals(endNode, relation.getEndNode());
    }
}

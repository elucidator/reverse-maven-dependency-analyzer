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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Test case
 */
@Transactional
public class VersionNodeTest extends AbstractDatabaseTest {

    @Test
    public void basic() {
        VersionNode versionNode = new VersionNode("a:b:1.0");
        assertEquals("1.0", versionNode.getVersion());
        assertNull(versionNode.getDependencies());
    }

    @Test
    public void toStringTest() {
        VersionNode versionNode = new VersionNode("a:b:1.0");
        assertTrue(versionNode.toString().contains("1.0"));
    }

    @Test
    public void dependencies() {
        VersionNode versionNode = new VersionNode("a:b:1.0");
        DependencyRelation relation = mock(DependencyRelation.class);
        versionNode.addDependency(relation);
        assertEquals(1, versionNode.getDependencies().size());
        assertEquals(relation, versionNode.getDependencies().iterator().next());
    }

    @Transactional
    @Test
    public void dependency() {
        VersionNode versionNodeA = new VersionNode("a:b:a1");
        template.save(versionNodeA);
        System.out.println("versionNodeA = " + template.save(versionNodeA).getNodeId());

        VersionNode versionNodeB = new VersionNode("a:b:c1");
        template.save(versionNodeB);

        DependencyRelation dependencyRelation = new DependencyRelation(Scope.Compile, versionNodeA, versionNodeB);
        versionNodeA.addDependency(dependencyRelation);
        template.save(dependencyRelation);

        VersionNode retrieved = template.findOne(versionNodeA.getNodeId(), VersionNode.class);
        assertNotNull(retrieved);
        assertNotNull(retrieved.getDependencies());
        assertEquals(1, retrieved.getDependencies().size());
        DependencyRelation retrievedRelation = retrieved.getDependencies().iterator().next();
        assertEquals(versionNodeB.getNodeId(), retrievedRelation.getEndNode().getNodeId());
        assertEquals(versionNodeA.getNodeId(), retrievedRelation.getStartNode().getNodeId());
    }
}

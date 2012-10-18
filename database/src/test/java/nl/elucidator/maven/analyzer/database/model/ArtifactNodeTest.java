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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 */
@Transactional
public class ArtifactNodeTest extends AbstractDatabaseTest {

    @Test
    @Transactional
    public void storeAndRetrieve() {
        ArtifactNode toSave = new ArtifactNode("groupIdA", "artifactIdA");
        ArtifactNode artifactNode = template.save(toSave);
        ArtifactNode retrieve = template.findOne(artifactNode.getNodeId(), ArtifactNode.class);
        assertEquals(artifactNode.getArtifactId(), retrieve.getArtifactId());
        System.out.println("retrieve.getNodeId() = " + retrieve.getNodeId());
    }

    @Test
    @Transactional
    public void versionRelations() {

        ArtifactNode artifactNode = new ArtifactNode("groupIdB", "artifactIdB");
        artifactNode = template.save(artifactNode);
        System.out.println("artifactNode.getNodeId() = " + artifactNode.getNodeId());

        VersionNode versionNode1_0 = new VersionNode(artifactNode.getGroupId() + ":" + artifactNode.getArtifactId() + ":" + "1.0");
        versionNode1_0 = template.save(versionNode1_0);
        template.save(versionNode1_0);
        artifactNode.addVersion(versionNode1_0);

        VersionNode versionNode1_1 = new VersionNode(artifactNode.getGroupId() + ":" + artifactNode.getArtifactId() + ":" + "1.1");
        versionNode1_1 = template.save(versionNode1_1);
        template.save(versionNode1_1);
        artifactNode.addVersion(versionNode1_1);
        template.save(artifactNode);

        ArtifactNode retrieved = this.artifactRepository.findByGa(artifactNode.getGa());

        Iterable<VersionNode> versionRelations = retrieved.getVersionNodes();
        assertTrue(versionRelations.iterator().hasNext());
        for (VersionNode versionNode : versionRelations) {
            assertTrue(versionNode.getVersion().equals("1.0") || versionNode.getVersion().equals("1.1"));
        }
    }

    @Test
    @Transactional
    public void versions() {

        ArtifactNode artifactNode = new ArtifactNode("groupIdC", "artifactIdC");
        VersionNode versionNode1_0 = new VersionNode("groupIdC:artifactIdC:1.0");
        VersionNode versionNode1_1 = new VersionNode("groupIdC:artifactIdC:1.1");
        artifactNode.addVersion(versionNode1_0);
        artifactNode.addVersion(versionNode1_1);
        artifactNode = template.save(artifactNode);
        System.out.println("artifactNode.getNodeId() = " + artifactNode.getNodeId());

        ArtifactNode retrieved = this.artifactRepository.findByGa(artifactNode.getGa());

        Iterable<VersionNode> versionRelations = retrieved.getVersionNodes();

        for (VersionNode versionNode : versionRelations) {
            assertTrue(versionNode.getVersion().equals("1.0") || versionNode.getVersion().equals("1.1"));
        }
    }

    @Test
    public void toStringTest() {
        ArtifactNode artifactNode = new ArtifactNode("groupId", "artifactId");
        assertTrue(artifactNode.toString().contains("groupId"));
        assertTrue(artifactNode.toString().contains("artifactId"));

    }

    @Test
    @Transactional
    public void doubleAdd() {
        ArtifactNode artifact_Node_1 = new ArtifactNode("A", "B");
        template.save(artifact_Node_1);

        ArtifactNode artifact_Node_2 = new ArtifactNode("A", "B");
        template.save(artifact_Node_2);

        ArtifactNode retrieve = this.artifactRepository.findByGa("A:B");
        assertEquals(artifact_Node_1.getNodeId(), retrieve.getNodeId());
        assertEquals(artifact_Node_2.getNodeId(), retrieve.getNodeId());
    }
}

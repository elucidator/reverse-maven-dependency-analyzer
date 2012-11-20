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
        ArtifactNode toSave = new ArtifactNode(makeArtifact("groupIdA:artifactIdA:1.0"));
        ArtifactNode artifactNode = template.save(toSave);
        ArtifactNode retrieve = template.findOne(artifactNode.getNodeId(), ArtifactNode.class);
        assertEquals(artifactNode.getArtifactId(), retrieve.getArtifactId());
    }

    @Test
    @Transactional
    public void versionRelations() {

        ArtifactNode artifactNode = new ArtifactNode(makeArtifact("groupIdB:artifactIdB:1.0"));
        artifactNode = template.save(artifactNode);

        VersionNode versionNode1_0 = new VersionNode(makeArtifact(artifactNode.getGroupId() + ":" + artifactNode.getArtifactId() + ":" + "1.0"));
        versionNode1_0 = template.save(versionNode1_0);
        template.save(versionNode1_0);
        artifactNode.addVersion(versionNode1_0);

        VersionNode versionNode1_1 = new VersionNode(makeArtifact(artifactNode.getGroupId() + ":" + artifactNode.getArtifactId() + ":" + "1.1"));
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

        ArtifactNode artifactNode = new ArtifactNode(makeArtifact("groupIdC:artifactIdC:1.0"));
        VersionNode versionNode1_0 = new VersionNode(makeArtifact("groupIdC:artifactIdC:1.0"));
        VersionNode versionNode1_1 = new VersionNode(makeArtifact("groupIdC:artifactIdC:1.1"));
        artifactNode.addVersion(versionNode1_0);
        artifactNode.addVersion(versionNode1_1);
        artifactNode = template.save(artifactNode);

        ArtifactNode retrieved = this.artifactRepository.findByGa(artifactNode.getGa());

        Iterable<VersionNode> versionRelations = retrieved.getVersionNodes();

        for (VersionNode versionNode : versionRelations) {
            assertTrue(versionNode.getVersion().equals("1.0") || versionNode.getVersion().equals("1.1"));
        }
    }

    @Test
    public void toStringTest() {
        ArtifactNode artifactNode = new ArtifactNode(makeArtifact("groupId:artifactId:1.0"));
        assertTrue(artifactNode.toString().contains("groupId"));
        assertTrue(artifactNode.toString().contains("artifactId"));

    }

    @Test
    @Transactional
    public void doubleAdd() {
        ArtifactNode artifact_Node_1 = new ArtifactNode(makeArtifact("A:B:1.0"));
        template.save(artifact_Node_1);

        ArtifactNode artifact_Node_2 = new ArtifactNode(makeArtifact("A:B:1.0"));
        template.save(artifact_Node_2);

        ArtifactNode retrieve = this.artifactRepository.findByGa("A:B");
        assertEquals(artifact_Node_1.getNodeId(), retrieve.getNodeId());
        assertEquals(artifact_Node_2.getNodeId(), retrieve.getNodeId());
    }
}

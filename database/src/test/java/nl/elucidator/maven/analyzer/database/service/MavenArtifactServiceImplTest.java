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

package nl.elucidator.maven.analyzer.database.service;

import nl.elucidator.maven.analyzer.database.model.*;
import nl.elucidator.maven.analyzer.database.repository.GroupRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test cases
 */
@Transactional
public class MavenArtifactServiceImplTest extends AbstractDatabaseTest {

    @Autowired
    MavenArtifactService mavenArtifactService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    Cleaner cleaner;

    @Test
    public void simple() {
        VersionNode versionNode = mavenArtifactService.addArtifact("a:b:1.0");
        assertNotNull(versionNode);

        GroupNode groupNode = groupRepository.findByG("a");
        assertNotNull(groupNode);
        Set<ArtifactNode> artifactNodes = groupNode.getArtifactNodes();
        assertNotNull(artifactNodes);
        assertEquals(1, artifactNodes.size());
        ArtifactNode artifactNode = artifactNodes.iterator().next();
        assertNotNull(artifactNode);
        artifactNode = template.findOne(artifactNode.getNodeId(), ArtifactNode.class);
        assertEquals("a:b", artifactNode.getGa());
        Set<VersionNode> versionNodes = artifactNode.getVersionNodes();
        assertNotNull(versionNodes);
        assertEquals(1, versionNodes.size());
        assertEquals("1.0", versionNodes.iterator().next().getVersion());
    }

    @Test
    public void subGroups() {

        VersionNode versionNode = mavenArtifactService.addArtifact("c.d:e:2.0");
        assertNotNull(versionNode);

        GroupNode groupNode = groupRepository.findByG("c");
        assertNotNull(groupNode);
        Set<GroupNode> groupNodes = groupNode.getGroupNodes();
        assertNotNull(groupNodes);
        assertEquals(1, groupNodes.size());
        groupNode = groupNodes.iterator().next();
        System.out.println("groupNode = " + groupNode);
        groupNode = template.findOne(groupNode.getNodeId(), GroupNode.class);

        Set<ArtifactNode> artifactNodes = groupNode.getArtifactNodes();
        assertNotNull(artifactNodes);
        assertEquals(1, artifactNodes.size());
        ArtifactNode artifactNode = artifactNodes.iterator().next();
        assertNotNull(artifactNode);
        artifactNode = template.findOne(artifactNode.getNodeId(), ArtifactNode.class);
        assertEquals("c.d:e", artifactNode.getGa());
        Set<VersionNode> versionNodes = artifactNode.getVersionNodes();
        assertNotNull(versionNodes);
        assertEquals(1, versionNodes.size());
        assertEquals("2.0", versionNodes.iterator().next().getVersion());
    }

    @Test
    public void addSecondVersion() {
        mavenArtifactService.addArtifact("a.b:c:1.0");
        mavenArtifactService.addArtifact("a.b:c:2.0");

        ArtifactNode artifactNode = artifactRepository.findByGa("a.b:c");
        Set<VersionNode> versionNodes = artifactNode.getVersionNodes();
        assertEquals(2, versionNodes.size());
        for (VersionNode versionNode : versionNodes) {
            VersionNode node = template.findOne(versionNode.getNodeId(), VersionNode.class);
            assertTrue(node.getVersion().contains("1.0") || node.getVersion().contains("2.0"));
        }
    }

    @Test
    public void doubleVersionAdd() {
        VersionNode once = mavenArtifactService.addArtifact("d.e:f:1.0");
        VersionNode twice = mavenArtifactService.addArtifact("d.e:f:1.0");
        assertEquals(once.getNodeId(), twice.getNodeId());
    }

    @Test
    public void secondArtifactOnGroup() {
        VersionNode once = mavenArtifactService.addArtifact("g.h:a:1.0");
        VersionNode twice = mavenArtifactService.addArtifact("g.h:b:1.0");
        //check group g.h has two artifacts
        GroupNode groupNode = groupRepository.findByG("g.h");
        assertNotNull(groupNode);
        Set<ArtifactNode> artifactNodes = groupNode.getArtifactNodes();
        assertEquals(2, artifactNodes.size());
        for (ArtifactNode artifactNode : artifactNodes) {
            Set<VersionNode> versionNodes = template.findOne(artifactNode.getNodeId(), ArtifactNode.class).getVersionNodes();
            for (VersionNode versionNode : versionNodes) {
                assertTrue(once.getNodeId() == versionNode.getNodeId() || twice.getNodeId() == versionNode.getNodeId());
            }
        }
    }


    @Test
    public void addArtifactWithNewSubGroup() {
        VersionNode once = mavenArtifactService.addArtifact("g.h:a:1.0");
        VersionNode twice = mavenArtifactService.addArtifact("g.h.f:b:1.0");
        //check group g.h has two artifacts
        GroupNode groupNode = groupRepository.findByG("g.h");
        assertNotNull(groupNode);
        Set<ArtifactNode> artifactNodes = groupNode.getArtifactNodes();
        assertEquals(1, artifactNodes.size());
        Long versionNodeId = template.findOne(artifactNodes.iterator().next().getNodeId(), ArtifactNode.class).getVersionNodes().iterator().next().getNodeId();
        assertEquals(once.getNodeId(), versionNodeId);

        groupNode = groupRepository.findByG("g.h.f");
        assertNotNull(groupNode);
        artifactNodes = groupNode.getArtifactNodes();
        assertEquals(1, artifactNodes.size());
        versionNodeId = template.findOne(artifactNodes.iterator().next().getNodeId(), ArtifactNode.class).getVersionNodes().iterator().next().getNodeId();
        assertEquals(twice.getNodeId(), versionNodeId);
    }
}

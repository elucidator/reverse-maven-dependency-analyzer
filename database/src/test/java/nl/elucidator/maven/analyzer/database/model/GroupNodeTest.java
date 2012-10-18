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

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases
 */
@Transactional
public class GroupNodeTest extends AbstractDatabaseTest {
    private static final GroupNode GROUP_NODE = new GroupNode("groupId");


    @Test
    public void getGroup() {
        assertEquals("groupId", GROUP_NODE.getG());
    }

    @Test
    public void addGroup() {
        GroupNode addedGroupNode = mock(GroupNode.class);
        when(addedGroupNode.getG()).thenReturn("groupId:subGroup");
        GROUP_NODE.addGroup(addedGroupNode);
        assertNotNull(GROUP_NODE.getGroupNodes());
        assertEquals(addedGroupNode, GROUP_NODE.getGroupNodes().iterator().next());
    }

    @Test
    public void addArtifact() {
        ArtifactNode artifactNode = mock(ArtifactNode.class);
        GROUP_NODE.addArtifact(artifactNode);

        assertEquals(1, GROUP_NODE.getArtifactNodes().size());
        assertEquals(artifactNode, GROUP_NODE.getArtifactNodes().iterator().next());
    }

    @Test
    @Transactional
    public void insert() {
        template.save(GROUP_NODE);
        GroupNode retrieved = this.groupRepository.findByG("groupId");
        assertEquals(GROUP_NODE.getG(), retrieved.getG());
    }

    @Test
    @Transactional
    public void doubleInsert() {
        GroupNode toInsert = new GroupNode("grpA");
        GroupNode groupNode1 = template.save(toInsert);
        GroupNode groupNode2 = template.save(new GroupNode("grpA"));
        GroupNode retrieved = this.groupRepository.findByG("grpA");
        assertEquals(toInsert.getG(), retrieved.getG());
        assertEquals(groupNode1.getNodeId(), retrieved.getNodeId());
        assertEquals(groupNode2.getNodeId(), retrieved.getNodeId());
    }

    @Test
    @Transactional
    public void subGroups() {
        GroupNode groupNode1 = new GroupNode("1");
        GroupNode groupNode2 = new GroupNode("1:2");
        groupNode1.addGroup(groupNode2);

        GroupNode saved = template.save(groupNode1);
        GroupNode found = groupRepository.findByG("1");
        assertEquals(saved.getNodeId(), found.getNodeId());
        Set<GroupNode> groupNodeSet = found.getGroupNodes();
        assertNotNull(groupNodeSet);
        assertEquals(1, groupNodeSet.size());
        GroupNode subGroupNode = groupNodeSet.iterator().next();
        assertNotNull(subGroupNode);
        assertNotNull(subGroupNode.getNodeId());
        //The SubGroup is not lazy loaded
        GroupNode foundSubGroupNode = groupRepository.findOne(subGroupNode.getNodeId());
        assertEquals("1:2", foundSubGroupNode.getG());
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSubGroup() {
        GroupNode groupNode1 = new GroupNode("1");
        GroupNode groupNode2 = new GroupNode("A:B");
        groupNode1.addGroup(groupNode2);
    }
}

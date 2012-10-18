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

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Container for groupId's
 */
@NodeEntity
public class GroupNode {
    @GraphId
    private Long nodeId;
    @Indexed(unique = true)
    private String g;
    @RelatedTo(type = RelationType.SUBGROUP)
    private Set<GroupNode> groupNodes;
    @RelatedTo(type = RelationType.HAS)
    private Set<ArtifactNode> artifactNodes;

    public GroupNode(String g) {
        this.g = g;
    }

    private GroupNode() {
    }

    public Long getNodeId() {
        return nodeId;
    }

    public String getG() {
        return g;
    }

    public void addGroup(final GroupNode subGroupNode) {
        if (subGroupNode.getG().startsWith(g)) {

            if (groupNodes == null) {
                groupNodes = new HashSet<GroupNode>();
            }

            groupNodes.add(subGroupNode);
            return;
        }

        throw new IllegalArgumentException(subGroupNode.getG() + " is no sub g of " + g);
    }

    public Set<GroupNode> getGroupNodes() {
        return groupNodes;
    }

    public Set<ArtifactNode> getArtifactNodes() {
        return artifactNodes;
    }

    public void addArtifact(final ArtifactNode artifactNode) {
        if (artifactNodes == null) {
            artifactNodes = new HashSet<ArtifactNode>();
        }

        artifactNodes.add(artifactNode);
    }

    @Override
    public String toString() {
        return "GroupNode{" +
                "nodeId=" + nodeId +
                ", g='" + g + '\'' +
                ", groupNodes=" + groupNodes +
                ", artifactNodes=" + artifactNodes +
                '}';
    }
}

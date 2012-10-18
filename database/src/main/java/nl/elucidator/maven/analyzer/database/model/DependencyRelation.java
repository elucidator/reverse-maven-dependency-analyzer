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

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Relation between Artifacts.
 * Actually the relation is between {@link VersionNode}s of a {@link ArtifactNode}
 * Note that the relations are not loaded eagerly by Spring.
 * Thus to get the {@link VersionNode} nodes you must load explicitly.
 */
@RelationshipEntity(type = RelationType.DEPENDENCY)
public class DependencyRelation {
    @GraphId
    private Long nodeId;
    private Scope scope;
    @StartNode
    VersionNode startNode;
    @EndNode
    VersionNode endNode;

    public DependencyRelation(Scope scope, VersionNode startNode, VersionNode endNode) {
        this.scope = scope;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    private DependencyRelation() {
    }

    public Long getNodeId() {
        return nodeId;
    }

    public Scope getScope() {
        return scope;
    }

    public VersionNode getStartNode() {
        return startNode;
    }

    public VersionNode getEndNode() {
        return endNode;
    }
}

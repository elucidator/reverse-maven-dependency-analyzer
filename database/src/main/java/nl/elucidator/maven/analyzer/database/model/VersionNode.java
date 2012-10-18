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

import org.neo4j.graphdb.Direction;
import org.sonatype.aether.artifact.Artifact;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import java.util.HashSet;
import java.util.Set;

/**
 * VersionNode of an {@link ArtifactNode}
 */
@NodeEntity
public class VersionNode {
    @GraphId
    private Long nodeId;
    private String version;
    private String extension;
    private String classifier;

    @Indexed(unique = true)
    private String gav;
    @RelatedToVia(type = RelationType.DEPENDENCY, direction = Direction.OUTGOING)
    private Set<DependencyRelation> dependencies;


    public VersionNode(final Artifact artifact) {
        this.version = artifact.getVersion();
        this.gav = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
        this.extension = artifact.getExtension();
        this.classifier = artifact.getClassifier();
    }


    /**
     * Constructor
     *
     * @param gav version
     */
    public VersionNode(final String gav) {
        //TODO split all elements
        String[] splitted = gav.split(":");
        if (splitted.length < 3) {
            throw new IllegalArgumentException("A GAV has at least 3 elements, " + gav + "does not.");
        }
        this.version = splitted[2];
        this.gav = gav;
    }

    /**
     * Default constructor required by Spring
     * private to prevent empty construction
     */
    private VersionNode() {
        //Left empty
    }

    public void addDependency(DependencyRelation dependency) {
        if (dependencies == null) {
            dependencies = new HashSet<DependencyRelation>();
        }
        dependencies.add(dependency);
    }

    public Set<DependencyRelation> getDependencies() {
        return dependencies;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "VersionNode{" +
                "nodeId=" + nodeId +
                ", version='" + version + '\'' +
                '}';
    }
}

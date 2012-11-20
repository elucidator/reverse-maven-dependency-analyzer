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
import org.springframework.data.neo4j.annotation.*;

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
    @Fetch
    private Set<String> extensions;
    @Fetch
    private Set<String> classifiers;

    @Indexed(unique = true)
    private String gav;
    @RelatedToVia(type = RelationType.DEPENDENCY, direction = Direction.OUTGOING)
    private Set<DependencyRelation> dependencies;


    public VersionNode(final Artifact artifact) {
        this.version = artifact.getVersion();
        this.gav = artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
        addExtensions(artifact.getExtension());
        addClassifier(artifact.getClassifier());
    }

    private boolean addExtensions(String extension) {
        if (extensions == null) {
            extensions = new HashSet<String>();
        }
        return extensions.add(extension);
    }


    /**
     * Default constructor required by Spring
     * private to prevent empty construction
     */
    private VersionNode() {
        //Left empty
    }

    public boolean addClassifier(final String classifier) {
        if (classifier == null || classifier.isEmpty()) {
            return false;
        }
        if (classifiers == null) {
            classifiers = new HashSet<String>();
        }

        return classifiers.add(classifier);
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

    public Set<String> getExtensions() {
        return extensions;
    }

    public Set<String> getClassifiers() {
        return classifiers;
    }

    public String getGav() {
        return gav;
    }

    @Override
    public String toString() {
        return "VersionNode{" +
                "nodeId=" + nodeId +
                ", version='" + version + '\'' +
                ", classifier='" + classifiers + '\'' +
                ", extension='" + extensions + '\'' +
                '}';
    }

    /**
     * update data for the artifact. This can be either a classifier or an extension
     *
     * @param artifact the artifact
     * @return true when something has changes
     */
    public boolean update(Artifact artifact) {
        boolean extensionAdded = addExtensions(artifact.getExtension());
        boolean classifierAdded = addClassifier(artifact.getClassifier());
        return extensionAdded || classifierAdded;
    }
}

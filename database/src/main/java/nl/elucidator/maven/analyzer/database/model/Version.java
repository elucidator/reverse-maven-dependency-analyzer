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
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import java.util.HashSet;
import java.util.Set;

/**
 * Version of an {@link Artifact}
 */
@NodeEntity
public class Version {
    @GraphId
    private Long nodeId;
    private String version;

    @RelatedToVia(type = RelationType.DEPENDENCY, direction = Direction.OUTGOING)
    private Set<DependencyRelation> dependencies;

    /**
     * Constructor
     * @param version version
     */
    public Version(String version) {
        this.version = version;
    }

    /**
     * Default constructor required by Spring
     * private to prevent empty construction
     */
    private Version() {
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
        return "Version{" +
                "nodeId=" + nodeId +
                ", version='" + version + '\'' +
                '}';
    }
}

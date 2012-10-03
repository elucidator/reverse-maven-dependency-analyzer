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

import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Node entity for Artifacts
 */
@NodeEntity
public class Artifact {

    @GraphId
    private Long nodeId;
    @Indexed
    private String ga;
    private String groupId;
    private String artifactId;

    //The Fetch is acceptable cause there is a limited set of versions
    //associated with a Artifact
    @RelatedTo(type = RelationType.VERSION)
    @Fetch
    private Set<Version> versions;


    /**
     * Constructor required by Spring
     */
    private Artifact() {
        //Empty
    }

    /**
     * Default constructor
     *
     * @param groupId    groupId
     * @param artifactId artifactId
     */
    public Artifact(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.ga = groupId + ":" + artifactId;
    }

    /**
     * Add a {@link Version}
     *
     * @param version version
     */
    public void addVersion(final Version version) {
        if (versions == null) {
            versions = new HashSet<Version>();
        }

        versions.add(version);
    }

    public Long getNodeId() {
        return nodeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGa() {
        return ga;
    }

    public Set<Version> getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "nodeId=" + nodeId +
                ", ga='" + ga + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
//                ", versions=" + versions +
                '}';
    }
}

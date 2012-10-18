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

import org.sonatype.aether.artifact.Artifact;
import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Node entity for Artifacts
 */
@NodeEntity
public class ArtifactNode {

    @GraphId
    private Long nodeId;
    @Indexed(unique = true)
    private String ga;
    private String groupId;
    private String artifactId;

    //The Fetch is acceptable cause there is a limited set of versionNodes
    //associated with a ArtifactNode
    @RelatedTo(type = RelationType.VERSION)
    @Fetch
    private Set<VersionNode> versionNodes;
    private String classifier;


    /**
     * Constructor required by Spring
     */
    private ArtifactNode() {
        //Empty
    }

    public ArtifactNode(final Artifact artifact) {
        this.artifactId = artifact.getArtifactId();
        this.groupId = artifact.getGroupId();
        this.ga = this.groupId + ":" + this.artifactId;
    }


    /**
     * Default constructor
     *
     * @param groupId    groupId
     * @param artifactId artifactId
     */
    public ArtifactNode(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.ga = groupId + ":" + artifactId;
    }

    /**
     * Add a {@link VersionNode}
     *
     * @param versionNode versionNode
     */
    public void addVersion(final VersionNode versionNode) {
        if (versionNodes == null) {
            versionNodes = new HashSet<VersionNode>();
        }

        versionNodes.add(versionNode);
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

    public Set<VersionNode> getVersionNodes() {
        return versionNodes;
    }

    @Override
    public String toString() {
        return "ArtifactNode{" +
                "nodeId=" + nodeId +
                ", ga='" + ga + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", versionNodes=" + versionNodes +
                '}';
    }
}

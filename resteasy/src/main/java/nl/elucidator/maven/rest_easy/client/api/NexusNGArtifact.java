/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.maven.rest_easy.client.api;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * groupId (string)	0/1	 The group id of the artifact.
 * artifactId (string)	0/1	 The artifact id of the artifact.
 * version (string)	0/1	 The version of the artifact.
 * latestSnapshot (string)	0/1	 The latest snapshot version of the artifact.
 * latestSnapshotRepositoryId (string)	0/1	 The repository of latest snapshot version of the artifact.
 * latestRelease (string)	0/1	 The latest release version of the artifact.
 * latestReleaseRepositoryId (string)	0/1	 The repository of latest release version of the artifact.
 * highlightedFragment (string)	0/1	 A HTML highlighted fragment of the matched hit.
 * artifactHit (artifactHit)	0/unbounded	 Field artifactHits.
 */
@XmlRootElement
public class NexusNGArtifact implements Visitable {
    private static final String INDENT = "\n\t\t";
    // (string)	0/1	 The group id of the artifact.
    String groupId;
    // (string)	0/1	 The artifact id of the artifact.
    String artifactId;
    // (string)	0/1	 The version of the artifact.
    String version;
    // (string)	0/1	 The latest snapshot version of the artifact.
    String latestSnapshot;
    // (string)	0/1	 The repository of latest snapshot version of the artifact.
    String latestSnapshotRepositoryId;
    // (string)	0/1	 The latest release version of the artifact.
    String latestRelease;
    // (string)	0/1	 The repository of latest release version of the artifact.
    String latestReleaseRepositoryId;
    // (string)	0/1	 A HTML highlighted fragment of the matched hit.
    String highlightedFragment;
    // (artifactHit)	0/unbounded	 Field artifactHits.
    List<ArtifactHit> artifactHits;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLatestSnapshot() {
        return latestSnapshot;
    }

    public void setLatestSnapshot(String latestSnapshot) {
        this.latestSnapshot = latestSnapshot;
    }

    public String getLatestSnapshotRepositoryId() {
        return latestSnapshotRepositoryId;
    }

    public void setLatestSnapshotRepositoryId(String latestSnapshotRepositoryId) {
        this.latestSnapshotRepositoryId = latestSnapshotRepositoryId;
    }

    public String getLatestRelease() {
        return latestRelease;
    }

    public void setLatestRelease(String latestRelease) {
        this.latestRelease = latestRelease;
    }

    public String getLatestReleaseRepositoryId() {
        return latestReleaseRepositoryId;
    }

    public void setLatestReleaseRepositoryId(String latestReleaseRepositoryId) {
        this.latestReleaseRepositoryId = latestReleaseRepositoryId;
    }

    public String getHighlightedFragment() {
        return highlightedFragment;
    }

    public void setHighlightedFragment(String highlightedFragment) {
        this.highlightedFragment = highlightedFragment;
    }

    public List<ArtifactHit> getArtifactHits() {
        return artifactHits;
    }

    public void setArtifactHits(List<ArtifactHit> artifactHits) {
        this.artifactHits = artifactHits;
    }

    @Override
    public void accept(final ResponseVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return INDENT + "NexusNGArtifact{" +
                INDENT + " groupId='" + groupId + '\'' +
                INDENT + " artifactId='" + artifactId + '\'' +
                INDENT + " version='" + version + '\'' +
                INDENT + " latestSnapshot='" + latestSnapshot + '\'' +
                INDENT + " latestSnapshotRepositoryId='" + latestSnapshotRepositoryId + '\'' +
                INDENT + " latestRelease='" + latestRelease + '\'' +
                INDENT + " latestReleaseRepositoryId='" + latestReleaseRepositoryId + '\'' +
                INDENT + " highlightedFragment='" + highlightedFragment + '\'' +
                INDENT + artifactHits +
                INDENT + '}' + "\n\t";
    }
}

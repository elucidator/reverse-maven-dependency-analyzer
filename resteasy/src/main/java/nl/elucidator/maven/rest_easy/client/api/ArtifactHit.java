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

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 repositoryId (string)	0/1	 The repository ID.
 artifactLinks (artifactLinks)	0/unbounded	 Field artifactLinks
 */
@XmlRootElement
public class ArtifactHit implements Visitable {
    private static final String INDENT = "\n\t\t\t";
    // (string)	0/1	 The repository ID.
    private String repositoryId;
    // (artifactLinks)	0/unbounded	 Field artifactLinks
    private List<ArtifactLink> artifactLinks;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(final String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public List<ArtifactLink> getArtifactLinks() {
        return artifactLinks;
    }

    public void setArtifactLinks(final List<ArtifactLink> artifactLinks) {
        this.artifactLinks = artifactLinks;
    }

    @Override
    public void accept(final ResponseVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return INDENT + "ArtifactHit{" +
                INDENT + " repositoryId='" + repositoryId + '\'' +
                INDENT + artifactLinks +
                INDENT + '}' + "\n\t\t";
    }
}

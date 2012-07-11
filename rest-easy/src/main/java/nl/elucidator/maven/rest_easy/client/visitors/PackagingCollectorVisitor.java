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

package nl.elucidator.maven.rest_easy.client.visitors;

import nl.elucidator.maven.rest_easy.client.api.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/19/12
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PackagingCollectorVisitor implements ResponseVisitor {
    private List<SearchArtifact>  artifacts = new ArrayList<SearchArtifact>();
    private final String packaging;

    public PackagingCollectorVisitor(String packaging) {
        this.packaging = packaging;
    }

    @Override
    public void visit(SearchResponse response) {
        for (NexusNGArtifact nexusNGArtifact : response.getData()) {
            this.visit(nexusNGArtifact);
        }
    }

    @Override
    public void visit(ArtifactHit hit) {
        //Nothing to do
    }

    @Override
    public void visit(ArtifactLink link) {
        //Nothing to do
    }

    @Override
    public void visit(NexusNGArtifact nexusArtifact) {
        for (ArtifactHit artifactHit : nexusArtifact.getArtifactHits()) {
            for (ArtifactLink link: artifactHit.getArtifactLinks()) {
                if (link.getExtension().equalsIgnoreCase(packaging)) {
                    SearchArtifact artifact = new SearchArtifact();
                    artifact.artifactId = nexusArtifact.getArtifactId();
                    artifact.groupId = nexusArtifact.getGroupId();
                    artifact.packaging = link.getExtension();
                    artifact.version = nexusArtifact.getVersion();
                    artifact.classifier = link.getClassifier();
                    artifacts.add(artifact);
                }
            }
        }
    }

    @Override
    public void visit(RepositoryDetail detail) {
        //Nothing to do.
    }

    @Override
    public String toString() {
        return "PackagingCollectorVisitor{" +
                "artifacts=" + artifacts +
                '}';
    }
}

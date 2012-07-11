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

package nl.elucidator.maven.rest_easy.client.visitors;

import nl.elucidator.maven.rest_easy.client.api.*;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 7/4/12
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAVVisitor implements ResponseVisitor {

    @Override
    public void visit(SearchResponse response) {
        for (NexusNGArtifact nexusNGArtifact : response.getData()) {
            nexusNGArtifact.accept(this);
        }
    }

    @Override
    public void visit(ArtifactHit hit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void visit(ArtifactLink link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void visit(NexusNGArtifact nexusArtifact) {
        System.out.println(nexusArtifact.getGroupId() + ":" + nexusArtifact.getArtifactId() + ":" + nexusArtifact.getVersion());
    }

    @Override
    public void visit(RepositoryDetail detail) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

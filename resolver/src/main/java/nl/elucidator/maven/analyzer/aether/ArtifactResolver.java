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

package nl.elucidator.maven.analyzer.aether;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolver for artifacts
 */
@Component
public class ArtifactResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactResolver.class);

    private final RepositorySystem system;
    private final RemoteRepository repo;
    private final RepositorySystemSession session;

    public ArtifactResolver() {
        system = Booter.newRepositorySystem();

        session = Booter.newRepositorySystemSession(system);

        repo = Booter.newCentralRepository();
    }

    public List<DependencyResultRecord> resolve(Artifact artifact) throws DependencyCollectionException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving: " + artifact);
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

//        FlatDependencyGraphDumper flat = new FlatDependencyGraphDumper();
//        collectResult.getRoot().accept(flat);
//
        List<DependencyResultRecord> nodes = new ArrayList<DependencyResultRecord>();
//        nodes.addAll(flat.getNodes());

        TransitiveDependencyGraphDumper transitive = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitive);
        nodes.addAll(transitive.getNodes());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result " + nodes.size() + " depending artifacts.");
        }

        return nodes;
    }
}

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

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolver for artifacts
 */
public class ArtifactResolver {

    private final RepositorySystem system;
    private final RemoteRepository repo;
    private final RepositorySystemSession session;

    public ArtifactResolver() {
        system = Booter.newRepositorySystem();

        session = Booter.newRepositorySystemSession(system);

        repo = Booter.newCentralRepository();
    }

    public List<String> resolve(String gav) throws DependencyCollectionException {
        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.shared:maven-dependency-analyzer:1.2");
        DefaultArtifact artifact = new DefaultArtifact(gav);
        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

        FlatDependencyGraphDumper flat = new FlatDependencyGraphDumper();
        collectResult.getRoot().accept(flat);

        List<String> nodes = new ArrayList<String>();
        nodes.addAll(flat.getNodes());

        TransitiveDependencyGraphDumper transitive = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitive);
        nodes.addAll(transitive.getNodes());


        return nodes;
    }
}

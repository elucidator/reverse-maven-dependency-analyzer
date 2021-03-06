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

package nl.elucidator.maven.analyzer.database.service;

import nl.elucidator.maven.analyzer.database.model.Scope;
import nl.elucidator.maven.analyzer.database.model.VersionNode;
import org.sonatype.aether.artifact.Artifact;
import org.springframework.transaction.annotation.Transactional;

/**
 * Services towards the database
 */
public interface MavenArtifactService {

    @Transactional
    VersionNode addArtifact(final Artifact artifact);


    @Transactional
    void addRelation(final VersionNode from, final VersionNode to, final Scope scope);

    @Transactional
    void addRelation(final Artifact from, final Artifact to, final String scope);


}

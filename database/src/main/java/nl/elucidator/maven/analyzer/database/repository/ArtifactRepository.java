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

package nl.elucidator.maven.analyzer.database.repository;

import nl.elucidator.maven.analyzer.database.model.ArtifactNode;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

/**
 * Data Repository for Artifacts
 * (Spring does all the magic @see http://java.dzone.com/articles/domain-modeling-spring-data)
 */
public interface ArtifactRepository extends GraphRepository<ArtifactNode>,
        NamedIndexRepository<ArtifactNode>,
        RelationshipOperationsRepository<ArtifactNode> {

    ArtifactNode findByGa(String ga);
}

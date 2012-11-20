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

import nl.elucidator.maven.analyzer.database.repository.ArtifactRepository;
import nl.elucidator.maven.analyzer.database.repository.GroupRepository;
import nl.elucidator.maven.analyzer.database.repository.VersionRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base for DB test cases
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/database-context.xml"})
public abstract class AbstractDatabaseTest {
    @Autowired
    protected ArtifactRepository artifactRepository;
    @Autowired
    protected GroupRepository groupRepository;
    @Autowired
    protected VersionRepository versionRepository;

    @Autowired
    protected Neo4jOperations template;

    @Before
    public void before() {
        EndResult<VersionNode> versionNodes = template.findAll(VersionNode.class);
        for (VersionNode versionNode : versionNodes) {
            template.delete(versionNode);
        }

        EndResult<GroupNode> groupNodes = template.findAll(GroupNode.class);
        for (GroupNode groupNode : groupNodes) {
            template.delete(groupNode);
        }

        EndResult<ArtifactNode> artifactNodes = template.findAll(ArtifactNode.class);
        for (ArtifactNode artifactNode : artifactNodes) {
            template.delete(artifactNode);
        }
    }


    protected Artifact makeArtifact(final String g, final String a, final String v, final String extension, final String classifier) {
        return new DefaultArtifact(g, a, classifier, extension, v);
    }

    protected Artifact makeArtifact(final String g, final String a, final String v) {
        return makeArtifact(g, a, v, "jar", null);
    }

    protected Artifact makeArtifact(final String gav) {
        String[] splitter = gav.split(":");
        return makeArtifact(splitter[0], splitter[1], splitter[2]);
    }

}

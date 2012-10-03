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

import nl.elucidator.maven.analyzer.database.DependencyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 10/2/12
 * Time: 8:21 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/database-context.xml"})
public class ArtifactTest {

    @Autowired
    DependencyRepository dependencyRepository;

    @Autowired
    Neo4jOperations template;

    @Before
    public void beforeClass() {
        Cleaner cleaner = new Cleaner(template);
        cleaner.cleanDb();

    }

    @Test
    @Transactional
    public void storeAndRetrieve() {
        Artifact toSave = new Artifact("groupIdA", "artifactIdA");
        Artifact artifact = template.save(toSave);
        Artifact retrieve = template.findOne(artifact.getNodeId(), Artifact.class);
        assertEquals(artifact.getArtifactId(), retrieve.getArtifactId());
        System.out.println("retrieve.getNodeId() = " + retrieve.getNodeId());
    }

    @Test
    @Transactional
    public void versionRelations() {

        Artifact artifact = new Artifact("groupIdB", "artifactIdB");
        artifact = template.save(artifact);
        System.out.println("artifact.getNodeId() = " + artifact.getNodeId());

        Version version1_0 = new Version("1.0");
        version1_0 = template.save(version1_0);
        template.save(version1_0);
        artifact.addVersion(version1_0);

        Version version1_1 = new Version("1.1");
        version1_1 = template.save(version1_1);
        template.save(version1_1);
        artifact.addVersion(version1_1);
        template.save(artifact);

        Artifact retrieved = this.dependencyRepository.findById(artifact.getGa());

        Iterable<Version> versionRelations = retrieved.getVersions();
        assertTrue(versionRelations.iterator().hasNext());
        for (Version version : versionRelations) {
            assertTrue(version.getVersion().equals("1.0") || version.getVersion().equals("1.1"));
        }
    }

    @Test
    @Transactional
    public void versions() {

        Artifact artifact = new Artifact("groupIdC", "artifactIdC");
        Version version1_0 = new Version("1.0");
        Version version1_1 = new Version("1.1");
        artifact.addVersion(version1_0);
        artifact.addVersion(version1_1);
        artifact = template.save(artifact);
        System.out.println("artifact.getNodeId() = " + artifact.getNodeId());

        Artifact retrieved = this.dependencyRepository.findById(artifact.getGa());

        Iterable<Version> versionRelations = retrieved.getVersions();

        for (Version version : versionRelations) {
            assertTrue(version.getVersion().equals("1.0") || version.getVersion().equals("1.1"));
        }
    }

    @Transactional
    @Test
    public void dependency() {
        Version versionA = new Version("a1");
        template.save(versionA);
        System.out.println("versionA = " + template.save(versionA).getNodeId());

        Version versionB =  new Version("c1");
        template.save(versionB);

        DependencyRelation dependencyRelation = new DependencyRelation("compile", versionA, versionB);
        versionA.addDependency(dependencyRelation);
        template.save(dependencyRelation);

        Version retrieved = template.findOne(versionA.getNodeId(), Version.class);
        assertNotNull(retrieved);
        assertNotNull(retrieved.getDependencies());
        assertEquals(1, retrieved.getDependencies().size());
        assertEquals(versionB.getNodeId(), retrieved.getDependencies().iterator().next().getEndNode().getNodeId());
    }
}

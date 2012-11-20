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

package nl.elucidator.maven.analyzer.loader;


import nl.elucidator.maven.analyzer.aether.ArtifactResolver;
import nl.elucidator.maven.analyzer.aether.DependencyResultRecord;
import nl.elucidator.maven.analyzer.database.service.MavenArtifactService;
import nl.elucidator.maven.analyzer.indexer.IndexSearcher;
import org.apache.maven.index.ArtifactInfo;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 10/22/12
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/loader-database-context.xml"})
public class LoaderTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoaderTest.class);
    public static final String REPO_URL = "http://nexus.pieni.nl/nexus/content/groups/public/";
    private static final String WORK_DIRECTORY = "target/index";


    @Autowired
    MavenArtifactService mavenArtifactService;
    @Autowired
    ArtifactResolver resolver;

    private Artifact toArtifact(final ArtifactInfo artifactInfo) {
        return new DefaultArtifact(artifactInfo.groupId, artifactInfo.artifactId, artifactInfo.classifier, artifactInfo.fextension, artifactInfo.version);
    }

    private class ArtifactInfoComparator implements Comparator<ArtifactInfo> {

        @Override
        public int compare(ArtifactInfo artifactInfo, ArtifactInfo artifactInfo2) {
            return artifactInfo.toString().compareTo(artifactInfo2.toString());
        }
    }

    @Test
    public void indexer() throws PlexusContainerException, ComponentLookupException, IOException, DependencyCollectionException, InterruptedException {
        IndexSearcher indexSearcher = new IndexSearcher(REPO_URL, WORK_DIRECTORY);
        indexSearcher.update();
        Set<ArtifactInfo> result = indexSearcher.getUniqueGAV();
        SortedSet sortedSet = new TreeSet<ArtifactInfo>(new ArtifactInfoComparator());
        sortedSet.addAll(result);

        LOGGER.debug("Found " + result.size() + " unique artifacts");
        int i = 0;
        for (ArtifactInfo artifactInfo : result) {
            mavenArtifactService.addArtifact(toArtifact(artifactInfo));
        }

        for (ArtifactInfo artifactInfo : result) {
            Artifact artifact = new DefaultArtifact(artifactInfo.groupId, artifactInfo.artifactId, artifactInfo.classifier, artifactInfo.fextension, artifactInfo.version);
            try {
                List<DependencyResultRecord> resolved = resolver.resolve(artifact);

                for (DependencyResultRecord dependencyResultRecord : resolved) {
                    mavenArtifactService.addRelation(dependencyResultRecord.getFrom(), dependencyResultRecord.getTo(), dependencyResultRecord.getScope());
                }
            } catch (Exception e) {
                LOGGER.error("Error processing: " + artifact, e);
            }
        }


    }

}

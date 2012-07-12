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

package nl.elucidator.maven.analyzer.pre_processor;


import nl.elucidator.maven.analyzer.aether.ArtifactResolver;
import nl.elucidator.maven.analyzer.indexer.IndexSearcher;
import org.apache.maven.index.ArtifactInfo;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.DependencyNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 7/12/12
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Process {
    private static IndexSearcher indexSearcher;
    private static ArtifactResolver artifactResolver;

    public int getArtifactCount() {
        return artifactCount;
    }

    private int artifactCount;

    public Process() throws IOException, PlexusContainerException, ComponentLookupException {
        indexSearcher = new IndexSearcher();
        artifactResolver = new ArtifactResolver();
    }

    public List<String> getAllArtifacts() throws IOException, ComponentLookupException, DependencyCollectionException {
        indexSearcher.update();
        indexSearcher.getUniqueGAV();
        List<String> result = new ArrayList<>();
//        Set<ArtifactInfo> artifactInfoSet = indexSearcher.getUniqueGAV();
//        Iterator<ArtifactInfo> iter = artifactInfoSet.iterator();
//        for (int i = 0; i < 50; i++) {
//            ArtifactInfo artifactInfo = iter.next();
//            if (artifactInfo != null && artifactInfo.classifier == null) {
//                System.out.println("artifactInfo = " + artifactInfo);
//                result.addAll(artifactResolver.resolve(artifactInfo.groupId + ":" + artifactInfo.artifactId + ":" + artifactInfo.version));
//            }
//        }

        artifactCount = 0;
        for (ArtifactInfo artifactInfo : indexSearcher.getUniqueGAV()) {
            if (artifactInfo != null && artifactInfo.classifier == null) {
                result.addAll(artifactResolver.resolve(artifactInfo.groupId + ":" + artifactInfo.artifactId + ":" + artifactInfo.version));
                artifactCount++;
            }
        }

        return result;
    }


}

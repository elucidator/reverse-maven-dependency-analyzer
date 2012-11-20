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

package nl.elucidator.maven.analyzer.indexer;

import org.apache.lucene.queryParser.ParseException;
import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.ArtifactInfoGroup;
import org.apache.maven.index.context.UnsupportedExistingLuceneIndexException;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple test
 */
public class SimpleRun {
    public static void main(String[] args) throws IOException, PlexusContainerException, ComponentLookupException, UnsupportedExistingLuceneIndexException, ParseException {

        IndexSearcher searcher = new IndexSearcher("http://nexus.pieni.nl/nexus/content/groups/public/", "target/indexer"/*"http://mirrors.ibiblio.org/maven2/"*/);
        searcher.update();
        //dumpGav(searcher);

        searchG(searcher);

        //TODO get a full list of packaging, classifiers
    }

    private static void searchG(IndexSearcher searcher) throws IOException {
        List<String> gs = new ArrayList<String>();
        gs.add("junit");
        Map<String, ArtifactInfoGroup> result = searcher.searchGa(gs);
        for (String s : result.keySet()) {
            ArtifactInfoGroup artifactInfoGroup = result.get(s);
            for (ArtifactInfo artifactInfo : artifactInfoGroup.getArtifactInfos()) {
                System.out.println("artifactInfo = " + artifactInfo);
            }
        }
    }

    private static void dumpGav(IndexSearcher searcher) throws IOException, ComponentLookupException {
        Set<ArtifactInfo> result = searcher.getUniqueGAV();
        System.out.println("result.size() = " + result.size());
        for (ArtifactInfo artifactInfo : result) {
            if (artifactInfo.packaging != null && artifactInfo.packaging.equalsIgnoreCase("jar")) {
                System.out.println("artifactInfo = " + artifactInfo);
            }
        }
    }
}

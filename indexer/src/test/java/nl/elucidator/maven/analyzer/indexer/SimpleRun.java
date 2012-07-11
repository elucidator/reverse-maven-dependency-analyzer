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
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/12/12
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRun {
    public static void main(String[] args) throws IOException, PlexusContainerException, ComponentLookupException, UnsupportedExistingLuceneIndexException, ParseException {

        IndexSearcher searcher =  new IndexSearcher();
        searcher.update();
        Set<ArtifactInfo> result = searcher.getUniqueGAV();
        System.out.println("result.size() = " + result.size());
    }
}

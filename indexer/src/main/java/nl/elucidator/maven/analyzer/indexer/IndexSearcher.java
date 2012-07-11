/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.maven.analyzer.indexer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.maven.index.*;
import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.context.IndexUtils;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.context.UnsupportedExistingLuceneIndexException;
import org.apache.maven.index.creator.MinimalArtifactInfoIndexCreator;
import org.apache.maven.index.expr.SourcedSearchExpression;
import org.apache.maven.index.search.grouping.GAGrouping;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.ArtifactRepository;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/13/12
 * Time: 8:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class IndexSearcher {
    private final static Logger LOGGER = LoggerFactory.getLogger(IndexSearcher.class);
    // create Plexus IoC (actually SISU-plexus compat)
    private final DefaultPlexusContainer plexus;
    private final IndexUpdater indexUpdater;

    public IndexSearcher() throws PlexusContainerException, ComponentLookupException, IOException {
        plexus = new DefaultPlexusContainer();
        indexUpdater = new IndexUpdater();
    }

    public void update() throws IOException, ComponentLookupException {
        indexUpdater.update();
    }

    public Set<ArtifactInfo> getUniqueGAV() throws IOException, ComponentLookupException {
        IndexingContext centralContext = indexUpdater.getIndexContext();
        centralContext.lock();
        Set<ArtifactInfo> artifactInfoSet =  new HashSet<>();

        try
        {
            final IndexReader ir = centralContext.getIndexReader();

            for ( int i = 0; i < ir.maxDoc(); i++ )
            {
                if ( !ir.isDeleted( i ) )
                {
                    final Document doc = ir.document( i );

                    final ArtifactInfo ai = IndexUtils.constructArtifactInfo(doc, centralContext);
                    artifactInfoSet.add(ai);
                }
            }

        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            centralContext.unlock();
        }
       return artifactInfoSet;
    }
}

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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.NexusIndexer;
import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.context.IndexUtils;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.creator.MinimalArtifactInfoIndexCreator;
import org.apache.maven.index.updater.IndexUpdateRequest;
import org.apache.maven.index.updater.IndexUpdateResult;
import org.apache.maven.index.updater.ResourceFetcher;
import org.apache.maven.index.updater.WagonHelper;
import org.apache.maven.wagon.Wagon;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/12/12
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexUpdater {
    // create Plexus IoC (actually SISU-plexus compat)
    DefaultPlexusContainer plexus;
    Wagon wagon;
    TransferListener listener = new TransferListener();
    // Create context for central repository index
    IndexingContext centralContext;
    // Files where local cache is (if any) and Lucene Index should be located
    File centralLocalCache = new File( "target/indexer/central-cache" );
    File centralIndexDir = new File( "target/indexer/central-index" );

    public IndexUpdater() throws PlexusContainerException, ComponentLookupException, IOException {
        plexus = new DefaultPlexusContainer();
        wagon = plexus.lookup( Wagon.class, "http" );
        // lookup the indexer instance from plexus
        NexusIndexer nexusIndexer = plexus.lookup( NexusIndexer.class );
        // Creators we want to use (search for fields it defines)
        List<IndexCreator> indexers = new ArrayList<IndexCreator>();
        indexers.add( plexus.lookup( IndexCreator.class, "min" ) );
        centralContext =
        nexusIndexer.addIndexingContextForced( "central", "central", centralLocalCache, centralIndexDir,
                "http://nexus.pieni.nl/nexus/content/groups/public/", null, indexers );
    }

    IndexingContext getIndexContext() {
        return centralContext;
    }

    public IndexUpdateResult update() throws ComponentLookupException, IOException {
        ResourceFetcher resourceFetcher = new WagonHelper.WagonFetcher( wagon, listener, null, null );

        Date centralContextCurrentTimestamp = centralContext.getTimestamp();
        IndexUpdateRequest updateRequest = new IndexUpdateRequest( centralContext, resourceFetcher );
        org.apache.maven.index.updater.IndexUpdater updater = plexus.lookup( org.apache.maven.index.updater.IndexUpdater.class );
        IndexUpdateResult updateResult = updater.fetchAndUpdateIndex( updateRequest );
        if ( updateResult.isFullUpdate() )
        {
            System.out.println( "Full update happened!" );
        }
        else if ( updateResult.getTimestamp().equals( centralContextCurrentTimestamp ) )
        {
            System.out.println( "No update needed, index is up to date!" );
        }
        else
        {
            System.out.println( "Incremental update happened, change covered " + centralContextCurrentTimestamp
                    + " - " + updateResult.getTimestamp() + " period." );
        }

        System.out.println();

        return updateResult;


    }
}

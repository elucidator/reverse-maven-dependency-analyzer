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

import org.apache.maven.index.NexusIndexer;
import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.updater.IndexUpdateRequest;
import org.apache.maven.index.updater.IndexUpdateResult;
import org.apache.maven.index.updater.ResourceFetcher;
import org.apache.maven.index.updater.WagonHelper;
import org.apache.maven.wagon.Wagon;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Update/download of the Nexus index
 */
public class IndexUpdater {
    public static final Logger LOGGER = LoggerFactory.getLogger(IndexUpdater.class);

    // create Plexus IoC (actually SISU-plexus compat)
    private DefaultPlexusContainer plexus;
    private Wagon wagon;
    private TransferListener listener = new TransferListener();
    // Create context for central repository index
    private IndexingContext centralContext;
    private final NexusIndexer nexusIndexer;

    public IndexUpdater(final String repoUrl, final String workDirectoryBase) throws PlexusContainerException, ComponentLookupException, IOException {
        plexus = new DefaultPlexusContainer();
        wagon = plexus.lookup(Wagon.class, "http");
        // lookup the indexer instance from plexus
        nexusIndexer = plexus.lookup(NexusIndexer.class);
        // Creators we want to use (search for fields it defines)
        List<IndexCreator> indexers = new ArrayList<IndexCreator>();
        indexers.add(plexus.lookup(IndexCreator.class, "min"));

        File centralLocalCache = new File(workDirectoryBase + "/central-cache");
        File centralIndexDir = new File(workDirectoryBase + "/central-index");

        centralContext =
                nexusIndexer.addIndexingContextForced("central", "central", centralLocalCache, centralIndexDir,
                        repoUrl, null, indexers);


        dumpContextInfo(centralContext);
    }

    private void dumpContextInfo(IndexingContext centralContext) {
        LOGGER.info("Index context information:");
        LOGGER.info("\tId                  : " + centralContext.getId());
        LOGGER.info("\tIndex directory     : " + centralContext.getIndexDirectoryFile().getAbsolutePath());
        LOGGER.info("\tRepository directory: " + centralContext.getRepository().getAbsolutePath());
        LOGGER.info("\tRepository URL      : " + centralContext.getRepositoryUrl());
        LOGGER.info("\tIndex update URL    : " + centralContext.getIndexUpdateUrl());
    }

    public IndexingContext getIndexContext() {
        return centralContext;
    }

    public NexusIndexer getNexusIndexer() {
        return nexusIndexer;
    }

    public IndexUpdateResult update() throws ComponentLookupException, IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Updating index...");
        }

        ResourceFetcher resourceFetcher = new WagonHelper.WagonFetcher(wagon, listener, null, null);

        Date centralContextCurrentTimestamp = centralContext.getTimestamp();
        IndexUpdateRequest updateRequest = new IndexUpdateRequest(centralContext, resourceFetcher);
        org.apache.maven.index.updater.IndexUpdater updater = plexus.lookup(org.apache.maven.index.updater.IndexUpdater.class);
        IndexUpdateResult updateResult = updater.fetchAndUpdateIndex(updateRequest);
        if (updateResult.isFullUpdate()) {
            LOGGER.info("Full update happened!");
        } else if (updateResult.getTimestamp().equals(centralContextCurrentTimestamp)) {
            LOGGER.info("No update needed, index is up to date!");
        } else {
            LOGGER.info("Incremental update happened, change covered " + centralContextCurrentTimestamp
                    + " - " + updateResult.getTimestamp() + " period.");
        }

        return updateResult;
    }
}

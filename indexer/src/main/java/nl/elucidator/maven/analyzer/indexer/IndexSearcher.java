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
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.maven.index.*;
import org.apache.maven.index.context.IndexUtils;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.expr.StringSearchExpression;
import org.apache.maven.index.search.grouping.GAGrouping;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Search over index
 */
public class IndexSearcher {
    private final static Logger LOGGER = LoggerFactory.getLogger(IndexSearcher.class);
    // create Plexus IoC (actually SISU-plexus compat)
    private final DefaultPlexusContainer plexus;
    private final IndexUpdater indexUpdater;
    private final NexusIndexer indexer;

    public IndexSearcher(final String repoUrl, final String workBaseDirectory) throws PlexusContainerException, ComponentLookupException, IOException {
        plexus = new DefaultPlexusContainer();
        indexUpdater = new IndexUpdater(repoUrl, workBaseDirectory);
        indexer = indexUpdater.getNexusIndexer();

    }

    public void update() throws IOException, ComponentLookupException {
        indexUpdater.update();
    }

    public Map<String, ArtifactInfoGroup> searchGa(final List<String> groups) throws IOException {

        Query query = createQuery(groups);

        GroupedSearchRequest groupedSearchRequest = new GroupedSearchRequest(query, new GAGrouping());

        GroupedSearchResponse flatSearchResponse = indexer.searchGrouped(groupedSearchRequest);

        return flatSearchResponse.getResults();
    }

    public Set<ArtifactInfo> getUniqueGAV() throws IOException, ComponentLookupException {
        IndexingContext centralContext = indexUpdater.getIndexContext();
        centralContext.lock();
        Set<ArtifactInfo> artifactInfoSet = new HashSet<ArtifactInfo>();

        try {
            final IndexReader ir = centralContext.getIndexReader();

            for (int i = 0; i < ir.maxDoc(); i++) {
                if (!ir.isDeleted(i)) {
                    final Document doc = ir.document(i);

                    final ArtifactInfo ai = IndexUtils.constructArtifactInfo(doc, centralContext);
                    if (ai != null) {
                        artifactInfoSet.add(ai);
                    }
                }
            }

        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            centralContext.unlock();
        }
        return artifactInfoSet;
    }

    /**
     * Create the query to perform
     *
     * @param groupPatterns list of patters to search for
     * @return {@link org.apache.lucene.search.BooleanQuery}
     */
    private Query createQuery(final List<String> groupPatterns) {
        BooleanQuery bq = new BooleanQuery();

        Query query;

        Field field = MAVEN.GROUP_ID;
        BooleanQuery groupQuery = new BooleanQuery();
        for (String pattern : groupPatterns) {

            StringSearchExpression expression = new StringSearchExpression(pattern);
            query = indexer.constructQuery(field, expression);
            groupQuery.add(query, BooleanClause.Occur.SHOULD);
        }
        bq.add(groupQuery, BooleanClause.Occur.MUST);

        Query queryClassifierSources = indexer.constructQuery(MAVEN.CLASSIFIER, new StringSearchExpression("sources"));
        Query queryClassifierJavaDoc = indexer.constructQuery(MAVEN.CLASSIFIER, new StringSearchExpression("javadoc"));

        bq.add(new BooleanClause(queryClassifierJavaDoc, BooleanClause.Occur.MUST_NOT));
        bq.add(new BooleanClause(queryClassifierSources, BooleanClause.Occur.MUST_NOT));

        return bq;

    }
}

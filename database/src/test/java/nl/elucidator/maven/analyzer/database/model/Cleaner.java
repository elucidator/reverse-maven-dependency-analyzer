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

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.DelegatingGraphDatabase;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Clean the DB
 */

@Component
public class Cleaner {
    @Autowired
    private GraphDatabaseService graph;

    public Cleaner(Neo4jOperations template) {
        this.graph = ((DelegatingGraphDatabase) template.getGraphDatabase()).getGraphDatabaseService();

    }

    public Cleaner() {
    }

    public Map<String, Object> cleanDb() {
        Map<String, Object> result = new HashMap<String, Object>();
        Transaction tx = graph.beginTx();
        try {
            removeNodes(result);
            clearIndex(result);
            tx.success();
        } finally {
            tx.finish();
        }
        return result;
    }

    private void removeNodes(Map<String, Object> result) {
        Node refNode = graph.getReferenceNode();
        int nodes = 0, relationships = 0;
        for (Node node : graph.getAllNodes()) {
            for (Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                rel.delete();
                relationships++;
            }
            if (!refNode.equals(node)) {
                node.delete();
                nodes++;
            }
        }
        result.put("nodes", nodes);
        result.put("relationships", relationships);

    }

    private void clearIndex(Map<String, Object> result) {
        IndexManager indexManager = graph.index();
        System.out.println("indexManager = " + indexManager);
        //indexManager.getNodeAutoIndexer().getAutoIndex().getEntityType()
        for (String s : indexManager.nodeIndexNames()) {

        }
        result.put("node-indexes", Arrays.asList(indexManager.nodeIndexNames()));
        result.put("relationship-indexes", Arrays.asList(indexManager.relationshipIndexNames()));
        for (String ix : indexManager.nodeIndexNames()) {
            indexManager.forNodes(ix).delete();
        }
        for (String ix : indexManager.relationshipIndexNames()) {
            indexManager.forRelationships(ix).delete();
        }

        indexManager.getNodeAutoIndexer().setEnabled(true);

    }
}

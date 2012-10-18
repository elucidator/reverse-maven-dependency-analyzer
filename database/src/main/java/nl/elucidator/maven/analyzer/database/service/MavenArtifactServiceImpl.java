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

package nl.elucidator.maven.analyzer.database.service;

import nl.elucidator.maven.analyzer.database.model.*;
import nl.elucidator.maven.analyzer.database.repository.ArtifactRepository;
import nl.elucidator.maven.analyzer.database.repository.GroupRepository;
import nl.elucidator.maven.analyzer.database.repository.VersionRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of services
 */
@Transactional
@Component
public class MavenArtifactServiceImpl implements MavenArtifactService {
    private static final Logger LOGGER = Logger.getLogger(MavenArtifactServiceImpl.class);

    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private VersionRepository versionRepository;
    @Autowired
    protected Neo4jOperations template;

    @Override
    public VersionNode addArtifact(String gav) {
        VersionNode versionNode = versionRepository.findByGav(gav);
        if (versionNode != null) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Direct hit version node: " + versionNode);
            }
            return versionNode;
        }

        String[] gavs = gav.split(":");
        ArtifactNode artifactNode = artifactRepository.findByGa(gavs[0] + ":" + gavs[1]);
        if (artifactNode != null) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.debug("Direct hit artifactNode: " + artifactNode);
            }
            versionNode = new VersionNode(gav);
            artifactNode.addVersion(versionNode);
            template.save(artifactNode);
            return versionNode;
        }

        artifactNode = new ArtifactNode(gavs[0], gavs[1]);
        GroupNode groupNode = findGroup(gavs[0]);
        versionNode = new VersionNode(gav);
        artifactNode.addVersion(versionNode);


        groupNode.addArtifact(artifactNode);
        template.save(groupNode);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.debug("Saved groupNode: " + groupNode);
        }

        return versionNode;

    }


    private GroupNode findGroup(final String g) {
        GroupNode groupNode = groupRepository.findByG(g);

        if (groupNode != null) {
            return groupNode;
        }

        return findLastGroup(g);
    }

    private GroupNode findLastGroup(final String g) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.debug("Searching group: " + g);
        }
        GroupNode lastGroup = null;
        if (groupRepository.findByG(g) == null) {
            if (g.lastIndexOf(".") > 0) {
                lastGroup = findGroup(g.substring(0, g.lastIndexOf(".")));
            }
        }
        GroupNode groupNode = new GroupNode(g);
        if (lastGroup != null) {
            lastGroup.addGroup(groupNode);
            template.save(lastGroup);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.debug("Saved groupNode: " + lastGroup);
            }
        }

        return groupNode;
    }

    @Override
    public void addRelation(VersionNode from, VersionNode to, Scope scope) {
        VersionNode fromVersionNode = template.findOne(from.getNodeId(), VersionNode.class);
        VersionNode toVersionNode = template.findOne(to.getNodeId(), VersionNode.class);
        DependencyRelation relation = new DependencyRelation(scope, fromVersionNode, toVersionNode);
        template.save(relation);
    }
}

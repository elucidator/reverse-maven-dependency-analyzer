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

package nl.elucidator.maven.analyzer.aether;

import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.DependencyVisitor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 7/11/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlatDependencyGraphDumper implements DependencyVisitor {
    private DependencyNode root;
    List<String> nodes = new ArrayList<>();
    @Override
    public boolean visitEnter(DependencyNode node) {
        if (root == null) {
            root = node;
            return true;
        }

        nodes.add(root.getDependency().getArtifact() + " -> " + node.getDependency().getArtifact() + "-> " + "resolved");
        return true;
    }

    @Override
    public boolean visitLeave(DependencyNode node) {
        return true;
    }

    public Collection<? extends String> getNodes() {
        return nodes;
    }
}

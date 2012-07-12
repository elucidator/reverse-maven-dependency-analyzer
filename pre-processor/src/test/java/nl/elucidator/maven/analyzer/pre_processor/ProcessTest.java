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

import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Test;
import org.sonatype.aether.collection.DependencyCollectionException;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 7/12/12
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessTest {
    @Test
    public void run() throws IOException, PlexusContainerException, ComponentLookupException, DependencyCollectionException {
        Process process = new Process();
        List<String> result = process.getAllArtifacts();
        for (String s : result) {
            System.out.println("s = " + s);
        }
        System.out.println("result.size() = " + result.size());
        System.out.println("Artifact Count = " + process.getArtifactCount());
    }
}

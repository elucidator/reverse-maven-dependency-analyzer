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

package nl.elucidator.maven.analyzer.loader;

import nl.elucidator.maven.analyzer.database.model.VersionNode;
import nl.elucidator.maven.analyzer.database.repository.VersionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 11/20/12
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/loader-database-context.xml"})
public class SearcherTest {

    @Autowired
    VersionRepository versionRepository;

    @Test
    public void searchA() {
        List<VersionNode> versions = versionRepository.getDependentVersions("junit:junit:4.8.2");
        assertNotNull(versions);
        for (VersionNode version : versions) {
            System.out.println("version = " + version.getGav());
        }
    }
}

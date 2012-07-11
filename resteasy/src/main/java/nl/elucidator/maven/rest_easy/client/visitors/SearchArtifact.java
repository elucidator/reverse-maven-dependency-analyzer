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

package nl.elucidator.maven.rest_easy.client.visitors;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/19/12
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchArtifact {
    public String groupId;
    public String artifactId;
    public String packaging;
    public String version;
    public String classifier;

    @Override
    public String toString() {
        return "\n\tSearchArtifact{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", packaging='" + packaging + '\'' +
                ", classifier=" + classifier + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
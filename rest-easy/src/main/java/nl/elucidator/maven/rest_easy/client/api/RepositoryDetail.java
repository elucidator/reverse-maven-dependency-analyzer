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

package nl.elucidator.maven.rest_easy.client.api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;

/**
 repositoryId (string)	0/1	 The repository ID.
 repositoryName (string)	0/1	 The repository name.
 repositoryContentClass (string)	0/1	 The repository content class.
 repositoryKind (string)	0/1	 The repository kind (hosted, proxy, group).
 repositoryPolicy (string)	0/1	 The repository policy (RELEASE or SNAPSHOT).
 repositoryURL (string)	0/1	 The repository base URL.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class RepositoryDetail implements Visitable {
    private static final String INDENT = "\n\t\t";
    String repositoryId;
    String repositoryName;
    RepositoryKind repositoryKind;
    RepositoryPolicy repositoryPolicy;
    URL repositoryURL;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public RepositoryKind getRepositoryKind() {
        return repositoryKind;
    }

    public void setRepositoryKind(RepositoryKind repositoryKind) {
        this.repositoryKind = repositoryKind;
    }

    public RepositoryPolicy getRepositoryPolicy() {
        return repositoryPolicy;
    }

    public void setRepositoryPolicy(RepositoryPolicy repositoryPolicy) {
        this.repositoryPolicy = repositoryPolicy;
    }

    public URL getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryURL(URL repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    @Override
    public void accept(ResponseVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return INDENT + "RepositoryDetail {" +
                INDENT + " repositoryId='" + repositoryId + '\'' +
                INDENT + " repositoryName='" + repositoryName + '\'' +
                INDENT + " repositoryKind='" + repositoryKind + '\'' +
                INDENT + " repositoryPolicy='" + repositoryPolicy + '\'' +
                INDENT + " repositoryURL='" + repositoryURL + '\'' +
                INDENT + "}" + "\n\t";
    }
}

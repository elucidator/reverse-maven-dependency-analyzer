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

import nl.elucidator.maven.rest_easy.client.api.NexusNGArtifact;
import nl.elucidator.maven.rest_easy.client.api.RepositoryDetail;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 totalCount (int)	1/1	 The total number of results.
 from (int)	1/1	 The starting index of the results.
 count (int)	1/1	 The number of results in this response.
 tooManyResults (boolean)	1/1	 Flag that states if too many results were found.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
@XmlRootElement
public class SearchResponse implements Visitable {

    int totalCount;
    int from;
    int count;
    boolean tooManyResults;
    boolean collapsed;
    List<RepositoryDetail> repoDetails;
    List<NexusNGArtifact> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isTooManyResults() {
        return tooManyResults;
    }

    public void setTooManyResults(final boolean toManyResults) {
        this.tooManyResults = toManyResults;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<RepositoryDetail> getRepoDetails() {
        return repoDetails;
    }

    public void setRepoDetails(List<RepositoryDetail> repoDetails) {
        this.repoDetails = repoDetails;
    }

    public List<NexusNGArtifact> getData() {
        return data;
    }

    public void setData(List<NexusNGArtifact> data) {
        this.data = data;
    }

    @Override
    public void accept(final ResponseVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "\n\ttotalCount=" + totalCount +
                "\n\tfrom=" + from +
                "\n\tcount=" + count +
                "\n\ttooManyResults=" + tooManyResults +
                "\n\tcollapsed=" + collapsed +
                "\n\t" + repoDetails +
                "\n\t" + data +
                '}';
    }
}

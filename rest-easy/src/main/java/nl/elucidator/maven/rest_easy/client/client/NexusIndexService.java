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

package nl.elucidator.maven.rest_easy.client.client;

import nl.elucidator.maven.rest_easy.client.api.SearchResponse;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/13/12
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
//"http://localhost:8081/nexus/service/local/lucene/search?q=jboss&versionexpand=true"
@Path("/service/local/lucene/search")
public interface NexusIndexService {
    @GET
    @Produces("application/json")
    ClientResponse<SearchResponse> query(@QueryParam("q") final String query, @QueryParam("versionexpand")final boolean versionExpand, @QueryParam("count")final int count, @QueryParam("repositoryId") final String repoId);

    @GET
    @Produces("application/json")
    ClientResponse<SearchResponse> query(@QueryParam("q") final String query, @QueryParam("versionexpand")final boolean versionExpand, @QueryParam("count")final int count);

    @GET
    @Produces("application/json")
    ClientResponse<SearchResponse> query(@QueryParam("q") final String query, @QueryParam("versionexpand")final boolean versionExpand, @QueryParam("count")final int count, @QueryParam("from") final int from);

    @GET
    @Produces("application/json")
    ClientResponse<SearchResponse> queryGA(@QueryParam("g")final String groupId, @QueryParam("a")final String artifactId);

}

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

package nl.elucidator.maven.rest_easy.client;

import nl.elucidator.maven.rest_easy.client.api.SearchResponse;
import nl.elucidator.maven.rest_easy.client.client.NexusIndexService;
import nl.elucidator.maven.rest_easy.client.visitors.*;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/13/12
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleClient {
    public static void main(String[] args) {
        try {

            final String URL= "http://oss.sonatype.org/";
            //final String URL= "http://nexus.pieni.nl/nexus";



            //final String queryString="org.apache.openejb";
            final String queryString="nl.pieni";

            final int batchSize = 1000;
            NexusIndexService service = ProxyFactory.create(NexusIndexService.class, URL);
            ClientResponse<SearchResponse> response = service.query(queryString, true, batchSize);

            boolean done = false;
            int fromCount = 0;
            do {

                if (response.getStatus() == 200) {
                    processResponse(response);
                } else {
                    System.out.println("Request processing failed. HTTP Status: " + response.getStatus()
                            + " " + response.getLocation());
                }
                    fromCount += response.getEntity().getCount();
                if (response.getEntity().isTooManyResults()) {
                  response = service.query(queryString, true, batchSize, fromCount);
                }

                if (fromCount >= response.getEntity().getTotalCount()) {
                    done = true;
                }

            } while (!done);


            response.releaseConnection();

//            response = service.queryGA("classworlds", "classworlds");
//            if (response.getStatus() == 200 ) {
//                SearchResponse names = response.getEntity();
////                System.out.println(names);
//                PackagingCollectorVisitor visitor = new PackagingCollectorVisitor("jar");
//                names.accept(visitor);
//                System.out.println("visitor = " + visitor);
//            } else {
//                System.out.println("Request processing failed. HTTP Status: " + response.getStatus()
//                        + " " + response.getLocation());
//            }


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private static void processResponse(ClientResponse<SearchResponse> response) {
        SearchResponse names = response.getEntity();
        System.out.println(names);
//        JarNoClassifierCollectorVisitor visitor = new JarNoClassifierCollectorVisitor();
//        names.accept(visitor);
//        System.out.println("visitor = " + visitor);
        GAVVisitor gavVisitor =  new GAVVisitor();
        System.out.println("GAV:");
        names.accept(gavVisitor);
        ExtendedGAVVisitor extendedGAVVisitor =  new ExtendedGAVVisitor();
        System.out.println("ExtendedGAV:");
        names.accept(extendedGAVVisitor);
    }


}

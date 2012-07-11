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

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: pieter
 * Date: 6/13/12
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleBase {
    public static void main(String[] args) {

        try {

            ClientRequest request = new ClientRequest(
                    "https://oss.sonatype.org/service/local/lucene/search?q=nl.elucidator&versionexpand=true");
            request.accept("application/json");
            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(response.getEntity().getBytes())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

        } catch (
                ClientProtocolException e
                )

        {

            e.printStackTrace();

        } catch (
                IOException e
                )

        {

            e.printStackTrace();

        } catch (
                Exception e
                )

        {

            e.printStackTrace();

        }
    }
}

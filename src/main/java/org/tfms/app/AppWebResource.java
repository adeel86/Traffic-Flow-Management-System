/*
 * Copyright 2019-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tfms.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Sample web resource.
 */
@Path("tfms")
public class AppWebResource extends AbstractWebResource {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @POST
    @Path("intent/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response intentCreate(InputStream inputStream) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            IntentTFMS intentTFMS = mapper.readValue(inputStream, IntentTFMS.class);
            ServiceTFMS serviceTFMS = get(ServiceTFMS.class);
            serviceTFMS.intentCreate(intentTFMS);

        } catch (Exception ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(ex.toString())
                    .build();
        }

        ObjectNode node = mapper().createObjectNode();
        node.put("ApplicationId", "org.tfms.app");
        node.put("Host", "Host Provisioned Successfully.");
        node.put("Intent", "Proactive Load Balancing Intent Created Successfully.");
        return ok(node).build();
    }

    @DELETE
    @Path("intent/delete/{appId}/{srcMac}")
    public Response intentDelete(@PathParam("appId") String appId,
                                     @PathParam("srcMac") String srcMac) {

        try {

            IntentTFMS intentTFMS = new IntentTFMS();

            intentTFMS.setAppId(appId);
            intentTFMS.setHostMac(srcMac);

            ServiceTFMS serviceTFMS = get(ServiceTFMS.class);
            serviceTFMS.intentDelete(intentTFMS);

        } catch (Exception ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(ex.toString())
                    .build();
        }

        return Response.noContent().build();
    }
}

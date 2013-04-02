/**
 *
 * SIROCCO
 * Copyright (C) 2013 France Telecom
 * Contact: sirocco@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *
 */
package org.ow2.sirocco.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.CimiNetworkTemplate;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkTemplateCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkTemplateCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

public class NetworkTemplate extends Resource<CimiNetworkTemplate> {
    private NetworkConfiguration networkConfig;

    /**
     * Instantiates a new network template.
     */
    public NetworkTemplate() {
        super(null, new CimiNetworkTemplate());
    }

    NetworkTemplate(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiNetworkTemplate());
        this.cimiObject.setHref(id);
    }

    NetworkTemplate(final CimiNetworkTemplate cimiObject) {
        super(null, cimiObject);
    }

    NetworkTemplate(final CimiClient cimiClient, final CimiNetworkTemplate cimiObject) {
        super(cimiClient, cimiObject);
        this.networkConfig = new NetworkConfiguration(cimiClient, cimiObject.getNetworkConfig());
    }

    /**
     * Gets the network configuration to be used to create a network from this
     * template.
     * 
     * @return the network configuration to be used to create a network from
     *         this template
     */
    public NetworkConfiguration getNetworkConfig() {
        return this.networkConfig;
    }

    /**
     * Sets the network configuration to be used to create a network from this
     * template.
     * 
     * @param networkConfig the network configuration to be used to create a
     *        network from this template
     */
    public void setNetworkConfig(final NetworkConfiguration networkConfig) {
        this.networkConfig = networkConfig;
        this.cimiObject.setNetworkConfig(networkConfig.cimiObject);
    }

    /**
     * Sets the reference of the network configuration to be used to create a
     * network from this template.
     * 
     * @param networkConfigRef the reference of the network configuration to be
     *        used to create a network from this template
     */
    public void setNetworkConfigRef(final String networkConfigRef) {
        this.networkConfig = new NetworkConfiguration(this.cimiClient, networkConfigRef);
        this.cimiObject.setNetworkConfig(this.networkConfig.cimiObject);
    }

    /**
     * Deletes this network template.
     * 
     * @return the job representing this operation or null if the CIMI provider
     *         does not support Jobs
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public Job delete() throws CimiClientException, CimiProviderException {
        String deleteRef = Helper.findOperation("delete", this.cimiObject);
        if (deleteRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiJob job = this.cimiClient.deleteRequest(deleteRef);
        if (job != null) {
            return new Job(this.cimiClient, job);
        } else {
            return null;
        }
    }

    /**
     * Creates a new network template.
     * 
     * @param client the CIMI client
     * @param networkTemplate the network template to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<NetworkTemplate> createNetworkTemplate(final CimiClient client,
        final NetworkTemplate networkTemplate) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworkTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkTemplateCollection networkTemplateCollection = client
            .getRequest(client.extractPath(client.cloudEntryPoint.getNetworkTemplates().getHref()),
                CimiNetworkTemplateCollectionRoot.class);
        String addRef = Helper.findOperation("add", networkTemplateCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiNetworkTemplate> result = client.postCreateRequest(addRef, networkTemplate.cimiObject,
            CimiNetworkTemplate.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        NetworkTemplate createdNetworkTemplate = result.getResource() != null ? new NetworkTemplate(client,
            result.getResource()) : null;
        return new CreateResult<NetworkTemplate>(job, createdNetworkTemplate);
    }

    /**
     * Retrieves the collection of network templates visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the network templates
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<NetworkTemplate> getNetworkTemplates(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworkTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkTemplateCollection networkTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getNetworkTemplates().getHref()),
            CimiNetworkTemplateCollectionRoot.class, queryParams);

        List<NetworkTemplate> result = new ArrayList<NetworkTemplate>();

        if (networkTemplateCollection.getCollection() != null) {
            for (CimiNetworkTemplate cimiNetworkTemplate : networkTemplateCollection.getCollection().getArray()) {
                result.add(new NetworkTemplate(client, cimiNetworkTemplate));
            }
        }
        return result;
    }

    /**
     * Retrieves the network template with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the network template by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static NetworkTemplate getNetworkTemplateByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new NetworkTemplate(client, client.getCimiObjectByReference(id, CimiNetworkTemplate.class, queryParams));
    }
}

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
import org.ow2.sirocco.cimi.domain.CimiNetworkConfiguration;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkConfigurationCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkConfigurationCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

public class NetworkConfiguration extends Resource<CimiNetworkConfiguration> {

    /**
     * Instantiates a new volume configuration.
     */
    public NetworkConfiguration() {
        super(null, new CimiNetworkConfiguration());
    }

    NetworkConfiguration(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiNetworkConfiguration());
        this.cimiObject.setHref(id);
    }

    NetworkConfiguration(final CimiClient cimiClient, final CimiNetworkConfiguration cimiObject) {
        super(cimiClient, cimiObject);
    }

    /**
     * Sets the type of the network.
     * 
     * @param type the type of the network
     */
    public void setNetworkType(final String type) {
        this.cimiObject.setNetworkType(type);
    }

    /**
     * Gets the type of the network to be created.
     * 
     * @return the type of the network to be created
     */
    public String getNetworkType() {
        return this.cimiObject.getNetworkType();
    }

    public void setCidr(final String cidr) {
        this.cimiObject.setCidr(cidr);
    }

    public String getCidr() {
        return this.cimiObject.getCidr();
    }

    /**
     * Deletes this network config.
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
     * Creates a new network config.
     * 
     * @param client the CIMI client
     * @param networkConfiguration the network config to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<NetworkConfiguration> createNetworkConfiguration(final CimiClient client,
        final NetworkConfiguration networkConfiguration) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworkConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkConfigurationCollection networkConfigurationCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getNetworkConfigs().getHref()),
            CimiNetworkConfigurationCollectionRoot.class);
        String addRef = Helper.findOperation("add", networkConfigurationCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiNetworkConfiguration> result = client.postCreateRequest(addRef, networkConfiguration.cimiObject,
            CimiNetworkConfiguration.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        NetworkConfiguration createdNetworkConfiguration = result.getResource() != null ? new NetworkConfiguration(client,
            result.getResource()) : null;
        return new CreateResult<NetworkConfiguration>(job, createdNetworkConfiguration);
    }

    /**
     * Retrieves the collection of network configs visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the network configs
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<NetworkConfiguration> getNetworkConfigurations(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworkConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkConfigurationCollection networkConfigurationCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getNetworkConfigs().getHref()),
            CimiNetworkConfigurationCollectionRoot.class, queryParams);

        List<NetworkConfiguration> result = new ArrayList<NetworkConfiguration>();

        if (networkConfigurationCollection.getCollection() != null) {
            for (CimiNetworkConfiguration cimiNetworkConfiguration : networkConfigurationCollection.getCollection().getArray()) {
                result.add(new NetworkConfiguration(client, cimiNetworkConfiguration));
            }
        }
        return result;
    }

    /**
     * Retrieves the network config with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the network config by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static NetworkConfiguration getNetworkConfigurationByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new NetworkConfiguration(client,
            client.getCimiObjectByReference(id, CimiNetworkConfiguration.class, queryParams));
    }
}

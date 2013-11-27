/**
 *
 * SIROCCO
 * Copyright (C) 2012 France Telecom
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
 *  $Id$
 *
 */
package org.ow2.sirocco.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.cimi.domain.CimiNetwork;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiNetworkCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * L2 Network.
 */
public class Network extends Resource<CimiNetwork> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Network";

    /**
     * Network state.
     */
    public static enum State {
        CREATING, STARTING, STARTED, STOPPING, STOPPED, DELETING, DELETED, ERROR
    }

    public Network() {
        super(null, new CimiNetwork());
    }

    Network(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiNetwork());
        this.cimiObject.setHref(id);
    }

    Network(final CimiClient cimiClient, final CimiNetwork cimiNetwork) {
        super(cimiClient, cimiNetwork);
    }

    /**
     * Gets the state of this network.
     * 
     * @return the state of this network
     */
    public State getState() {
        if (this.cimiObject.getState() != null) {
            return State.valueOf(this.cimiObject.getState());
        } else {
            return null;
        }
    }

    /**
     * Gets the network type.
     * 
     * @return the network type
     */
    public String getNetworkType() {
        return this.cimiObject.getNetworkType();
    }

    /**
     * Sets the network type.
     * 
     * @param networkType the new network type
     */
    public void setNetworkType(final String networkType) {
        this.cimiObject.setNetworkType(networkType);
    }

    public ProviderInfo getProviderInfo() {
        if (this.cimiObject.getProviderInfo() == null) {
            return null;
        }
        return new ProviderInfo(this.cimiObject.getProviderInfo());
    }

    /**
     * Creates a new network.
     * 
     * @param client the client
     * @param networkCreate creation parameters
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<Network> createNetwork(final CimiClient client, final NetworkCreate networkCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworks() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkCollection networksCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getNetworks().getHref()), CimiNetworkCollectionRoot.class);
        String addRef = Helper.findOperation("add", networksCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiNetwork> result = client.postCreateRequest(addRef, networkCreate.cimiNetworkCreate, CimiNetwork.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Network network = result.getResource() != null ? new Network(client, result.getResource()) : null;
        return new CreateResult<Network>(job, network);
    }

    /**
     * Retrieves the collection of networks visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the networks
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Network> getNetworks(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getNetworks() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiNetworkCollection addressCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getNetworks().getHref()), CimiNetworkCollectionRoot.class, queryParams);
        List<Network> result = new ArrayList<Network>();

        if (addressCollection.getCollection() != null) {
            for (CimiNetwork cimiNetwork : addressCollection.getCollection().getArray()) {
                result.add(new Network(client, cimiNetwork));
            }
        }
        return result;
    }

    /**
     * Retrieves the network with the given id.
     * 
     * @param client the client
     * @param id the id
     * @param queryParams optional query parameters
     * @return the network by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Network getNetworkByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        return new Network(client, client.getCimiObjectByReference(id, CimiNetwork.class, queryParams));
    }
}
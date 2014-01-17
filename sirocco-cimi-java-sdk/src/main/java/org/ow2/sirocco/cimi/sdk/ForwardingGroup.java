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

import org.ow2.sirocco.cimi.domain.CimiForwardingGroup;
import org.ow2.sirocco.cimi.domain.CimiForwardingGroupNetwork;
import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.collection.CimiForwardingGroupCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiForwardingGroupCollectionRoot;
import org.ow2.sirocco.cimi.domain.collection.CimiForwardingGroupNetworkCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Forwarding group.
 */
public class ForwardingGroup extends Resource<CimiForwardingGroup> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/ForwardingGroup";

    public ForwardingGroup() {
        super(null, new CimiForwardingGroup());
    }

    ForwardingGroup(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiForwardingGroup());
        this.cimiObject.setHref(id);
    }

    ForwardingGroup(final CimiClient cimiClient, final CimiForwardingGroup cimiForwardingGroup) {
        super(cimiClient, cimiForwardingGroup);
    }

    public ProviderInfo getProviderInfo() {
        if (this.cimiObject.getProviderInfo() == null) {
            return null;
        }
        return new ProviderInfo(this.cimiObject.getProviderInfo());
    }

    public List<Network> getNetworks() throws CimiClientException, CimiProviderException {
        List<Network> result = new ArrayList<Network>();

        if (this.cimiObject.getNetworks() != null && this.cimiObject.getNetworks().getArray() == null) {
            String fgNetRef = this.cimiObject.getNetworks().getHref();
            if (fgNetRef != null) {
                CimiForwardingGroupNetworkCollectionRoot cimiNets = this.cimiClient.getRequest(
                    this.cimiClient.extractPath(fgNetRef), CimiForwardingGroupNetworkCollectionRoot.class);
                this.cimiObject.getNetworks().setArray(cimiNets.getArray());
            }
        }
        if (this.cimiObject.getNetworks() != null && this.cimiObject.getNetworks().getArray() != null) {
            for (CimiForwardingGroupNetwork cimiFgNet : this.cimiObject.getNetworks().getArray()) {
                Network net = new Network(this.cimiClient, cimiFgNet.getNetwork());
                result.add(net);
            }
        }
        return result;
    }

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
     * Creates a new forwarding group.
     * 
     * @param client the client
     * @param forwardingGroupCreate creation parameters
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<ForwardingGroup> createForwardingGroup(final CimiClient client,
        final ForwardingGroupCreate forwardingGroupCreate) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getForwardingGroups() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiForwardingGroupCollectionRoot fgCollection = client
            .getRequest(client.extractPath(client.cloudEntryPoint.getForwardingGroups().getHref()),
                CimiForwardingGroupCollectionRoot.class);
        String addRef = Helper.findOperation("add", fgCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiForwardingGroup> result = client.postCreateRequest(addRef,
            forwardingGroupCreate.cimiForwardingGroupCreate, CimiForwardingGroup.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        ForwardingGroup fg = result.getResource() != null ? new ForwardingGroup(client, result.getResource()) : null;
        return new CreateResult<ForwardingGroup>(job, fg);
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
    public static List<ForwardingGroup> getForwardingGroups(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getForwardingGroups() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiForwardingGroupCollection fgCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getForwardingGroups().getHref()),
            CimiForwardingGroupCollectionRoot.class, queryParams);
        List<ForwardingGroup> result = new ArrayList<ForwardingGroup>();

        if (fgCollection.getCollection() != null) {
            for (CimiForwardingGroup cimiForwardingGroup : fgCollection.getCollection().getArray()) {
                result.add(new ForwardingGroup(client, cimiForwardingGroup));
            }
        }
        return result;
    }

    /**
     * Retrieves the forwarding group with the given id.
     * 
     * @param client the client
     * @param id the id
     * @param queryParams optional query parameters
     * @return the forwarding group by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static ForwardingGroup getForwardingGroupByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new ForwardingGroup(client, client.getCimiObjectByReference(id, CimiForwardingGroup.class, queryParams));
    }
}
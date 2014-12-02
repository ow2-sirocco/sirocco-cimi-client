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

import org.ow2.sirocco.cimi.domain.CimiAddress;
import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.collection.CimiAddressCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiAddressCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * IP address, and its associated metadata, for a particular Network.
 */
public class Address extends Resource<CimiAddress> {
    public static enum Allocation {
        STATIC, DYNAMIC
    }

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Address";

    Address(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiAddress());
        this.cimiObject.setHref(id);
    }

    Address(final CimiClient cimiClient, final CimiAddress cimiAddress) {
        super(cimiClient, cimiAddress);
    }

    /**
     * Gets the IP address.
     * 
     * @return the IP address
     */
    public String getIp() {
        return this.cimiObject.getIp();
    }

    /**
     * Sets the IP address.
     * 
     * @param ip the new IP address
     */
    public void setIp(final String ip) {
        this.cimiObject.setIp(ip);
    }

    /**
     * Gets the DNS resolvable name associated with this address.
     * 
     * @return the DNS resolvable name associated with this address
     */
    public String getHostname() {
        return this.cimiObject.getHostname();
    }

    /**
     * Sets the DNS resolvable name associated with this address.
     * 
     * @param hostname the DNS resolvable name associated with this address
     */
    public void setHostname(final String hostname) {
        this.cimiObject.setHostname(hostname);
    }

    /**
     * Gets the allocation mode: either static or dynamic
     * 
     * @return the allocation mode of this address
     */
    public Allocation getAllocation() {
        return Allocation.valueOf(this.cimiObject.getAllocation().toUpperCase());
    }

    /**
     * Sets the allocation mode: either static or dynamic.
     * 
     * @param allocation the new allocation mode
     */
    public void setAllocation(final Allocation allocation) {
        this.cimiObject.setAllocation(allocation.toString());
    }

    /**
     * Gets the default gateway.
     * 
     * @return the default gateway
     */
    public String getDefaultGateway() {
        return this.cimiObject.getDefaultGateway();
    }

    /**
     * Sets the default gateway.
     * 
     * @param defaultGateway the new default gateway
     */
    public void setDefaultGateway(final String defaultGateway) {
        this.cimiObject.setDefaultGateway(defaultGateway);
    }

    /**
     * Gets the IP addresses of the DNS servers.
     * 
     * @return the IP addresses of the DNS servers
     */
    public String[] getDns() {
        return this.cimiObject.getDns();
    }

    /**
     * Sets the IP addresses of the DNS servers.
     * 
     * @param dns the IP addresses of the DNS servers
     */
    public void setDns(final String[] dns) {
        this.cimiObject.setDns(dns);
    }

    /**
     * Gets the protocol.
     * 
     * @return the protocol
     */
    public String getProtocol() {
        return this.cimiObject.getProtocol();
    }

    /**
     * Sets the protocol.
     * 
     * @param protocol the new protocol
     */
    public void setProtocol(final String protocol) {
        this.cimiObject.setProtocol(protocol);
    }

    /**
     * Gets the network mask.
     * 
     * @return the network mask
     */
    public String getMask() {
        return this.cimiObject.getMask();
    }

    /**
     * Sets the network mask.
     * 
     * @param mask the network mask
     */
    public void setMask(final String mask) {
        this.cimiObject.setMask(mask);
    }

    /**
     * Gets the network with which this address is associated.
     * 
     * @return the network with which this address is associated
     */
    public Network getNetwork() {
        if (this.cimiObject.getNetwork() != null) {
            return new Network(this.cimiClient, this.cimiObject.getNetwork());
        } else {
            return null;
        }
    }

    /**
     * Sets the network with which this address is associated.
     * 
     * @param network the network with which this address is associated
     */
    public void setNetwork(final Network network) {
        this.cimiObject.setNetwork(network.cimiObject);
    }

    public static CreateResult<Address> createAddress(final CimiClient client, final AddressCreate addressCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getAddresses() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiAddressCollection addressesCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getAddresses().getHref()), CimiAddressCollectionRoot.class);
        String addRef = Helper.findOperation("add", addressesCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiAddress> result = client.postCreateRequest(addRef, addressCreate.cimiAddressCreate, CimiAddress.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Address address = result.getResource() != null ? Address.getAddressByReference(client, result.getResource().getId())
            : null;
        return new CreateResult<Address>(job, address);
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
     * Retrieves the collection of addresses visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the addresses visible to the client
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Address> getAddresses(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getAddresses() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiAddressCollection addressCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getAddresses().getHref()), CimiAddressCollectionRoot.class, queryParams);
        List<Address> result = new ArrayList<Address>();

        if (addressCollection.getCollection() != null) {
            for (CimiAddress cimiAddress : addressCollection.getCollection().getArray()) {
                result.add(new Address(client, cimiAddress));
            }
        }
        return result;
    }

    /**
     * Retrieves the address with the given id.
     * 
     * @param client the client
     * @param id the id of the address
     * @param params optional query parameters
     * @return the address
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Address getAddressByReference(final CimiClient client, final String id, final QueryParams... params)
        throws CimiClientException, CimiProviderException {
        return new Address(client, client.getCimiObjectByReference(id, CimiAddress.class, params));
    }
}
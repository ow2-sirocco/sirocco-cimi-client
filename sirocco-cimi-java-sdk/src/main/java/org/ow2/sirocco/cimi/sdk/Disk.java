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

import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;
import org.ow2.sirocco.cimi.server.domain.CimiJob;
import org.ow2.sirocco.cimi.server.domain.CimiMachineDisk;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineDiskCollection;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineDiskCollectionRoot;

/**
 * Disk (local storage) of a Machine.
 */
public class Disk extends Resource<CimiMachineDisk> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Disk";

    /**
     * Instantiates a new disk.
     */
    public Disk() {
        super(null, new CimiMachineDisk());
    }

    Disk(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineDisk());
        this.cimiObject.setHref(id);
    }

    Disk(final CimiClient cimiClient, final CimiMachineDisk cimiMachineDisk) {
        super(cimiClient, cimiMachineDisk);
    }

    /**
     * Gets the capacity of this disk in kilobytes.
     * 
     * @return the capacity of this disk in kilobytes
     */
    public Integer getCapacity() {
        return this.cimiObject.getCapacity();
    }

    /**
     * Gets the initial location.
     * 
     * @return the initial location
     */
    public String getInitialLocation() {
        return this.cimiObject.getInitialLocation();
    }

    /**
     * Deletes this disk.
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
     * Adds a disk to a machine.
     * 
     * @param client the CIMI client
     * @param machineId the machine id
     * @param machineDisk the machine disk to add
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<Disk> createMachineDisk(final CimiClient client, final String machineId, final Disk machineDisk)
        throws CimiClientException, CimiProviderException {
        Machine machine = Machine.getMachineByReference(client, machineId, QueryParams.builder().expand("disks").build());
        String addRef = Helper.findOperation("add", machine.cimiObject.getVolumes());
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiMachineDisk> result = client.postCreateRequest(addRef, machineDisk.cimiObject, CimiMachineDisk.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Disk createdMachineDisk = result.getResource() != null ? new Disk(client, result.getResource()) : null;
        return new CreateResult<Disk>(job, createdMachineDisk);
    }

    /**
     * Retrieves the collection of machine disks belonging to a client.
     * 
     * @param client the client
     * @param machineId the machine id
     * @param queryParams optional query parameters
     * @return the machine disks
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Disk> getMachineDisks(final CimiClient client, final String machineId, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        Machine machine = Machine.getMachineByReference(client, machineId);
        if (machine.cimiObject.getDisks() == null) {
            throw new CimiClientException("Unsupported operation");
        }

        CimiMachineDiskCollection machineDiskCollection = client.getRequest(
            client.extractPath(machine.cimiObject.getDisks().getHref()), CimiMachineDiskCollectionRoot.class, queryParams);
        List<Disk> result = new ArrayList<Disk>();

        if (machineDiskCollection.getCollection() != null) {
            for (CimiMachineDisk cimiMachineDisk : machineDiskCollection.getCollection().getArray()) {
                result.add(new Disk(client, cimiMachineDisk));
            }
        }
        return result;
    }

    /**
     * Retrieves the machine with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the machine disk by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Disk getMachineDiskByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        return new Disk(client, client.getCimiObjectByReference(id, CimiMachineDisk.class, queryParams));
    }

}

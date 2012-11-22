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

import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.CimiMachineVolume;
import org.ow2.sirocco.cimi.domain.CimiVolume;
import org.ow2.sirocco.cimi.domain.collection.CimiMachineVolumeCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiMachineVolumeCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Represents the attachment of a Volume to a Machine.
 */
public class MachineVolume extends Resource<CimiMachineVolume> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/MachineVolume";

    /**
     * Instantiates a new machine volume.
     */
    public MachineVolume() {
        super(null, new CimiMachineVolume());
    }

    MachineVolume(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineVolume());
        this.cimiObject.setHref(id);
    }

    MachineVolume(final CimiClient cimiClient, final CimiMachineVolume cimiMachineVolume) {
        super(cimiClient, cimiMachineVolume);
    }

    /**
     * Gets the attached volume.
     * 
     * @return the attached volume
     */
    public Volume getVolume() {
        if (this.cimiObject.getVolume() != null) {
            return new Volume(this.cimiClient, this.cimiObject.getVolume());
        } else {
            return null;
        }
    }

    /**
     * Gets the initial location of the attached volume.
     * 
     * @return the initial location
     */
    public String getInitialLocation() {
        return this.cimiObject.getInitialLocation();
    }

    /**
     * Sets the reference of the volume to be attached.
     * 
     * @param volumeRef the reference of the volume to be attached
     */
    public void setVolumeRef(final String volumeRef) {
        CimiVolume vol = new CimiVolume();
        vol.setHref(volumeRef);
        this.cimiObject.setVolume(vol);
    }

    /**
     * Sets the initial location of the volume.
     * 
     * @param initialLocation the initial location of the volume
     */
    public void setInitialLocation(final String initialLocation) {
        this.cimiObject.setInitialLocation(initialLocation);
    }

    /**
     * Detaches a volume from a machine.
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
     * Attaches a volume to a machine.
     * 
     * @param client the CIMI client
     * @param machineId the id of the machine to which the volume is attached
     * @param machineVolume the machine volume to attach
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<MachineVolume> createMachineVolume(final CimiClient client, final String machineId,
        final MachineVolume machineVolume) throws CimiClientException, CimiProviderException {
        Machine machine = Machine.getMachineByReference(client, machineId, QueryParams.builder().expand("volumes").build());
        String addRef = Helper.findOperation("add", machine.cimiObject.getVolumes());
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiMachineVolume> result = client.postCreateRequest(addRef, machineVolume.cimiObject,
            CimiMachineVolume.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        MachineVolume createdMachineVolume = result.getResource() != null ? new MachineVolume(client, result.getResource())
            : null;
        return new CreateResult<MachineVolume>(job, createdMachineVolume);
    }

    /**
     * Retrieves the collection of machine volumes belonging to a given machine
     * 
     * @param client the client
     * @param machineId the machine id
     * @param queryParams optional query parameters
     * @return the machine volumes
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<MachineVolume> getMachineVolumes(final CimiClient client, final String machineId,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        Machine machine = Machine.getMachineByReference(client, machineId);
        if (machine.cimiObject.getVolumes() == null) {
            throw new CimiClientException("Unsupported operation");
        }

        CimiMachineVolumeCollection machineVolumeCollection = client.getRequest(
            client.extractPath(machine.cimiObject.getVolumes().getHref()), CimiMachineVolumeCollectionRoot.class, queryParams);
        List<MachineVolume> result = new ArrayList<MachineVolume>();

        if (machineVolumeCollection.getCollection() != null) {
            for (CimiMachineVolume cimiMachineVolume : machineVolumeCollection.getCollection().getArray()) {
                result.add(new MachineVolume(client, cimiMachineVolume));
            }
        }
        return result;
    }

    /**
     * Retrieves the machine volume with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the machine volume by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static MachineVolume getMachineVolumeByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new MachineVolume(client, client.getCimiObjectByReference(id, CimiMachineVolume.class, queryParams));
    }

}

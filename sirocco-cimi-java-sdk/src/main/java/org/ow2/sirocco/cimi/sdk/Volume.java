/**
 *
 * SIROCCO
 * Copyright (C) 2011 France Telecom
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

import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiVolume;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Unit of persistent storage at either the block or the file-system level. A
 * Volume can be attached to a Machine and persists independently from the life
 * of the Machine(s) it is attached to
 */
public class Volume extends Resource<CimiVolume> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Volume";

    /**
     * Volume state.
     */
    public static enum State {
        CREATING, AVAILABLE, DELETING, DELETED, ERROR
    }

    Volume(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiVolume());
        this.cimiObject.setHref(id);
    }

    Volume(final CimiClient cimiClient, final CimiVolume cimiVolume) {
        super(cimiClient, cimiVolume);
    }

    /**
     * Gets the state of this volume.
     * 
     * @return the state of this volume
     */
    public State getState() {
        if (this.cimiObject.getState() != null) {
            return State.valueOf(this.cimiObject.getState());
        } else {
            return null;
        }
    }

    /**
     * Gets the type of this volume.
     * 
     * @return the type of this volume
     */
    public String getType() {
        return this.cimiObject.getType();
    }

    /**
     * Gets the capacity of this volume in kilobytes.
     * 
     * @return the capacity of this volume in kilobytes
     */
    public Integer getCapacity() {
        return this.cimiObject.getCapacity();
    }

    /**
     * True if this volume is bootable.
     * 
     * @return true if this volume is bootable
     */
    public Boolean isBootable() {
        return this.cimiObject.getBootable();
    }

    /**
     * Deletes this volume.
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
     * Creates a new volume.
     * 
     * @param client the CIMI client
     * @param volumeCreate creation parameters
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<Volume> createVolume(final CimiClient client, final VolumeCreate volumeCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumes() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeCollection volumeCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumes().getHref()), CimiVolumeCollectionRoot.class);
        String addRef = Helper.findOperation("add", volumeCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiVolume> result = client.postCreateRequest(addRef, volumeCreate.cimiVolumeCreate, CimiVolume.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Volume volume = result.getResource() != null ? new Volume(client, result.getResource()) : null;
        return new CreateResult<Volume>(job, volume);
    }

    /**
     * Retrieves the collection of volumes visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the volumes
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Volume> getVolumes(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumes() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeCollection volumeCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumes().getHref()), CimiVolumeCollectionRoot.class, queryParams);
        List<Volume> result = new ArrayList<Volume>();

        if (volumeCollection.getCollection() != null) {
            for (CimiVolume cimiVolume : volumeCollection.getCollection().getArray()) {
                result.add(new Volume(client, cimiVolume));
            }
        }
        return result;
    }

    /**
     * Retrieves the volume with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the volume by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Volume getVolumeByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        return new Volume(client, client.getCimiObjectByReference(id, CimiVolume.class, queryParams));
    }

}

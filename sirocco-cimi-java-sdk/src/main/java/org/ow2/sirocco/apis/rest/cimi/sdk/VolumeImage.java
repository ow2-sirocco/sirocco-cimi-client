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

package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiVolumeImage;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeImageCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeImageCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient.CimiResult;

/**
 * Image that can be placed on a pre-loaded Volume.
 */
public class VolumeImage extends Resource<CimiVolumeImage> {

    /**
     * VolumeImage state.
     */
    public static enum State {
        CREATING, AVAILABLE, DELETING, DELETED, ERROR
    }

    /**
     * Instantiates a new volume image.
     */
    public VolumeImage() {
        super(null, new CimiVolumeImage());
    }

    VolumeImage(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiVolumeImage());
        this.cimiObject.setHref(id);
    }

    VolumeImage(final CimiClient cimiClient, final CimiVolumeImage cimiVolumeImage) {
        super(cimiClient, cimiVolumeImage);
    }

    /**
     * Gets the state of the volume image.
     * 
     * @return the state of the volume image
     */
    public State getState() {
        if (this.cimiObject.getState() != null) {
            return State.valueOf(this.cimiObject.getState());
        } else {
            return null;
        }
    }

    /**
     * Gets the image location.
     * 
     * @return the image location
     */
    public String getImageLocation() {
        return this.cimiObject.getImageLocation().getHref();
    }

    /**
     * True if this volume image is bootable.
     * 
     * @return true if this volume image is bootable
     */
    public boolean isBootable() {
        return this.cimiObject.getBootable();
    }

    /**
     * Deletes this volume image.
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
     * Creates a new volume image.
     * 
     * @param client the CIMI client
     * @param volumeImage the volume image to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<VolumeImage> createVolumeImage(final CimiClient client, final VolumeImage volumeImage)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeImages() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeImageCollection volumeImagesCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeImages().getHref()), CimiVolumeImageCollectionRoot.class);
        String addRef = Helper.findOperation("add", volumeImagesCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiVolumeImage> result = client.postCreateRequest(addRef, volumeImage.cimiObject, CimiVolumeImage.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        VolumeImage createdVolumeConfiguration = result.getResource() != null ? new VolumeImage(client, result.getResource())
            : null;
        return new CreateResult<VolumeImage>(job, createdVolumeConfiguration);
    }

    /**
     * Retrieves the collection of volume images visible to the client
     * 
     * @param client the CIMI client
     * @param queryParams optional query parameters
     * @return the volume images visible to the client
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<VolumeImage> getVolumeImages(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeImages() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeImageCollection volumeImagesCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeImages().getHref()), CimiVolumeImageCollectionRoot.class,
            queryParams);

        List<VolumeImage> result = new ArrayList<VolumeImage>();

        if (volumeImagesCollection.getCollection() != null) {
            for (CimiVolumeImage cimiVolumeImage : volumeImagesCollection.getCollection().getArray()) {
                result.add(new VolumeImage(client, cimiVolumeImage));
            }
        }
        return result;
    }

    /**
     * Retrieves the volume image with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the volume image by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static VolumeImage getVolumeImageByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new VolumeImage(client, client.getCimiObjectByReference(id, CimiVolumeImage.class, queryParams));
    }

}

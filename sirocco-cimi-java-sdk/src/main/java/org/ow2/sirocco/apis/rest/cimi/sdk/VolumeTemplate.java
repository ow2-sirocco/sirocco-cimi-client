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
import org.ow2.sirocco.apis.rest.cimi.domain.CimiVolumeTemplate;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeTemplateCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiVolumeTemplateCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient.CimiResult;

/**
 * Set of hardware and software settings required to create a Volume.
 */
public class VolumeTemplate extends Resource<CimiVolumeTemplate> {
    private VolumeImage volumeImage;

    private VolumeConfiguration volumeConfig;

    /**
     * Instantiates a new volume template.
     */
    public VolumeTemplate() {
        super(null, new CimiVolumeTemplate());
    }

    VolumeTemplate(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiVolumeTemplate());
        this.cimiObject.setHref(id);
    }

    VolumeTemplate(final CimiVolumeTemplate cimiObject) {
        super(null, cimiObject);
    }

    VolumeTemplate(final CimiClient cimiClient, final CimiVolumeTemplate cimiObject) {
        super(cimiClient, cimiObject);
        if (cimiObject.getVolumeImage() != null) {
            this.volumeImage = new VolumeImage(cimiClient, cimiObject.getVolumeImage());
        }
        this.volumeConfig = new VolumeConfiguration(cimiClient, cimiObject.getVolumeConfig());
    }

    /**
     * Gets the volume image to be used to create a volume from this template.
     * 
     * @return the volume image to be used to create a volume from this template
     */
    public VolumeImage getVolumeImage() {
        return this.volumeImage;
    }

    /**
     * Sets the volume image to be used to create a volume from this template.
     * 
     * @param volumeImage the volume image to be used to create a volume from
     *        this template
     */
    public void setVolumeImage(final VolumeImage volumeImage) {
        this.volumeImage = volumeImage;
        this.cimiObject.setVolumeImage(volumeImage.cimiObject);
    }

    /**
     * Sets the reference of volume image to be used to create a volume from
     * this template.
     * 
     * @param the reference of volume image to be used to create a volume from
     *        this template
     */
    public void setVolumeImageRef(final String volumeImageRef) {
        this.volumeImage = new VolumeImage(this.cimiClient, volumeImageRef);
        this.cimiObject.setVolumeImage(this.volumeImage.cimiObject);
    }

    /**
     * Gets the volume configuration to be used to create a volume from this
     * template.
     * 
     * @return the volume configuration to be used to create a volume from this
     *         template
     */
    public VolumeConfiguration getVolumeConfig() {
        return this.volumeConfig;
    }

    /**
     * Sets the volume configuration to be used to create a volume from this
     * template.
     * 
     * @param volumeConfig the volume configuration to be used to create a
     *        volume from this template
     */
    public void setVolumeConfig(final VolumeConfiguration volumeConfig) {
        this.volumeConfig = volumeConfig;
        this.cimiObject.setVolumeConfig(volumeConfig.cimiObject);
    }

    /**
     * Sets the reference of the volume configuration to be used to create a
     * volume from this template.
     * 
     * @param volumeConfigRef the reference of the volume configuration to be
     *        used to create a volume from this template
     */
    public void setVolumeConfigRef(final String volumeConfigRef) {
        this.volumeConfig = new VolumeConfiguration(this.cimiClient, volumeConfigRef);
        this.cimiObject.setVolumeConfig(this.volumeConfig.cimiObject);
    }

    /**
     * Deletes this volume template.
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
     * Creates a new volume template.
     * 
     * @param client the CIMI client
     * @param volumeTemplate the volume template to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<VolumeTemplate> createVolumeTemplate(final CimiClient client, final VolumeTemplate volumeTemplate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeTemplateCollection volumeTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeTemplates().getHref()), CimiVolumeTemplateCollectionRoot.class);
        String addRef = Helper.findOperation("add", volumeTemplateCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiVolumeTemplate> result = client.postCreateRequest(addRef, volumeTemplate.cimiObject,
            CimiVolumeTemplate.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        VolumeTemplate createdVolumeTemplate = result.getResource() != null ? new VolumeTemplate(client, result.getResource())
            : null;
        return new CreateResult<VolumeTemplate>(job, createdVolumeTemplate);
    }

    /**
     * Retrieves the collection of volume templates visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the volume templates
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<VolumeTemplate> getVolumeTemplates(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeTemplateCollection volumeTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeTemplates().getHref()), CimiVolumeTemplateCollectionRoot.class,
            queryParams);

        List<VolumeTemplate> result = new ArrayList<VolumeTemplate>();

        if (volumeTemplateCollection.getCollection() != null) {
            for (CimiVolumeTemplate cimiVolumeTemplate : volumeTemplateCollection.getCollection().getArray()) {
                result.add(new VolumeTemplate(client, cimiVolumeTemplate));
            }
        }
        return result;
    }

    /**
     * Retrieves the volume template with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the volume template by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static VolumeTemplate getVolumeTemplateByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new VolumeTemplate(client, client.getCimiObjectByReference(id, CimiVolumeTemplate.class, queryParams));
    }

}

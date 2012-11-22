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

import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.CimiVolumeConfiguration;
import org.ow2.sirocco.cimi.domain.collection.CimiVolumeConfigurationCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiVolumeConfigurationCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Set of hardware settings used to create a Volume.
 */
public class VolumeConfiguration extends Resource<CimiVolumeConfiguration> {

    /**
     * Instantiates a new volume configuration.
     */
    public VolumeConfiguration() {
        super(null, new CimiVolumeConfiguration());
    }

    VolumeConfiguration(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiVolumeConfiguration());
        this.cimiObject.setHref(id);
    }

    VolumeConfiguration(final CimiClient cimiClient, final CimiVolumeConfiguration cimiObject) {
        super(cimiClient, cimiObject);
    }

    /**
     * Sets the type of the volume to be created.
     * 
     * @param type the type of the volume to be created
     */
    public void setType(final String type) {
        this.cimiObject.setType(type);
    }

    /**
     * Gets the type of the volume to be created.
     * 
     * @return the type of the volume to be created
     */
    public String getType() {
        return this.cimiObject.getType();
    }

    /**
     * Gets the capacity of the volume to be created in kilobytes.
     * 
     * @return the capacity of the volume to be created in kilobytes
     */
    public Integer getCapacity() {
        return this.cimiObject.getCapacity();
    }

    /**
     * Sets the capacity of the volume to be created in kilobytes.
     * 
     * @param capacity the capacity of the volume to be created in kilobytes
     */
    public void setCapacity(final int capacity) {
        this.cimiObject.setCapacity(capacity);
    }

    /**
     * Gets the format of the volume to be created.
     * 
     * @return the format of the volume to be created
     */
    public String getFormat() {
        return this.cimiObject.getFormat();
    }

    /**
     * Sets the format of the volume to be created.
     * 
     * @param format the format of the volume to be created
     */
    public void setFormat(final String format) {
        this.cimiObject.setFormat(format);
    }

    /**
     * Deletes this volume configuration.
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
     * Creates a new volume configuration.
     * 
     * @param client the CIMI client
     * @param volumeConfig the volume configuration to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<VolumeConfiguration> createVolumeConfiguration(final CimiClient client,
        final VolumeConfiguration volumeConfig) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeConfigurationCollection volumeConfigCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeConfigs().getHref()),
            CimiVolumeConfigurationCollectionRoot.class);
        String addRef = Helper.findOperation("add", volumeConfigCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiVolumeConfiguration> result = client.postCreateRequest(addRef, volumeConfig.cimiObject,
            CimiVolumeConfiguration.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        VolumeConfiguration createdVolumeConfiguration = result.getResource() != null ? new VolumeConfiguration(client,
            result.getResource()) : null;
        return new CreateResult<VolumeConfiguration>(job, createdVolumeConfiguration);
    }

    /**
     * Retrieves the collection of volume configurations visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the volume configurations
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<VolumeConfiguration> getVolumeConfigurations(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getVolumeConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiVolumeConfigurationCollection volumeConfigCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getVolumeConfigs().getHref()),
            CimiVolumeConfigurationCollectionRoot.class, queryParams);

        List<VolumeConfiguration> result = new ArrayList<VolumeConfiguration>();

        if (volumeConfigCollection.getCollection() != null) {
            for (CimiVolumeConfiguration cimiVolumeConfig : volumeConfigCollection.getCollection().getArray()) {
                result.add(new VolumeConfiguration(client, cimiVolumeConfig));
            }
        }
        return result;
    }

    /**
     * Retrieves the volume configuration with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the volume configuration by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static VolumeConfiguration getVolumeConfigurationByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new VolumeConfiguration(client, client.getCimiObjectByReference(id, CimiVolumeConfiguration.class, queryParams));
    }

}

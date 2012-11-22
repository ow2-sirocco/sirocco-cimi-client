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

import java.util.HashMap;
import java.util.Map;

import org.ow2.sirocco.cimi.domain.CimiVolumeCreate;

/**
 * Helper class used to create a Volume.
 */
public class VolumeCreate {
    CimiVolumeCreate cimiVolumeCreate;

    private VolumeTemplate volumeTemplate;

    /**
     * Instantiates a new volume create.
     */
    public VolumeCreate() {
        this.cimiVolumeCreate = new CimiVolumeCreate();
    }

    /**
     * Gets the name that will assigned to the created volume.
     * 
     * @return the name that will assigned to the created volume
     */
    public String getName() {
        return this.cimiVolumeCreate.getName();
    }

    /**
     * Sets the name that will assigned to the created volume.
     * 
     * @param name the name that will assigned to the created volume
     */
    public void setName(final String name) {
        this.cimiVolumeCreate.setName(name);
    }

    /**
     * Gets the description that will assigned to the created volume.
     * 
     * @return the description that will assigned to the created volume
     */
    public String getDescription() {
        return this.cimiVolumeCreate.getDescription();
    }

    /**
     * Sets the description that will assigned to the created volume.
     * 
     * @param description the description that will assigned to the created
     *        volume
     */
    public void setDescription(final String description) {
        this.cimiVolumeCreate.setDescription(description);
    }

    /**
     * Gets the properties that will assigned to the created volume.
     * 
     * @return the properties that will assigned to the created volume
     */
    public Map<String, String> getProperties() {
        return this.cimiVolumeCreate.getProperties();
    }

    /**
     * Sets the properties that will assigned to the created volume.
     * 
     * @param properties the properties that will assigned to the created volume
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiVolumeCreate.setProperties(properties);
    }

    /**
     * Adds a property that will assigned to the created volume.
     * 
     * @param key the property key
     * @param value the property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiVolumeCreate.getProperties() == null) {
            this.cimiVolumeCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiVolumeCreate.getProperties().put(key, value);
    }

    /**
     * Gets the volume template to be used to create a volume.
     * 
     * @return the volume template to be used to create a volume
     */
    public VolumeTemplate getVolumeTemplate() {
        return this.volumeTemplate;
    }

    /**
     * Sets the volume template to be used to create a volume.
     * 
     * @param volumeTemplate the volume template to be used to create a volume
     */
    public void setVolumeTemplate(final VolumeTemplate volumeTemplate) {
        this.volumeTemplate = volumeTemplate;
        this.cimiVolumeCreate.setVolumeTemplate(volumeTemplate.cimiObject);
    }

    /**
     * Sets the reference of the volume template to be used to create a volume.
     * 
     * @param volumeTemplateRef the reference of the volume template to be used
     *        to create a volume
     */
    public void setVolumeTemplateRef(final String volumeTemplateRef) {
        this.volumeTemplate = new VolumeTemplate(null, volumeTemplateRef);
        this.cimiVolumeCreate.setVolumeTemplate(this.volumeTemplate.cimiObject);
    }

}

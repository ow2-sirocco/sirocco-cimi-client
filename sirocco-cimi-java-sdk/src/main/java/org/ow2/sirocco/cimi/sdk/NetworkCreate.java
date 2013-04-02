/**
 *
 * SIROCCO
 * Copyright (C) 2013 France Telecom
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
 *
 */

package org.ow2.sirocco.cimi.sdk;

import java.util.HashMap;
import java.util.Map;

import org.ow2.sirocco.cimi.domain.CimiNetworkCreate;

/**
 * Helper class to create a Network.
 */
public class NetworkCreate {
    CimiNetworkCreate cimiNetworkCreate;

    private NetworkTemplate networkTemplate;

    /**
     * Instantiates a new network create.
     */
    public NetworkCreate() {
        this.cimiNetworkCreate = new CimiNetworkCreate();
    }

    /**
     * Gets the name that will assigned to the created network.
     * 
     * @return the name that will assigned to the created network
     */
    public String getName() {
        return this.cimiNetworkCreate.getName();
    }

    /**
     * Sets the name that will assigned to the created network.
     * 
     * @param name the name that will assigned to the created network
     */
    public void setName(final String name) {
        this.cimiNetworkCreate.setName(name);
    }

    /**
     * Gets the description that will assigned to the created network.
     * 
     * @return the description that will assigned to the created network
     */
    public String getDescription() {
        return this.cimiNetworkCreate.getDescription();
    }

    /**
     * Sets the description that will assigned to the created network.
     * 
     * @param description the description that will assigned to the created
     *        network
     */
    public void setDescription(final String description) {
        this.cimiNetworkCreate.setDescription(description);
    }

    /**
     * Gets the properties that will assigned to the created network.
     * 
     * @return the properties that will assigned to the created network
     */
    public Map<String, String> getProperties() {
        return this.cimiNetworkCreate.getProperties();
    }

    /**
     * Sets the properties that will assigned to the created network.
     * 
     * @param properties the properties that will assigned to the created
     *        network
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiNetworkCreate.setProperties(properties);
    }

    /**
     * Adds a property that will assigned to the created network.
     * 
     * @param key the property key
     * @param value the property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiNetworkCreate.getProperties() == null) {
            this.cimiNetworkCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiNetworkCreate.getProperties().put(key, value);
    }

    /**
     * Gets the network template that will be used to create a network.
     * 
     * @return the network template that will be used to create a network
     */
    public NetworkTemplate getNetworkTemplate() {
        return this.networkTemplate;
    }

    /**
     * Sets the network template that will be used to create a network.
     * 
     * @param networkTemplate the network template that will be used to create a
     *        network
     */
    public void setNetworkTemplate(final NetworkTemplate networkTemplate) {
        this.networkTemplate = networkTemplate;
        this.cimiNetworkCreate.setNetworkTemplate(networkTemplate.cimiObject);
    }

    /**
     * Sets the reference of the network template that will be used to create a
     * network.
     * 
     * @param networkTemplateRef the reference of the network template that will
     *        be used to create a network.
     */
    public void setNetworkTemplateRef(final String networkTemplateRef) {
        this.networkTemplate = new NetworkTemplate(null, networkTemplateRef);
        this.cimiNetworkCreate.setNetworkTemplate(this.networkTemplate.cimiObject);
    }

}

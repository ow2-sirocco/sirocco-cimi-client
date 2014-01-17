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
import java.util.List;
import java.util.Map;

import org.ow2.sirocco.cimi.domain.CimiForwardingGroupCreate;
import org.ow2.sirocco.cimi.domain.CimiForwardingGroupTemplate;
import org.ow2.sirocco.cimi.domain.CimiNetwork;

/**
 * Helper class to create a ForwardingGroup.
 */
public class ForwardingGroupCreate {
    CimiForwardingGroupCreate cimiForwardingGroupCreate;

    /**
     * Instantiates a new forwardingGroup create.
     */
    public ForwardingGroupCreate() {
        this.cimiForwardingGroupCreate = new CimiForwardingGroupCreate();
        CimiForwardingGroupTemplate template = new CimiForwardingGroupTemplate();
        this.cimiForwardingGroupCreate.setForwardingGroupTemplate(template);
    }

    /**
     * Gets the name that will assigned to the created forwardingGroup.
     * 
     * @return the name that will assigned to the created forwardingGroup
     */
    public String getName() {
        return this.cimiForwardingGroupCreate.getName();
    }

    /**
     * Sets the name that will assigned to the created forwardingGroup.
     * 
     * @param name the name that will assigned to the created forwardingGroup
     */
    public void setName(final String name) {
        this.cimiForwardingGroupCreate.setName(name);
    }

    /**
     * Gets the description that will assigned to the created forwardingGroup.
     * 
     * @return the description that will assigned to the created forwardingGroup
     */
    public String getDescription() {
        return this.cimiForwardingGroupCreate.getDescription();
    }

    /**
     * Sets the description that will assigned to the created forwardingGroup.
     * 
     * @param description the description that will assigned to the created
     *        forwardingGroup
     */
    public void setDescription(final String description) {
        this.cimiForwardingGroupCreate.setDescription(description);
    }

    /**
     * Gets the properties that will assigned to the created forwardingGroup.
     * 
     * @return the properties that will assigned to the created forwardingGroup
     */
    public Map<String, String> getProperties() {
        return this.cimiForwardingGroupCreate.getProperties();
    }

    /**
     * Sets the properties that will assigned to the created forwardingGroup.
     * 
     * @param properties the properties that will assigned to the created
     *        forwardingGroup
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiForwardingGroupCreate.setProperties(properties);
    }

    /**
     * Adds a property that will assigned to the created forwardingGroup.
     * 
     * @param key the property key
     * @param value the property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiForwardingGroupCreate.getProperties() == null) {
            this.cimiForwardingGroupCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiForwardingGroupCreate.getProperties().put(key, value);
    }

    public void setNetworks(final List<Network> networks) {
        CimiNetwork[] nets = new CimiNetwork[networks.size()];
        for (int i = 0; i < nets.length; i++) {
            nets[i] = networks.get(i).getResource();
        }
        this.cimiForwardingGroupCreate.getForwardingGroupTemplate().setNetworks(nets);
    }

    /**
     * Sets the provider account id where the resource will be created
     */
    public void setProviderAccountId(final String providerAccountId) {
        this.cimiForwardingGroupCreate.setProviderAccountId(providerAccountId);
    }

    /**
     * Gets the location constraint
     */
    public String getLocation() {
        return this.cimiForwardingGroupCreate.getLocation();
    }

    /**
     * Sets the location constraint. If null, the resource will be placed on any
     * location available to the provider account.
     */
    public void setLocation(final String location) {
        this.cimiForwardingGroupCreate.setLocation(location);
    }

}

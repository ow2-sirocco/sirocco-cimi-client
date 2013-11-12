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

import org.ow2.sirocco.cimi.domain.CimiMachineCreate;

/**
 * Helper class to create a Machine.
 */
public class MachineCreate {
    CimiMachineCreate cimiMachineCreate;

    private MachineTemplate machineTemplate;

    /**
     * Instantiates a new machine create.
     */
    public MachineCreate() {
        this.cimiMachineCreate = new CimiMachineCreate();
    }

    /**
     * Gets the name that will assigned to the created machine.
     * 
     * @return the name that will assigned to the created machine
     */
    public String getName() {
        return this.cimiMachineCreate.getName();
    }

    /**
     * Sets the name that will assigned to the created machine.
     * 
     * @param name the name that will assigned to the created machine
     */
    public void setName(final String name) {
        this.cimiMachineCreate.setName(name);
    }

    /**
     * Gets the description that will assigned to the created machine.
     * 
     * @return the description that will assigned to the created machine
     */
    public String getDescription() {
        return this.cimiMachineCreate.getDescription();
    }

    /**
     * Sets the description that will assigned to the created machine.
     * 
     * @param description the description that will assigned to the created
     *        machine
     */
    public void setDescription(final String description) {
        this.cimiMachineCreate.setDescription(description);
    }

    /**
     * Gets the properties that will assigned to the created machine.
     * 
     * @return the properties that will assigned to the created machine
     */
    public Map<String, String> getProperties() {
        return this.cimiMachineCreate.getProperties();
    }

    /**
     * Sets the properties that will assigned to the created machine.
     * 
     * @param properties the properties that will assigned to the created
     *        machine
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiMachineCreate.setProperties(properties);
    }

    /**
     * Adds a property that will assigned to the created machine.
     * 
     * @param key the property key
     * @param value the property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiMachineCreate.getProperties() == null) {
            this.cimiMachineCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiMachineCreate.getProperties().put(key, value);
    }

    /**
     * Gets the machine template that will be used to create a machine.
     * 
     * @return the machine template that will be used to create a machine
     */
    public MachineTemplate getMachineTemplate() {
        return this.machineTemplate;
    }

    /**
     * Sets the machine template that will be used to create a machine.
     * 
     * @param machineTemplate the machine template that will be used to create a
     *        machine
     */
    public void setMachineTemplate(final MachineTemplate machineTemplate) {
        this.machineTemplate = machineTemplate;
        this.cimiMachineCreate.setMachineTemplate(machineTemplate.cimiObject);
    }

    /**
     * Sets the reference of the machine template that will be used to create a
     * machine.
     * 
     * @param machineTemplateRef the reference of the machine template that will
     *        be used to create a machine.
     */
    public void setMachineTemplateRef(final String machineTemplateRef) {
        this.machineTemplate = new MachineTemplate(null, machineTemplateRef);
        this.cimiMachineCreate.setMachineTemplate(this.machineTemplate.cimiObject);
    }

    /**
     * Gets the provider account id where the resource will be created
     */
    public String getProviderAccountId() {
        return this.cimiMachineCreate.getProviderAccountId();
    }

    /**
     * Sets the provider account id where the resource will be created
     */
    public void setProviderAccountId(final String providerAccountId) {
        this.cimiMachineCreate.setProviderAccountId(providerAccountId);
    }

    /**
     * Gets the location constraint
     */
    public String getLocation() {
        return this.cimiMachineCreate.getLocation();
    }

    /**
     * Sets the location constraint. If null, the resource will be placed on any
     * location available to the provider account.
     */
    public void setLocation(final String location) {
        this.cimiMachineCreate.setLocation(location);
    }

}

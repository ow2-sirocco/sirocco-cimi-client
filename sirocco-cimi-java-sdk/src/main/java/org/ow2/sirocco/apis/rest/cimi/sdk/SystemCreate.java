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

import java.util.HashMap;
import java.util.Map;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiSystemCreate;

/**
 * Helper class used to create a System
 */
public class SystemCreate {
    CimiSystemCreate cimiSystemCreate;

    private SystemTemplate systemTemplate;

    /**
     * Creates a new SystemCreate
     */
    public SystemCreate() {
        this.cimiSystemCreate = new CimiSystemCreate();
    }

    /**
     * Returns the name that will assigned to the System
     * 
     * @return name that will assigned to the System
     */
    public String getName() {
        return this.cimiSystemCreate.getName();
    }

    /**
     * Sets the name that will assigned to the System
     * 
     * @param name
     */
    public void setName(final String name) {
        this.cimiSystemCreate.setName(name);
    }

    /**
     * Returns the description of the System
     * 
     * @return
     */
    public String getDescription() {
        return this.cimiSystemCreate.getDescription();
    }

    /**
     * Sets the description of the System
     * 
     * @param description
     */
    public void setDescription(final String description) {
        this.cimiSystemCreate.setDescription(description);
    }

    /**
     * Returns the properties that will be assigned to the System
     * 
     * @return
     */
    public Map<String, String> getProperties() {
        return this.cimiSystemCreate.getProperties();
    }

    /**
     * Sets the properties that will be assigned to the System
     * 
     * @param properties
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiSystemCreate.setProperties(properties);
    }

    /**
     * Adds a property that will be assigned to the System
     * 
     * @param key property key
     * @param value property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiSystemCreate.getProperties() == null) {
            this.cimiSystemCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiSystemCreate.getProperties().put(key, value);
    }

    /**
     * Returns the SystemTemplate that will used to create the System
     * 
     * @return
     */
    public SystemTemplate getSystemTemplate() {
        return this.systemTemplate;
    }

    /**
     * Sets the SystemTemplate that will used to create the System
     * 
     * @param systemTemplate
     */
    public void setSystemTemplate(final SystemTemplate systemTemplate) {
        this.systemTemplate = systemTemplate;
        this.cimiSystemCreate.setSystemTemplate(systemTemplate.cimiObject);
    }

    /**
     * Sets the reference of the SystemTemplate that will used to create the
     * System
     * 
     * @param systemTemplateRef
     */
    public void setSystemTemplateRef(final String systemTemplateRef) {
        this.systemTemplate = new SystemTemplate(null, systemTemplateRef);
        this.cimiSystemCreate.setSystemTemplate(this.systemTemplate.cimiObject);
    }

}

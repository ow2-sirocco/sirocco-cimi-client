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

import org.ow2.sirocco.apis.rest.cimi.domain.CimiCredentialCreate;

/**
 * Helper class used to create a Credential.
 */
public class CredentialCreate {
    CimiCredentialCreate cimiCredentialsCreate;

    private CredentialTemplate credentialTemplate;

    /**
     * Instantiates a new credential create.
     */
    public CredentialCreate() {
        this.cimiCredentialsCreate = new CimiCredentialCreate();
    }

    /**
     * Gets the name that will assigned to the created credential.
     * 
     * @return the name that will assigned to the created credential
     */
    public String getName() {
        return this.cimiCredentialsCreate.getName();
    }

    /**
     * Sets the name that will assigned to the created credential.
     * 
     * @param name the name that will assigned to the created credential
     */
    public void setName(final String name) {
        this.cimiCredentialsCreate.setName(name);
    }

    /**
     * Gets the description that will assigned to the created credential.
     * 
     * @return the description that will assigned to the created credential
     */
    public String getDescription() {
        return this.cimiCredentialsCreate.getDescription();
    }

    /**
     * Sets the description that will assigned to the created credential.
     * 
     * @param description the description that will assigned to the created
     *        credential
     */
    public void setDescription(final String description) {
        this.cimiCredentialsCreate.setDescription(description);
    }

    /**
     * Gets the properties that will assigned to the created credential.
     * 
     * @return the properties that will assigned to the created credential
     */
    public Map<String, String> getProperties() {
        return this.cimiCredentialsCreate.getProperties();
    }

    /**
     * Sets the properties that will assigned to the created credential.
     * 
     * @param properties the properties that will assigned to the created
     *        credential
     */
    public void setProperties(final Map<String, String> properties) {
        this.cimiCredentialsCreate.setProperties(properties);
    }

    /**
     * Adds a property that will assigned to the created credential.
     * 
     * @param key property key
     * @param value property value
     */
    public void addProperty(final String key, final String value) {
        if (this.cimiCredentialsCreate.getProperties() == null) {
            this.cimiCredentialsCreate.setProperties(new HashMap<String, String>());
        }
        this.cimiCredentialsCreate.getProperties().put(key, value);
    }

    /**
     * Gets the credential template to be used to create a credential.
     * 
     * @return the credential template to be used to create a credential
     */
    public CredentialTemplate getCredentialTemplate() {
        return this.credentialTemplate;
    }

    /**
     * Sets the credential template to be used to create a credential.
     * 
     * @param credentialTemplate the credential template to be used to create a
     *        credential
     */
    public void setCredentialTemplate(final CredentialTemplate credentialTemplate) {
        this.credentialTemplate = credentialTemplate;
        this.cimiCredentialsCreate.setCredentialTemplate(credentialTemplate.cimiObject);
    }

}

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

import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;
import org.ow2.sirocco.cimi.server.domain.CimiComponentDescriptor;
import org.ow2.sirocco.cimi.server.domain.CimiJob;
import org.ow2.sirocco.cimi.server.domain.CimiSystemTemplate;
import org.ow2.sirocco.cimi.server.domain.collection.CimiSystemTemplateCollection;
import org.ow2.sirocco.cimi.server.domain.collection.CimiSystemTemplateCollectionRoot;

/**
 * Set of hardware and software settings required to create a System. A
 * SystemTemplate consists of a list component descriptors, each descriptor
 * describing how to create a System component that can be any CIMI resource
 */
public class SystemTemplate extends Resource<CimiSystemTemplate> {

    /**
     * Instantiates a new system template.
     */
    public SystemTemplate() {
        super(null, new CimiSystemTemplate());
    }

    SystemTemplate(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiSystemTemplate());
        this.cimiObject.setHref(id);
    }

    SystemTemplate(final CimiClient cimiClient, final CimiSystemTemplate cimiObject) {
        super(cimiClient, cimiObject);
    }

    /**
     * Deletes this system template.
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
     * Gets the component descriptors of this system template.
     * 
     * @return the component descriptors of this system template
     */
    public List<ComponentDescriptor> getComponentDescriptors() {
        List<ComponentDescriptor> result = new ArrayList<ComponentDescriptor>();
        if (this.cimiObject.getComponentDescriptors() != null) {
            for (CimiComponentDescriptor comp : this.cimiObject.getComponentDescriptors()) {
                result.add(new ComponentDescriptor(comp));
            }
        }
        return result;
    }

    /**
     * Creates a new system template.
     * 
     * @param client the CIMI client
     * @param systemTemplate the system template to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<SystemTemplate> createSystemTemplate(final CimiClient client, final SystemTemplate systemTemplate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getSystemTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiSystemTemplateCollection systemTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getSystemTemplates().getHref()), CimiSystemTemplateCollectionRoot.class);
        String addRef = Helper.findOperation("add", systemTemplateCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiSystemTemplate> result = client.postCreateRequest(addRef, systemTemplate.cimiObject,
            CimiSystemTemplate.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        SystemTemplate createdSystemTemplate = result.getResource() != null ? new SystemTemplate(client, result.getResource())
            : null;
        return new CreateResult<SystemTemplate>(job, createdSystemTemplate);
    }

    /**
     * Retrieves the collection of system templates visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the system templates
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<SystemTemplate> getSystemTemplates(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getSystemTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiSystemTemplateCollection systemTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getSystemTemplates().getHref()), CimiSystemTemplateCollectionRoot.class,
            queryParams);

        List<SystemTemplate> result = new ArrayList<SystemTemplate>();

        if (systemTemplateCollection.getCollection() != null) {
            for (CimiSystemTemplate cimiSystemTemplate : systemTemplateCollection.getCollection().getArray()) {
                result.add(new SystemTemplate(client, cimiSystemTemplate));
            }
        }
        return result;
    }

    /**
     * Retrieves the system template with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the system template by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static SystemTemplate getSystemTemplateByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new SystemTemplate(client, client.getCimiObjectByReference(id, CimiSystemTemplate.class, queryParams));
    }

}

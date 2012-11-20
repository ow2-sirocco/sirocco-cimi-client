/**
 *
 * SIROCCO
 * Copyright (C) 2012 France Telecom
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
 */
package org.ow2.sirocco.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiResourceMetadata;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiResourceMetadataCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiResourceMetadataCollectionRoot;

/**
 * Resource metadata.
 */
public class ResourceMetadata {

    /**
     * Attribute metadata.
     */
    public static class AttributeMetadata {
        private final String name;

        private final String namespace;

        private final String type;

        private final Boolean required;

        AttributeMetadata(final CimiResourceMetadata.AttributeMetadata from) {
            this.name = from.getName();
            this.namespace = from.getNamespace();
            this.type = from.getType();
            this.required = from.getRequired();
        }

        /**
         * Gets the name of the attribute.
         * 
         * @return the name of the attribute
         */
        public String getName() {
            return this.name;
        }

        /**
         * Gets the namespace of the attribute.
         * 
         * @return the namespace of the attribute
         */
        public String getNamespace() {
            return this.namespace;
        }

        /**
         * Gets the type of the attribute.
         * 
         * @return the type of the attribute
         */
        public String getType() {
            return this.type;
        }

        /**
         * True if the attribute is required.
         * 
         * @return true if the attribute is required
         */
        public Boolean isRequired() {
            return this.required;
        }
    }

    private CimiClient cimiClient;

    private CimiResourceMetadata cimiResourceMetaData;

    private List<AttributeMetadata> attributeMetadata;

    /**
     * Gets the id of the ResourceMetadata.
     * 
     * @return the id of the ResourceMetadata
     */
    public String getId() {
        if (this.cimiResourceMetaData.getId() != null) {
            return this.cimiResourceMetaData.getId();
        } else {
            return this.cimiResourceMetaData.getHref();
        }
    }

    /**
     * Gets the type URI of the resource.
     * 
     * @return the type URI of the resource
     */
    public String getTypeURI() {
        return this.cimiResourceMetaData.getTypeURI();
    }

    /**
     * Gets the name of the resource type.
     * 
     * @return the name of the resource type
     */
    public String getName() {
        return this.cimiResourceMetaData.getName();
    }

    /**
     * Gets the attribute metadata.
     * 
     * @return the attribute metadata
     */
    public List<AttributeMetadata> getAttributes() {
        return this.attributeMetadata;
    }

    ResourceMetadata(final CimiClient cimiClient, final CimiResourceMetadata cimiResourceMetaData) {
        this.cimiClient = cimiClient;
        this.cimiResourceMetaData = cimiResourceMetaData;
        this.attributeMetadata = new ArrayList<AttributeMetadata>();
        for (CimiResourceMetadata.AttributeMetadata from : cimiResourceMetaData.getAttributes()) {
            this.attributeMetadata.add(new AttributeMetadata(from));
        }
    }

    /**
     * Retrieves the resource metadata of the CIMI provider.
     * 
     * @param client the CIMI client
     * @param queryParams optional query parameters
     * @return the resource metadata
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<ResourceMetadata> getResourceMetadatas(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getResourceMetadata() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResourceMetadataCollection metadataCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getResourceMetadata().getHref()),
            CimiResourceMetadataCollectionRoot.class, queryParams);
        List<ResourceMetadata> result = new ArrayList<ResourceMetadata>();

        if (metadataCollection.getCollection() != null) {
            for (CimiResourceMetadata cimiResourceMetadata : metadataCollection.getCollection().getArray()) {
                result.add(new ResourceMetadata(client, cimiResourceMetadata));
            }
        }
        return result;
    }

    /**
     * Retrieves the resource metadata with a given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the resource metadata by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static ResourceMetadata getResourceMetadataByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        ResourceMetadata result = new ResourceMetadata(client, client.getCimiObjectByReference(id, CimiResourceMetadata.class,
            queryParams));
        return result;
    }

}

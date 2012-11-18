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
package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiResourceMetadata;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiResourceMetadataCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiResourceMetadataCollectionRoot;

public class ResourceMetadata {
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

        public String getName() {
            return this.name;
        }

        public String getNamespace() {
            return this.namespace;
        }

        public String getType() {
            return this.type;
        }

        public Boolean getRequired() {
            return this.required;
        }
    }

    private CimiClient cimiClient;

    private CimiResourceMetadata cimiResourceMetaData;

    private List<AttributeMetadata> attributeMetadata;

    public String getId() {
        if (this.cimiResourceMetaData.getId() != null) {
            return this.cimiResourceMetaData.getId();
        } else {
            return this.cimiResourceMetaData.getHref();
        }
    }

    public String getTypeURI() {
        return this.cimiResourceMetaData.getTypeURI();
    }

    public String getName() {
        return this.cimiResourceMetaData.getName();
    }

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

    public static List<ResourceMetadata> getResourceMetadatas(final CimiClient client, final QueryParams... queryParams)
        throws CimiException {
        if (client.cloudEntryPoint.getResourceMetadata() == null) {
            throw new CimiException("Unsupported operation");
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

    public static ResourceMetadata getResourceMetadataByReference(final CimiClient client, final String ref,
        final QueryParams... queryParams) throws CimiException {
        ResourceMetadata result = new ResourceMetadata(client, client.getCimiObjectByReference(ref, CimiResourceMetadata.class,
            queryParams));
        return result;
    }

}

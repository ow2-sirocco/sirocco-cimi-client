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

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiCredential;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiCredentialCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiCredentialCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient.CimiResult;

/**
 * Information required to create the initial administrative superuser of a
 * Machine.
 */
public class Credential extends Resource<CimiCredential> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Credential";

    /**
     * Instantiates a new credential.
     */
    public Credential() {
        super(null, new CimiCredential());
    }

    Credential(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiCredential());
        this.cimiObject.setHref(id);
    }

    Credential(final CimiClient cimiClient, final CimiCredential cimiObject) {
        super(cimiClient, cimiObject);
    }

    /**
     * Deletes this credential.
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
     * Creates a new credential.
     * 
     * @param client the CIMI client
     * @param credentialCreate the credential to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<Credential> createCredential(final CimiClient client, final CredentialCreate credentialCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getCredentials() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiCredentialCollection credentialCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getCredentials().getHref()), CimiCredentialCollectionRoot.class);
        String addRef = Helper.findOperation("add", credentialCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiCredential> result = client.postCreateRequest(addRef, credentialCreate.cimiCredentialsCreate,
            CimiCredential.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Credential cred = result.getResource() != null ? new Credential(client, result.getResource()) : null;
        return new CreateResult<Credential>(job, cred);
    }

    /**
     * Retrieves the collection of credentials visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the credentials
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Credential> getCredentials(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getCredentials() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiCredentialCollection credentialCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getCredentials().getHref()), CimiCredentialCollectionRoot.class,
            queryParams);

        List<Credential> result = new ArrayList<Credential>();

        if (credentialCollection.getCollection() != null) {
            for (CimiCredential cimiCrdential : credentialCollection.getCollection().getArray()) {
                result.add(new Credential(client, cimiCrdential));
            }
        }
        return result;
    }

    /**
     * Retrieves the credential with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the credential by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Credential getCredentialByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new Credential(client, client.getCimiObjectByReference(id, CimiCredential.class, queryParams));
    }

}

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
import java.util.Map;
import java.util.Map.Entry;

import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;
import org.ow2.sirocco.cimi.server.domain.CimiAddress;
import org.ow2.sirocco.cimi.server.domain.CimiJob;
import org.ow2.sirocco.cimi.server.domain.CimiMachineConfiguration;
import org.ow2.sirocco.cimi.server.domain.CimiMachineImage;
import org.ow2.sirocco.cimi.server.domain.CimiMachineTemplate;
import org.ow2.sirocco.cimi.server.domain.CimiMachineTemplateNetworkInterface;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineTemplateCollection;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineTemplateCollectionRoot;

/**
 * Set of hardware and software settings required to create a Machine.
 */
public class MachineTemplate extends Resource<CimiMachineTemplate> {

    /**
     * Network interface to be created on Machines.
     */
    public static class NetworkInterface {
        private Network network;

        private List<Address> addresses;

        private String networkType;

        /**
         * Gets the network associated with this network interface.
         * 
         * @return the network associated with this network interface
         */
        public Network getNetwork() {
            return this.network;
        }

        /**
         * Sets the network associated with this network interface.
         * 
         * @param network the network associated with this network interface
         */
        public void setNetwork(final Network network) {
            this.network = network;
        }

        /**
         * Gets the addresses of this network interface.
         * 
         * @return the addresses of this network interface
         */
        public List<Address> getAddresses() {
            return this.addresses;
        }

        /**
         * Sets the addresses of this network interface.
         * 
         * @param addresses the addresses of this network interface
         */
        public void setAddresses(final List<Address> addresses) {
            this.addresses = addresses;
        }

        /**
         * Gets the network type.
         * 
         * @return the network type
         */
        public String getNetworkType() {
            return this.networkType;
        }

        /**
         * Sets the network type.
         * 
         * @param networkType the network type
         */
        public void setNetworkType(final String networkType) {
            this.networkType = networkType;
        }
    }

    private MachineImage machineImage;

    private MachineConfiguration machineConfig;

    private Credential credential;

    /**
     * Instantiates a new machine template.
     */
    public MachineTemplate() {
        super(null, new CimiMachineTemplate());
    }

    MachineTemplate(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineTemplate());
        this.cimiObject.setHref(id);
    }

    MachineTemplate(final CimiMachineTemplate cimiObject) {
        super(null, cimiObject);
    }

    MachineTemplate(final CimiClient cimiClient, final CimiMachineTemplate cimiObject) {
        super(cimiClient, cimiObject);
        this.machineImage = new MachineImage(cimiClient, cimiObject.getMachineImage());
        this.machineConfig = new MachineConfiguration(cimiClient, cimiObject.getMachineConfig());
        if (cimiObject.getCredential() != null) {
            this.credential = new Credential(cimiClient, cimiObject.getCredential());
        }
    }

    /**
     * Gets the machine image that will be used to create a machine from this
     * template.
     * 
     * @return the machine image that will be used to create a machine from this
     *         template
     */
    public MachineImage getMachineImage() {
        return this.machineImage;
    }

    /**
     * Sets the machine image that will be used to create a machine from this
     * template.
     * 
     * @param machineImage the machine image that will be used to create a
     *        machine from this template
     */
    public void setMachineImage(final MachineImage machineImage) {
        this.machineImage = machineImage;
        this.cimiObject.setMachineImage(machineImage.cimiObject);
    }

    /**
     * Sets the reference of the machine image that will be used to create a
     * machine from this template.
     * 
     * @param machineImageRef the reference of the machine image that will be
     *        used to create a machine from this template
     */
    public void setMachineImageRef(final String machineImageRef) {
        this.machineImage = new MachineImage(this.cimiClient, machineImageRef);
        this.cimiObject.setMachineImage(this.machineImage.cimiObject);
    }

    /**
     * Gets the machine configuration that will be used to create a machine from
     * this template.
     * 
     * @return the machine configuration that will be used to create a machine
     *         from this template
     */
    public MachineConfiguration getMachineConfig() {
        return this.machineConfig;
    }

    /**
     * Sets the machine configuration that will be used to create a machine from
     * this template.
     * 
     * @param machineConfig the machine configuration that will be used to
     *        create a machine from this template
     */
    public void setMachineConfig(final MachineConfiguration machineConfig) {
        this.machineConfig = machineConfig;
        this.cimiObject.setMachineConfig(machineConfig.cimiObject);
    }

    /**
     * Sets the reference of the machine configuration that will be used to
     * create a machine from this template.
     * 
     * @param machineConfigRef the reference of the machine configuration that
     *        will be used to create a machine from this template
     */
    public void setMachineConfigRef(final String machineConfigRef) {
        this.machineConfig = new MachineConfiguration(this.cimiClient, machineConfigRef);
        this.cimiObject.setMachineConfig(this.machineConfig.cimiObject);
    }

    /**
     * Gets the credential that will be used to create the login credentials of
     * the machine created with this template.
     * 
     * @return the credential that will be used to create the login credentials
     *         of the machine created with this template
     */
    public Credential getCredential() {
        return this.credential;
    }

    /**
     * Sets the credential that will be used to create the login credentials of
     * the machine created with this template.
     * 
     * @param credential the credential that will be used to create the login
     *        credentials of the machine created with this template
     */
    public void setCredential(final Credential credential) {
        this.credential = credential;
        this.cimiObject.setCredential(credential.cimiObject);
    }

    /**
     * Sets the reference of the credential that will be used to create the
     * login credentials of the machine created with this template.
     * 
     * @param credentialRef the reference of the credential that will be used to
     *        create the login credentials of the machine created with this
     *        template
     */
    public void setCredentialRef(final String credentialRef) {
        this.credential = new Credential(this.cimiClient, credentialRef);
        this.cimiObject.setCredential(this.credential.cimiObject);
    }

    /**
     * Gets the network interfaces that be created on machines created from this
     * template.
     * 
     * @return the network interfaces that be created on machines created from
     *         this template
     */
    public List<NetworkInterface> getNetworkInterface() {
        List<NetworkInterface> nics = new ArrayList<NetworkInterface>();
        if (this.cimiObject.getListNetworkInterfaces() != null) {
            for (CimiMachineTemplateNetworkInterface cimiNic : this.cimiObject.getListNetworkInterfaces()) {
                NetworkInterface nic = new NetworkInterface();
                List<Address> addresses = new ArrayList<Address>();
                if (cimiNic.getAddresses() != null && cimiNic.getAddresses().length > 0) {
                    for (CimiAddress addr : cimiNic.getAddresses()) {
                        addresses.add(new Address(this.cimiClient, addr));
                    }
                }
                nic.setAddresses(addresses);
                nic.setNetworkType(cimiNic.getNetworkType());
                if (cimiNic.getNetwork() != null) {
                    nic.setNetwork(new Network(this.cimiClient, cimiNic.getNetwork()));
                }

                nics.add(nic);
            }
        }
        return nics;
    }

    /**
     * Sets the network interfaces that be created on machines created from this
     * template.
     * 
     * @param nics the network interfaces that be created on machines created
     *        from this template
     */
    public void setNetworkInterface(final List<NetworkInterface> nics) {
        List<CimiMachineTemplateNetworkInterface> templateNics = new ArrayList<CimiMachineTemplateNetworkInterface>();
        for (NetworkInterface nic : nics) {
            CimiMachineTemplateNetworkInterface templateNic = new CimiMachineTemplateNetworkInterface();
            templateNic.setNetworkType(nic.getNetworkType());
            if (nic.getAddresses() != null) {
                CimiAddress[] addresses = new CimiAddress[nic.getAddresses().size()];
                int i = 0;
                for (Address addr : nic.getAddresses()) {
                    addresses[i++] = addr.cimiObject;
                }
                templateNic.setAddresses(addresses);
            }
            if (nic.getNetwork() != null) {
                templateNic.setNetwork(nic.getNetwork().cimiObject);
            }
            templateNics.add(templateNic);
        }
        this.cimiObject.setListNetworkInterfaces(templateNics);
    }

    /**
     * Gets the user data that will be injected into machines created from this
     * template.
     * 
     * @return the user data that will be injected into machines created from
     *         this template
     */
    public String getUserData() {
        return this.cimiObject.getUserData();
    }

    /**
     * Sets the user data that will be injected into machines created from this
     * template.
     * 
     * @param userData the user data that will be injected into machines created
     *        from this template
     */
    public void setUserData(final String userData) {
        this.cimiObject.setUserData(userData);
    }

    /**
     * Deletes this machine template.
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
     * Creates a new machine template.
     * 
     * @param client the CIMI client
     * @param machineTemplate the machine template to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<MachineTemplate> createMachineTemplate(final CimiClient client,
        final MachineTemplate machineTemplate) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachineTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineTemplateCollection machineTemplateCollection = client
            .getRequest(client.extractPath(client.cloudEntryPoint.getMachineTemplates().getHref()),
                CimiMachineTemplateCollectionRoot.class);
        String addRef = Helper.findOperation("add", machineTemplateCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiMachineTemplate> result = client.postCreateRequest(addRef, machineTemplate.cimiObject,
            CimiMachineTemplate.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        MachineTemplate createdMachineTemplate = result.getResource() != null ? new MachineTemplate(client,
            result.getResource()) : null;
        return new CreateResult<MachineTemplate>(job, createdMachineTemplate);
    }

    /**
     * Updates this machine template.
     * 
     * @param client the client
     * @param id the id
     * @param attributeValues the attribute values
     * @return the update result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static UpdateResult<MachineTemplate> updateMachineTemplate(final CimiClient client, final String id,
        final Map<String, Object> attributeValues) throws CimiClientException, CimiProviderException {
        CimiMachineTemplate cimiObject = new CimiMachineTemplate();
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> entry : attributeValues.entrySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            String attribute = entry.getKey();
            sb.append(attribute);
            if (attribute.equals("name")) {
                cimiObject.setName((String) entry.getValue());
            } else if (attribute.equals("description")) {
                cimiObject.setDescription((String) entry.getValue());
            } else if (attribute.equals("properties")) {
                cimiObject.setProperties((Map<String, String>) entry.getValue());
            } else if (attribute.equals("machineConfig")) {
                cimiObject.setMachineConfig(new CimiMachineConfiguration((String) entry.getValue()));
            } else if (attribute.equals("machineImage")) {
                cimiObject.setMachineImage(new CimiMachineImage((String) entry.getValue()));
            }
        }
        CimiResult<CimiMachineTemplate> cimiResult = client.partialUpdateRequest(id, cimiObject, sb.toString());
        Job job = cimiResult.getJob() != null ? new Job(client, cimiResult.getJob()) : null;
        MachineTemplate machineTemplate = cimiResult.getResource() != null ? new MachineTemplate(client,
            cimiResult.getResource()) : null;
        return new UpdateResult<MachineTemplate>(job, machineTemplate);
    }

    /**
     * Retrieves the collection of machine templates visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the machine templates
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<MachineTemplate> getMachineTemplates(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachineTemplates() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineTemplateCollection machineTemplateCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getMachineTemplates().getHref()),
            CimiMachineTemplateCollectionRoot.class, queryParams);

        List<MachineTemplate> result = new ArrayList<MachineTemplate>();

        if (machineTemplateCollection.getCollection() != null) {
            for (CimiMachineTemplate cimiMachineTemplate : machineTemplateCollection.getCollection().getArray()) {
                result.add(new MachineTemplate(client, cimiMachineTemplate));
            }
        }
        return result;
    }

    /**
     * Retrieves the machine template with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the machine template by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static MachineTemplate getMachineTemplateByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new MachineTemplate(client, client.getCimiObjectByReference(id, CimiMachineTemplate.class, queryParams));
    }

}

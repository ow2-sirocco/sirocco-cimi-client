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

import org.ow2.sirocco.apis.rest.cimi.domain.ActionType;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiAction;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachine;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachineDisk;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachineNetworkInterface;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineDiskCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineNetworkInterfaceAddressCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineNetworkInterfaceCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Instantiated compute resource that encapsulates CPU and Memory and local
 * storage.
 */
public class Machine extends Resource<CimiMachine> {

    /** A unique URI denoting this resource type */
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Machine";

    /**
     * Machine state.
     */
    public static enum State {
        CREATING, STARTING, STARTED, STOPPING, STOPPED, PAUSING, PAUSED, SUSPENDING, SUSPENDED, DELETING, DELETED, ERROR
    }

    Machine(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachine());
        this.cimiObject.setHref(id);
    }

    Machine(final CimiClient cimiClient, final CimiMachine cimiMachine) {
        super(cimiClient, cimiMachine);
    }

    /**
     * Gets the state of this machine.
     * 
     * @return the state of this machine
     */
    public State getState() {
        if (this.cimiObject.getState() != null) {
            return State.valueOf(this.cimiObject.getState());
        } else {
            return null;
        }
    }

    /**
     * Gets the number of CPUs of this machine.
     * 
     * @return the number of CPUs of this machine
     */
    public Integer getCpu() {
        return this.cimiObject.getCpu();
    }

    /**
     * Gets the size of the memory of this machine in kibibytes.
     * 
     * @return the size of the memory of this machine in kibibytes
     */
    public Integer getMemory() {
        return this.cimiObject.getMemory();
    }

    /**
     * Gets the local disks of this machine.
     * 
     * @return the list of local disks of this machine
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public List<Disk> getDisks() throws CimiClientException, CimiProviderException {
        List<Disk> disks = null;
        if (this.cimiObject.getDisks() != null && this.cimiObject.getDisks().getArray() == null) {
            CimiMachineDiskCollectionRoot cimiDisks = this.cimiClient.getRequest(
                this.cimiClient.extractPath(this.cimiObject.getDisks().getHref()), CimiMachineDiskCollectionRoot.class);
            this.cimiObject.setDisks(cimiDisks);
        }
        if (this.cimiObject.getDisks() != null) {
            disks = new ArrayList<Disk>();
            for (CimiMachineDisk cimiDisk : this.cimiObject.getDisks().getArray()) {
                disks.add(new Disk(this.cimiClient, cimiDisk));
            }
        }
        return disks;
    }

    /**
     * Gets the network interfaces of this machine.
     * 
     * @return the network interfaces of this machine
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public List<MachineNetworkInterface> getNetworkInterfaces() throws CimiClientException, CimiProviderException {
        List<MachineNetworkInterface> nics = new ArrayList<MachineNetworkInterface>();

        if (this.cimiObject.getNetworkInterfaces() != null && this.cimiObject.getNetworkInterfaces().getArray() == null) {
            String machineNicsRef = this.cimiObject.getNetworkInterfaces().getHref();
            if (machineNicsRef != null) {
                CimiMachineNetworkInterfaceCollectionRoot cimiNics = this.cimiClient.getRequest(
                    this.cimiClient.extractPath(machineNicsRef), CimiMachineNetworkInterfaceCollectionRoot.class);
                this.cimiObject.getNetworkInterfaces().setArray(cimiNics.getArray());
            }
        }
        if (this.cimiObject.getNetworkInterfaces() != null && this.cimiObject.getNetworkInterfaces().getArray() != null) {
            for (CimiMachineNetworkInterface cimiNic : this.cimiObject.getNetworkInterfaces().getArray()) {
                if (cimiNic.getAddresses().getArray() == null && cimiNic.getAddresses().getHref() != null) {
                    CimiMachineNetworkInterfaceAddressCollectionRoot addresses = this.cimiClient
                        .getRequest(this.cimiClient.extractPath(cimiNic.getAddresses().getHref()),
                            CimiMachineNetworkInterfaceAddressCollectionRoot.class, QueryParams.builder().expand("address")
                                .build());
                    cimiNic.setAddresses(addresses);
                }
                MachineNetworkInterface nic = new MachineNetworkInterface(this.cimiClient, cimiNic);
                nics.add(nic);
            }
        }
        return nics;
    }

    /**
     * Starts this machine.
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
    public Job start() throws CimiClientException, CimiProviderException {
        String startRef = Helper.findOperation(ActionType.START.getPath(), this.cimiObject);
        if (startRef == null) {
            throw new CimiClientException("Illegal operation");
        }
        CimiAction actionStart = new CimiAction();
        actionStart.setAction(ActionType.START.getPath());
        CimiJob cimiJob = this.cimiClient.actionRequest(startRef, actionStart);
        if (cimiJob != null) {
            return new Job(this.cimiClient, cimiJob);
        } else {
            return null;
        }
    }

    /**
     * Stops this machine.
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
    public Job stop() throws CimiClientException, CimiProviderException {
        String stopRef = Helper.findOperation(ActionType.STOP.getPath(), this.cimiObject);
        if (stopRef == null) {
            throw new CimiClientException("Illegal operation");
        }
        CimiAction actionStop = new CimiAction();
        actionStop.setAction(ActionType.STOP.getPath());
        CimiJob cimiJob = this.cimiClient.actionRequest(stopRef, actionStop);
        if (cimiJob != null) {
            return new Job(this.cimiClient, cimiJob);
        } else {
            return null;
        }
    }

    /**
     * Deletes this machine.
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
     * Creates a new machine.
     * 
     * @param client the client
     * @param machineCreate creation parameters
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<Machine> createMachine(final CimiClient client, final MachineCreate machineCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachines() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineCollection machinesCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getMachines().getHref()), CimiMachineCollectionRoot.class);
        String addRef = Helper.findOperation("add", machinesCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiMachine> result = client.postCreateRequest(addRef, machineCreate.cimiMachineCreate, CimiMachine.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Machine machine = result.getResource() != null ? new Machine(client, result.getResource()) : null;
        return new CreateResult<Machine>(job, machine);
    }

    /**
     * Updates a machine.
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
    public static UpdateResult<Machine> updateMachine(final CimiClient client, final String id,
        final Map<String, Object> attributeValues) throws CimiClientException, CimiProviderException {
        CimiMachine cimiObject = new CimiMachine();
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
            }
        }
        CimiResult<CimiMachine> cimiResult = client.partialUpdateRequest(id, cimiObject, sb.toString());
        Job job = cimiResult.getJob() != null ? new Job(client, cimiResult.getJob()) : null;
        Machine machineConfig = cimiResult.getResource() != null ? new Machine(client, cimiResult.getResource()) : null;
        return new UpdateResult<Machine>(job, machineConfig);
    }

    /**
     * Retrieves the collection of machines visible to the client
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the machines
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Machine> getMachines(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachines() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineCollection machinesCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getMachines().getHref()), CimiMachineCollectionRoot.class, queryParams);
        List<Machine> result = new ArrayList<Machine>();

        if (machinesCollection.getCollection() != null) {
            for (CimiMachine cimiMachine : machinesCollection.getCollection().getArray()) {
                result.add(new Machine(client, cimiMachine));
            }
        }
        return result;
    }

    /**
     * Retrieves the machine with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the machine by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Machine getMachineByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        Machine result = new Machine(client, client.getCimiObjectByReference(id, CimiMachine.class, queryParams));
        return result;
    }

}

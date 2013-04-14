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

import org.ow2.sirocco.cimi.domain.ActionType;
import org.ow2.sirocco.cimi.domain.CimiAction;
import org.ow2.sirocco.cimi.domain.CimiJob;
import org.ow2.sirocco.cimi.domain.CimiSystem;
import org.ow2.sirocco.cimi.domain.CimiSystemMachine;
import org.ow2.sirocco.cimi.domain.CimiSystemNetwork;
import org.ow2.sirocco.cimi.domain.CimiSystemVolume;
import org.ow2.sirocco.cimi.domain.collection.CimiSystemCollection;
import org.ow2.sirocco.cimi.domain.collection.CimiSystemCollectionRoot;
import org.ow2.sirocco.cimi.domain.collection.CimiSystemMachineCollectionRoot;
import org.ow2.sirocco.cimi.domain.collection.CimiSystemNetworkCollectionRoot;
import org.ow2.sirocco.cimi.domain.collection.CimiSystemVolumeCollectionRoot;
import org.ow2.sirocco.cimi.sdk.CimiClient.CimiResult;

/**
 * Resource that combines one or more Machines, Volumes and Networks and that
 * can be operated and managed as a single unit.
 */
public class System extends Resource<CimiSystem> {

    /**
     * System state.
     */
    public static enum State {
        CREATING, CREATED, STARTING, STARTED, STOPPING, STOPPED, PAUSING, PAUSED, SUSPENDING, SUSPENDED, MIXED, DELETING, DELETED, ERROR
    }

    System(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiSystem());
        this.cimiObject.setHref(id);
    }

    System(final CimiClient cimiClient, final CimiSystem cimiSystem) {
        super(cimiClient, cimiSystem);
    }

    /**
     * Gets the state of this system.
     * 
     * @return the state of this system
     */
    public State getState() {
        if (this.cimiObject.getState() != null) {
            return State.valueOf(this.cimiObject.getState());
        } else {
            return null;
        }
    }

    /**
     * Starts this system.
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
     * Stops this system.
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
     * Deletes this system.
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
     * Gets the machines of this system.
     * 
     * @return the machines of this system
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public List<SystemMachine> getMachines() throws CimiClientException, CimiProviderException {
        String systemMachineCollection = this.cimiObject.getMachines().getHref();
        CimiSystemMachineCollectionRoot sysMachines = this.cimiClient.getRequest(
            this.cimiClient.extractPath(systemMachineCollection), CimiSystemMachineCollectionRoot.class, QueryParams.builder()
                .expand("machine").build());
        this.cimiObject.getMachines().setArray(sysMachines.getArray());
        List<SystemMachine> machines = new ArrayList<SystemMachine>();
        if (this.cimiObject.getMachines().getArray() != null) {
            for (CimiSystemMachine cimiSystemMachine : this.cimiObject.getMachines().getArray()) {
                SystemMachine systemMachine = new SystemMachine(this.cimiClient, cimiSystemMachine);
                machines.add(systemMachine);
            }
        }
        return machines;

    }

    /**
     * Gets the volumes of this system.
     * 
     * @return the volumes of this system
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public List<SystemVolume> getVolumes() throws CimiClientException, CimiProviderException {
        String systemVolumeCollection = this.cimiObject.getVolumes().getHref();
        CimiSystemVolumeCollectionRoot sysVolumes = this.cimiClient.getRequest(
            this.cimiClient.extractPath(systemVolumeCollection), CimiSystemVolumeCollectionRoot.class, QueryParams.builder()
                .expand("volume").build());
        this.cimiObject.getVolumes().setArray(sysVolumes.getArray());
        List<SystemVolume> volumes = new ArrayList<SystemVolume>();
        if (this.cimiObject.getVolumes().getArray() != null) {
            for (CimiSystemVolume cimiSystemVolume : this.cimiObject.getVolumes().getArray()) {
                SystemVolume systemVolume = new SystemVolume(this.cimiClient, cimiSystemVolume);
                volumes.add(systemVolume);
            }
        }
        return volumes;
    }

    /**
     * Gets the networks of this system.
     * 
     * @return the network of this system
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public List<SystemNetwork> getNetworks() throws CimiClientException, CimiProviderException {
        String systemNetworkCollection = this.cimiObject.getNetworks().getHref();
        CimiSystemNetworkCollectionRoot sysNetworks = this.cimiClient.getRequest(
            this.cimiClient.extractPath(systemNetworkCollection), CimiSystemNetworkCollectionRoot.class, QueryParams.builder()
                .expand("network").build());
        this.cimiObject.getNetworks().setArray(sysNetworks.getArray());
        List<SystemNetwork> networks = new ArrayList<SystemNetwork>();
        if (this.cimiObject.getNetworks().getArray() != null) {
            for (CimiSystemNetwork cimiSystemNetwork : this.cimiObject.getNetworks().getArray()) {
                SystemNetwork systemNetwork = new SystemNetwork(this.cimiClient, cimiSystemNetwork);
                networks.add(systemNetwork);
            }
        }
        return networks;
    }

    /**
     * Creates a new system.
     * 
     * @param client the CIMI client
     * @param systemCreate creation paramters
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<System> createSystem(final CimiClient client, final SystemCreate systemCreate)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getSystems() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiSystemCollection systemCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getSystems().getHref()), CimiSystemCollectionRoot.class);
        String addRef = Helper.findOperation("add", systemCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiSystem> result = client.postCreateRequest(addRef, systemCreate.cimiSystemCreate, CimiSystem.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        System system = result.getResource() != null ? new System(client, result.getResource()) : null;
        return new CreateResult<System>(job, system);
    }

    /**
     * Retrieves the collection of systems visible to the client.
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the systems
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<System> getSystems(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getSystems() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiSystemCollection systemCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getSystems().getHref()), CimiSystemCollectionRoot.class, queryParams);
        List<System> result = new ArrayList<System>();

        if (systemCollection.getCollection() != null) {
            for (CimiSystem cimiSystem : systemCollection.getCollection().getArray()) {
                result.add(new System(client, cimiSystem));
            }
        }
        return result;
    }

    /**
     * Retrieves the system with the given id..
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the system by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static System getSystemByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        System result = new System(client, client.getCimiObjectByReference(id, CimiSystem.class, queryParams));
        return result;
    }

}

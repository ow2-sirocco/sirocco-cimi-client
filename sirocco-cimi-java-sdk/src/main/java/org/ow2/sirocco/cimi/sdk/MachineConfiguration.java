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
import org.ow2.sirocco.cimi.server.domain.CimiDiskConfiguration;
import org.ow2.sirocco.cimi.server.domain.CimiJob;
import org.ow2.sirocco.cimi.server.domain.CimiMachineConfiguration;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineConfigurationCollection;
import org.ow2.sirocco.cimi.server.domain.collection.CimiMachineConfigurationCollectionRoot;

/**
 * Hardware resource settings (CPU, memory, disk) of a to-be-created Machine.
 */
public class MachineConfiguration extends Resource<CimiMachineConfiguration> {

    /**
     * Disk specification.
     */
    public static class Disk {

        /** The capacity of the disk in kilobytes. */
        public int capacity;

        /** The format of the disk. */
        public String format;

        /** The initial location of the fisk. */
        public String initialLocation;

        static Disk from(final CimiDiskConfiguration diskConfig) {
            Disk disk = new Disk();
            disk.capacity = diskConfig.getCapacity();
            disk.format = diskConfig.getFormat();
            disk.initialLocation = diskConfig.getInitialLocation();
            return disk;
        }
    }

    /**
     * Instantiates a new machine configuration.
     */
    public MachineConfiguration() {
        super(null, new CimiMachineConfiguration());
    }

    MachineConfiguration(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineConfiguration());
        this.cimiObject.setHref(id);
    }

    MachineConfiguration(final CimiClient cimiClient, final CimiMachineConfiguration cimiObject) {
        super(cimiClient, cimiObject);
    }

    /**
     * Gets the number of CPUs.
     * 
     * @return the number of CPUs
     */
    public Integer getCpu() {
        return this.cimiObject.getCpu();
    }

    /**
     * Sets the number of CPUs.
     * 
     * @param cpu the number of CPUs
     */
    public void setCpu(final int cpu) {
        this.cimiObject.setCpu(cpu);
    }

    /**
     * Gets the size of the memory in kibibytes.
     * 
     * @return the size of the memory in kibibytes
     */
    public Integer getMemory() {
        return this.cimiObject.getMemory();
    }

    /**
     * Sets the size of the memory in kibibytes.
     * 
     * @param memory the size of the memory in kibibytes
     */
    public void setMemory(final int memory) {
        this.cimiObject.setMemory(memory);
    }

    /**
     * Gets the local disks.
     * 
     * @return the local disks
     */
    public Disk[] getDisks() {
        Disk[] disks = new Disk[this.cimiObject.getDisks().length];
        for (int i = 0; i < disks.length; i++) {
            disks[i] = Disk.from(this.cimiObject.getDisks()[i]);
        }
        return disks;
    }

    private static CimiDiskConfiguration[] diskArrayToCimiDiskConfigurationArray(final Disk[] disks) {
        CimiDiskConfiguration diskConfigs[] = new CimiDiskConfiguration[disks.length];
        for (int i = 0; i < disks.length; i++) {
            diskConfigs[i] = new CimiDiskConfiguration();
            diskConfigs[i].setCapacity(disks[i].capacity);
            if (disks[i].format != null) {
                diskConfigs[i].setFormat(disks[i].format);
            } else {
                diskConfigs[i].setFormat("");
            }
            if (disks[i].initialLocation != null) {
                diskConfigs[i].setInitialLocation(disks[i].initialLocation);
            } else {
                diskConfigs[i].setInitialLocation("");
            }
        }
        return diskConfigs;
    }

    /**
     * Sets the local disks.
     * 
     * @param disks the local disks
     */
    public void setDisks(final Disk[] disks) {
        this.cimiObject.setDisks(MachineConfiguration.diskArrayToCimiDiskConfigurationArray(disks));
    }

    /**
     * Deletes this configuration.
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
     * Creates a new machine configuration.
     * 
     * @param client the CIMI client
     * @param machineConfig the machine config to create
     * @return creation result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static CreateResult<MachineConfiguration> createMachineConfiguration(final CimiClient client,
        final MachineConfiguration machineConfig) throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachineConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineConfigurationCollection machineConfigCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getMachineConfigs().getHref()),
            CimiMachineConfigurationCollectionRoot.class);
        String addRef = Helper.findOperation("add", machineConfigCollection);
        if (addRef == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiResult<CimiMachineConfiguration> result = client.postCreateRequest(addRef, machineConfig.cimiObject,
            CimiMachineConfiguration.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        MachineConfiguration createdMachineConfig = result.getResource() != null ? new MachineConfiguration(client,
            result.getResource()) : null;
        return new CreateResult<MachineConfiguration>(job, createdMachineConfig);
    }

    /**
     * Updates this machine configuration.
     * 
     * @param client the client
     * @param id the id of the machine config to update
     * @param attributeValues the attribute values
     * @return the update result
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static UpdateResult<MachineConfiguration> updateMachineConfiguration(final CimiClient client, final String id,
        final Map<String, Object> attributeValues) throws CimiClientException, CimiProviderException {
        CimiMachineConfiguration cimiObject = new CimiMachineConfiguration();
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
            } else if (attribute.equals("cpu")) {
                cimiObject.setCpu((Integer) entry.getValue());
            } else if (attribute.equals("memory")) {
                cimiObject.setMemory((Integer) entry.getValue());
            } else if (attribute.equals("disks")) {
                cimiObject.setDisks(MachineConfiguration.diskArrayToCimiDiskConfigurationArray((Disk[]) entry.getValue()));
            } else if (attribute.equals("cpuArch")) {
                cimiObject.setCpuArch((String) entry.getValue());
            }
        }
        CimiResult<CimiMachineConfiguration> cimiResult = client.partialUpdateRequest(id, cimiObject, sb.toString());
        Job job = cimiResult.getJob() != null ? new Job(client, cimiResult.getJob()) : null;
        MachineConfiguration machineConfig = cimiResult.getResource() != null ? new MachineConfiguration(client,
            cimiResult.getResource()) : null;
        return new UpdateResult<MachineConfiguration>(job, machineConfig);
    }

    /**
     * Retrieves the collection of machine configurations visible to the client
     * 
     * @param client the client
     * @param queryParams optional query parameters
     * @return the machine configurations
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<MachineConfiguration> getMachineConfigurations(final CimiClient client, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        if (client.cloudEntryPoint.getMachineConfigs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        CimiMachineConfigurationCollection machineConfigCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getMachineConfigs().getHref()),
            CimiMachineConfigurationCollectionRoot.class, queryParams);

        List<MachineConfiguration> result = new ArrayList<MachineConfiguration>();

        if (machineConfigCollection.getCollection() != null) {
            for (CimiMachineConfiguration cimiMachineConfig : machineConfigCollection.getCollection().getArray()) {
                result.add(new MachineConfiguration(client, cimiMachineConfig));
            }
        }
        return result;
    }

    /**
     * Retrieves the machine configuration with the given id.
     * 
     * @param client the client
     * @param id the id of the resource
     * @param queryParams optional query parameters
     * @return the machine configuration by reference
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static MachineConfiguration getMachineConfigurationByReference(final CimiClient client, final String id,
        final QueryParams... queryParams) throws CimiClientException, CimiProviderException {
        return new MachineConfiguration(client,
            client.getCimiObjectByReference(id, CimiMachineConfiguration.class, queryParams));
    }

}

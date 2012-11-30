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
package org.ow2.sirocco.cimi.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration.Disk;
import org.ow2.sirocco.cimi.sdk.QueryParams;
import org.ow2.sirocco.cimi.sdk.UpdateResult;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "update machine config")
public class MachineConfigUpdateCommand implements Command {
    @Parameter(description = "<machine config id>", required = true)
    private List<String> machineConfigId;

    @Parameter(names = "-name", description = "name of the config", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the config", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Parameter(names = "-cpu", description = "number of cpu", required = false)
    private Integer numberOfCpus;

    @Parameter(names = "-memory", description = "memory size in KB", required = false)
    private Integer memoryInKB;

    @Parameter(names = "-disk", description = "add disk with size in KB", required = false)
    private List<Integer> diskSizes = new ArrayList<Integer>();

    @Override
    public String getName() {
        return "machineconfig-update";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Map<String, Object> attributeValues = new HashMap<String, Object>();
        if (this.name != null) {
            attributeValues.put("name", this.name);
        }
        if (this.description != null) {
            attributeValues.put("description", this.description);
        }
        if (this.properties != null) {
            Map<String, String> props = new HashMap<String, String>();
            for (int i = 0; i < this.properties.size() / 2; i++) {
                props.put(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
            attributeValues.put("properties", props);
        }
        if (this.numberOfCpus != null) {
            attributeValues.put("cpu", this.numberOfCpus);
        }
        if (this.memoryInKB != null) {
            attributeValues.put("memory", this.memoryInKB);
        }

        if (this.diskSizes != null && this.diskSizes.size() > 0) {
            Disk disks[] = new Disk[this.diskSizes.size()];
            for (int i = 0; i < disks.length; i++) {
                disks[i] = new Disk();
                disks[i].capacity = this.diskSizes.get(i);
            }
            attributeValues.put("disks", disks);
        }
        String configId;
        if (CommandHelper.isResourceIdentifier(this.machineConfigId.get(0))) {
            configId = this.machineConfigId.get(0);
        } else {
            List<MachineConfiguration> configs = MachineConfiguration.getMachineConfigurations(cimiClient, QueryParams
                .builder().filter("name='" + this.machineConfigId.get(0) + "'").build());
            if (configs.isEmpty()) {
                System.err.println("No machine config with name " + this.machineConfigId.get(0));
                System.exit(-1);
            }
            configId = configs.get(0).getId();
        }

        UpdateResult<MachineConfiguration> result = MachineConfiguration.updateMachineConfiguration(cimiClient, configId,
            attributeValues);
        if (result.getJob() != null) {
            System.out.println("MachineConfig " + this.machineConfigId.get(0) + " being updated");
            JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
        } else {
            System.out.println("MachineConfig: " + this.machineConfigId + " updated");
        }
    }

}

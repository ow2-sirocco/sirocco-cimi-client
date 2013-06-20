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
import java.util.List;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration.Disk;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create machine config")
public class MachineConfigCreateCommand implements Command {
    @Parameter(names = "-name", description = "name of the template", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the template", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Parameter(names = "-cpu", description = "number of cpu", required = true)
    private int numberOfCpus;

    @Parameter(names = "-memory", description = "memory size in KB", required = true)
    private int memoryInKB;

    @Parameter(names = "-disk", description = "add disk with size in KB", required = true)
    private List<Integer> diskSizes = new ArrayList<Integer>();

    @Parameter(names = "-public", description = "public image", required = false)
    private boolean isPublic = false;

    @Parameter(names = "-v", description = "verbose", required = false)
    private boolean verbose;

    @Override
    public String getName() {
        return "machineconfig-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        MachineConfiguration machineConfig = new MachineConfiguration();

        machineConfig.setName(this.name);
        machineConfig.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                machineConfig.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        machineConfig.setCpu(this.numberOfCpus);
        machineConfig.setMemory(this.memoryInKB);

        Disk disks[] = new Disk[this.diskSizes.size()];
        for (int i = 0; i < disks.length; i++) {
            disks[i] = new Disk();
            disks[i].capacity = this.diskSizes.get(i);
        }
        machineConfig.setDisks(disks);
        machineConfig.setIsPublic(this.isPublic);

        CreateResult<MachineConfiguration> result = MachineConfiguration.createMachineConfiguration(cimiClient, machineConfig);
        if (this.verbose) {
            if (result.getJob() != null) {
                System.out.println("MachineConfig " + result.getJob().getTargetResourceRef() + " being created");
                JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
            } else {
                MachineConfigShowCommand.printMachineConfig(result.getResource(), new ResourceSelectParam());
            }
        } else {
            System.out.println(result.getResource().getId());
        }
    }

}

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

import java.util.List;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.Machine;
import org.ow2.sirocco.cimi.sdk.MachineImage;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "capture machine")
public class MachineCaptureCommand implements Command {
    @Parameter(description = "<machine id>", required = true)
    private List<String> machineIds;

    @Parameter(names = "-name", description = "name of the machine image", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the image", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Override
    public String getName() {
        return "machine-capture";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Machine machine;
        if (CommandHelper.isResourceIdentifier(this.machineIds.get(0))) {
            machine = Machine.getMachineByReference(cimiClient, this.machineIds.get(0));
        } else {
            List<Machine> machines = Machine.getMachines(cimiClient,
                QueryParams.builder().filter("name='" + this.machineIds.get(0) + "'").build());
            if (machines.isEmpty()) {
                System.err.println("No machine with name " + this.machineIds.get(0));
                System.exit(-1);
            }
            machine = machines.get(0);
        }
        MachineImage machineImage = new MachineImage();
        machineImage.setName(this.name);
        machineImage.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                machineImage.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        CreateResult<MachineImage> result = machine.capture(machineImage);
        if (result.getJob() != null) {
            System.out.println("Job:");
            JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
        }
        System.out.println("Machine Image:");
        MachineImageShowCommand.printMachineImage(result.getResource(), new ResourceSelectExpandParams());
    }
}

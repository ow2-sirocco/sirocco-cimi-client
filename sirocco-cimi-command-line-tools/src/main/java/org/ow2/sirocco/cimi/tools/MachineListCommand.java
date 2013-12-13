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

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.Disk;
import org.ow2.sirocco.cimi.sdk.Machine;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list machines")
public class MachineListCommand implements Command {
    public static String COMMAND_NAME = "machine-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "name", "created", "state", "provider", "location");

    @Override
    public String getName() {
        return MachineListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        List<Machine> machines = Machine.getMachines(cimiClient, this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "state", "cpu", "memory", "disks", "provider", "location");

        for (Machine machine : machines) {
            CommandHelper.printResourceCommonAttributes(table, machine, this.listParams);
            if (this.listParams.isSelected("state")) {
                table.addCell(machine.getState().toString());
            }
            if (this.listParams.isSelected("cpu")) {
                table.addCell(Integer.toString(machine.getCpu()));
            }
            if (this.listParams.isSelected("memory")) {
                table.addCell(CommandHelper.printKibibytesValue(machine.getMemory()));
            }

            if (this.listParams.isSelected("disks")) {
                StringBuffer sb = new StringBuffer();
                List<Disk> disks = machine.getDisks();
                for (int i = 0; i < disks.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(CommandHelper.printKilobytesValue(disks.get(i).getCapacity()));
                }
                table.addCell((sb.toString()));
            }
            if (this.listParams.isSelected("provider")) {
                if (machine.getProviderInfo() != null) {
                    table.addCell(machine.getProviderInfo().getProviderName());
                } else {
                    table.addCell("");
                }
            }
            if (this.listParams.isSelected("location")) {
                if (machine.getProviderInfo() != null) {
                    table.addCell(machine.getProviderInfo().getLocation());
                } else {
                    table.addCell("");
                }
            }
        }
        System.out.println(table.render());
    }
}

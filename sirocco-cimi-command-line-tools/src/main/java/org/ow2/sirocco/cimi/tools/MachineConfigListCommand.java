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
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list machine config")
public class MachineConfigListCommand implements Command {
    public static final String COMMAND_NAME = "machineconfig-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "name", "cpu", "memory", "disks");

    @Override
    public String getName() {
        return MachineConfigListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        List<MachineConfiguration> machineConfigs = MachineConfiguration.getMachineConfigurations(cimiClient,
            this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "cpu", "memory", "disks", "cpuArch");

        for (MachineConfiguration machineConfig : machineConfigs) {
            CommandHelper.printResourceCommonAttributes(table, machineConfig, this.listParams);
            if (this.listParams.isSelected("cpu")) {
                table.addCell(Integer.toString(machineConfig.getCpu()));
            }
            if (this.listParams.isSelected("memory")) {
                table.addCell(CommandHelper.printKibibytesValue(machineConfig.getMemory()));
            }

            if (this.listParams.isSelected("disks")) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < machineConfig.getDisks().length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(CommandHelper.printKilobytesValue(machineConfig.getDisks()[i].capacity));
                }
                table.addCell((sb.toString()));
            }
        }
        System.out.println(table.render());

    }
}

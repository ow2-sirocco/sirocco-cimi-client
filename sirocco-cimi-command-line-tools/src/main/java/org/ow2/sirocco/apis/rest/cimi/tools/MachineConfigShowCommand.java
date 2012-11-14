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
package org.ow2.sirocco.apis.rest.cimi.tools;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiException;
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineConfiguration.Disk;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show machine config")
public class MachineConfigShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the machine config", required = true)
    private String machineConfigId;

    @ParametersDelegate
    private ResourceSelectParam selectParam = new ResourceSelectParam();

    @Override
    public String getName() {
        return "machineconfig-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        MachineConfiguration machineConfig = MachineConfiguration.getMachineConfigurationByReference(cimiClient,
            this.machineConfigId, this.selectParam.buildQueryParams());
        MachineConfigShowCommand.printMachineConfig(machineConfig, this.selectParam);
    }

    public static void printMachineConfig(final MachineConfiguration machineConfig, final ResourceSelectParam selectParam) {
        Table table = CommandHelper.createResourceShowTable(machineConfig, selectParam);

        if (selectParam.isSelected("cpu")) {
            table.addCell("cpu");
            table.addCell(Integer.toString(machineConfig.getCpu()));
        }

        if (selectParam.isSelected("memory")) {
            table.addCell("memory (KB)");
            table.addCell(Integer.toString(machineConfig.getMemory()));
        }

        if (selectParam.isSelected("disks")) {
            for (int i = 0; i < machineConfig.getDisks().length; i++) {
                Disk disk = machineConfig.getDisks()[i];
                table.addCell("disk #" + i);
                table.addCell("capacity=" + disk.capacity + "KB, format=" + disk.format + ", initialLocation="
                    + disk.initialLocation);
            }
        }

        System.out.println(table.render());
    }
}

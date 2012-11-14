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
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineTemplate;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show machine template")
public class MachineTemplateShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the machine template", required = true)
    private String machineTemplateId;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "machinetemplate-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        MachineTemplate machineTemplate = MachineTemplate.getMachineTemplateByReference(cimiClient, this.machineTemplateId,
            this.showParams.buildQueryParams());
        MachineTemplateShowCommand.printMachineTemplate(machineTemplate, this.showParams);
    }

    public static void printMachineTemplate(final MachineTemplate machineTemplate, final ResourceSelectExpandParams showParams)
        throws CimiException {
        Table table = CommandHelper.createResourceShowTable(machineTemplate, showParams);

        if (showParams.isSelected("machineConfig")) {
            table.addCell("machine config");
            table.addCell(machineTemplate.getMachineConfig().getId());
        }
        if (showParams.isSelected("machineImage")) {
            table.addCell("machine image");
            table.addCell(machineTemplate.getMachineImage().getId());
        }
        if (showParams.isSelected("credential")) {
            table.addCell("credential");
            if (machineTemplate.getCredential() != null) {
                table.addCell(machineTemplate.getCredential().getId());
            } else {
                table.addCell("");
            }
        }
        if (showParams.isSelected("networkInterfaces")) {
            table.addCell("network Interfaces");
            StringBuffer sb = new StringBuffer();
            if (machineTemplate.getNetworkInterface() != null) {
                int i = 0;
                for (MachineTemplate.NetworkInterface nic : machineTemplate.getNetworkInterface()) {
                    sb.append("NIC#" + (i++) + ": " + nic.getNetworkType() + "  ");
                }
            }
            table.addCell(sb.toString());
        }

        System.out.println(table.render());
    }
}

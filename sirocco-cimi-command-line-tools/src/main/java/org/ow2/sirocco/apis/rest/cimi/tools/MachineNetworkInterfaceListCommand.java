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

import java.util.List;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.Address;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiException;
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineNetworkInterface;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list nics of a machine")
public class MachineNetworkInterfaceListCommand implements Command {
    public static String COMMAND_NAME = "nic-list";

    @Parameter(names = "-machine", description = "id of the machine", required = true)
    private String machineId;

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "addresses", "networkType", "state");

    @Override
    public String getName() {
        return MachineNetworkInterfaceListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        List<MachineNetworkInterface> nics = MachineNetworkInterface.getMachineNetworkInterfaces(cimiClient, this.machineId,
            this.listParams.buildQueryParams().setExpand("addresses"));

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "addresses", "networkType", "network", "state");

        for (MachineNetworkInterface nic : nics) {
            CommandHelper.printResourceCommonAttributes(table, nic, this.listParams);
            if (this.listParams.isSelected("addresses")) {
                StringBuffer sb = new StringBuffer();
                for (Address address : nic.getAddresses()) {
                    sb.append(address.getIp() + " ");
                }
                table.addCell(sb.toString());
            }
            if (this.listParams.isSelected("networkType")) {
                table.addCell(nic.getType().toString());
            }
            if (this.listParams.isSelected("network")) {
                if (nic.getNetwork() != null) {
                    table.addCell(nic.getNetwork().getId());
                } else {
                    table.addCell("");
                }
            }
            if (this.listParams.isSelected("state")) {
                table.addCell(nic.getState().toString());
            }
        }
        System.out.println(table.render());
    }
}

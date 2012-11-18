/**
 *
 * SIROCCO
 * Copyright (C) 2012 France Telecom
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

@Parameters(commandDescription = "show nic")
public class MachineNetworkInterfaceShowCommand implements Command {
    @Parameter(description = "<nic id>", required = true)
    private List<String> nicIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "nic-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        MachineNetworkInterface nic = MachineNetworkInterface.getMachineNetworkInterfaceByReference(cimiClient,
            this.nicIds.get(0), this.showParams.getQueryParams().toBuilder().expand("addresses").build());
        MachineNetworkInterfaceShowCommand.printMachineNetworkInterface(nic, this.showParams);
    }

    public static void printMachineNetworkInterface(final MachineNetworkInterface nic,
        final ResourceSelectExpandParams showParams) throws CimiException {
        Table table = CommandHelper.createResourceShowTable(nic, showParams);

        if (showParams.isSelected("state")) {
            table.addCell("state");
            table.addCell(nic.getState().toString());
        }

        if (showParams.isSelected("addresses")) {
            table.addCell("IP addresses");
            StringBuffer sb = new StringBuffer();
            for (Address address : nic.getAddresses()) {
                sb.append(address.getIp() + " ");
            }
            table.addCell(sb.toString());
        }

        if (showParams.isSelected("network")) {
            table.addCell("network");
            if (nic.getNetwork() != null) {
                table.addCell(nic.getNetwork().getId());
            } else {
                table.addCell("");
            }
        }

        if (showParams.isSelected("networkType")) {
            table.addCell("network type");
            table.addCell(nic.getType().toString());
        }

        System.out.println(table.render());
    }

}

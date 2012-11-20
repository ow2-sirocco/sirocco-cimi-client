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
import org.ow2.sirocco.cimi.sdk.Address;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show address")
public class AddressShowCommand implements Command {
    @Parameter(description = "<address id>", required = true)
    private List<String> addressIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "address-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Address address = Address.getAddressByReference(cimiClient, this.addressIds.get(0), this.showParams.getQueryParams());
        AddressShowCommand.printAddress(address, this.showParams);
    }

    public static void printAddress(final Address address, final ResourceSelectExpandParams showParams)
        throws CimiClientException {
        Table table = CommandHelper.createResourceShowTable(address, showParams);
        if (showParams.isSelected("ip")) {
            table.addCell("ip");
            table.addCell(address.getIp());
        }

        if (showParams.isSelected("hostname")) {
            table.addCell("hostname");
            table.addCell(address.getHostname());
        }

        if (showParams.isSelected("allocation")) {
            table.addCell("allocation");
            table.addCell(address.getAllocation().toString());
        }
        if (showParams.isSelected("defaultGateway")) {
            table.addCell("defaultGateway");
            table.addCell(address.getDefaultGateway());
        }
        if (showParams.isSelected("dns")) {
            table.addCell("dns");
            StringBuffer sb = new StringBuffer();
            if (address.getDns() != null) {
                for (String dns : address.getDns()) {
                    sb.append(dns + " ");
                }
            }
            table.addCell(sb.toString());
        }
        if (showParams.isSelected("protocol")) {
            table.addCell("protocol");
            table.addCell(address.getProtocol());
        }
        if (showParams.isSelected("mask")) {
            table.addCell("mask");
            table.addCell(address.getMask());
        }
        if (showParams.isSelected("network")) {
            table.addCell("network");
            if (address.getNetwork() != null) {
                table.addCell(address.getNetwork().getId());
            } else {
                table.addCell("");
            }
        }

        System.out.println(table.render());
    }

}

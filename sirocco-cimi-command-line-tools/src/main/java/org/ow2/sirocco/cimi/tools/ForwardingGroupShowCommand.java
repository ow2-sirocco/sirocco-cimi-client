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
import org.ow2.sirocco.cimi.sdk.ForwardingGroup;
import org.ow2.sirocco.cimi.sdk.Network;
import org.ow2.sirocco.cimi.sdk.ProviderInfo;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show forwarding group")
public class ForwardingGroupShowCommand implements Command {
    @Parameter(description = "<forwardingGroup id>", required = true)
    private List<String> forwardingGroupIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "forwarding-group-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        ForwardingGroup fg;
        if (CommandHelper.isResourceIdentifier(this.forwardingGroupIds.get(0))) {
            fg = ForwardingGroup.getForwardingGroupByReference(cimiClient, this.forwardingGroupIds.get(0),
                this.showParams.getQueryParams());
        } else {
            List<ForwardingGroup> groups = ForwardingGroup.getForwardingGroups(cimiClient, this.showParams.getQueryParams()
                .toBuilder().filter("name='" + this.forwardingGroupIds.get(0) + "'").build());
            if (groups.isEmpty()) {
                System.err.println("No forwarding group with name " + this.forwardingGroupIds.get(0));
                System.exit(-1);
            }
            fg = groups.get(0);
        }
        ForwardingGroupShowCommand.printForwardingGroup(fg, this.showParams);
    }

    public static void printForwardingGroup(final ForwardingGroup fg, final ResourceSelectExpandParams showParams)
        throws CimiClientException {
        Table table = CommandHelper.createResourceShowTable(fg, showParams);

        if (showParams.isSelected("provider")) {
            if (fg.getProviderInfo() != null) {
                ProviderInfo info = fg.getProviderInfo();
                table.addCell("provider");
                table.addCell("account id=" + info.getProviderAccountId() + " (" + info.getProviderName() + ")");
                table.addCell("provider-assigned id");
                table.addCell(info.getProviderAssignedId());
            }
        }
        for (Network net : fg.getNetworks()) {
            table.addCell("Network");
            table.addCell(net.getId());
        }

        System.out.println(table.render());
    }

}

/**
 *
 * SIROCCO
 * Copyright (C) 2014 Orange
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
import org.ow2.sirocco.cimi.sdk.ForwardingGroup;
import org.ow2.sirocco.cimi.sdk.ForwardingGroupCreate;
import org.ow2.sirocco.cimi.sdk.Network;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create forwarding group")
public class ForwardingGroupCreateCommand implements Command {
    @Parameter(names = "-providerAccountId", description = "id of the provider account", required = false)
    private String providerAccountId;

    @Parameter(names = "-location", description = "location", required = false)
    private String location;

    @Parameter(names = "-net", description = "network id", required = true)
    private List<String> networkIds;

    @Parameter(names = "-name", description = "name of the template", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the template", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Override
    public String getName() {
        return "forwarding-group-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        ForwardingGroupCreate forwardingGroupCreate = new ForwardingGroupCreate();
        forwardingGroupCreate.setName(this.name);
        forwardingGroupCreate.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                forwardingGroupCreate.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        forwardingGroupCreate.setProviderAccountId(this.providerAccountId);
        forwardingGroupCreate.setLocation(this.location);

        List<Network> nets = new ArrayList<>();
        for (String netId : this.networkIds) {
            Network net;
            if (CommandHelper.isResourceIdentifier(netId)) {
                net = Network.getNetworkByReference(cimiClient, netId);
            } else {
                List<Network> networks = Network.getNetworks(cimiClient, QueryParams.builder().filter("name='" + netId + "'")
                    .build());
                if (networks.isEmpty()) {
                    System.err.println("No network with name " + netId);
                    System.exit(-1);
                }
                net = networks.get(0);
            }
            nets.add(net);
        }
        forwardingGroupCreate.setNetworks(nets);

        CreateResult<ForwardingGroup> result = ForwardingGroup.createForwardingGroup(cimiClient, forwardingGroupCreate);
        if (result.getJob() != null) {
            System.out.println("Job:");
            JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
        }
        System.out.println("ForwardingGroup:");
        ForwardingGroupShowCommand.printForwardingGroup(result.getResource(), new ResourceSelectExpandParams());
    }
}

/**
 *
 * SIROCCO
 * Copyright (C) 2013 France Telecom
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
 */
package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.Network;
import org.ow2.sirocco.cimi.sdk.NetworkConfiguration;
import org.ow2.sirocco.cimi.sdk.NetworkCreate;
import org.ow2.sirocco.cimi.sdk.NetworkTemplate;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create network")
public class NetworkCreateCommand implements Command {
    @Parameter(names = "-config", description = "id of the config", required = false)
    private String configId;

    @Parameter(names = "-cidr", description = "cidr of the subnet associated with the network", required = false)
    private String cidr;

    @Parameter(names = "-providerAccountId", description = "id of the provider account", required = true)
    private String providerAccountId;

    @Parameter(names = "-location", description = "location", required = false)
    private String location;

    @Parameter(names = "-name", description = "name of the template", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the template", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Parameter(names = "-v", description = "verbose", required = false)
    private boolean verbose;

    @Override
    public String getName() {
        return "network-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        NetworkCreate networkCreate = new NetworkCreate();
        NetworkTemplate networkTemplate = new NetworkTemplate();

        if (this.configId != null) {
            if (!CommandHelper.isResourceIdentifier(this.configId)) {
                List<NetworkConfiguration> netConfigs = NetworkConfiguration.getNetworkConfigurations(cimiClient, QueryParams
                    .builder().filter("name='" + this.configId + "'").select("id").build());
                if (netConfigs.isEmpty()) {
                    System.err.println("No network config with name " + this.configId);
                    System.exit(-1);
                }
                this.configId = netConfigs.get(0).getId();
            }
            networkTemplate.setNetworkConfigRef(this.configId);
        } else if (this.cidr != null) {
            NetworkConfiguration networkConfig = new NetworkConfiguration();
            networkConfig.setCidr(this.cidr);
            networkConfig.setNetworkType("PRIVATE");
            networkTemplate.setNetworkConfig(networkConfig);
        }

        networkCreate.setNetworkTemplate(networkTemplate);
        networkCreate.setName(this.name);
        networkCreate.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                networkCreate.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        networkCreate.setProviderAccountId(this.providerAccountId);
        networkCreate.setLocation(this.location);
        CreateResult<Network> result = Network.createNetwork(cimiClient, networkCreate);
        if (this.verbose) {
            if (result.getJob() != null) {
                System.out.println("Job:");
                JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
            }
            System.out.println("Network being created:");
            NetworkShowCommand.printNetwork(result.getResource(), new ResourceSelectExpandParams());
        } else {
            System.out.println(result.getResource().getId());
        }
    }
}

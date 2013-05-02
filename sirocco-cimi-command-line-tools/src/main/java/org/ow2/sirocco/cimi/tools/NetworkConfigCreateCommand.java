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
import org.ow2.sirocco.cimi.sdk.NetworkConfiguration;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create network config")
public class NetworkConfigCreateCommand implements Command {
    @Parameter(names = "-type", description = "type of the network: PUBLIC or PRIVATE", required = true)
    private String type;

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
        return "networkconfig-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        NetworkConfiguration networkConfig = new NetworkConfiguration();
        networkConfig.setNetworkType(this.type);
        networkConfig.setName(this.name);
        networkConfig.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                networkConfig.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        CreateResult<NetworkConfiguration> result = NetworkConfiguration.createNetworkConfiguration(cimiClient, networkConfig);
        if (this.verbose) {
            if (result.getJob() != null) {
                System.out.println("Job:");
                JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
            }
            System.out.println("Network config being created:");
            NetworkConfigShowCommand.printNetworkConfig(result.getResource(), new ResourceSelectExpandParams());
        } else {
            System.out.println(result.getResource().getId());
        }
    }
}

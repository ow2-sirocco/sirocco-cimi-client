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

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.VolumeConfiguration;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create volume config")
public class VolumeConfigCreateCommand implements Command {
    @Parameter(names = "-name", description = "name of the volume config", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the volume config", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Parameter(names = "-capacity", description = "capacity in KB", required = true)
    private int capacity;

    @Parameter(names = "-format", description = "format", required = true)
    private String format;

    @Parameter(names = "-v", description = "verbose", required = false)
    private boolean verbose;

    @Override
    public String getName() {
        return "volumeconfig-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        VolumeConfiguration volumeConfig = new VolumeConfiguration();

        volumeConfig.setName(this.name);
        volumeConfig.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                volumeConfig.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        volumeConfig.setCapacity(this.capacity);
        volumeConfig.setFormat(this.format);
        volumeConfig.setType(""); // XXX

        CreateResult<VolumeConfiguration> result = VolumeConfiguration.createVolumeConfiguration(cimiClient, volumeConfig);
        if (this.verbose) {
            if (result.getJob() != null) {
                System.out.println("Job:");
                JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
            }
            System.out.println("VolumeConfiguration:");
            VolumeConfigShowCommand.printVolumeConfig(result.getResource(), new ResourceSelectExpandParams());
        } else {
            System.out.println(result.getResource().getId());
        }
    }
}

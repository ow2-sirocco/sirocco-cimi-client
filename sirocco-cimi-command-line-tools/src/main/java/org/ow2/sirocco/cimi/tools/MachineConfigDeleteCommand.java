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
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "delete machine config")
public class MachineConfigDeleteCommand implements Command {
    @Parameter(description = "<machine config id>", required = true)
    private List<String> machineConfigIds;

    @Override
    public String getName() {
        return "machineconfig-delete";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        MachineConfiguration machineConfig;
        if (CommandHelper.isResourceIdentifier(this.machineConfigIds.get(0))) {
            machineConfig = MachineConfiguration.getMachineConfigurationByReference(cimiClient, this.machineConfigIds.get(0));
        } else {
            List<MachineConfiguration> configs = MachineConfiguration.getMachineConfigurations(cimiClient, QueryParams
                .builder().filter("name='" + this.machineConfigIds.get(0) + "'").build());
            if (configs.isEmpty()) {
                System.err.println("No machine config with name " + this.machineConfigIds.get(0));
                System.exit(-1);
            }
            machineConfig = configs.get(0);
        }
        machineConfig.delete();
        System.out.println("MachineConfig " + this.machineConfigIds.get(0) + " deleted");
    }
}

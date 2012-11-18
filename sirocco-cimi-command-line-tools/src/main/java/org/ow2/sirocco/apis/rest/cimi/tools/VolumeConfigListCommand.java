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
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiException;
import org.ow2.sirocco.apis.rest.cimi.sdk.VolumeConfiguration;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list volume config")
public class VolumeConfigListCommand implements Command {
    public static String COMMAND_NAME = "volumeconfig-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "capacity");

    @Override
    public String getName() {
        return VolumeConfigListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        List<VolumeConfiguration> volumeConfigs = VolumeConfiguration.getVolumeConfigurations(cimiClient,
            this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "type", "format", "capacity");

        for (VolumeConfiguration volumeConfig : volumeConfigs) {
            CommandHelper.printResourceCommonAttributes(table, volumeConfig, this.listParams);
            if (this.listParams.isSelected("type")) {
                table.addCell(volumeConfig.getType());
            }
            if (this.listParams.isSelected("format")) {
                table.addCell(volumeConfig.getFormat());
            }
            if (this.listParams.isSelected("capacity")) {
                table.addCell(CommandHelper.printKilobytesValue(volumeConfig.getCapacity()));
            }

        }
        System.out.println(table.render());

    }
}

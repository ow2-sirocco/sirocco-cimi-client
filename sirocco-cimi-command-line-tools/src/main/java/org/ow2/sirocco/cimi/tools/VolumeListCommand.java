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
import org.ow2.sirocco.cimi.sdk.Volume;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list volumes")
public class VolumeListCommand implements Command {
    public static String COMMAND_NAME = "volume-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "state", "capacity");

    @Override
    public String getName() {
        return VolumeListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        List<Volume> volumes = Volume.getVolumes(cimiClient, this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "state", "type", "capacity", "bootable");

        for (Volume volume : volumes) {
            CommandHelper.printResourceCommonAttributes(table, volume, this.listParams);
            if (this.listParams.isSelected("state")) {
                table.addCell(volume.getState().toString());
            }
            if (this.listParams.isSelected("type")) {
                table.addCell(volume.getType());
            }
            if (this.listParams.isSelected("capacity")) {
                table.addCell(CommandHelper.printKilobytesValue(volume.getCapacity()));
            }
            if (this.listParams.isSelected("bootable")) {
                table.addCell(Boolean.toString(volume.isBootable()));
            }
        }
        System.out.println(table.render());
    }
}

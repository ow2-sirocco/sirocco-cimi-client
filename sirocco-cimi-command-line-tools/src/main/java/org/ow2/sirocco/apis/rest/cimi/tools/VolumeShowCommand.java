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
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClientException;
import org.ow2.sirocco.apis.rest.cimi.sdk.Volume;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show volume")
public class VolumeShowCommand implements Command {
    @Parameter(description = "<volume id>", required = true)
    private List<String> volumeIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "volume-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Volume volume = Volume.getVolumeByReference(cimiClient, this.volumeIds.get(0), this.showParams.getQueryParams());
        VolumeShowCommand.printVolume(volume, this.showParams);
    }

    public static void printVolume(final Volume volume, final ResourceSelectExpandParams showParams) throws CimiClientException {
        Table table = CommandHelper.createResourceShowTable(volume, showParams);

        if (showParams.isSelected("state") && volume.getState() != null) {
            table.addCell("state");
            table.addCell(volume.getState().toString());
        }
        if (showParams.isSelected("type") && volume.getType() != null) {
            table.addCell("type");
            table.addCell(volume.getType());
        }
        if (showParams.isSelected("capacity") && volume.getCapacity() != null) {
            table.addCell("capacity");
            table.addCell(CommandHelper.printKilobytesValue(volume.getCapacity()));
        }
        if (showParams.isSelected("bootable") && volume.isBootable() != null) {
            table.addCell("bootable");
            table.addCell(Boolean.toString(volume.isBootable()));
        }

        System.out.println(table.render());
    }

}

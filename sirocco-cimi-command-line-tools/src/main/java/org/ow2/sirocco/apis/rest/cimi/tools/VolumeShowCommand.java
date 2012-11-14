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

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiException;
import org.ow2.sirocco.apis.rest.cimi.sdk.Volume;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show volume")
public class VolumeShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the volume", required = true)
    private String volumeId;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "volume-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        Volume volume = Volume.getVolumeByReference(cimiClient, this.volumeId, this.showParams.buildQueryParams());
        VolumeShowCommand.printVolume(volume, this.showParams);
    }

    public static void printVolume(final Volume volume, final ResourceSelectExpandParams showParams) throws CimiException {
        Table table = CommandHelper.createResourceShowTable(volume, showParams);

        if (showParams.isSelected("state")) {
            table.addCell("state");
            table.addCell(volume.getState().toString());
        }
        if (showParams.isSelected("type")) {
            table.addCell("type");
            table.addCell(volume.getType());
        }
        if (showParams.isSelected("capacity")) {
            table.addCell("capacity");
            table.addCell(Integer.toString(volume.getCapacity()) + "KB");
        }
        if (showParams.isSelected("bootable")) {
            table.addCell("bootable");
            table.addCell(Boolean.toString(volume.getBootable()));
        }

        System.out.println(table.render());
    }

}

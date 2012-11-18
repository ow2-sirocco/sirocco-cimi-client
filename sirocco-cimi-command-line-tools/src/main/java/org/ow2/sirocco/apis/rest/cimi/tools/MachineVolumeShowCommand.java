/**
 *
 * SIROCCO
 * Copyright (C) 2012 France Telecom
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
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineVolume;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show MachineVolume")
public class MachineVolumeShowCommand implements Command {
    @Parameter(description = "<MachineVolume id>", required = true)
    private List<String> machineVolumeIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "machinevolume-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        MachineVolume machineVolume = MachineVolume.getMachineVolumeByReference(cimiClient, this.machineVolumeIds.get(0),
            this.showParams.getQueryParams().toBuilder().expand("volume").build());
        MachineVolumeShowCommand.printMachineVolume(machineVolume, this.showParams);
    }

    public static void printMachineVolume(final MachineVolume machineVolume, final ResourceSelectExpandParams showParams)
        throws CimiException {
        Table table = CommandHelper.createResourceShowTable(machineVolume, showParams);

        if (showParams.isSelected("initialLocation")) {
            table.addCell("initial location");
            table.addCell(machineVolume.getVolume().getId());
        }
        if (showParams.isSelected("volume")) {
            table.addCell("volume");
            table.addCell(machineVolume.getVolume().getId());
        }

        System.out.println(table.render());
    }

}

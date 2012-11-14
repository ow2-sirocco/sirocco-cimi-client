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
import org.ow2.sirocco.apis.rest.cimi.sdk.MachineImage;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show machine image")
public class MachineImageShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the machine image", required = true)
    private String machineImageId;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "machineimage-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        MachineImage machineImage = MachineImage.getMachineImageByReference(cimiClient, this.machineImageId,
            this.showParams.buildQueryParams());
        MachineImageShowCommand.printMachineImage(machineImage, this.showParams);
    }

    public static void printMachineImage(final MachineImage machineImage, final ResourceSelectExpandParams showParams) {
        Table table = CommandHelper.createResourceShowTable(machineImage, showParams);

        if (showParams.isSelected("state")) {
            table.addCell("state");
            table.addCell(machineImage.getState().toString());
        }
        if (showParams.isSelected("type")) {
            table.addCell("type");
            table.addCell(machineImage.getType().toString());
        }
        if (showParams.isSelected("imageLocation")) {
            table.addCell("image location");
            table.addCell(machineImage.getImageLocation());
        }
        if (showParams.isSelected("related image")) {
            table.addCell("related image");
            // TODO
        }

        System.out.println(table.render());
    }
}

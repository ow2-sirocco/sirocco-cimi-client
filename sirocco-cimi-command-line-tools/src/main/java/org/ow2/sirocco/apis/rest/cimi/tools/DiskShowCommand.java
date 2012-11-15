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

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiException;
import org.ow2.sirocco.apis.rest.cimi.sdk.Disk;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show disk")
public class DiskShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the Disk", required = true)
    private String diskId;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "disk-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        Disk disk = Disk.getMachineDiskByReference(cimiClient, this.diskId, this.showParams.getQueryParams());
        DiskShowCommand.printDisk(disk, this.showParams);
    }

    public static void printDisk(final Disk disk, final ResourceSelectExpandParams showParams) throws CimiException {
        Table table = CommandHelper.createResourceShowTable(disk, showParams);

        if (showParams.isSelected("capacity")) {
            table.addCell("capacity (KB)");
            table.addCell(Integer.toString(disk.getCapacity()));
        }
        if (showParams.isSelected("initialLocation")) {
            table.addCell("initial location");
            table.addCell(disk.getInitialLocation());
        }
        System.out.println(table.render());
    }

}

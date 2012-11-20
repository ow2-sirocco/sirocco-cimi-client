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
package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.Disk;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show disk")
public class DiskShowCommand implements Command {
    @Parameter(description = "<disk id>", required = true)
    private List<String> diskIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "disk-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Disk disk = Disk.getMachineDiskByReference(cimiClient, this.diskIds.get(0), this.showParams.getQueryParams());
        DiskShowCommand.printDisk(disk, this.showParams);
    }

    public static void printDisk(final Disk disk, final ResourceSelectExpandParams showParams) throws CimiClientException {
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

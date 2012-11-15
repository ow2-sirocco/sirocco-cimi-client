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
import org.ow2.sirocco.apis.rest.cimi.sdk.VolumeTemplate;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show volume template")
public class VolumeTemplateShowCommand implements Command {
    @Parameter(names = "-id", description = "id of the volume template", required = true)
    private String volumeTemplateId;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "volumetemplate-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiException {
        VolumeTemplate volumeTemplate = VolumeTemplate.getVolumeTemplateByReference(cimiClient, this.volumeTemplateId,
            this.showParams.getQueryParams());
        VolumeTemplateShowCommand.printVolumeTemplate(volumeTemplate, this.showParams);
    }

    public static void printVolumeTemplate(final VolumeTemplate volumeTemplate, final ResourceSelectExpandParams showParams)
        throws CimiException {
        Table table = CommandHelper.createResourceShowTable(volumeTemplate, showParams);

        if (showParams.isSelected("volumeConfig")) {
            table.addCell("volume config");
            table.addCell(volumeTemplate.getVolumeConfig().getId());
        }

        if (showParams.isSelected("volumeImage")) {
            table.addCell("volume image");
            if (volumeTemplate.getVolumeImage() != null) {
                table.addCell(volumeTemplate.getVolumeImage().getId());
            } else {
                table.addCell("");
            }
        }

        System.out.println(table.render());
    }

}

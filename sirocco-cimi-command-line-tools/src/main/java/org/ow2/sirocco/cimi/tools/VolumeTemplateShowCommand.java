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
import org.ow2.sirocco.cimi.sdk.VolumeTemplate;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show volume template")
public class VolumeTemplateShowCommand implements Command {
    @Parameter(description = "<volume template id>", required = true)
    private List<String> volumeTemplateIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "volumetemplate-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        VolumeTemplate volumeTemplate = VolumeTemplate.getVolumeTemplateByReference(cimiClient, this.volumeTemplateIds.get(0),
            this.showParams.getQueryParams());
        VolumeTemplateShowCommand.printVolumeTemplate(volumeTemplate, this.showParams);
    }

    public static void printVolumeTemplate(final VolumeTemplate volumeTemplate, final ResourceSelectExpandParams showParams)
        throws CimiClientException {
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

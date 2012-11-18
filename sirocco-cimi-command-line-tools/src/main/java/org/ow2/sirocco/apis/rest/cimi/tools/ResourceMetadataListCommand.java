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
 *
 */
package org.ow2.sirocco.apis.rest.cimi.tools;

import java.util.List;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClientException;
import org.ow2.sirocco.apis.rest.cimi.sdk.ResourceMetadata;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list resource metadata")
public class ResourceMetadataListCommand implements Command {
    public static String COMMAND_NAME = "metadata-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "typeURI", "name");

    @Override
    public String getName() {
        return ResourceMetadataListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        List<ResourceMetadata> metadatas = ResourceMetadata.getResourceMetadatas(cimiClient, this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "typeURI", "name");

        for (ResourceMetadata metadata : metadatas) {
            if (this.listParams.isSelected("id")) {
                table.addCell(metadata.getId());
            }
            if (this.listParams.isSelected("typeURI")) {
                table.addCell(metadata.getTypeURI());
            }
            if (this.listParams.isSelected("name")) {
                table.addCell(metadata.getName());
            }
        }
        System.out.println(table.render());
    }
}

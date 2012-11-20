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
 */
package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.nocrala.tools.texttablefmt.Table;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.ResourceMetadata;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "show resource metadata")
public class ResourceMetadataShowCommand implements Command {
    @Parameter(description = "<resource metadata id>", required = true)
    private List<String> metadataIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "metadata-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        ResourceMetadata disk = ResourceMetadata.getResourceMetadataByReference(cimiClient, this.metadataIds.get(0),
            this.showParams.getQueryParams());
        ResourceMetadataShowCommand.printResourceMetadata(disk, this.showParams);
    }

    public static void printResourceMetadata(final ResourceMetadata metadata, final ResourceSelectExpandParams showParams)
        throws CimiClientException {
        Table table = new Table(2);
        table.addCell("Attribute");
        table.addCell("Value");

        if (showParams.isSelected("id")) {
            table.addCell("id");
            table.addCell(metadata.getId());
        }

        if (showParams.isSelected("typeURI")) {
            table.addCell("typeURI");
            table.addCell(metadata.getTypeURI());
        }
        if (showParams.isSelected("name")) {
            table.addCell("name");
            table.addCell(metadata.getName());
        }
        if (showParams.isSelected("attributes")) {
            for (ResourceMetadata.AttributeMetadata attributeMetadata : metadata.getAttributes()) {
                table.addCell("attribute metadata");
                StringBuilder sb = new StringBuilder();
                sb.append("name=" + attributeMetadata.getName());
                if (attributeMetadata.getType() != null) {
                    sb.append(" type=" + attributeMetadata.getType());
                }
                if (attributeMetadata.getNamespace() != null) {
                    sb.append(" namespace=" + attributeMetadata.getNamespace());
                }
                sb.append(" required=" + attributeMetadata.isRequired());
                table.addCell(sb.toString());
            }
        }

        System.out.println(table.render());
    }
}

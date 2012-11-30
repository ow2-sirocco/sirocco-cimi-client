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

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.MachineTemplate;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "delete machine template")
public class MachineTemplateDeleteCommand implements Command {
    @Parameter(description = "<machine template id>", required = true)
    private List<String> machineTemplateIds;

    @Override
    public String getName() {
        return "machinetemplate-delete";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        MachineTemplate machineTemplate;
        if (CommandHelper.isResourceIdentifier(this.machineTemplateIds.get(0))) {
            machineTemplate = MachineTemplate.getMachineTemplateByReference(cimiClient, this.machineTemplateIds.get(0));
        } else {
            List<MachineTemplate> templates = MachineTemplate.getMachineTemplates(cimiClient,
                QueryParams.builder().filter("name='" + this.machineTemplateIds.get(0) + "'").build());
            if (templates.isEmpty()) {
                System.err.println("No machine template with name " + this.machineTemplateIds.get(0));
                System.exit(-1);
            }
            machineTemplate = templates.get(0);
        }
        machineTemplate.delete();
        System.out.println("MachineTemplate " + this.machineTemplateIds.get(0) + " deleted");
    }
}

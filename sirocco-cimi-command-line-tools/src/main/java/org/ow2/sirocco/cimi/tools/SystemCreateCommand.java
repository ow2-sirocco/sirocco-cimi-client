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
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.QueryParams;
import org.ow2.sirocco.cimi.sdk.System;
import org.ow2.sirocco.cimi.sdk.SystemCreate;
import org.ow2.sirocco.cimi.sdk.SystemTemplate;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create system")
public class SystemCreateCommand implements Command {
    @Parameter(names = "-template", description = "id of the template", required = true)
    private String templateId;

    @Parameter(names = "-name", description = "name of the template", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the template", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Override
    public String getName() {
        return "system-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        SystemCreate systemCreate = new SystemCreate();
        if (!CommandHelper.isResourceIdentifier(this.templateId)) {
            List<SystemTemplate> templates = SystemTemplate.getSystemTemplates(cimiClient,
                QueryParams.builder().filter("name='" + this.templateId + "'").select("id").build());
            if (templates.isEmpty()) {
                java.lang.System.err.println("No system template with name " + this.templateId);
                java.lang.System.exit(-1);
            }
            this.templateId = templates.get(0).getId();
        }
        systemCreate.setSystemTemplateRef(this.templateId);
        systemCreate.setName(this.name);
        systemCreate.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                systemCreate.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        CreateResult<System> result = System.createSystem(cimiClient, systemCreate);
        if (result.getJob() != null) {
            java.lang.System.out.println("Job:");
            JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
        }
        java.lang.System.out.println("System being created:");
        SystemShowCommand.printSystem(result.getResource(), new ResourceSelectExpandParams());
    }
}

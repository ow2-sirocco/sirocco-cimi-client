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
import org.ow2.sirocco.cimi.sdk.Job;
import org.ow2.sirocco.cimi.sdk.QueryParams;
import org.ow2.sirocco.cimi.sdk.System;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "stop system")
public class SystemStopCommand implements Command {
    @Parameter(description = "<system id>", required = true)
    private List<String> systemIds;

    @Override
    public String getName() {
        return "system-stop";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        System system;
        if (CommandHelper.isResourceIdentifier(this.systemIds.get(0))) {
            system = System.getSystemByReference(cimiClient, this.systemIds.get(0));
        } else {
            List<System> systems = System.getSystems(cimiClient,
                QueryParams.builder().filter("name='" + this.systemIds.get(0) + "'").build());
            if (systems.isEmpty()) {
                java.lang.System.err.println("No system with name " + this.systemIds.get(0));
                java.lang.System.exit(-1);
            }
            system = systems.get(0);
        }
        Job job = system.stop();
        java.lang.System.out.println("Stopping system " + this.systemIds.get(0));
        if (job != null) {
            java.lang.System.out.println("Job:");
            JobShowCommand.printJob(job, new ResourceSelectExpandParams());
        }
    }
}

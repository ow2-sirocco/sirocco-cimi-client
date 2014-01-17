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
import org.ow2.sirocco.cimi.sdk.ForwardingGroup;
import org.ow2.sirocco.cimi.sdk.Job;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "delete forwarding group")
public class ForwardingGroupDeleteCommand implements Command {
    @Parameter(description = "<forwarding group id>", required = true)
    private List<String> fgIds;

    @Override
    public String getName() {
        return "forwarding-group-delete";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        ForwardingGroup fg;
        if (CommandHelper.isResourceIdentifier(this.fgIds.get(0))) {
            fg = ForwardingGroup.getForwardingGroupByReference(cimiClient, this.fgIds.get(0));
        } else {
            List<ForwardingGroup> fgs = ForwardingGroup.getForwardingGroups(cimiClient,
                QueryParams.builder().filter("name='" + this.fgIds.get(0) + "'").build());
            if (fgs.isEmpty()) {
                System.err.println("No forwarding group with name " + this.fgIds.get(0));
                System.exit(-1);
            }
            fg = fgs.get(0);
        }

        Job job = fg.delete();
        System.out.println("ForwardingGroup " + this.fgIds.get(0) + " being deleted");
        if (job != null) {
            System.out.println("Job:");
            JobShowCommand.printJob(job, new ResourceSelectExpandParams());
        }
    }
}

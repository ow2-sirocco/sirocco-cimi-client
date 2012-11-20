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
import org.ow2.sirocco.cimi.sdk.Job;

import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "list jobs")
public class JobListCommand implements Command {
    public static String COMMAND_NAME = "job-list";

    @ParametersDelegate
    private ResourceListParams listParams = new ResourceListParams("id", "name", "state", "action", "targetResource");

    @Override
    public String getName() {
        return JobListCommand.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws Exception {
        List<Job> jobs = Job.getJobs(cimiClient, this.listParams.getQueryParams());

        Table table = CommandHelper.createResourceListTable(this.listParams, "id", "name", "description", "created", "updated",
            "properties", "state", "targetResource", "action", "statusMessage", "timeOfStatusChange");

        for (Job job : jobs) {
            CommandHelper.printResourceCommonAttributes(table, job, this.listParams);
            if (this.listParams.isSelected("state")) {
                table.addCell(job.getState().toString());
            }
            if (this.listParams.isSelected("targetResource")) {
                table.addCell(job.getTargetResourceRef());
            }
            if (this.listParams.isSelected("action")) {
                table.addCell(job.getAction());
            }
            if (this.listParams.isSelected("statusMessage")) {
                table.addCell(job.getStatusMessage());
            }
            if (this.listParams.isSelected("timeOfStatusChange")) {
                if (job.getTimeOfStatusChange() != null) {
                    table.addCell(job.getTimeOfStatusChange().toString());
                } else {
                    table.addCell("");
                }
            }
        }
        System.out.println(table.render());
    }

}

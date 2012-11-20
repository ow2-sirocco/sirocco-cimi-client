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
import org.ow2.sirocco.cimi.sdk.Job;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParametersDelegate;

@Parameters(commandDescription = "job machine")
public class JobShowCommand implements Command {
    @Parameter(description = "<job id>", required = true)
    private List<String> jobIds;

    @ParametersDelegate
    private ResourceSelectExpandParams showParams = new ResourceSelectExpandParams();

    @Override
    public String getName() {
        return "job-show";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws Exception {
        Job job = Job.getJobByReference(cimiClient, this.jobIds.get(0), this.showParams.getQueryParams());
        JobShowCommand.printJob(job, this.showParams);
    }

    public static void printJob(final Job job, final ResourceSelectExpandParams showParams) throws CimiClientException {
        Table table = CommandHelper.createResourceShowTable(job, showParams);

        if (showParams.isSelected("state") && job.getState() != null) {
            table.addCell("state");
            table.addCell(job.getState().toString());
        }
        if (showParams.isSelected("action") && job.getAction() != null) {
            table.addCell("action");
            table.addCell(job.getAction());
        }
        if (showParams.isSelected("targetResource") && job.getTargetResourceRef() != null) {
            table.addCell("target resource");
            table.addCell(job.getTargetResourceRef());
        }
        if (showParams.isSelected("affectedResources") && job.getAffectedResourceRefs() != null
            && job.getAffectedResourceRefs().length > 0) {
            table.addCell("affected resources");
            table.addCell(job.getAffectedResourceRefs()[0]);
        }
        if (showParams.isSelected("statusMessage") && job.getStatusMessage() != null) {
            table.addCell("status message");
            table.addCell(job.getStatusMessage());
        }
        if (showParams.isSelected("timeOfStatusChange") && job.getTimeOfStatusChange() != null) {
            table.addCell("time of status change");
            table.addCell(job.getTimeOfStatusChange().toString());
        }

        System.out.println(table.render());
    }

}

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

package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiJobCollectionRoot;

/**
 * Task performed by a CIMI provider.
 */
public class Job extends Resource<CimiJob> {

    /** The default poll period in seconds. */
    public final int DEFAULT_POLL_PERIOD_IN_SECONDS = 10;

    /**
     * Job status.
     */
    public static enum State {
        RUNNING, SUCCESS, FAILED, CANCELLED
    };

    Job(final CimiClient cimiClient, final CimiJob cimiJob) {
        super(cimiClient, cimiJob);
    }

    /**
     * State of the job.
     * 
     * @return the state
     */
    public State getState() {
        if (this.cimiObject.getStatus() != null) {
            return State.valueOf(this.cimiObject.getStatus());
        } else {
            return null;
        }
    }

    /**
     * A reference to the top-level resource upon which the operation is being
     * performed.
     * 
     * @return the target resource ref
     */
    public String getTargetResourceRef() {
        if (this.cimiObject.getTargetResource() != null) {
            return this.cimiObject.getTargetResource().getHref();
        } else {
            return null;
        }
    }

    /**
     * A list of references to resources that have been impacted by this Job.
     * 
     * @return the affected resource refs
     */
    public String[] getAffectedResourceRefs() {
        String result[] = new String[this.cimiObject.getAffectedResources() != null ? this.cimiObject.getAffectedResources().length
            : 0];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.cimiObject.getAffectedResources()[i].getHref();
        }
        return result;
    }

    /**
     * Type of action being performed.
     * 
     * @return the action
     */
    public String getAction() {
        return this.cimiObject.getAction();
    }

    /**
     * Human-readable string that provides information about the status of the
     * Job.
     * 
     * @return the status message
     */
    public String getStatusMessage() {
        return this.cimiObject.getStatusMessage();
    }

    /**
     * Last time that the status of the Job changed.
     * 
     * @return the time of status change
     */
    public Date getTimeOfStatusChange() {
        return this.cimiObject.getTimeOfStatusChange();
    }

    /**
     * Operation return code.
     * 
     * @return the return code
     */
    public Integer getReturnCode() {
        return this.cimiObject.getReturnCode();
    }

    /**
     * An integer value in the range 0 ... 100 that indicates the progress of
     * this Job
     * 
     * @return the progress
     */
    public Integer getProgress() {
        return this.cimiObject.getProgress();
    }

    /**
     * Waits for the current Job to be completed with a given polling period.
     * This method polls until which ever happens first: (1) the timeout
     * happens, (2) the Job is no longer running
     * 
     * @param timeout timeout until which this method polls for the Job status
     * @param period subsequent wait for Job checks take place at approximately
     *        regular intervals separated by the specified period
     * @param unit time unit in which timeout and period are specified
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     * @throws TimeoutException if the timeout happens
     * @throws InterruptedException if the method is interrupted
     */
    public void waitForCompletion(final long timeout, final long period, final TimeUnit unit) throws CimiClientException,
        CimiProviderException, TimeoutException, InterruptedException {
        long endTime = java.lang.System.nanoTime() + unit.toNanos(timeout);
        long periodInMilliseconds = TimeUnit.MILLISECONDS.convert(period, unit);
        while (true) {
            this.cimiObject = this.cimiClient.getCimiObjectByReference(this.getId(), CimiJob.class);
            if (this.getState() != Job.State.RUNNING) {
                break;
            }
            if (java.lang.System.nanoTime() > endTime) {
                throw new TimeoutException();
            }
            Thread.sleep(periodInMilliseconds);
        }
    }

    /**
     * Waits for the current Job to be completed. This method polls until which
     * ever happens first: (1) the timeout happens, (2) the Job is no longer
     * running
     * 
     * @param timeout timeout until which this method polls for the Job status
     * @param unit time unit in which the timeout is specified
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     * @throws TimeoutException if the timeout happens
     * @throws InterruptedException if the method is interrupted
     */
    public void waitForCompletion(final long timeout, final TimeUnit unit) throws CimiClientException, CimiProviderException,
        TimeoutException, InterruptedException {
        long period = unit.convert(this.DEFAULT_POLL_PERIOD_IN_SECONDS, TimeUnit.SECONDS);
        this.waitForCompletion(timeout, period, unit);
    }

    /**
     * Retrieves the collection of Jobs.
     * 
     * @param client client handle
     * @param queryParams optional query parameters
     * @return a list of Job resources
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static List<Job> getJobs(final CimiClient client, final QueryParams... queryParams) throws CimiClientException,
        CimiProviderException {
        if (client.cloudEntryPoint.getJobs() == null) {
            throw new CimiClientException("Unsupported operation");
        }
        org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiJobCollection jobCollection = client.getRequest(
            client.extractPath(client.cloudEntryPoint.getJobs().getHref()), CimiJobCollectionRoot.class, queryParams);

        List<Job> result = new ArrayList<Job>();

        if (jobCollection.getCollection() != null) {
            for (CimiJob cimiJob : jobCollection.getCollection().getArray()) {
                result.add(new Job(client, cimiJob));
            }
        }
        return result;
    }

    /**
     * Retrieves a Job resource from its reference.
     * 
     * @param client client handle
     * @param ref reference to the Job
     * @param queryParams optional query parameters
     * @return the Job resource
     * @throws CimiClientException If any internal errors are encountered inside
     *         the client while attempting to make the request or handle the
     *         response. For example if a network connection is not available.
     * @throws CimiProviderException If an error response is returned by the
     *         CIMI provider indicating either a problem with the data in the
     *         request, or a server side issue.
     */
    public static Job getJobByReference(final CimiClient client, final String id, final QueryParams... queryParams)
        throws CimiClientException, CimiProviderException {
        return new Job(client, client.getCimiObjectByReference(id, CimiJob.class, queryParams));
    }

}

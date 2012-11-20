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
 *  $Id$
 *
 */
package org.ow2.sirocco.cimi.sdk;

/**
 * Helper class representing the result of a CIMI resource creation.
 * 
 * @param <E> the element type
 */
public class CreateResult<E> {
    final Job job;

    final E resource;

    /**
     * Instantiates a new creates the result.
     * 
     * @param job the job
     * @param resource the resource
     */
    public CreateResult(final Job job, final E resource) {
        super();
        this.job = job;
        this.resource = resource;
    }

    /**
     * Gets the job.
     * 
     * @return the job representing this operation or null if the CIMI provider
     *         does not support Jobs
     */
    public Job getJob() {
        return this.job;
    }

    /**
     * Gets the created resource.
     * 
     * @return the created resource
     */
    public E getResource() {
        return this.resource;
    }

}

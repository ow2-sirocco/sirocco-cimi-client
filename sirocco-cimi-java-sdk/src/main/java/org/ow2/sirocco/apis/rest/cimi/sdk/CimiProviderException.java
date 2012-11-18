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
package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.Date;

/**
 * Represents an error response returned by a CIMI provider. Receiving an
 * exception of this type indicates that the caller's request was correctly
 * transmitted to the provider, but for some reason, the provider was not able
 * to process it, and returned an error response instead.
 */
public class CimiProviderException extends CimiClientException {
    private String statusMessage;

    private Integer returnCode;

    private Date timeOfStatusChange;

    /**
     * Constructs a CimiProviderException with the specified detailed message
     * 
     * @param message the detail message
     */
    public CimiProviderException(final String message) {
        super(message);
        this.statusMessage = message;
    }

    /**
     * Constructs a CimiProviderException with the specified detail message and
     * cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public CimiProviderException(final String message, final Throwable cause) {
        super(message, cause);
        this.statusMessage = message;
    }

    /**
     * Human-readable string that provides information about the status of the
     * Job.
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * Last time that the status of the Job changed
     */
    public Date getTimeOfStatusChange() {
        return this.timeOfStatusChange;
    }

    /**
     * Operation return code
     */
    public Integer getReturnCode() {
        return this.returnCode;
    }

    void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    void setReturnCode(final Integer returnCode) {
        this.returnCode = returnCode;
    }

    void setTimeOfStatusChange(final Date timeOfStatusChange) {
        this.timeOfStatusChange = timeOfStatusChange;
    }

}

/**
 *
 * SIROCCO
 * Copyright (C) 2013 France Telecom
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
 *
 */
package org.ow2.sirocco.cimi.sdk;

import org.ow2.sirocco.cimi.domain.CimiSystemNetwork;

/**
 * Represents a Network within a System.
 */
public class SystemNetwork extends Resource<CimiSystemNetwork> {
    private Network network;

    /**
     * Instantiates a new system network.
     */
    public SystemNetwork() {
        super(null, new CimiSystemNetwork());
    }

    SystemNetwork(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiSystemNetwork());
        this.cimiObject.setHref(id);
    }

    SystemNetwork(final CimiClient cimiClient, final CimiSystemNetwork cimiObject) throws CimiClientException,
        CimiProviderException {
        super(cimiClient, cimiObject);
        this.network = Network.getNetworkByReference(cimiClient, cimiObject.getNetwork().getHref());
    }

    /**
     * Gets the network.
     * 
     * @return the network
     */
    public Network getNetwork() {
        return this.network;
    }
}

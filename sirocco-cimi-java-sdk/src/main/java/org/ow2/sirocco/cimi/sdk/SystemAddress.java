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

import org.ow2.sirocco.cimi.domain.CimiSystemAddress;

/**
 * Represents a Address within a System.
 */
public class SystemAddress extends Resource<CimiSystemAddress> {
    private Address address;

    /**
     * Instantiates a new system address.
     */
    public SystemAddress() {
        super(null, new CimiSystemAddress());
    }

    SystemAddress(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiSystemAddress());
        this.cimiObject.setHref(id);
    }

    SystemAddress(final CimiClient cimiClient, final CimiSystemAddress cimiObject) throws CimiClientException,
        CimiProviderException {
        super(cimiClient, cimiObject);
        this.address = Address.getAddressByReference(cimiClient, cimiObject.getAddress().getHref());
    }

    /**
     * Gets the address.
     * 
     * @return the address
     */
    public Address getAddress() {
        return this.address;
    }
}

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

import org.ow2.sirocco.cimi.domain.CimiSystemVolume;

/**
 * Represents a Volume within a System.
 */
public class SystemVolume extends Resource<CimiSystemVolume> {
    private Volume volume;

    /**
     * Instantiates a new system volume.
     */
    public SystemVolume() {
        super(null, new CimiSystemVolume());
    }

    SystemVolume(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiSystemVolume());
        this.cimiObject.setHref(id);
    }

    SystemVolume(final CimiClient cimiClient, final CimiSystemVolume cimiObject) throws CimiClientException,
        CimiProviderException {
        super(cimiClient, cimiObject);
        this.volume = Volume.getVolumeByReference(cimiClient, cimiObject.getVolume().getHref());
    }

    /**
     * Gets the volume.
     * 
     * @return the volume
     */
    public Volume getVolume() {
        return this.volume;
    }
}

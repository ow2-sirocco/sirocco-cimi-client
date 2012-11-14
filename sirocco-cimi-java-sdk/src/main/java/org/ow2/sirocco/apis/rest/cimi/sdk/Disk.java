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

package org.ow2.sirocco.apis.rest.cimi.sdk;

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.apis.rest.cimi.domain.CimiJob;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachineDisk;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineDiskCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineDiskCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient.CimiResult;

/**
 * Disk (local storage) of a Machine
 */
public class Disk extends Resource<CimiMachineDisk> {
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/Disk";

    public Disk() {
        super(null, new CimiMachineDisk());
    }

    Disk(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineDisk());
        this.cimiObject.setHref(id);
    }

    Disk(final CimiClient cimiClient, final CimiMachineDisk cimiMachineDisk) {
        super(cimiClient, cimiMachineDisk);
    }

    public int getCapacity() {
        return this.cimiObject.getCapacity();
    }

    public String getInitialLocation() {
        return this.cimiObject.getInitialLocation();
    }

    public Job delete() throws CimiException {
        String deleteRef = Helper.findOperation("delete", this.cimiObject);
        if (deleteRef == null) {
            throw new CimiException("Unsupported operation");
        }
        CimiJob job = this.cimiClient.deleteRequest(deleteRef);
        if (job != null) {
            return new Job(this.cimiClient, job);
        } else {
            return null;
        }
    }

    public static CreateResult<Disk> createMachineDisk(final CimiClient client, final String machineId, final Disk machineDisk)
        throws CimiException {
        Machine machine = Machine.getMachineByReference(client, machineId, QueryParams.build().setExpand("disks"));
        String addRef = Helper.findOperation("add", machine.cimiObject.getVolumes());
        if (addRef == null) {
            throw new CimiException("Unsupported operation");
        }
        CimiResult<CimiMachineDisk> result = client.postCreateRequest(addRef, machineDisk.cimiObject, CimiMachineDisk.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        Disk createdMachineDisk = result.getResource() != null ? new Disk(client, result.getResource()) : null;
        return new CreateResult<Disk>(job, createdMachineDisk);
    }

    public static List<Disk> getMachineDisks(final CimiClient client, final String machineId, final QueryParams... queryParams)
        throws CimiException {
        Machine machine = Machine.getMachineByReference(client, machineId);
        if (machine.cimiObject.getDisks() == null) {
            throw new CimiException("Unsupported operation");
        }

        CimiMachineDiskCollection machineDiskCollection = client.getRequest(
            client.extractPath(machine.cimiObject.getDisks().getHref()), CimiMachineDiskCollectionRoot.class, queryParams);
        List<Disk> result = new ArrayList<Disk>();

        if (machineDiskCollection.getCollection() != null) {
            for (CimiMachineDisk cimiMachineDisk : machineDiskCollection.getCollection().getArray()) {
                result.add(new Disk(client, cimiMachineDisk));
            }
        }
        return result;
    }

    public static Disk getMachineDiskByReference(final CimiClient client, final String ref, final QueryParams... queryParams)
        throws CimiException {
        return new Disk(client, client.getCimiObjectByReference(ref, CimiMachineDisk.class, queryParams));
    }

}

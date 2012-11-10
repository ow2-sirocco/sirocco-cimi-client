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
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachineNetworkInterface;
import org.ow2.sirocco.apis.rest.cimi.domain.CimiMachineNetworkInterfaceAddress;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineNetworkInterfaceCollection;
import org.ow2.sirocco.apis.rest.cimi.domain.collection.CimiMachineNetworkInterfaceCollectionRoot;
import org.ow2.sirocco.apis.rest.cimi.sdk.CimiClient.CimiResult;

/**
 * Network interface of a Machine
 */
public class MachineNetworkInterface extends Resource<CimiMachineNetworkInterface> {
    public static final String TYPE_URI = "http://schemas.dmtf.org/cimi/1/MachineNetworkInterface";

    /**
     * NIC state
     */
    public static enum State {
        Active, Passive, Disabled
    }

    /**
     * NIC type
     */
    public static enum Type {
        PUBLIC, PRIVATE
    }

    public MachineNetworkInterface() {
        super(null, new CimiMachineNetworkInterface());
    }

    MachineNetworkInterface(final CimiClient cimiClient, final String id) {
        super(cimiClient, new CimiMachineNetworkInterface());
        this.cimiObject.setHref(id);
    }

    MachineNetworkInterface(final CimiClient cimiClient, final CimiMachineNetworkInterface cimiMachineNetworkInterface) {
        super(cimiClient, cimiMachineNetworkInterface);
    }

    public List<Address> getAddresses() {
        List<Address> result = new ArrayList<Address>();
        if (this.cimiObject.getAddresses().getArray() != null) {
            for (CimiMachineNetworkInterfaceAddress addr : this.cimiObject.getAddresses().getArray()) {
                Address address = new Address(this.cimiClient, addr.getAddress());
                result.add(address);
            }
        }
        return result;
    }

    public Network getNetwork() {
        return this.cimiObject.getNetwork() != null ? new Network(this.cimiClient, this.cimiObject.getNetwork()) : null;
    }

    public State getState() {
        return State.valueOf(this.cimiObject.getState());
    }

    public Type getType() {
        return Type.valueOf(this.cimiObject.getNetworkType());
    }

    public String getMacAddress() {
        return this.cimiObject.getMacAddress();
    }

    public int getMtu() {
        return this.cimiObject.getMtu();
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

    public static CreateResult<MachineNetworkInterface> createMachineNetworkInterface(final CimiClient client,
        final String machineId, final MachineNetworkInterface machineNetworkInterface) throws CimiException {
        Machine machine = Machine.getMachineByReference(client, machineId, QueryParams.build().setExpand("networkInterfaces"));
        String addRef = Helper.findOperation("add", machine.cimiObject.getNetworkInterfaces());
        if (addRef == null) {
            throw new CimiException("Unsupported operation");
        }
        CimiResult<CimiMachineNetworkInterface> result = client.postCreateRequest(addRef, machineNetworkInterface.cimiObject,
            CimiMachineNetworkInterface.class);
        Job job = result.getJob() != null ? new Job(client, result.getJob()) : null;
        MachineNetworkInterface createdMachineNetworkInterface = result.getResource() != null ? new MachineNetworkInterface(
            client, result.getResource()) : null;
        return new CreateResult<MachineNetworkInterface>(job, createdMachineNetworkInterface);
    }

    public static List<MachineNetworkInterface> getMachineNetworkInterfaces(final CimiClient client, final String machineId,
        final QueryParams queryParams) throws CimiException {
        Machine machine = Machine.getMachineByReference(client, machineId);
        if (machine.cimiObject.getNetworkInterfaces() == null) {
            throw new CimiException("Unsupported operation");
        }

        CimiMachineNetworkInterfaceCollection machineNetworkInterfaceCollection = client.getRequest(
            client.extractPath(machine.cimiObject.getNetworkInterfaces().getHref()),
            CimiMachineNetworkInterfaceCollectionRoot.class, queryParams.setExpand("addresses"));
        List<MachineNetworkInterface> result = new ArrayList<MachineNetworkInterface>();

        if (machineNetworkInterfaceCollection.getCollection() != null) {
            for (CimiMachineNetworkInterface cimiMachineNetworkInterface : machineNetworkInterfaceCollection.getCollection()
                .getArray()) {
                result.add(new MachineNetworkInterface(client, cimiMachineNetworkInterface));
            }
        }
        return result;
    }

    public static MachineNetworkInterface getMachineNetworkInterfaceByReference(final CimiClient client, final String ref)
        throws CimiException {
        return new MachineNetworkInterface(client, client.getCimiObjectByReference(ref, CimiMachineNetworkInterface.class,
            QueryParams.build().setExpand("addresses")));
    }

}

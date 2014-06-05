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
 */
package org.ow2.sirocco.cimi.tools;

import org.ow2.sirocco.cimi.sdk.Address;
import org.ow2.sirocco.cimi.sdk.AddressCreate;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "allocate ip address")
public class AddressCreateCommand implements Command {
    @Parameter(names = "-providerAccountId", description = "id of the provider account", required = true)
    private String providerAccountId;

    @Parameter(names = "-location", description = "location", required = false)
    private String location;

    @Parameter(names = "-v", description = "verbose", required = false)
    private boolean verbose;

    @Override
    public String getName() {
        return "Address-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        AddressCreate AddressCreate = new AddressCreate();

        AddressCreate.setProviderAccountId(this.providerAccountId);
        AddressCreate.setLocation(this.location);
        CreateResult<Address> result = Address.createAddress(cimiClient, AddressCreate);
        if (this.verbose) {
            if (result.getJob() != null) {
                System.out.println("Job:");
                JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
            }
            System.out.println("Address being created:");
            AddressShowCommand.printAddress(result.getResource(), new ResourceSelectExpandParams());
        } else {
            System.out.println(result.getResource().getId());
        }
    }
}

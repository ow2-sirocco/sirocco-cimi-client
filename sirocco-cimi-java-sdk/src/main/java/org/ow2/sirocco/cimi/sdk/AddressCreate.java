package org.ow2.sirocco.cimi.sdk;

import org.ow2.sirocco.cimi.domain.CimiAddressCreate;

public class AddressCreate {
    CimiAddressCreate cimiAddressCreate;

    /**
     * Instantiates a new address create.
     */
    public AddressCreate() {
        this.cimiAddressCreate = new CimiAddressCreate();
    }

    /**
     * Sets the provider account id where the resource will be created
     */
    public void setProviderAccountId(final String providerAccountId) {
        this.cimiAddressCreate.setProviderAccountId(providerAccountId);
    }

    /**
     * Gets the location constraint
     */
    public String getLocation() {
        return this.cimiAddressCreate.getLocation();
    }

    /**
     * Sets the location constraint. If null, the resource will be placed on any
     * location available to the provider account.
     */
    public void setLocation(final String location) {
        this.cimiAddressCreate.setLocation(location);
    }
}

package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.ow2.sirocco.cimi.sdk.Address;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "delete address")
public class AddressDeleteCommand implements Command {
    @Parameter(description = "<address id>", required = true)
    private List<String> addressIds;

    @Override
    public String getName() {
        return "address-delete";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Address address;
        if (CommandHelper.isResourceIdentifier(this.addressIds.get(0))) {
            address = Address.getAddressByReference(cimiClient, this.addressIds.get(0));
        } else {
            List<Address> addresses = Address.getAddresses(cimiClient,
                QueryParams.builder().filter("name='" + this.addressIds.get(0) + "'").build());
            if (addresses.isEmpty()) {
                java.lang.System.err.println("No address with name " + this.addressIds.get(0));
                java.lang.System.exit(-1);
            }
            address = addresses.get(0);
        }
        address.delete();
        System.out.println("Address " + this.addressIds.get(0) + " deleted");
    }
}

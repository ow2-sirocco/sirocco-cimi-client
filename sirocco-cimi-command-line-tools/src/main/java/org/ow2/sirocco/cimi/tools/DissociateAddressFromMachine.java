package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "dissociate an IP address from a machine")
public class DissociateAddressFromMachine implements Command {
    public static String COMMAND_NAME = "address-dissociate";

    @Parameter(names = "-machine", description = "<machine id>", required = true)
    private List<String> machineIds;

    @Parameter(description = "<address id>", required = true)
    private List<String> addressIds;

    @Override
    public String getName() {
        return DissociateAddressFromMachine.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        // TODO
    }
}

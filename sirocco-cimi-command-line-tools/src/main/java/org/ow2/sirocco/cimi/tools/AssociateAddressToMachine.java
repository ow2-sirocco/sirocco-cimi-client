package org.ow2.sirocco.cimi.tools;

import java.util.List;

import org.ow2.sirocco.cimi.sdk.Address;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.Machine;
import org.ow2.sirocco.cimi.sdk.MachineNetworkInterface;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "associate an IP address to a machine")
public class AssociateAddressToMachine implements Command {
    public static String COMMAND_NAME = "address-associate";

    @Parameter(names = "-machine", description = "<machine id>", required = true)
    private List<String> machineIds;

    @Parameter(description = "<address id>", required = true)
    private List<String> addressIds;

    @Override
    public String getName() {
        return AssociateAddressToMachine.COMMAND_NAME;
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        Machine machine;
        if (CommandHelper.isResourceIdentifier(this.machineIds.get(0))) {
            machine = Machine.getMachineByReference(cimiClient, this.machineIds.get(0));
        } else {
            List<Machine> machines = Machine.getMachines(cimiClient,
                QueryParams.builder().filter("name='" + this.machineIds.get(0) + "'").build());
            if (machines.isEmpty()) {
                System.err.println("No machine with name " + this.machineIds.get(0));
                System.exit(-1);
            }
            machine = machines.get(0);
        }
        if (machine.getNetworkInterfaces().isEmpty()) {
            System.err.println("Machine has no network inerface");
            System.exit(-1);
        }
        MachineNetworkInterface nic = machine.getNetworkInterfaces().get(0);
        Address address = Address.getAddressByReference(cimiClient, this.addressIds.get(0));
        nic.addAddress(address);
    }
}

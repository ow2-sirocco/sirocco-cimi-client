/**
 *
 * SIROCCO
 * Copyright (C) 2011 France Telecom
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
package org.ow2.sirocco.cimi.tools;

import javax.ws.rs.core.MediaType;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClient.Options;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CimiProviderException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class Client {
    private static final String CIMI_USERNAME_ENV_NAME = "CIMICLIENT_USERNAME";

    private static final String CIMI_PASSWORD_ENV_NAME = "CIMICLIENT_PASSWORD";

    private static final String CIMI_TENANT_ID_ENV_NAME = "CIMICLIENT_TENANT_ID";

    private static final String CIMI_TENANT_NAME_ENV_NAME = "CIMICLIENT_TENANT_NAME";

    private static final String CIMI_ENDPOINT_URL_ENV_NAME = "CIMICLIENT_ENDPOINT_URL";

    private static final String SIROCCO_USERNAME_ENV_NAME = "SIROCCO_USERNAME";

    private static final String SIROCCO_PASSWORD_ENV_NAME = "SIROCCO_PASSWORD";

    private static final String SIROCCO_TENANT_ID_ENV_NAME = "SIROCCO_TENANT_ID";

    private static final String SIROCCO_TENANT_NAME_ENV_NAME = "SIROCCO_TENANT_NAME";

    private static final String SIROCCO_ENDPOINT_URL_ENV_NAME = "SIROCCO_ENDPOINT_URL";

    @Parameter(names = "-debug", description = "turn on debug mode", required = false)
    private boolean debug;

    @Parameter(names = "-xml", description = "xml serialization", required = false)
    private boolean xml;

    private static Command commands[] = {new MachineCreateCommand(), new MachineShowCommand(), new MachineListCommand(),
        new MachineDeleteCommand(), new MachineUpdateCommand(), new MachineStartCommand(), new MachineStopCommand(),
        new MachineRestartCommand(), new MachineCaptureCommand(), new MachineVolumeCreateCommand(),
        new MachineVolumeDeleteCommand(), new MachineVolumeListCommand(), new MachineVolumeShowCommand(),
        new DiskListCommand(), new DiskShowCommand(), new MachineNetworkInterfaceListCommand(),
        new MachineNetworkInterfaceShowCommand(), new MachineImageCreateCommand(), new MachineImageUpdateCommand(),
        new MachineImageShowCommand(), new MachineImageListCommand(), new MachineImageDeleteCommand(),
        new MachineConfigCreateCommand(), new MachineConfigUpdateCommand(), new MachineConfigShowCommand(),
        new MachineConfigListCommand(), new MachineConfigDeleteCommand(), new MachineTemplateCreateCommand(),
        new MachineTemplateShowCommand(), new MachineTemplateListCommand(), new MachineTemplateDeleteCommand(),
        new MachineTemplateUpdateCommand(), new JobListCommand(), new JobShowCommand(), new CredentialCreateCommand(),
        new CredentialShowCommand(), new CredentialListCommand(), new CredentialDeleteCommand(),
        new VolumeConfigCreateCommand(), new VolumeConfigShowCommand(), new VolumeConfigListCommand(),
        new VolumeConfigDeleteCommand(), new VolumeTemplateCreateCommand(), new VolumeTemplateShowCommand(),
        new VolumeTemplateListCommand(), new VolumeTemplateDeleteCommand(), new VolumeCreateCommand(), new VolumeShowCommand(),
        new VolumeListCommand(), new VolumeDeleteCommand(), new SystemCreateCommand(), new SystemDeleteCommand(),
        new SystemListCommand(), new SystemShowCommand(), new SystemStartCommand(), new SystemStopCommand(),
        new SystemTemplateListCommand(), new SystemTemplateShowCommand(), new AddressCreateCommand(), new AddressListCommand(),
        new AddressShowCommand(), new AddressDeleteCommand(), new AssociateAddressToMachine(), new NetworkListCommand(),
        new NetworkShowCommand(), new NetworkCreateCommand(), new NetworkDeleteCommand(), new NetworkConfigShowCommand(),
        new NetworkConfigCreateCommand(), new NetworkConfigListCommand(), new ResourceMetadataListCommand(),
        new ResourceMetadataShowCommand(), new ForwardingGroupListCommand(), new ForwardingGroupCreateCommand(),
        new ForwardingGroupShowCommand(), new ForwardingGroupDeleteCommand()};

    private Client(final String[] args) {
        String userName = System.getenv(Client.CIMI_USERNAME_ENV_NAME);
        if (userName == null) {
            userName = System.getenv(Client.SIROCCO_USERNAME_ENV_NAME);
        }
        if (userName == null) {
            System.err.println(Client.SIROCCO_USERNAME_ENV_NAME + " environment variable not set");
            System.exit(1);
        }
        String password = System.getenv(Client.CIMI_PASSWORD_ENV_NAME);
        if (password == null) {
            password = System.getenv(Client.SIROCCO_PASSWORD_ENV_NAME);
        }
        if (password == null) {
            System.err.println(Client.SIROCCO_PASSWORD_ENV_NAME + " environment variable not set");
            System.exit(1);
        }
        String tenantId = System.getenv(Client.CIMI_TENANT_ID_ENV_NAME);
        String tenantName = System.getenv(Client.CIMI_TENANT_NAME_ENV_NAME);
        if (tenantId == null && tenantName == null) {
            tenantId = System.getenv(Client.SIROCCO_TENANT_ID_ENV_NAME);
            tenantName = System.getenv(Client.SIROCCO_TENANT_NAME_ENV_NAME);
        }
        if (tenantId == null && tenantName == null) {
            System.err.println(Client.SIROCCO_TENANT_ID_ENV_NAME + " or " + Client.SIROCCO_TENANT_NAME_ENV_NAME
                + " environment variables not set");
            System.exit(1);
        }
        String endpointUrl = System.getenv(Client.CIMI_ENDPOINT_URL_ENV_NAME);
        if (endpointUrl == null) {
            endpointUrl = System.getenv(Client.SIROCCO_ENDPOINT_URL_ENV_NAME);
            if (endpointUrl != null) {
                endpointUrl += "/cloudEntryPoint";
            }
        }
        if (endpointUrl == null) {
            System.err.println(Client.SIROCCO_ENDPOINT_URL_ENV_NAME + " environment variable not set");
            System.exit(1);
        }

        JCommander jCommander = new JCommander();
        jCommander.addObject(this);
        for (Command command : Client.commands) {
            jCommander.addCommand(command.getName(), command);
        }

        String commandName = null;
        try {
            jCommander.parse(args);
            commandName = jCommander.getParsedCommand();
            if (commandName == null) {
                // find command name if any
                for (String s : args) {
                    if (!s.startsWith("-")) {
                        commandName = s;
                        break;
                    }
                }
                if (commandName != null && jCommander.getCommands().get(commandName) == null) {
                    commandName = null;
                }
                this.printUsageAndExit(jCommander, commandName);
            }
            Command command = (Command) jCommander.getCommands().get(commandName).getObjects().get(0);

            Options options = Options.build();
            if (this.debug) {
                options.setDebug(true);
            }
            if (this.xml) {
                options.setMediaType(MediaType.APPLICATION_XML_TYPE);
            } else {
                options.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            }
            CimiClient cimiClient = CimiClient.login(endpointUrl, userName, password, tenantId, tenantName, options);

            command.execute(cimiClient);
        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            // find command name if any
            for (String s : args) {
                if (!s.startsWith("-")) {
                    commandName = s;
                    break;
                }
            }
            if (commandName != null && jCommander.getCommands().get(commandName) == null) {
                commandName = null;
            }
            this.printUsageAndExit(jCommander, commandName);
        } catch (CimiProviderException ex) {
            System.out.println(ex.getStatusMessage());
            System.exit(1);
        } catch (CimiClientException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

    }

    private void printUsageAndExit(final JCommander jCommander, final String commandName) {
        StringBuilder sb = new StringBuilder();
        if (commandName != null) {
            jCommander.usage(commandName, sb);
        } else {
            jCommander.usage(sb);
        }
        System.out.println(sb.toString().replaceFirst("<main class>", "sirocco"));
        System.exit(1);
    }

    public static void main(final String[] args) {
        new Client(args);
    }

}

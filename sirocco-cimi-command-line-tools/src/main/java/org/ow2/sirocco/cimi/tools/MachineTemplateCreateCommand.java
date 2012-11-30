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

import java.util.ArrayList;
import java.util.List;

import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClientException;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.Credential;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineImage;
import org.ow2.sirocco.cimi.sdk.MachineTemplate;
import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "create machine template")
public class MachineTemplateCreateCommand implements Command {
    @Parameter(names = "-name", description = "name of the template", required = false)
    private String name;

    @Parameter(names = "-description", description = "description of the template", required = false)
    private String description;

    @Parameter(names = "-properties", variableArity = true, description = "key value pairs", required = false)
    private List<String> properties;

    @Parameter(names = "-config", description = "machine config id", required = true)
    private String machineConfigId;

    @Parameter(names = "-image", description = "machine image id", required = true)
    private String machineImageId;

    @Parameter(names = "-cred", description = "credential id", required = false)
    private String credentialId;

    @Parameter(names = "-nic", description = "network interface", required = false)
    private List<String> nicTypes;

    @Override
    public String getName() {
        return "machinetemplate-create";
    }

    @Override
    public void execute(final CimiClient cimiClient) throws CimiClientException {
        MachineTemplate machineTemplate = new MachineTemplate();
        machineTemplate.setName(this.name);
        machineTemplate.setDescription(this.description);
        if (this.properties != null) {
            for (int i = 0; i < this.properties.size() / 2; i++) {
                machineTemplate.addProperty(this.properties.get(i * 2), this.properties.get(i * 2 + 1));
            }
        }
        if (!CommandHelper.isResourceIdentifier(this.machineConfigId)) {
            List<MachineConfiguration> configs = MachineConfiguration.getMachineConfigurations(cimiClient, QueryParams
                .builder().filter("name='" + this.machineConfigId + "'").select("id").build());
            if (configs.isEmpty()) {
                System.err.println("No machine config with name " + this.machineConfigId);
                System.exit(-1);
            }
            this.machineConfigId = configs.get(0).getId();
        }
        machineTemplate.setMachineConfigRef(this.machineConfigId);
        if (!CommandHelper.isResourceIdentifier(this.machineImageId)) {
            List<MachineImage> images = MachineImage.getMachineImages(cimiClient,
                QueryParams.builder().filter("name='" + this.machineImageId + "'").select("id").build());
            if (images.isEmpty()) {
                System.err.println("No machine image with name " + this.machineImageId);
                System.exit(-1);
            }
            this.machineImageId = images.get(0).getId();
        }
        machineTemplate.setMachineImageRef(this.machineImageId);
        List<MachineTemplate.NetworkInterface> nics = new ArrayList<MachineTemplate.NetworkInterface>();
        if (this.nicTypes != null) {
            for (String nicType : this.nicTypes) {
                MachineTemplate.NetworkInterface nic = new MachineTemplate.NetworkInterface();
                nic.setNetworkType(nicType);
                nics.add(nic);
            }
        }
        machineTemplate.setNetworkInterface(nics);

        if (this.credentialId != null) {
            if (!CommandHelper.isResourceIdentifier(this.credentialId)) {
                List<Credential> creds = Credential.getCredentials(cimiClient,
                    QueryParams.builder().filter("name='" + this.credentialId + "'").select("id").build());
                if (creds.isEmpty()) {
                    System.err.println("No credential with name " + this.credentialId);
                    System.exit(-1);
                }
                this.credentialId = creds.get(0).getId();
            }
            machineTemplate.setCredentialRef(this.credentialId);
        }

        CreateResult<MachineTemplate> result = MachineTemplate.createMachineTemplate(cimiClient, machineTemplate);
        if (result.getJob() != null) {
            System.out.println("Job:");
            JobShowCommand.printJob(result.getJob(), new ResourceSelectExpandParams());
        }
        System.out.println("Machine Template:");
        MachineTemplateShowCommand.printMachineTemplate(result.getResource(), new ResourceSelectExpandParams());
    }

}

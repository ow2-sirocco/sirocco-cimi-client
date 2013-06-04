package org.ow2.sirocco.cimi.sdk.itest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClient.Options;
import org.ow2.sirocco.cimi.sdk.CimiProviderException;
import org.ow2.sirocco.cimi.sdk.ComponentDescriptor;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.CredentialTemplate;
import org.ow2.sirocco.cimi.sdk.Job;
import org.ow2.sirocco.cimi.sdk.Machine;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineImage;
import org.ow2.sirocco.cimi.sdk.MachineTemplate;
import org.ow2.sirocco.cimi.sdk.Network;
import org.ow2.sirocco.cimi.sdk.NetworkConfiguration;
import org.ow2.sirocco.cimi.sdk.NetworkTemplate;
import org.ow2.sirocco.cimi.sdk.QueryParams;
import org.ow2.sirocco.cimi.sdk.System;
import org.ow2.sirocco.cimi.sdk.SystemCreate;
import org.ow2.sirocco.cimi.sdk.SystemMachine;
import org.ow2.sirocco.cimi.sdk.SystemNetwork;
import org.ow2.sirocco.cimi.sdk.SystemTemplate;
import org.ow2.sirocco.cimi.sdk.SystemVolume;
import org.ow2.sirocco.cimi.sdk.Volume;
import org.ow2.sirocco.cimi.sdk.VolumeTemplate;

public class SystemTest {
    private static CimiClient client;

    @BeforeClass
    public static void readCimiProviderProperties() throws Exception {
        String userName = java.lang.System.getProperty("test.userName");
        String password = java.lang.System.getProperty("test.password");
        String tenantId = java.lang.System.getProperty("test.tenant");
        String cimiEndpointUrl = java.lang.System.getProperty("test.endpoint");
        if (userName == null) {
            throw new Exception("Missing test.userName property");
        }
        if (password == null) {
            throw new Exception("Missing test.password property");
        }
        if (tenantId == null) {
            throw new Exception("Missing test.tenantId property");
        }
        if (cimiEndpointUrl == null) {
            throw new Exception("Missing test.endpoint property");
        }
        SystemTest.client = CimiClient.login(cimiEndpointUrl, userName, password, tenantId, Options.build().setDebug(true)
            .setMediaType(MediaType.APPLICATION_JSON_TYPE));
    }

    private ComponentDescriptor createMachineComponentDescriptor(final MachineConfiguration config, final MachineImage image,
        final Network publicNetwork, final String name, final String description) throws Exception {
        MachineTemplate machineTemplate = new MachineTemplate();
        machineTemplate.setMachineConfigRef(config.getId());
        machineTemplate.setMachineImageRef(image.getId());
        List<MachineTemplate.NetworkInterface> nics = new ArrayList<MachineTemplate.NetworkInterface>();
        MachineTemplate.NetworkInterface privateNic = new MachineTemplate.NetworkInterface();
        privateNic.setNetworkRef("#MyPrivateNetwork");
        nics.add(privateNic);
        if (publicNetwork != null) {
            MachineTemplate.NetworkInterface publicNic = new MachineTemplate.NetworkInterface();
            publicNic.setNetworkRef(publicNetwork.getId());
            nics.add(publicNic);
        }
        machineTemplate.setNetworkInterface(nics);

        ComponentDescriptor component = new ComponentDescriptor();
        component.setName(name);
        component.setQuantity(1);
        component.setComponentTemplate(machineTemplate);
        component.setDescription(description);
        return component;
    }

    // @Test
    // public void createSystemTemplate() throws Exception {
    //
    // MachineConfiguration config =
    // MachineConfiguration.getMachineConfigurations(SystemTest.client,
    // QueryParams.builder().filter("name='small'").build()).get(0);
    // MachineImage image = MachineImage.getMachineImages(SystemTest.client,
    // QueryParams.builder().filter("name='springoo'").build()).get(0);
    // Network publicNetwork = Network.getNetworks(SystemTest.client,
    // QueryParams.builder().filter("networkType='PUBLIC'").build()).get(0);
    // NetworkConfiguration privateNetworkConfig =
    // NetworkConfiguration.getNetworkConfigurations(SystemTest.client,
    // QueryParams.builder().filter("networkType='PRIVATE'").build()).get(0);
    //
    // List<ComponentDescriptor> componentDescriptors = new
    // ArrayList<ComponentDescriptor>();
    //
    // componentDescriptors.add(this.createMachineComponentDescriptor(config,
    // image, publicNetwork, "LoadBalancer",
    // "Apache load balancer"));
    // componentDescriptors.add(this.createMachineComponentDescriptor(config,
    // image, null, "Application Server",
    // "JOnAS application server"));
    // componentDescriptors.add(this.createMachineComponentDescriptor(config,
    // image, null, "Database", "MySQL database"));
    //
    // NetworkTemplate privateNetworkTemplate = new NetworkTemplate();
    // privateNetworkTemplate.setNetworkConfigRef(privateNetworkConfig.getId());
    //
    // ComponentDescriptor netComponentDescriptor = new ComponentDescriptor();
    // netComponentDescriptor.setName("MyPrivateNetwork");
    // netComponentDescriptor.setQuantity(1);
    // netComponentDescriptor.setComponentTemplate(privateNetworkTemplate);
    // netComponentDescriptor.setDescription("private network");
    //
    // componentDescriptors.add(netComponentDescriptor);
    //
    // SystemTemplate systemTemplate = new SystemTemplate();
    // systemTemplate.setName("SpringooTemplateNew");
    // systemTemplate.setDescription("Springoo multi-tier template");
    //
    // systemTemplate.setComponentDescriptors(componentDescriptors);
    //
    // CreateResult<SystemTemplate> result =
    // SystemTemplate.createSystemTemplate(SystemTest.client, systemTemplate);
    //
    // String id = result.getResource().getId();
    //
    // systemTemplate =
    // SystemTemplate.getSystemTemplateByReference(SystemTest.client, id);
    //
    // }

    @Test
    public void cimiPrimerCreateSystemTemplate() throws Exception {

        MachineConfiguration config = MachineConfiguration.getMachineConfigurations(SystemTest.client).get(0);
        MachineImage image = MachineImage.getMachineImages(SystemTest.client).get(0);
        CredentialTemplate credTemplate = null;
        List<CredentialTemplate> credTemplates = CredentialTemplate.getCredentialTemplates(SystemTest.client, QueryParams
            .builder().select("id").build());
        if (credTemplates.isEmpty()) {
            credTemplate = new CredentialTemplate();
            // credTemplate.setExtensionAttribute("userName", "guest");
            // credTemplate.setExtensionAttribute("password", "guest");
            credTemplate.setExtensionAttribute("key", "1234");
            credTemplate = CredentialTemplate.createCredentialTemplate(SystemTest.client, credTemplate).getResource();
        } else {
            credTemplate = credTemplates.get(0);
        }

        VolumeTemplate volumeTemplate = VolumeTemplate.getVolumeTemplates(SystemTest.client).get(0);

        List<ComponentDescriptor> componentDescriptors = new ArrayList<ComponentDescriptor>();

        MachineTemplate machineTemplate = new MachineTemplate();
        machineTemplate.setName("Machine in system demo222");
        machineTemplate.setDescription("Machine in system");
        machineTemplate.setMachineConfigRef(config.getId());
        machineTemplate.setMachineImageRef(image.getId());
        machineTemplate.setCredentialRef("#MyCredential");
        List<MachineTemplate.Volume> volumes = new ArrayList<MachineTemplate.Volume>();
        MachineTemplate.Volume templateVol = new MachineTemplate.Volume();
        templateVol.setInitialLocation("/vol");
        templateVol.setVolumeRef("#MyVolume");
        volumes.add(templateVol);
        machineTemplate.setVolumes(volumes);

        ComponentDescriptor component = new ComponentDescriptor();
        component.setName("MyMachine");
        component.setQuantity(1);
        component.setComponentTemplate(machineTemplate);
        componentDescriptors.add(component);

        component = new ComponentDescriptor();
        component.setName("MyCredential");
        component.setQuantity(1);
        component.setComponentTemplateRef(credTemplate);
        componentDescriptors.add(component);

        component = new ComponentDescriptor();
        component.setName("MyVolume");
        component.setQuantity(1);
        component.setComponentTemplateRef(volumeTemplate);
        componentDescriptors.add(component);

        SystemTemplate systemTemplate = new SystemTemplate();
        systemTemplate.setName("System Demo222");
        systemTemplate.setDescription("My first system template demo");

        systemTemplate.setComponentDescriptors(componentDescriptors);

        CreateResult<SystemTemplate> result = SystemTemplate.createSystemTemplate(SystemTest.client, systemTemplate);

        String id = result.getResource().getId();

        systemTemplate = SystemTemplate.getSystemTemplateByReference(SystemTest.client, id);

    }

    @Test
    public void testSystem() throws Exception {

        // retrieve SystemTemplate
        SystemTemplate systemTemplate = SystemTemplate.getSystemTemplates(SystemTest.client,
            QueryParams.builder().filter("name='SpringooTemplate2'").build()).get(0);

        // create System
        SystemCreate systemCreate = new SystemCreate();
        systemCreate.setName("MySystem1");
        systemCreate.setDescription("my first system");
        systemCreate.setSystemTemplateRef(systemTemplate.getId());
        CreateResult<System> result = System.createSystem(SystemTest.client, systemCreate);
        if (result.getJob().getState() == Job.State.FAILED) {
            Assert.fail("Failed to create System: " + result.getJob().getStatusMessage());
        }
        System system = result.getResource();

        // wait until System started or stopped

        int tries = 10;
        while (tries-- > 0) {
            system = System.getSystemByReference(SystemTest.client, system.getId());
            if (system.getState() != System.State.CREATING) {
                break;
            }
            Thread.sleep(2000);
        }

        if (system.getState() != System.State.STARTED && system.getState() != System.State.STOPPED) {
            Assert.fail("Wrong System state after creation: " + system.getState());
        }

        // explore System collections

        for (SystemMachine systemMachine : system.getMachines()) {
            Machine machine = systemMachine.getMachine();
            java.lang.System.out.println("Machine name=" + machine.getName() + " state=" + machine.getState());
        }

        for (SystemVolume systemVolume : system.getVolumes()) {
            Volume volume = systemVolume.getVolume();
            java.lang.System.out.println("Volume name=" + volume.getName() + " state=" + volume.getState());
        }

        for (SystemNetwork systemNetwork : system.getNetworks()) {
            Network network = systemNetwork.getNetwork();
            java.lang.System.out.println("Network name=" + network.getName() + " state=" + network.getState());
        }

        // for (SystemAddress systemAddress : system.getAddresses()) {
        // Address address = systemAddress.getAddress();
        // java.lang.System.out.println("Address ip=" + address.getIp());
        // }

        // start System

        if (system.getState() == System.State.STOPPED) {
            Job job = system.start();
            if (job.getState() == Job.State.FAILED) {
                Assert.fail("Failed to start System: " + job.getStatusMessage());
            }
            tries = 10;
            while (tries-- > 0) {
                Thread.sleep(4000);
                system = System.getSystemByReference(SystemTest.client, system.getId());
                if (system.getState() != System.State.STARTING && system.getState() != System.State.MIXED) {
                    break;
                }
            }
            if (system.getState() != System.State.STARTED) {
                Assert.fail("Failed to start System: " + system.getState());
            }
        }

        // check that System machines are started

        for (SystemMachine systemMachine : system.getMachines()) {
            Machine machine = Machine.getMachineByReference(SystemTest.client, systemMachine.getMachine().getId());
            Assert.assertEquals(machine.getState(), Machine.State.STARTED);
        }

        // stop System

        Job job = system.stop();
        if (job.getState() == Job.State.FAILED) {
            Assert.fail("Failed to stop System: " + job.getStatusMessage());
        }
        tries = 10;
        while (tries-- > 0) {
            Thread.sleep(4000);
            system = System.getSystemByReference(SystemTest.client, system.getId());
            if (system.getState() != System.State.STOPPING && system.getState() != System.State.MIXED) {
                break;
            }
        }
        if (system.getState() != System.State.STOPPED) {
            Assert.fail("Failed to start System: " + system.getState());
        }

        // check that System machines are stopped

        for (SystemMachine systemMachine : system.getMachines()) {
            Machine machine = Machine.getMachineByReference(SystemTest.client, systemMachine.getMachine().getId());
            Assert.assertEquals(machine.getState(), Machine.State.STOPPED);
        }

        // delete System

        job = system.delete();
        if (job.getState() == Job.State.FAILED) {
            Assert.fail("Failed to delete System: " + job.getStatusMessage());
        }
        tries = 10;
        while (tries-- > 0) {
            try {
                system = System.getSystemByReference(SystemTest.client, system.getId());
            } catch (CimiProviderException e) {
                break;
            }
            if (system.getState() != System.State.DELETING) {
                Assert.fail("Wrong System state after deleting: " + system.getState());
                break;
            }
            Thread.sleep(2000);
        }
        try {
            system = System.getSystemByReference(SystemTest.client, system.getId());
        } catch (CimiProviderException e) {
            return;
        }
        Assert.fail("Deleted system is still there !");

    }

    @Test
    public void createSpringooSystemTemplate() throws Exception {

        MachineConfiguration config = MachineConfiguration.getMachineConfigurations(SystemTest.client,
            QueryParams.builder().filter("name='small'").build()).get(0);
        MachineImage image = MachineImage.getMachineImages(SystemTest.client,
            QueryParams.builder().filter("name='springoo'").build()).get(0);
        Network publicNetwork = Network.getNetworks(SystemTest.client,
            QueryParams.builder().filter("name='public network'").build()).get(0);
        NetworkConfiguration privateNetworkConfig = NetworkConfiguration.getNetworkConfigurations(SystemTest.client,
            QueryParams.builder().filter("name='privateNetworkConfig'").build()).get(0);

        List<ComponentDescriptor> componentDescriptors = new ArrayList<ComponentDescriptor>();

        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, publicNetwork, "LoadBalancer",
            "Apache load balancer"));
        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, null, "Application Server",
            "JOnAS application server"));
        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, null, "Database", "MySQL database"));

        NetworkTemplate privateNetworkTemplate = new NetworkTemplate();
        privateNetworkTemplate.setNetworkConfigRef(privateNetworkConfig.getId());

        ComponentDescriptor netComponentDescriptor = new ComponentDescriptor();
        netComponentDescriptor.setName("MyPrivateNetwork");
        netComponentDescriptor.setQuantity(1);
        netComponentDescriptor.setComponentTemplate(privateNetworkTemplate);
        netComponentDescriptor.setDescription("private network");

        componentDescriptors.add(netComponentDescriptor);

        SystemTemplate systemTemplate = new SystemTemplate();
        systemTemplate.setName("SpringooTemplate");
        systemTemplate.setDescription("Springoo multi-tier template");

        systemTemplate.setComponentDescriptors(componentDescriptors);

        CreateResult<SystemTemplate> result = SystemTemplate.createSystemTemplate(SystemTest.client, systemTemplate);

        String id = result.getResource().getId();

        systemTemplate = SystemTemplate.getSystemTemplateByReference(SystemTest.client, id);

    }

    @Test
    @Ignore
    public void createInvalidSystemTemplateWithWrongNetworkRef() throws Exception {
        MachineConfiguration config = MachineConfiguration.getMachineConfigurations(SystemTest.client).get(0);
        MachineImage image = MachineImage.getMachineImages(SystemTest.client).get(0);
        Network publicNetwork = Network.getNetworks(SystemTest.client,
            QueryParams.builder().filter("networkType='PUBLIC'").build()).get(0);
        NetworkConfiguration privateNetworkConfig = NetworkConfiguration.getNetworkConfigurations(SystemTest.client,
            QueryParams.builder().filter("networkType='PRIVATE'").build()).get(0);

        List<ComponentDescriptor> componentDescriptors = new ArrayList<ComponentDescriptor>();

        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, publicNetwork, "LoadBalancer",
            "Apache load balancer"));
        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, null, "Application Server",
            "JOnAS application server"));
        componentDescriptors.add(this.createMachineComponentDescriptor(config, image, null, "Database", "MySQL database"));

        NetworkTemplate privateNetworkTemplate = new NetworkTemplate();
        privateNetworkTemplate.setNetworkConfigRef(privateNetworkConfig.getId());

        ComponentDescriptor netComponentDescriptor = new ComponentDescriptor();
        netComponentDescriptor.setName("BADPrivateNetwork");
        netComponentDescriptor.setQuantity(1);
        netComponentDescriptor.setComponentTemplate(privateNetworkTemplate);
        netComponentDescriptor.setDescription("private network");

        componentDescriptors.add(netComponentDescriptor);

        SystemTemplate systemTemplate = new SystemTemplate();
        systemTemplate.setName("SpringooTemplate");
        systemTemplate.setDescription("Springoo multi-tier template");

        systemTemplate.setComponentDescriptors(componentDescriptors);

        try {
            CreateResult<SystemTemplate> result = SystemTemplate.createSystemTemplate(SystemTest.client, systemTemplate);
        } catch (CimiProviderException ex) {
            java.lang.System.out.println(ex.getMessage());
            return;
        }
        Assert.fail("Invalid system template creation did not raise an exception");
    }
}

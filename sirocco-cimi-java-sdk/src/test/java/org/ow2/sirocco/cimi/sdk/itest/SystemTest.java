package org.ow2.sirocco.cimi.sdk.itest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClient.Options;
import org.ow2.sirocco.cimi.sdk.CimiProviderException;
import org.ow2.sirocco.cimi.sdk.ComponentDescriptor;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineImage;
import org.ow2.sirocco.cimi.sdk.MachineTemplate;
import org.ow2.sirocco.cimi.sdk.Network;
import org.ow2.sirocco.cimi.sdk.NetworkConfiguration;
import org.ow2.sirocco.cimi.sdk.NetworkTemplate;
import org.ow2.sirocco.cimi.sdk.QueryParams;
import org.ow2.sirocco.cimi.sdk.SystemTemplate;

public class SystemTest {
    private static CimiClient client;

    @BeforeClass
    public static void readCimiProviderProperties() throws Exception {
        String userName = System.getProperty("test.userName");
        String password = System.getProperty("test.password");
        String cimiEndpointUrl = System.getProperty("test.endpoint");
        if (userName == null) {
            throw new Exception("Missing test.userName property");
        }
        if (password == null) {
            throw new Exception("Missing test.password property");
        }
        if (cimiEndpointUrl == null) {
            throw new Exception("Missing test.endpoint property");
        }
        SystemTest.client = CimiClient.login(cimiEndpointUrl, userName, password,
            Options.build().setDebug(true).setMediaType(MediaType.APPLICATION_XML_TYPE));
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

    @Test
    public void createSystemTemplate() throws Exception {

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
            System.out.println(ex.getMessage());
            return;
        }
        Assert.fail("Invalid system template creation did not raise an exception");
    }
}

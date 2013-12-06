package org.ow2.sirocco.cimi.sdk.itest;

import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;
import org.ow2.sirocco.cimi.sdk.Address;
import org.ow2.sirocco.cimi.sdk.CimiClient;
import org.ow2.sirocco.cimi.sdk.CimiClient.Options;
import org.ow2.sirocco.cimi.sdk.CreateResult;
import org.ow2.sirocco.cimi.sdk.Job;
import org.ow2.sirocco.cimi.sdk.Machine;
import org.ow2.sirocco.cimi.sdk.MachineConfiguration;
import org.ow2.sirocco.cimi.sdk.MachineCreate;
import org.ow2.sirocco.cimi.sdk.MachineImage;
import org.ow2.sirocco.cimi.sdk.MachineNetworkInterface;
import org.ow2.sirocco.cimi.sdk.MachineTemplate;

public class MachineTests {
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
        if (cimiEndpointUrl == null) {
            throw new Exception("Missing test.endpoint property");
        }
        MachineTests.client = CimiClient.login(cimiEndpointUrl, userName, password, tenantId, null,
            Options.build().setDebug(true).setMediaType(MediaType.APPLICATION_XML_TYPE));
    }

    private Machine waitForMachineState(final String machineId, final int seconds, final Machine.State... expectedStates)
        throws Exception {
        int tries = seconds;
        while (tries-- > 0) {
            Machine machine = Machine.getMachineByReference(MachineTests.client, machineId);
            if (machine.getState() == Machine.State.ERROR) {
                throw new Exception("Machine state ERROR");
            }
            for (Machine.State expectedFinalState : expectedStates) {
                if (machine.getState() == expectedFinalState) {
                    return machine;
                }
            }
            Thread.sleep(1000);
        }
        throw new Exception("Timeout waiting for Machine state transition");
    }

    private MachineImage waitForMachineImageState(final String machineImageId, final int seconds,
        final MachineImage.State... expectedStates) throws Exception {
        int tries = seconds;
        while (tries-- > 0) {
            MachineImage machineImage = MachineImage.getMachineImageByReference(MachineTests.client, machineImageId);
            if (machineImage.getState() == MachineImage.State.ERROR) {
                throw new Exception("MachineImage state ERROR");
            }
            for (MachineImage.State expectedFinalState : expectedStates) {
                if (machineImage.getState() == expectedFinalState) {
                    return machineImage;
                }
            }
            Thread.sleep(1000);
        }
        throw new Exception("Timeout waiting for MachineImage state transition");
    }

    @Test
    public void captureMachine() throws Exception {
        Machine machine = null;
        MachineImage capturedMachineImage = null;

        try {
            // create Machine

            MachineConfiguration machineConfig = MachineConfiguration.getMachineConfigurations(MachineTests.client).get(0);
            MachineImage machineImage = MachineImage.getMachineImages(MachineTests.client).get(0);
            MachineTemplate machineTemplate = new MachineTemplate();
            machineTemplate.setMachineConfigRef(machineConfig.getId());
            machineTemplate.setMachineImageRef(machineImage.getId());
            MachineCreate machineCreate = new MachineCreate();
            machineCreate.setMachineTemplate(machineTemplate);
            machineCreate.setName("MachineCaptureTest");
            machineCreate.setDescription("machine capture test");

            CreateResult<Machine> result = Machine.createMachine(MachineTests.client, machineCreate);
            machine = result.getResource();
            String machineId = result.getResource().getId();

            machine = this.waitForMachineState(machineId, 30, Machine.State.STARTED, Machine.State.STOPPED);

            // capture image

            MachineImage newMachineImage = new MachineImage();
            newMachineImage.setDescription("captured image from machine " + machineCreate.getName());
            newMachineImage.setName("capturedMachineImage");
            newMachineImage.setType(MachineImage.Type.IMAGE);
            CreateResult<MachineImage> captureResult = machine.capture(newMachineImage);
            capturedMachineImage = captureResult.getResource();

            String capturedMachineImageId = captureResult.getResource().getId();

            capturedMachineImage = this.waitForMachineImageState(capturedMachineImageId, 30, MachineImage.State.AVAILABLE);

        } finally {
            // Cleanup
            Exception deleteException = null;

            if (capturedMachineImage != null) {
                try {
                    Job job = capturedMachineImage.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    deleteException = e;
                    System.err.println("Failed to delete Machine Image " + capturedMachineImage.getId());
                }
            }
            if (machine != null) {
                try {
                    if (machine.getState() == Machine.State.STARTED) {
                        machine.stop();
                        machine = this.waitForMachineState(machine.getId(), 30, Machine.State.STOPPED);
                    }
                    Job job = machine.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    deleteException = e;
                    System.err.println("Failed to delete Machine  " + machine.getId());
                }
            }
            if (deleteException != null) {
                throw deleteException;
            }
        }

    }

    @Test
    public void restartMachine() throws Exception {
        Machine machine = null;

        try {
            // create Machine

            MachineConfiguration machineConfig = MachineConfiguration.getMachineConfigurations(MachineTests.client).get(0);
            MachineImage machineImage = MachineImage.getMachineImages(MachineTests.client).get(0);
            MachineTemplate machineTemplate = new MachineTemplate();
            machineTemplate.setMachineConfigRef(machineConfig.getId());
            machineTemplate.setMachineImageRef(machineImage.getId());
            machineTemplate.setExtensionAttribute("realm", "UZXC0GRT-ZG8ZJCJ07-N-SECURE1");
            MachineCreate machineCreate = new MachineCreate();
            machineCreate.setMachineTemplate(machineTemplate);
            machineCreate.setName("MachineWithRealmTest");
            machineCreate.setDescription("machine with realm test");

            CreateResult<Machine> result = Machine.createMachine(MachineTests.client, machineCreate);
            machine = result.getResource();
            String machineId = result.getResource().getId();

            machine = this.waitForMachineState(machineId, 30, Machine.State.STARTED, Machine.State.STOPPED);

            if (machine.getState() == Machine.State.STOPPED) {
                machine.start();
                this.waitForMachineState(machineId, 30, Machine.State.STARTED);
            }

            // restart machine

            Job job = machine.restart(false);
            this.waitForMachineState(machineId, 30, Machine.State.STARTED);

        } finally {
            // Cleanup
            Exception deleteException = null;

            if (machine != null) {
                try {
                    machine.stop();
                    machine = this.waitForMachineState(machine.getId(), 30, Machine.State.STOPPED);
                    Job job = machine.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    deleteException = e;
                    System.err.println("Failed to delete Machine  " + machine.getId());
                }
            }
            if (deleteException != null) {
                throw deleteException;
            }
        }
    }

    @Test
    public void createMachineWithUserData() throws Exception {
        Machine machine = null;

        try {
            // create Machine

            MachineConfiguration machineConfig = MachineConfiguration.getMachineConfigurations(MachineTests.client).get(0);
            MachineImage machineImage = MachineImage.getMachineImages(MachineTests.client).get(0);
            MachineTemplate machineTemplate = new MachineTemplate();
            machineTemplate.setMachineConfigRef(machineConfig.getId());
            machineTemplate.setMachineImageRef(machineImage.getId());
            machineTemplate.setUserData("1234567890");
            MachineCreate machineCreate = new MachineCreate();
            machineCreate.setMachineTemplate(machineTemplate);
            machineCreate.setName("MachineUserDataTest");
            machineCreate.setDescription("machine user data test");

            CreateResult<Machine> result = Machine.createMachine(MachineTests.client, machineCreate);
            machine = result.getResource();
            String machineId = result.getResource().getId();

            machine = this.waitForMachineState(machineId, 30, Machine.State.STARTED, Machine.State.STOPPED);

            if (machine.getState() == Machine.State.STOPPED) {
                machine.start();
                this.waitForMachineState(machineId, 30, Machine.State.STARTED);
            }

            // get IP address(es) of the created machine

            for (MachineNetworkInterface nic : machine.getNetworkInterfaces()) {
                if (nic.getNetwork() != null) {
                    System.out.println("Network type: " + nic.getNetwork().getNetworkType());
                } else {
                    System.out.println("NIC has no Network !!!");
                }
                for (Address addr : nic.getAddresses()) {
                    System.out.println("IP: " + addr.getIp());
                }
            }

        } finally {
            // Cleanup
            Exception deleteException = null;

            if (machine != null) {
                try {
                    machine.stop();
                    machine = this.waitForMachineState(machine.getId(), 30, Machine.State.STOPPED);
                    Job job = machine.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    deleteException = e;
                    System.err.println("Failed to delete Machine  " + machine.getId());
                }
            }
            if (deleteException != null) {
                throw deleteException;
            }
        }
    }

}

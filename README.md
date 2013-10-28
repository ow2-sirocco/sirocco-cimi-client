# Sirocco DMTF CIMI Java SDK and commmand-line tools

Can be used with any CIMI compliant provider.

## Java Client example

    // machine creation
    
    CimiClient cimiClient = CimiClient.login(cimiEndpointUrl, login,password);
    MachineCreate machineCreate = new MachineCreate();
    MachineTemplate machineTemplate = new MachineTemplate();
    machineTemplate.setMachineConfigRef(configId);
    machineTemplate.setMachineImageRef(imageId);
    machineTemplate.setCredentialRef(credId);
    machineCreate.setMachineTemplate(machineTemplate);
    machineCreate.setName("myMachine");
    machineCreate.setDescription("a test machine");
    CreateResult<Machine> result = Machine.createMachine(cimiClient,machineCreate);
    String machineId=result.getResource().getId();
    System.out.println("Creating machine "+machineId);
    result.getJob().waitForCompletion(60, TimeUnit.SECONDS);
    			
    Machine machine=Machine.getMachineByReference(cimiClient, machineId);
    			
    for(MachineNetworkInterface nic: machine.getNetworkInterfaces()) {
       System.out.println("IP address: "+nic.getAddresses().get(0));
    }


## Command-line tools example

    cimiclient machineconfig-list -select name,cpu,memory
    +-------------------------------------------+--------+---+------+
    |id                                         |name    |cpu|memory|
    +-------------------------------------------+--------+---+------+
    |http://myprovider.com/cimi/machineConfigs/1|micro   |1  |630 MB|
    |http://myprovider.com/cimi/machineConfigs/2|tiny    |1  |512 MB|
    |http://myprovider.com/cimi/machineConfigs/3|small   |2  |2 GB  |
    +-------------------------------------------+--------+---+------+
    
    cimiclient  machineimage-list -select id,description -first 1 -last 4
    +------------------------------------------+-----------------------------------+
    |id                                        |description                        |
    +------------------------------------------+-----------------------------------+
    |http://myprovider.com/cimi/machineImages/1|Ubuntu Oneiric 11.10 Server 64 bits|
    |http://myprovider.com/cimi/machineImages/2|Ubuntu 11.04 server 64bits         |
    |http://myprovider.com/cimi/machineImages/3|Debian 5.0 32 bits                 |
    |http://myprovider.com/cimi/machineImages/4|LAMP stack on Debian 5.0           |
    +------------------------------------------+-----------------------------------+

## More info

* Javadoc: http://sirocco.ow2.org/apidocs/cimijavasdk/ 
* CIMI Java SDK:  http://wiki.sirocco.ow2.org/xwiki/bin/view/Components/cimiclient
* CIMI command-line tools: http://wiki.sirocco.ow2.org/xwiki/bin/view/Components/cimitools

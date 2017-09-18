## IMVS Home Automation

### Technologies
Java, Bluetooth LE, i4ds-imvs-internal KNX API, Debian, Sonos API, Philips Hue API, SOAP, HTTP, REST, and more

***

### Required Hardware
* Raspberry 3 or similar (Needs Ethernet/802.11x access and a BLE Controller)
* "Output-Gadgets / Actors" like Sonos music speakers, knx enabled blinds, and Philips Hue bulbs
* "Input-Gadgets / Sensors" like Flic.io Buttons, Webinterfaces, and more
* KNX gateway or KNX Twisted-Pair/IP access.

***

### Configuration of an Action
A flic button knows 3 events: Single Click, Double Click and Hold.  
To trigger an action, a first and a second event is necessary. (In total 3 events times 3 events = 9 different events are possible)  
The configuration file configIMVS.json defines what actions follows to which event.  
An action invokes a method from an output class. (By using java reflection)

***

#### About the configuration configIMVS.json
The automation application is configured by ~/imvsautomation/configIMVS.json.  

The configuration describes:
- The ip to the Philips Hue bridge and the generated Philips Hue Bridge API username
- Serviceendpoints to control the binds (depending on the i4DS team)
- Playlists and radio channels for the Sonos system
- Describes Scenarios: Setting multiple actors in predefined states at the same time
- Describes Actions: An action invokes a method of an output class depending on the triggered event (pressing a button)

***

### Access to the Raspberry Pi 3, running in the IMVS break room
~~~~
ssh -l pi [IP]
[PASSWORD]
screen -R bleAgent          // Screen session running the Bluetooth LE agent
screen -R broker            // Screen session running the broker (by Robin Schoch)
screen -R automation        // Screen session running the IMVS automation application
~~~~

***

### Adding a new or unpaired flic button
The bluetooth agent uses a database to manage the paired flic BLE buttons. (Location: ~/imvsautomation/buttonsIMVSSeptember2017).  
Ensure the bleAgent is running, otherwise start the ble agent described below.  
To pair a new flic button pass the "wizard" argument to the main automation application: `~/imvsautomation/build/install/imvsautomation/bin/imvsautomation wizard`.  
Follow the instructions (mostly keep the new button pressed for 7 seconds).

***
***

### Start the application
#### 0a. Optional: GitLab Pull
Ensure the latest code from the GITLAB repo is available: `cd ~/imvsautomation; git pull;`  

#### 0b. Optional: Build the project
Build the project using gradle: `cd ~/imvsautomation; gradle clean installDist;`

#### 0c. Optional: Internet access for LAN players
The RP3 acts as a DHCP server. To allow its clients to access the internet, do:  
`sudo sh ~/imvsautomation/routes`


#### 1. Start the Bluetooth LE agent
If present attach the screen session bleAgent using `screen -R bleAgent`, otherwise create a screen session using `screen -S bleAgent`  
Start the Bluetooth Agent: `sudo ~/imvsautomation/runtimes/armv6l/flicd -f ~/imvsautomation/buttonsIMVSSeptember2017`  
Exit the session using `CTRL + A + D`  

#### 2. Start the broker
If present attach the screen session broker using `screen -R broker`, otherwise create a screen session using `screen -S broker`  
Start the Broker: `cd ~/imvsautomation/broker/bin; ./moquette.sh;`  
Exit the session using `CTRL + A + D`  

#### 3. Start the main automation application
If present attach the screen session automation using `screen -R automation`, otherwise create a screen session using `screen -S automation`  
Start the automation application and pass the configuration: `~/imvsautomation/build/install/imvsautomation/bin/imvsautomation ~/imvsautomation/configIMVS.json`  
Exit the session using `CTRL + A + D`  
***
***

### Some words about the output classes
* Output classes are located at `~/imvsautomation/src/main/java/ch/fhnw/imvs/automation/java/output/`
* No need for implementing an interface or extending a superclass.
* Has to implement the singleton pattern. (Each output class exists once per application runtime)
* Has to provide the `public static [className] getInstance()` method
* Therefore the output classes could have a state if necessary

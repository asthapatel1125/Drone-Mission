***Installation Manual*** 
**Backend & Frontend**
*Installation:*
Backend was built using java with also running the main file to run every team's code. A javaFX with a webview is used to achieve this. There are some files to be downloaded and configured to get the project running: 

Need to make sure to download these jar files from this website:https://openjfx.io/ 

Once you downloaded it, add it to the project and also make sure to add it to the configuration.
*How to run:*
After this just run the main file -> frontend -> backend -> simulation -> firmware which will launch the application and will ensure all the network connections. After this runs the py game and the network for the pygame so it is connected to the backend merge. Then just interact with the frontend application and you can see the demo of the drone when you get to the drone screen. 

**Simulation**
*Installation:* 
The simulation was built using python. To ensure compatibility, it is advised to install this version: https://www.python.org/downloads/release/python-3110/ 

The simulation utilized the pygame library: https://www.pygame.org/download.shtml 

To download this library, pip can be used. The command pip install pygame should suffice, assuming pip is installed on the user's machine.

More information regarding pip can be found here: https://pypi.org/project/pip/ 

*How to run:*
NOTE: The user will observe that there exists a Simulation directory, ensure that the user is within this directory (otherwise the paths for various items will not be correct)

This can be achieved in various ways, for instance, if the user is utilizing git bash: https://git-scm.com/downloads, then running cd <<path here>>./Simulation will suffice 

To verify that this step was done correctly, try running the “ls” command and observe the resulting files 

The Simulation component has 2 runnable files which can be run separately and both function, even if the other is not running. These files are called: Runnable_Simulation.py and Runnable_Network.

Runnable_Simulation.py will launch the simulation. This can be run via the following command: $ python Runnable_Simulation.py

Runnable_Network.py will launch the network capabilities for the Simulation component, allowing for the Frontend and Backend to communicate with the Simulation. This program can be run via the following command: $ python Runnable_Network.py

Once the network receives a packet, the simulation will respond accordingly


**Firmware**
*Installation:* 
The firmware requires the Arduino IDE. It can be installed via this link: https://www.arduino.cc/en/software 

For this project, the firmware utilized the pyserial library: https://pypi.org/project/pyserial/#files

Similar to the simulation team, pip can be used. The command  pip install pyserial should suffice, assuming pip is installed on the user’s machine.

To get the best results, it is recommended to physically use an Arduino microcontroller. If this is not possible, an alternative approach could be to run the code in a simulation environment such as Tinkercad: https://www.tinkercad.com/ 

*How to run:*
Flash the code onto a microcontroller or copy the code into a simulation environment
Run Firmware.py to connect with the backend

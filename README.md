# Arm Control Android
Android remote control app for arm robot running on Raspberry Pi with Python cokect server. Socket server code that runs on the Raspberry Pi is also on GitHub at https://github.com/pnetherwood/ArmControlPiPython.

Trying it Out
=============

Download the APK and install on your phone/tablet. You need to allow installs from unknown sources in your settings. The app requires INTERNET_PERMISSION to allow it to communicate with the Python server running on the Pi.
For instructions of setting up the Python server see https://github.com/pnetherwood/ArmControlPiPython

After launching the app for the first time you will need to enter the settings menu to set the IP address or hostname of the Raspberry Pi. Setup the Pi with the static IP address to ensure the app finds the Pi each time. When the app connects to Python server, the IP address of the phone/tablet is shown in the logging output of the server. Failing to connect is normally down to your wifi network. Make sure the Pi and the phone/tablet have good connections on the same network.

Other items on the action bar is Refresh which tries to reconnect to the server, Stop which is an emmergency stop if the last finger up command was not received i.e the robot doesn't stop moving.

Building
========

The app is built in Android Studio. Download the zip and unzip into a new folder.
Install and launch Android Studio. Make sure you have at least 1 SDK installed. Open the menu Tools->Android->SDK Manager and install Android 5.1 SDK if its not already installed.

Choose menu option File->Import Project

Select the folder with the unzipped files and it will import a new project into Android Studio

In order to build you need to launch and Android Virtual Device. Goto menu option Tools->Android->AVD Manager and Create a new device such as a Nexus 4.

Clicking the Run icon (or selecting Run from Run menu) will launch the AVD. This will allow you to test your build. The APK is created in the app\build\outputs\apk folder.


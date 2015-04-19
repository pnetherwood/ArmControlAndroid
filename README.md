# ArmControlAndroid
Android remote control app for arm robot running on Raspberry Pi

Trying it Out
=============

Download the APK an install on your phone/tablet. It requires INTERNET_PERMISSSION to allow it to communicate with the Python server runnong on the Pi.
For instructions of setting up the Python server see https://github.com/pnetherwood/ArmControlPiPython

After launching the app for the first time you will need to enter the setting menu to set the IP address or hostname of the raspberry Pi. Setup the Pi with the static IP address to ensure the app finds the Pi each time.

Building
========

The app is built in Android Studio. Download the zip and unzip into a new folder.
Install and launch Android Studio. Make sure you have at least 1 SDK installed. Open the menu Tools->Android->SDK Manager and install Android 5.1 SDK if its not already installed.

Choose menu option File->Import Project

Select the folder with the unzipped files and it will import a new project into Android Studio

In order to build you need to launch and Android Virtual Device. Goto menu option Tools->Android ->AVD Manager and Create a new devide such as a Nexus 4.

Clicking the Run icon (or selecting Run from Run menu) will launch the AVD. This will allow you to test your build. The APK is created in the app\build\outputs\apk folder.


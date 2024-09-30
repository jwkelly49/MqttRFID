========================
GETTING STARTED
========================

I recommend reading at least the first few pages of the manual before proceeding.

I tried to make a zip file but it became too large for a Github upload. 
- make a folder (MqttRFID) somewhere permanent on your hard drive.
- copy the EXE file here
- make a subFolder (lib)
- copy the lib files to your lib folder.

-------------------------------------------
if you downloaded MqttRFID_jar.zip
To run the program from the command line, go to the folder and
type the following:

java -jar "MqttRFID.jar" 

You can make a batch or shell file to make this easier to launch.

-------------------------------------------
If you downloaded MqttRFID_exe.zip
Go to the folder and double click on MqttRFID.exe

You can make a shortcut and place it on the desktop ... etc.

--------------------------------------------

After running for the first time the program will create other folders and files. They are used internally by the program and you should never need to access them directly.

========================
RFIDBASE.ZIP
========================
This is to code to be loaded into the Arduino ESP32 (I recommend Version 2 of the Arduino IDE)

========================
MQTT BROKER
========================
Don't forget you need a MQTT broker installed before anything works. (I used Mosquitto)

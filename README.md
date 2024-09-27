# MqttRFID
RF Identification tags for model trains using a Mqtt Broker.
(This is a rewrite of the SerialRFID program replacing the serial prot with a MQTT broker.)
To download use the release button on the right-hand side. You can use the EXE file if running on Windows or the ZIP file to get the JAR files. You will also need the Arduino.zip file to go into the reader Nano Esp32.

Fundamentally MqttRFID provides the user with an easy to use Liquid Crystal Display (LCD) showing the road markings of your rolling stock. 
To accomplish this each piece of stock must have a Radio Frequency Identification (RFID) tag. The local LCD display will show 

1- Type (box car), 

2- Color (Green), 

3- Road name/number (Milw123)

4- Owner (John Doe). 

Items 1-3 were selected because of the ease they create while working a yard; say during an operations session. Item 4 can help clubs and OPS hosts identify who owns that piece of stock when there is duplicate stock with identical markings.

MqttRFID is not integrated with JMRI but it does have a "hook" into its XML files so that if you have entered your engines, cars and locations that information can be pulled into MqttRFID and even send that information to a RFID chip without having to manually reenter all that data. More details are explained later in this manual.

MqttRFID is written in Java and that means if you already have JMRI running it's a quick and easy install. 

The Arduino Nano ESP32 chip was programmed using WiFi manager. This makes it easy to 1) add the Reader to your network 2) address the Reader 3) link to the Mqtt Broker.

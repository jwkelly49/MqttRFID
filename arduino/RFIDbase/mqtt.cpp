#ifndef tagHeader
#include "RFIDbase.h"
#endif


const char *BUFFER_FORMAT = "%s";
const size_t BUFFER_SIZE = 130;
char pubBuffer[BUFFER_SIZE + 1];                              // Extra one for NULL terminator.
char publishData[130] = "send nothing";                       // data to be sent to Mosquitto broker

void SetupMqtt()
{
  mqttClient.setServer(hostName, MQTT_PORT);
  
  mqttClient.setCallback(CallbackMqtt);                       // set the callback function
  
}

/* Once a network connection has been establish the reader
will enter an endless loop until a Broker connection
has been established.
*/

void ConnectToMqtt()
{
  // Serial.println("Connecting to MQTT Broker...");
  char clientId[100] = "\0";
  sprintf(clientId, "ESP32Client-%04X", random(0xffff));
  displayLCD(18);                                                    // no broker message
  while (true)
  {
    if (mqttClient.connect(clientId))
    {
      // Serial.println("Connected to MQTT broker.");                // all topics listed as a reference
      // subscribe to topic (all topics listed as a reference)
      mqttClient.subscribe("rfid/writeTag");                         // only subscribe to topics you want to receive data from
      //mqttClient.subscribe("rfid/tagReply");                       // subscribing to topics that you publish to will cause
      //mqttClient.subscribe("rfid/request");                        // an echo back to the publishing client (this reader)
      mqttClient.subscribe("rfid/requestReply");
      //mqttClient.subscribe("rfid/error");
      mqttClient.subscribe("rfid/readersOnline");
      //mqttClient.subscribe("rfid/onlineReply");
      mqttClient.subscribe("rfid/visitor");
      //mqttClient.subscribe("rfid/visitorReply");
      break;
    } 
  }
  displayLCD(1);                                                     // clear display
}

// ***************************************************

void CallbackMqtt(char* topic, byte* payload, unsigned int length)
{
  //handles all subscriptions for this reader
  byte* content = new byte[length];
  // Serial.print("Callback - Topic:  ");
  // Serial.println(topic);
  // Serial.print("Callback - Message:  ");
  for (int i = 0; i < length; i++)
  {
    content[i] = payload[i];
    //Serial.print((char)content[i]);               //  used for testing  
  }
  Serial.println();
  int returnedCmd;
  returnedCmd = validMessage(content, length);      // does the message start with an * and a capital A-Y that matches this reader's address
  if ( returnedCmd > 0)                             // and a valid command number
  {
    if (strcmp(topic,"rfid/requestReply")==0)
    {
      // get the computer reply to a request from a reader
      //Serial.println("got to request reply if statement");
      processReplies(returnedCmd, content, length);
    }
    else if (strcmp(topic,"rfid/writeTag")==0)
    {
      //Serial.println("got to tag write else if statement");
      // only process if this is reader A
      if (thisReader == 'A')
      {
        // this is data the reader will write to a new RFID tag
        //Serial.println("-------- got to tag write for reader A ----------");
        processCommands(returnedCmd, content, length);
      }
    }
    else if (strcmp(topic,"rfid/visitor")==0)
    {
      //Serial.println("got to tag write else if statement");
      // only process if this is reader A
      if (thisReader == 'A')
      {
        // this is data the reader will write to a new RFID tag
        //Serial.println("-------- got to tag write for reader A ----------");
        processCommands(returnedCmd, content, length);
      }
    }
    else if (strcmp(topic,"rfid/readersOnline")==0)
    {
      // get the computer reply to a request from a reader
      //Serial.println("got to request reply if statement");
      processCommands(returnedCmd, content, length);
    }
    // do something here
  }
  else
  {
    // Serial.println("CallbackMqtt() message ignored");
  }
  delete[] content;                                                      // Free the allocated memory
}

// ****************************************************

void publisher(int pubTrigger, char* data, unsigned int length )
{
 
  if (strlen(data) == 0)
  {
    //faultRecovery(10);   // don't publish an empty buffer
    return;
  }
  else
  {
    strncpy(publishData, data, sizeof(publishData));       
    snprintf(pubBuffer, BUFFER_SIZE, BUFFER_FORMAT, publishData);
  }

  switch (pubTrigger) 
  {
    case 1:
      if (thisReader == 'A')
      {
        // sent data to a new tag this is new RFID tag number reply
        // make sure only reader "A" can reply to a write tag command
        mqttClient.publish("rfid/tagReply", pubBuffer);
        //Serial.print("Publish tagReply: ");
        //Serial.println(pubBuffer);
        
      }
      break;  
    case 2:
      // have the computer record a Drop-off - Pick-up - Assign engine request
      mqttClient.publish("rfid/request", pubBuffer);
      //Serial.print("Publish request: ");
      //Serial.println(pubBuffer);
      break;
    case 3:
      // send error message to computer
      mqttClient.publish("rfid/error", pubBuffer);
      //Serial.print("Publish error: ");
      //Serial.println(pubBuffer);
      break;
    case 4:
      // send error message to computer
      mqttClient.publish("rfid/onlineReply", pubBuffer);
      //Serial.print("Publish onLine status: ");
      //Serial.println(pubBuffer);
      break;  
    case 5:
      // send visiting car message to computer
      mqttClient.publish("rfid/visitorReply", pubBuffer);
      //Serial.print("Publish visiting car: ");
      //Serial.println(pubBuffer);
      break; 
    default:
      // statements
      break;
  }
}

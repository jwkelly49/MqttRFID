#ifndef tagHeader
#include "RFIDbase.h"
#endif



// *********************** validate Message *****************************
int validMessage(byte* content, unsigned int length)  
{
  // command 1          = *[reader address][command]\n  *A01\n  (deprecated for MQTT)
  // command 2          = *[reader][command]~[data]~[data]~[data]~[data]~[data]\n  *A02~roadName~roadNumber~type~color/model~owner\n
  // commands 20/21/22  = *[reader][command]~[True/False]\n    *A20~True\n
  // Serial.println("got to valid message function");

  char startByte;
  char addressByte;
  int cmd = 0;           // no a valid message  or command
  String cmdText;
  String x1;
  String x2;

  if (length < 4) {
    return false;       // Not enough data to process
  }
  startByte = (char)content[0];
  addressByte = (char)content[1];       // address sent from broker
  x1 = (char)content[2];
  x2 = (char)content[3];
  cmdText = x1 + x2;
  cmd = cmdText.toInt();
  
  if (startByte == '*')
  {
    if (addressByte == thisReader)
    {
      // get the computer reply to a request from this reader
      // Serial.println("got a matching reader address");
    }
    else
    {
      cmd = 0;
      // not addressed for this reader might do something here
      // Serial.println("not addressed for this reader");
    }

    if (cmd == 1 || 2 || 20 || 21 || 22 || 23 || 99) 
    {
      // get the computer reply to a request from this reader
      // Serial.println("got a valid command");
    }
    else
    {
      cmd = 0;
      // not addressed for this reader might do something here
      // Serial.println("not a recognized command");
    }

  }
  else
  {
    cmd = 0;
    // not a valid start to a message might do something here
    // Serial.println("not a valid message start character");
  }
  return cmd;
}


//********************************************************************

void parseData()                              // only needed for command 2 writing to a new RFID tag
{

  /*
  Filling the array with space characters ensures that all unused elements appear to display nothing
  on the LCD display. this is needed because each variable can hold a "variable" sized string. Using
  he "space" character also gives us something to sort against when reading a new tag filled with NULL
  characters. See updateTag.clearValues()
  THIS loop clears arrays prior to processing data from the server (softSerial)
  */

  for (int i = 0; i < 16; ++i) {              // all these variables have lengths of 17
    carRoadName[i] = ' ';                     // this loop ensures that each array
    carRoadNumber[i] = ' ';                   // element has a printable "space" character
    carType[i] = ' ';                         // for the LCD display.
    carColor[i] = ' ';
    carOwner[i] = ' ';
  }
  carRoadName[16] = '\0';                     // each variable is then NULL terminated.
  carRoadNumber[16] = '\0';
  carType[16] = '\0';
  carColor[16] = '\0';
  carOwner[16] = '\0';

  char trash[5];
  char * strtokIndx;                           // this is used by strtok() as an index

  strtokIndx = strtok(globalMessage, "~");     // get the first part - the string
  strcpy(trash, strtokIndx);                   // drop start bit - address - command

  strtokIndx = strtok(NULL, "~");              // get road name
  strcpy(carRoadName, strtokIndx);
  // Serial.print("roadName = ");
  // Serial.println(carRoadName);
  strtokIndx = strtok(NULL, "~");              // get road number
  strcpy(carRoadNumber, strtokIndx);
    // Serial.print("roadNumber = ");
  // Serial.println(carRoadNumber);
  strtokIndx = strtok(NULL, "~");              // get type of car
  strcpy(carType, strtokIndx);
    // Serial.print("carType = ");
  // Serial.println(carType);
  strtokIndx = strtok(NULL, "~");              // get color of car  or model of engine
  strcpy(carColor, strtokIndx);
    // Serial.print("carColor = ");
  // Serial.println(carColor);
  strtokIndx = strtok(NULL, "~");              // get the owner of car
  strcpy(carOwner, strtokIndx);
    // Serial.print("carOwner = ");
  // Serial.println(carOwner);
}
//*******************************************************

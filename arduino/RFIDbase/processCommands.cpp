#ifndef tagHeader
#include "RFIDbase.h"
#endif

extern int retry;

// **********************************************************************

void processReplies(int cmd, byte* content, unsigned int length) 
{
  int selectCmd = cmd;
  bool goodReply;
  
  goodReply = replyStatus(content, length);          // common code for True/False

  if (!goodReply)
  {
    displayLCD(7);                                   // request failed message
    // Serial.println("got a bad reply");
    return;
  }
  switch (selectCmd) {
    case 1:
      // noop                                        // command 1 deprecated only used in old serial version of this program
      break;
    case 2:
      // noop                                        // This is a command from the broker to write a new tag
      break;
    case 20:                                         // assign an engine to this reader (reader is the same as a layout location)
      assignedEngine = "";
      assignedEngine = carID;                        // remember this ID as an engine
      displayLCD(4);                                 // display proper complete message
      delay(3000);
      pendingReply = false;
      // Serial.println("good reply for command 20");

    //   for (int i = 0; i < length; i++)
    //  {
    //     Serial.print((char)content[i]);
    //   }
    //   Serial.println();
      break;
    case 21:                                         // drop car at reader
      displayLCD(2);                                 // display proper complete message
      delay(3000);
      pendingReply = false;
      // Serial.println("good reply for command 21");
      // for (int i = 0; i < length; i++)
      // {
      //  Serial.print((char)content[i]);
      // }
      // Serial.println();
      break;
    case 22:                                         // pickup this car and add to engine
      displayLCD(3);                                 // display proper complete message
      delay(3000);
      pendingReply = false;
      // Serial.println("good reply for command 22");
      // for (int i = 0; i < length; i++)
      // {
      //   Serial.print((char)content[i]);
      // }
      // Serial.println();      
      break;
    default:
      displayLCD(14);                                // bad command received
      delay(3000);
      pendingReply = false;
      break;
  }
}

// **********************************************************************

bool replyStatus(byte* content, unsigned int length) {
  char reply;
  byte reply1;
  bool goodReply = false;
  reply1 = content[5];
  reply = reply1;
  if (reply == 'T') {
    goodReply = true;
  } else {
    goodReply = false;
    displayLCD(7);                                  // display request failure
  }
  return goodReply;
}

// ************************** process commands **************************

void processCommands(int cmd, byte* content, unsigned int length) 
{

  int selectCmd = cmd;
  int x = 0;
  char assigning[30];
  switch (selectCmd) 
  {
    case 1:
      // command 1 is deprecaded used for old serial version only
      // "was" -- nothing pending just acknowledge the polling command
      break;  
    case 2:
      // assigning[0] = '*';
      // assigning[1] = thisReader;
      // assigning[2] = '0';
      // assigning[3] = '2';
      // assigning[4] = '~';
      // assigning[5] = '1';
      // assigning[6] = '2';
      // assigning[7] = '3';
      // assigning[8] = '4';
      // assigning[9] = '5';
      // assigning[10] = '\n';
      // assigning[11] = '\0';
      // x = strlen(assigning);
      // publisher(1, assigning, x);
      // *****************************************************
      for (int i = 0; i < length; i++)
      {
        globalMessage[i] = content[i];                         // places tag data in global so version 1 code still works
      }
      parseData();                                             // Server request to write to the RFID tag (always reader A) (always command 02)
      updateTag();                                             // *[A][02]~[roadName]~[roadNumber]~[type]~[color]~[owner]\n  *A02~name~number~type~color~owner\n
      //                                                       // reader replies with the new RFID tag  *[A] [02]~[RFID]\n   *A02~1234567890\n
      break;
    case 23:
      visitingCar();
      break;
    case 98:
      onlineRely(content, length);
      break;
    default:
      break;
  }
 
}

// ******************************************************

void onlineRely(byte* content, unsigned int length)
{ char reader = content[1];
  if (reader == thisReader )
  { 
    char onLine[10];
    onLine[0] = '*';
    onLine[1] = thisReader;
    onLine[2] = '9';
    onLine[3] = '8';
    onLine[4] = '~';
    onLine[5] = thisReader;
    onLine[6] = '\n';
    onLine[7] = '\0';
    int count = strlen(onLine);
    publisher(4, onLine, count);                                        // *A98~thisReader\n 
    // Serial.println("onlineReply reader = " + reader); 
  }
}


// ******************* visiting car *********************
void visitingCar()
{
  char visitor[130];
  int next;

  if (carPresent)
  {
    visitor[0] = '*';                                  // 5 character header
    visitor[1] = thisReader;
    visitor[2] = '2';
    visitor[3] = '3';
    visitor[4] = '~';
    int y = carID.length();
    next =5;

    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carID[i];                  // insert the RFID tag
      ++next;
    }
    visitor[next] = '~';
    ++next;

    y = strlen(carRoadName); 
    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carRoadName[i];            // insert the road name
      ++next;
    }
    visitor[next] = '~';
    ++next;

    y = strlen(carRoadNumber); 
    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carRoadNumber[i];          // insert the road name
      ++next;
    }
    visitor[next] = '~';
    ++next;

    y = strlen(carType); ;
    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carType[i];                // insert the car type
      ++next;
    }
    visitor[next] = '~';
    ++next;

    y = strlen(carColor); 
    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carColor[i];               // insert the car color
      ++next;
    }
    visitor[next] = '~';
    ++next;

    y = strlen(carOwner);
    for (int i=0 ; i < y; ++i)
    {
      visitor[next] = carOwner[i];               // insert the car owner
      ++next;
    }
    visitor[next] = '\n';
    ++next;
    visitor[next] = '\0';
  }
  else
  {
    // must contain 7 elements to pass valid message testing
    char noVisitor[30] = "*A23~noVisitor~3~4~5~6~7\n"; //  test for noVisitor 
    int next = strlen(noVisitor);
    for (int i=0 ; i < next; ++i)
    {
      visitor[i] = noVisitor[i];                                                       
    }
    visitor[next+1] = '\0';
  }
  publisher(5, visitor, next);
}





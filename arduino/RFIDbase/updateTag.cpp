#ifndef tagHeader
#include "RFIDbase.h"
#endif


/*
   Timing windows from the MFRC522 are so short that capturing the read and a quick error status
   prevents a constant looping of the tag. Leaving mfrc522.PICC_HaltA() disabled allows repeat
   reads to the same tag. Variable carPresent blocks loop from reading tag until a hardware release switch
   is toggled.
*/

void updateTag() {
  int readCheck = 0;
  int getCheck = 0;


  bool error = false;



  error = showCardID();
  //error = true;                                       // ***** testing only
  if (!error) {
    error = authenication(_name);                       // sector 1
  }
  if (!error) {
    getCheck = writeBlock(_name);
    readCheck = readCheck + getCheck;
    getCheck = writeBlock(_number);
    readCheck = readCheck + getCheck;
    getCheck = writeBlock(_type);
    readCheck = readCheck + getCheck;
  }
  if (!error) {
    error = authenication(_color);                      // sector 2
  }
  if (!error) {
    getCheck = writeBlock(_color);
    readCheck = readCheck + getCheck;
    getCheck = writeBlock(_owner);
    readCheck = readCheck + getCheck; 
  }

  if (readCheck >= 5) {                                 // valid name/number/type/color/owner
    unsigned long uid = ((unsigned long)mfrc522.uid.uidByte[0] << 24UL) |
                        ((unsigned long)mfrc522.uid.uidByte[1] << 16UL) |
                        (mfrc522.uid.uidByte[2] << 8UL) |
                        mfrc522.uid.uidByte[3];
    carID = String(uid); 
    updateResult();
    displayLCD(5);                                       // display write complete
    carPresent = true;                                   // don't allow a new tag read
    displayLCD(10);                                      // display car data
     
  } else {
    carID = "000000";                                    // return bad RFID update to server
    updateResult();                                                         
    digitalWrite(redPin, HIGH);
    digitalWrite(greenPin, LOW);
    clearValues();                                       // fill data values with ?
    displayLCD(8);                                       // display write failure
    //delay(3000);
    carPresent = true;                                   // don't allow a new tag read
    badWrite = true;                                     // force a car release reset to clear display
    displayLCD(10);                                      // display ?????????
    
  }
}

/*
   A failed write needs something to display to the user on the LCD display. I chose "?"
   hoping that it expresses the unknown state of the tag write operation.
*/
void clearValues() {
  for (int i = 0; i < 16; ++i) {              // all these variables have lengths of 17
    carRoadName[i] = '?';                    // this function ensures that each array
    carRoadNumber[i] = '?';                  // element has a printable character
    carType[i] = '?';                        // for the LCD display and ensures that
    carColor[i] = '?';                       // each variable is always NULL terminated.
    carOwner[i] = '?';
  }
  carRoadName[16] = '\0';
  carRoadNumber[16] = '\0';
  carType[16] = '\0';
  carColor[16] = '\0';
  carOwner[16] = '\0';
}

void updateResult() {
  // Serial.println("got to update results function");
  pendingReply = true;
  displayLCD(13);                            // request pending
  if(carID.isEmpty())                        // assign the permanent engine -- used mostly for first time program launch  
  {
    carID = "0000000000";                    // missing tag UID return bad update 
  }
   
  int x = carID.length() ;
  --x;                                       // ignore the terminator
  // Serial.print("------  length =   ");
  // Serial.println(x);                         // *A02~carID\n;
  //                                         // carID -- new tag UID  -- or 0000000000 for bad update
  char updating[30];
  updating[0] = '*';
  updating[1] = thisReader;
  updating[2] = '0';
  updating[3] = '2';
  updating[4] = '~';
  for (int i=0 ; i <= x; ++i)
  {
    updating[i+5] = carID[i];
  }
  updating[x+6] = '\n';
  updating[x+7] = '\0';
  // Serial.print("------  message =   ");
  // for (int i = 0; i <= x+6; i++)
  // {
  //   Serial.print((char)updating[i]);
  // }
  // Serial.println();
  x = strlen(updating);
  // Serial.print("------  length =   ");
  // Serial.println(x);
  publisher(1, updating, x);                   // sending tag UID to database record
  // pendingReply = true;
  // displayLCD(16);
}


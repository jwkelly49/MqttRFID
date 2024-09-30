#ifndef tagHeader
#include "RFIDbase.h"
#endif


byte _name = 4;
byte _number = 5;
byte _type = 6;
byte _color = 8;
byte _owner = 9;
byte _id = 10;
bool carPresent = false;
/*
   Timing windows from the MFRC522 are so short that capturing the read and a quick error status
   prevents a constant looping of the tag. Leaving mfrc522.PICC_HaltA() disabled allows repeat
   reads to the same tag. Variable carPresent blocks loop from reading tag until a hardware switch
   is toggled.
*/

void scanTag() {
  // Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
  int readCheck = 0;
  int getCheck = 0;
    // char myTest5[30] = "*A98~got to scantag()\n";
    // int length = strlen(myTest5);
    // int pubTrigger = 4;                                                                              // on line
    // publisher(pubTrigger, myTest5, length);
    
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    carPresent = false;
    displayLCD(1);                                           // clear display 1
    //delay(1500);
    return;
  } else {
    displayLCD(9);                                           // display new car detected
  }

  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    displayLCD(1);                                         // clear display 1
    //delay(1500);
    return;
  } else {
    displayLCD(9);                                           // display new car detected
  }

  // Show some details of the PICC (that is: the tag/card)
  bool error = false;
  displayLCD(14);           // remove after testing
  error = showCardID();
  error = authenication(_name);                                // sector 1
  if (!error) {
    getCheck = readBlock(_name);
    readCheck = readCheck + getCheck;
    getCheck = readBlock(_number);
    readCheck = readCheck + getCheck;
    getCheck = readBlock(_type);
    readCheck = readCheck + getCheck;
  }
  if (!error) {
    error = authenication(_color);                             // sector 2
  }
  if (!error) {
    getCheck = readBlock(_color);
    readCheck = readCheck + getCheck;
    getCheck = readBlock(_owner);
    readCheck = readCheck + getCheck;
  }

  if (readCheck >= 5) {                                // valid name/number/type/color/owner
    char myTest5[30] = "*A98~readCheck >=5)\n";
    int length = strlen(myTest5);
    int pubTrigger = 4;                                                                              // on line
    publisher(pubTrigger, myTest5, length);
    carPresent = true;
    displayLCD(10);                                     // display car values
    unsigned long uid = ((unsigned long)mfrc522.uid.uidByte[0] << 24UL) |
                        ((unsigned long)mfrc522.uid.uidByte[1] << 16UL) |
                        (mfrc522.uid.uidByte[2] << 8UL) |
                        mfrc522.uid.uidByte[3];
    carID = String(uid);
    digitalWrite(greenPin, HIGH);
  } else {
    displayLCD(12);                                     // display read failure
    carPresent = false;
    digitalWrite(greenPin, LOW);
    delay(1000);
    displayLCD(1);                                      // clear display
  }
}

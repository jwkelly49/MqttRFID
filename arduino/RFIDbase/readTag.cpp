#ifndef tagHeader
#include "RFIDbase.h"
#endif




//****************** READ BLOCK ****************************************************
int readBlock(byte whichBlock) {
  // Read data from the block 4
  //  authenication(whichBlock);
  //Serial.print(F("Reading data from block ")); Serial.print(whichBlock);
  //Serial.println(F(" ..."));
      char myTest5[30] = "*A98~got to readBlock()\n";
    int length = strlen(myTest5);
    int pubTrigger = 4;                                                                              // on line
    publisher(pubTrigger, myTest5, length);
  bool error = false;
  int result = 0;       // preset for bad read
  status = (MFRC522::StatusCode) mfrc522.MIFARE_Read(whichBlock, buffer, &size);
  if (status != MFRC522::STATUS_OK) {
    error = true;
  }
  String a = String(whichBlock);
  byte fromBuffer;
  char toChar;
  char validChar;
  int block = whichBlock;
  bool skip = false;
  if (!error) {
    /*
       If reading a new unprogrammed tag the factory default data is UNKNOWN but
       anecdotal observation has showed that it is usually NULL bytes. The for loops
       used here replace the nulls with the period (or dot) character to show the
       user that no data is recorded on this tag.
    */
    switch (block) {
      case 4:  // road name
        for (int i = 0; i < 16; ++i) {              // fill each element with "space" character
          carRoadName[i] = ' ';                    // before loading new characters (again variable string lengths
        }
        for (byte i = 0; i < 16; i++) {
          toChar = (char)buffer[i];
          if (isPrintable(toChar)) {
            carRoadName[i] = toChar;
            skip = true;
          } else {
            if (skip == false) {
              toChar = '.';
              carRoadName[i] = toChar;
            }
          }
        }
        break;
      case 5:  // road number
        for (int i = 0; i < 16; ++i) {
          carRoadNumber[i] = ' ';
        }
        for (byte i = 0; i < 16; i++) {
          toChar = (char)buffer[i];
          if (isPrintable(toChar)) {
            carRoadNumber[i] = toChar;
            skip = true;
          } else {
            if (skip == false) {
              toChar = '.';
              carRoadNumber[i] = toChar;
            }
          }
        }
        break;
      case 6:  // type
        for (int i = 0; i < 16; ++i) {
          carType[i] = ' ';
        }
        for (byte i = 0; i < 16; i++) {
          toChar = (char)buffer[i];
          if (isPrintable(toChar)) {
            carType[i] = toChar;
            skip = true;
          } else {
            if (skip == false) {
              toChar = '.';
              carType[i] = toChar;
            }
          }
        }
        break;
      case 8:  //color
        for (int i = 0; i < 16; ++i) {
          carColor[i] = ' ';
        }
        for (byte i = 0; i < 16; i++) {
          toChar = (char)buffer[i];
          if (isPrintable(toChar)) {
            carColor[i] = toChar;
            skip = true;
          } else {
            if (skip == false) {
              toChar = '.';
              carColor[i] = toChar;
            }
          }
        }
        break;
      case 9:  //owner
        for (int i = 0; i < 16; ++i) {
          carOwner[i] = ' ';
        }
        for (byte i = 0; i < 16; i++) {
          toChar = (char)buffer[i];
          if (isPrintable(toChar)) {
            carOwner[i] = toChar;
            skip = true;
          } else {
            if (skip == false) {
              toChar = '.';
              carOwner[i] = toChar;
            }
          }
        }
        break;
    }

    result = 1;    // should be good update
  }
  return result;   // good / bad update
}

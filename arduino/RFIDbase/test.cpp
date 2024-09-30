#ifndef tagHeader
#include "RFIDbase.h"
#endif



//****************** AUTHENICATION ***************************************************
bool authenication(byte block) {
  // Authenticate using key A
    //   char myTest5[30] = "*A98~got to authenication()\n";
    // int length = strlen(myTest5);
    // int pubTrigger = 4;                                                                              // on line
    // publisher(pubTrigger, myTest5, length);
  byte trailingBlock = setTrailingBlock(block);
  status = (MFRC522::StatusCode) mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailingBlock, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    return true;
  }
  return false;
}

//********************** DUMP_BYTE_ARRAY ************************************************
// Helper routine to dump a byte array as hex values to Serial.
void dump_byte_array(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    //prepData(buffer[i] < 0x10 ? " 0" : " ");
    //String x = String(buffer[i], HEX);
    //prepData(x);
  }
}

//******************** DUMP FUlL SECTOR ****************************************
void dumpFullSector(byte sector) {
  // Dump the sector data
  mfrc522.PICC_DumpMifareClassicSectorToSerial(&(mfrc522.uid), &key, sector);
}

//******************** Show card ID *******************************************
bool showCardID() {
    // char myTest5[30] = "*A98~got to showCardIDg()\n";
    // int length = strlen(myTest5);
    // int pubTrigger = 4;                                                                              // on line
    // publisher(pubTrigger, myTest5, length);
    // delay(3000);     // remove after testing
  // Show some details of the PICC (that is: the tag/card)
  bool error = false;
  MFRC522::PICC_Type piccType = mfrc522.PICC_GetType(mfrc522.uid.sak);
  // Check for compatibility
  if (    piccType != MFRC522::PICC_TYPE_MIFARE_MINI
          &&  piccType != MFRC522::PICC_TYPE_MIFARE_1K
          &&  piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    error = true;
  }
  return error;
}

//***********SET TRAILING BLOCK *****************************************************
byte setTrailingBlock(byte block) {
  byte trail;
  if ( block > 3 && block < 7) {
    trail = 7; 
  };     // sector 1
  if ( block > 7 && block < 11) {
    trail = 11; 
  };    // sector 2
  if ( block > 11 && block < 15) {
    trail = 15; 
  };    // sector 3
  if ( block > 15 && block < 19) {
    trail = 19; 
  };    // sector 4
  if ( block > 19 && block < 23) {
    trail = 23; 
  };    // sector 5
  if ( block > 23 && block < 27) {
    trail = 27; 
  };    // sector 6
  if ( block > 27 && block < 31) {
    trail = 31; 
  };    // sector 7
  if ( block > 31 && block < 35) {
    trail = 35; 
  };    // sector 8
  if ( block > 35 && block < 39) {
    trail = 39; 
  };    // sector 9
  if ( block > 39 && block < 43) {
    trail = 43; 
  };    // sector 10
  if ( block > 43 && block < 47) {
    trail = 47; 
  };    // sector 11
  if ( block > 47 && block < 51) {
    trail = 51; 
  };    // sector 12
  if ( block > 51 && block < 55) {
    trail = 55; 
  };    // sector 13
  if ( block > 55 && block < 59) {
    trail = 59; 
  };    // sector 14
  if ( block > 59 && block < 63) {
    trail = 63; 
  };    // sector 15
  return trail; 
}

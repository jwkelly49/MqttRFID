#ifndef tagHeader
#include "RFIDbase.h"
#endif


byte dataBlock[16];
//*************** SET DATA BLOCK ****************************************************
void setDataBlock(String storeIt) {
  byte toByte;
  for (byte i = 0; i < 16; i++) {
    toByte = (char)storeIt[i];
    dataBlock[i] = toByte;
  }
}

//************************* WRITE BLOCK ****************************************************
int writeBlock(byte whichBlock) {
  // Write data to the block
  //authenication(whichBlock);
  //    bool error = false;
  int result = 0;       // preset for bad write
  int block = whichBlock;
  switch (block) {
    case 4:  // road name
      setDataBlock(carRoadName);
      break;
    case 5:  // road number
      setDataBlock(carRoadNumber);
      break;
    case 6:  // type
      setDataBlock(carType);
      break;
    case 8:  //color
      setDataBlock(carColor);
      break;
    case 9:  //owner
      setDataBlock(carOwner);
      break;
  }

  //prepData("Writing data into block "); prepData(String(whichBlock));
  dump_byte_array(dataBlock, 16); 
  status = (MFRC522::StatusCode) mfrc522.MIFARE_Write(whichBlock, dataBlock, 16);
  if (status != MFRC522::STATUS_OK) {
    return 0;
  }
  result = checkWrite(whichBlock);
  return result;
}

//********************* CHECK WRITE *****************************************************
int checkWrite(byte checkBlock) {
  // Check that data in block is what we have written
  // by counting the number of bytes that are equal
  readBlock(checkBlock);                      // read back what was written
  byte count = 0;
  for (byte i = 0; i < 16; i++) {
    // Compare buffer (= what we've read) with checkBlock (= what we've written)
    if (buffer[i] == dataBlock[i])
      count++;
  }
  //prepData("Number of bytes that match = "); 
  if (count == 16) {
    return 1;
  } else {
    return 0;
  }
}

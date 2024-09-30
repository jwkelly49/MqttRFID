#ifndef tagHeader
#include "RFIDbase.h"
#endif



//***************  pick up ************************
/*
   Tell the server to transfer the database record for this RFID tag from this
   location (reader) to the assigned engine.
*/
void pickUp() {
  if (assignedEngine == "000000") {                                                           // power up default to test against
    displayLCD(6);                                                                             // must assign engine
    if (carPresent) {
      displayLCD(10);                                                                          // car information
    } else {
      displayLCD(1);                                                                           // clear screen
    }
  }
  else
  {       
    // Serial.println("got to pick up switch");
    pendingReply = true;
    displayLCD(13);                                                                            // request pending
    if(carID.isEmpty())                                                                        // assign the permanent engine -- used mostly for first time program launch  
    {
      carID = "0123456789";                                                                    // when database is empty. But available with fresh reboot of the Nano 
    }
    
    //carID = "0123456789";
    //assignedEngine = "0123456789";
    int y = assignedEngine.length();
    //--y;                                                                                       // ignore the terminator
    int x = carID.length();
    //--x;                                                                                       // ignore the terminator
    // Serial.print("------  Y length =   ");
    // Serial.println(y);                                                                         // *A22~assignedEngine~carID\n;
    // Serial.print("------  X length =   ");
    // Serial.println(x);
    //                                                                                         // carID -- check if engine before linking with this reader (in proccessReply)
    char picking[50];
    picking[0] = '*';                                                                         // 5 character header
    picking[1] = thisReader;
    picking[2] = '2';
    picking[3] = '2';
    picking[4] = '~';
    for (int i=0 ; i < y; ++i)
    {
      picking[i+5] = assignedEngine[i];                                                       // insert the engine
    }
    int z = 5+y;                                                                             // header + assignedEngine size + seperator
    int a = z + x;                                                                             // length of header/engine/seperator and car
    picking[z] = '~';
    ++z;                                                                                       // starting point for carID insert (i+z)
    for (int i=0 ; i < x; ++i)                                                                // insert the car
    {
      picking[i+z] = carID[i];
    }
    picking[a+1] = '\n';                                                                      // new line used by computer as end of string
    picking[a+2] = '\0';                                                                      // c++ string terminator
    // Serial.print("------  message =   ");
    // for (int i = 0; i <= a+2; i++)
    // {
    //   Serial.print((char)picking[i]);
    // }
    // Serial.println();
    x = strlen(picking);
    // Serial.print("------  length =   ");
    // Serial.println(x);
    publisher(2, picking, x);                   // sending a drop off car request
    pendingReply = true;
    displayLCD(16);
  }    
}

//****************** drop off **********************
/*
   Tell the server to transfer the database record for this RFID tag from the
   engine to this location (reader.)
*/
void dropOff() {
  if (assignedEngine == "000000") {                                                           // power up default to test against
    displayLCD(6);                                                                             // must assign engine
    if (carPresent) {
      displayLCD(10);                                                                          // car information
    } else {
      displayLCD(1);                                                                           // clear screen
    }
  }
  else
  {       
    // Serial.println("got to drop off switch");
    pendingReply = true;
    displayLCD(13);                                                                            // request pending
    if(carID.isEmpty())                                                                        // assign the permanent engine -- used mostly for first time program launch  
    {
      carID = "0123456789";                                                                    // when database is empty. But available with fresh reboot of the Nano 
    }
    
    //carID = "0123456789";
    //assignedEngine = "0123456789";
    int y = assignedEngine.length();
    //--y;                                                                                       // ignore the terminator
    int x = carID.length();
    //--x;                                                                                       // ignore the terminator
    // Serial.print("------  Y length =   ");
    // Serial.println(y);                                                                         // *A21~assignedEngine~carID\n;
    // Serial.print("------  X length =   ");
    // Serial.println(x);
    //                                                                                         // carID -- check if engine before linking with this reader (in proccessReply)
    char dropping[50];
    dropping[0] = '*';                                                                         // 5 character header
    dropping[1] = thisReader;
    dropping[2] = '2';
    dropping[3] = '1';
    dropping[4] = '~';
    for (int i=0 ; i < y; ++i)
    {
      dropping[i+5] = assignedEngine[i];                                                       // insert the engine
    }
    int z = 5+y;                                                                             // header + assignedEngine size + seperator
    int a = z + x;                                                                             // length of header/engine/seperator and car
    dropping[z] = '~';
    ++z;                                                                                       // starting point for carID insert (i+z)
    for (int i=0 ; i < x; ++i)                                                                // insert the car
    {
      dropping[i+z] = carID[i];
    }
    dropping[a+1] = '\n';                                                                      // new line used by computer as end of string
    dropping[a+2] = '\0';                                                                      // c++ string terminator
    // Serial.print("------  message =   ");
    // for (int i = 0; i <= a+2; i++)
    // {
    //   Serial.print((char)dropping[i]);
    // }
    // Serial.println();
    x = strlen(dropping);
    // Serial.print("------  length =   ");
    // Serial.println(x);
    publisher(2, dropping, x);                   // sending a drop off car request
    pendingReply = true;
    displayLCD(16);
  }  
}

//******************* assign it *********************
/*
   Tell the server to link the RFID tag of this engine to
   this location (reader.) This allows movement of rolling stock
   between locations (readers.)
*/
void assignIt() {
  // Serial.println("got to assign engine switch");
  pendingReply = true;
  displayLCD(13);                                                                            // request pending
  if(carID.isEmpty())                                                                        // assign the permanent engine -- used mostly for first time program launch  
  {
    carID = "0123456789";                                                                    // when database is empty. But available with fresh reboot of the Nano 
  }
   
  int x = carID.length() ;
  --x;                                                                                       // ignore the terminator
  // Serial.print("------  length =   ");
  // Serial.println(x);                                                                         // *A20~carID\n;
  //                                                                                         // carID -- check if engine before linking with this reader (in proccessReply)
  char assigning[30];
  assigning[0] = '*';
  assigning[1] = thisReader;
  assigning[2] = '2';
  assigning[3] = '0';
  assigning[4] = '~';
  for (int i=0 ; i <= x; ++i)
  {
    assigning[i+5] = carID[i];
  }
  assigning[x+6] = '\n';
  assigning[x+7] = '\0';
  // Serial.print("------  message =   ");
  // for (int i = 0; i <= x+6; i++)
  // {
  //   Serial.print((char)assigning[i]);
  // }
  // Serial.println();
  x = strlen(assigning);
  // Serial.print("------  length =   ");
  // Serial.println(x);
  publisher(2, assigning, x);                   // sending an assign engine request
  pendingReply = true;
  displayLCD(16);
}

//******************* car release *********************
void releaseCar(){
  carPresent = false;
  mfrc522.PCD_StopCrypto1();                // must stop or next tag will never read
  delay(500);
  digitalWrite(redPin, LOW);
  digitalWrite(greenPin, LOW);
  for (int i = 0; i < 3; ++i) 
  {
    displayLCD(15);                                 // car released
    badWrite = false;
    delay(1500);                              
    displayLCD(1);                                 // clear screen
    delay(1500);
  }
}


/*
   Tell the nano to clear the block on the main loop scanning
   for new tags (cars) and rests related variables
*/

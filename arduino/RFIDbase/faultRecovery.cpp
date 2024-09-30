/*
undeveloped module -- but an interesting idea
so I left the code for a future project.
*/


// #ifndef tagHeader
// #include "RFIDbase.h"
// #endif

// bool wifiFault = false;                                         // Maybe use as a fault recovery trigger


// //****************************************************

// void faultRecovery(int var){                                    // Maybe display a meesage on the LED screen
//   wifiFault = true;                                             // Maybe trigger other program logic
//   //Serial.println("Got to fault recovery ...");
//   String text;
//   switch (var) 
//   {
//     case 1:
//       text ="Failed to mount file system";
//       break;  
//     case 2:
//       text = "json config file not found";
//       break;
//     case 3:
//       text = "json config file did not read correctly";
//       break;
//     case 4:
//       text = "Error deserializing JSON data";
//       break;
//     case 5:
//       text = "failed to open config file for writing";
//       break;
//     case 6:
//       text = "Failed to write to config file";
//       break;
//     case 7:
//       text = "Normal startup failed to connect and hit setup timeout";
//       break;
//     case 8:
//       text = "Configure mode failed to connect and hit setup timeout";
//       break;
//     case 9:
//       text = "No WiFi connection.....";
//       break;
//     case 10:
//       text = "No broker connection.....";
//       break;
//     default:
//       text = "unknown error reported";
//       break;
//   }
   
// }

// void publishError(String text)
// {
//   int y = text.length();
//   char error[100];
//   error[0] = '*';                                                                         // 5 character header
//   error[1] = thisReader;
//   error[2] = '9';
//   error[3] = '9';
//   error[4] = '~';
//   for (int i=0 ; i < y; ++i)
//   {
//     error[i+5] = text[i];                                                      
//   }
//   error[y+1] = '\n';                                                                      // new line used by computer as end of string
//   error[y+2] = '\0';                                                                      // c++ string terminator
//   int pubTrigger = 3;                                                                               
//   int length = strlen(error);
//   publisher(pubTrigger, error, length);                                                  // *A99~errorString\n
// }

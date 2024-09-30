#ifndef tagHeader
#include "RFIDbase.h"
#endif



/*
 * Try flashing LEDs but couldn't get COMMs to be stable.
 * I left the logic thinking it might be used with a
 * different chip like the esp32 (with 2 CPUs)
 */


//bool redFlash = false;
//bool redSolid = false;
//bool greenFlash = false;
//bool greenSolid = false;

//void flashLoop() {
//  unsigned long looping = millis();
//  unsigned long flash = millis();
//  unsigned long x;
//  bool z = false;
//  bool flasher;
//  int count = 0;
//  while (count < 6) {
//    x = looping - flash;                               // create a half second timer
//    if (x >= 500) {
//      flash = looping;
//      if (!z) {                                          // toggle the flasher
//        z = true;
//        flasher = true;
//      } else {
//        z = false;
//        flasher = false;
//      }
//      ++count;
//    }
//    if ((redFlash and flasher) or redSolid ) {           // NOTE: LED delays can't be used
//      digitalWrite(redPin, HIGH);                        // as they prevent comm from syncing
//    } else {                                             
//      digitalWrite(redPin, LOW);
//    }
//    if ((greenFlash and flasher) or greenSolid ) {
//      digitalWrite(greenPin, HIGH);
//    } else {
//      digitalWrite(greenPin, LOW);
//    }
//    looping =  millis();
//  }
//}

void displayLCD(int value) {
  int show = value;
  switch (show) {
    case 1:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print(" ");
      lcd.setCursor(0, 2);
      lcd.print(" ");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 2:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print("Drop");
      lcd.setCursor(0, 2);
      lcd.print("Completed");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 3:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print("Pick");
      lcd.setCursor(0, 2);
      lcd.print("Completed");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 4:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print("");
      lcd.setCursor(0, 1);
      lcd.print("Engine");
      lcd.setCursor(0, 2);
      lcd.print("Assigned");
      lcd.setCursor(0, 3);
      lcd.print("");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 5:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print("RFID");
      lcd.setCursor(0, 1);
      lcd.print("Write");
      lcd.setCursor(0, 2);
      lcd.print("Completed");
      lcd.setCursor(0, 3);
      lcd.print("");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 6:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Must");
      lcd.setCursor(0, 1);
      lcd.print("Assign");
      lcd.setCursor(0, 2);
      lcd.print("Engine");
      lcd.setCursor(0, 3);
      lcd.print("First");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, HIGH);
      break;
    case 7:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = true;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Pick / Drop");
      lcd.setCursor(0, 1);
      lcd.print("or");
      lcd.setCursor(0, 2);
      lcd.print("Engine Assign");
      lcd.setCursor(0, 3);
      lcd.print("FAILED");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, HIGH);
      break;
    case 8:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = true;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("RFID write");
      lcd.setCursor(0, 1);
      lcd.print("FAILED");
      lcd.setCursor(0, 2);
      lcd.print("Release car");
      lcd.setCursor(0, 3);
      lcd.print("to try again");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, HIGH);
      break;
    case 9:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print("New");
      lcd.setCursor(0, 1);
      lcd.print("Car");
      lcd.setCursor(0, 2);
      lcd.print("Detected");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 10:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print(carRoadName);
      lcd.setCursor(0, 1);
      lcd.print(carRoadNumber);
      lcd.setCursor(0, 2);
      lcd.print(carType);
      lcd.setCursor(0, 3);
      lcd.print(carColor);
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 11:
//      greenFlash = false;
//      greenSolid = true;
//      redFlash = false;
//      redSolid = false;
      lcd.clear();
      lcd.noBlink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print(carOwner);
      lcd.setCursor(0, 2);
      lcd.print(" ");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, HIGH);
      digitalWrite(redPin, LOW);
      break;
    case 12:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = true;
      lcd.clear();
      //lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Car");
      lcd.setCursor(0, 1);
      lcd.print("Read");
      lcd.setCursor(0, 2);
      lcd.print("FAILED");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, HIGH);
      break;
    case 13:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Request");
      lcd.setCursor(0, 1);
      lcd.print("Pending");
      lcd.setCursor(0, 2);
      lcd.print(" ");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 14:
//      greenFlash = false;
//      greenSolid = false;
//      redFlash = false;
//      redSolid = true;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Bad");
      lcd.setCursor(0, 1);
      lcd.print("Command");
      lcd.setCursor(0, 2);
      lcd.print("Received");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, HIGH);
      break;
    case 15:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print("Car");
      lcd.setCursor(0, 2);
      lcd.print("Released");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 16:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print(" ");
      lcd.setCursor(0, 1);
      lcd.print("Reply");
      lcd.setCursor(0, 2);
      lcd.print("Pending");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 17:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("No WiFi");
      lcd.setCursor(0, 1);
      lcd.print("Auto Reboot ! Min");
      lcd.setCursor(0, 2);
      lcd.print("Or use the");
      lcd.setCursor(0, 3);
      lcd.print("Config Button");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 18:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("No");
      lcd.setCursor(0, 1);
      lcd.print("Broker");
      lcd.setCursor(0, 2);
      lcd.print("Connection");
      lcd.setCursor(0, 3);
      lcd.print("Established");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 19:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("Reader");
      lcd.setCursor(0, 1);
      lcd.print("power up");
      lcd.setCursor(0, 2);
      lcd.print("mode");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 20:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("WiFi");
      lcd.setCursor(0, 1);
      lcd.print("Configuration");
      lcd.setCursor(0, 2);
      lcd.print("Mode");
      lcd.setCursor(0, 3);
      lcd.print("");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 21:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("WiFi");
      lcd.setCursor(0, 1);
      lcd.print("Normal");
      lcd.setCursor(0, 2);
      lcd.print("Mode");
      lcd.setCursor(0, 3);
      lcd.print(" ");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;
    case 22:
//      greenFlash = true;
//      greenSolid = false;
//      redFlash = true;
//      redSolid = false;
      lcd.clear();
      lcd.blink();
      lcd.setCursor(0, 0);
      lcd.print("WiFi");
      lcd.setCursor(0, 1);
      lcd.print("Config");
      lcd.setCursor(0, 2);
      lcd.print("Mode");
      lcd.setCursor(0, 3);
      lcd.print("active");
      //flashLoop();
      digitalWrite(greenPin, LOW);
      digitalWrite(redPin, LOW);
      break;                              
    default:
      // do nothing
      break;
  }

}
void toggle() {
  static int show = 10;
  if (show == 10) {
    displayLCD(10);       // show car information
    show = 11;
    return;
  }
  if (show == 11) {
    if (!badWrite) {
      displayLCD(11);     // show owner's name
      show = 10;
      return;
    } else {
      displayLCD(8);     // show bad write
      show = 10;
      return;
    }
  }
}

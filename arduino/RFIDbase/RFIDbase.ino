#ifndef tagHeader
#include "RFIDbase.h"
#endif


/*
WiFiManager with saved textboxes Demo
wfm-text-save-demo.ino
Saves data in JSON file on ESP32
Uses SPIFFS

DroneBot Workshop 2022
https://dronebotworkshop.com

Functions based upon sketch by Brian Lough
https://github.com/witnessmenow/ESP32-WiFi-Manager-Examples

code modified by Jim Kelly
*/

#define RST_PIN         9
#define SS_PIN          10
MFRC522 mfrc522(SS_PIN, RST_PIN);         // Create MFRC522 instance. (tag reader)
MFRC522::MIFARE_Key key;
MFRC522::StatusCode status;                      
LiquidCrystal_I2C lcd(0x27, 20, 4);

char thisReader;                           // hold the address of this reader
byte buffer[18];
byte size = sizeof(buffer);
bool buttonPressed = false;
bool pendingReply = false;
int retry = 0;
char globalMessage[100];                   // hold valid message interfaces back to version tag logic
char carRoadName[17];
char carRoadNumber[17];
char carType[17];
char carColor[17];
char carOwner[17];
String carID;
String assignedEngine = "000000";          // default RFID of engine at startup of program
bool badWrite = false;
int configTimeout = 180;                   // Web service run time in seconds
int timeout = 10;

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

// Variables to hold data from custom textboxes
char nodeAddress[5] = "A";                // Default Reader address
char hostName[50] = "10.0.0.13";          // Default Mosquitto broker IP address


//WiFiManager wm;                           // Define WiFiManager Object

bool shouldSaveConfig = false;            // Flag for saving data
bool forceConfig = false;                 // no saved data found

// *********************************************************

void setup()
{
  // Serial.println("starting setup()");
  // local panel hardware
  pinMode(pickPin, INPUT_PULLUP);                                 // picked
  pinMode(dropPin, INPUT_PULLUP);                                 // drop
  pinMode(assignPin, INPUT_PULLUP);                               // assign
  pinMode(releasePin, INPUT_PULLUP);                              // car release
  pinMode(redPin, OUTPUT);                                        // Red LED
  pinMode(greenPin, OUTPUT);                                      // Green LED
  pinMode(TRIGGER_PIN, INPUT_PULLUP);                             // trigger WiFi configuration

  pinMode(RST_PIN, OUTPUT);                                       // RFID reset
  digitalWrite(RST_PIN, HIGH);                                    // set rest for normal mode
  
  lcd.init();                                                     //initialize lcd screen
  lcd.backlight();                                                // turn on the backlight
  lcd.setCursor(0, 0);
  SPI.begin();                                                    // Init SPI bus
  mfrc522.PCD_Init();                                             // Init MFRC522 card (RFID)
  mfrc522.PCD_SetAntennaGain( MFRC522::RxGain_max);               // allows more distance between reader and the tag

  for (byte i = 0; i < 6; i++) {                                  // Prepare the key (using key A)
    key.keyByte[i] = 0xFF;                                        // using FFFFFFFFFFFFh which is the default at chip delivery from the factory
  }

  displayLCD(19);
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);                                            // explicitly set mode for WiFiManager 
  WiFi.setTxPower(WIFI_POWER_8_5dBm);                             // Antenna power
  bool spiffsSetup = loadConfigFile();                            // get saved reader address + broker IP address
  if (!spiffsSetup)
  {
    // Serial.println(F("Forcing config mode as there is no saved config"));
    forceConfig = true;
  } 

  WiFiManager wm;                                                 // Define WiFiManager Object
  wm.setEnableConfigPortal(false);                                // only allow button (or no saved data) to start config
  wm.setConnectTimeout(timeout); 
  wm.setConnectRetries(8);                                        // allows retries with router before failing

  if (!wm.autoConnect("Normal Startup"))
  {
    // Serial.println("auto connect failed to connect and hit timeout");
    forceConfig = true;                                           // used in main loop -- checkButton()
  }

  if (!forceConfig){
        
    // Serial.println("connecting to WiFi ...");
    // Serial.println("");
    // Serial.println("WiFi connected");
    // Serial.print("IP address: ");
    // Serial.println(WiFi.localIP());
    SetupMqtt();                                                  // if you get here you have connected to the WiFi
    ConnectToMqtt();
  }  
  
  // if ( digitalRead(TRIGGER_PIN) == LOW) {              // testing foe noise triggers
  //   Serial.println("trigger pin from setup is low");
  // }else{
  //   Serial.println("trigger pin from setup is high");  // expected state
  // }

} // end of setup

// **********************************************************

void checkButton(){
  // check for button press or a need to configure the WiFi connection
  if ( digitalRead(TRIGGER_PIN) == LOW || forceConfig) {
    delay(50);                                               // debounce/press-hold
    if( digitalRead(TRIGGER_PIN) == LOW || forceConfig ){
      delay(3000);                                           // still holding button for 3000 ms, reset settings
      if( digitalRead(TRIGGER_PIN) == LOW || forceConfig ){
        displayLCD(20);
        // if(forceConfig){
        //   Serial.println("Button Pressed forceConfig is TRUE");
        // }
        WiFiManager wm;                                     // Define WiFiManager Object
        wm.setConfigPortalTimeout(configTimeout);           // set on demand timer
        WiFi.setTxPower(WIFI_POWER_8_5dBm);
        wm.setSaveConfigCallback(saveConfigCallback);
        wm.setAPCallback(configModeCallback);
        wm.setConfigPortalBlocking(true);
        wm.setCleanConnect(true);
        wm.setBreakAfterConfig(true);
        wm.setConnectRetries(8);
        wm.setSaveParamsCallback(saveParamCallback);
        wm.setDebugOutput(true);                            // enable debug information
        wm.setBreakAfterConfig(true);                       // always exit configportal even if wifi save fails

        // Custom elements
        // Text box (String) - 1 characters maximum
        WiFiManagerParameter custom_text_box_2("key_text2", "Enter this reader's address (A-Y) - 1 charater only", "A", 1); 
        // Text box (String) - 50 characters maximum
        WiFiManagerParameter custom_text_box_3("key_text3", "Enter the IP address for your Mosquitto broker", "10.0.0.13", 50);

        // Add all defined parameters
        //wm.addParameter(&custom_text_box);
        wm.addParameter(&custom_text_box_2);
        wm.addParameter(&custom_text_box_3);  

        if (!wm.startConfigPortal("New Reader"))
       {
          // Serial.println("failed to connect and hit startconfig timeout");
          delay(1000);
          ESP.restart();
          delay(8000);
        }
        strncpy(nodeAddress, custom_text_box_2.getValue(), sizeof(nodeAddress));       // Used to save the custom parameters to File System
        strncpy(hostName, custom_text_box_3.getValue(), sizeof(hostName));
        if (shouldSaveConfig)                                                          // set in callback
        {
          saveConfigFile();                                                            // Save the custom parameters to FS
        } 
        ESP.restart();
      }
      
    }

  }
  
}

//********************************************************

unsigned long tagTimer =  millis();                    // time to test for emergency release
unsigned long loopTimer = millis();                    // holds current time
int picked;                                            // panel switch  active LOW
int drop;                                              // panel switch  active LOW
int assign;                                            // panel switch  active LOW
int carRelease;                                        // panel switch  active LOW
int retryReset = 0;                                    // count - emergency release for pending replies
int pubTrigger = 0;                                    // select what to publish  ---- 0 = nothing to publish
bool firstPass = true;                                 // deBounce switches on program startup

//**********************************************************

void loop() 
{   
  checkButton();                                       // is configuration portal requested?

  if (WiFi.status() != WL_CONNECTED) 
  {
    //Serial.println("WiFi Connection lost"); 
    displayLCD(17); 
    //faultRecovery(9);
    int count = 0;
    while (WiFi.status() != WL_CONNECTED){
      delay(10000);                                   // 10 seconds between retries
      ++count;
      if(count >5){                                   // 1 minute delay before reboot
        ESP.restart();
      }
    }
    
    ESP.restart();
  }

  if (!mqttClient.connected()){                             // Readers powered up before the broker was started (computer reboot etc)
    //Serial.println("Mosquitto Broker connection lost");
    int count = 0;
    displayLCD(18);
    //faultRecovery(10);
    while(!mqttClient.connected()){
      delay(10000);                                    // 10 seconds between retries
      ConnectToMqtt();
      ++count;
      if(count >11){                                   // 2 minute delay before reboot
        ESP.restart();
      }
    }
  }

  mqttClient.loop();

  if (loopTimer - tagTimer > 1500) {
    if (pendingReply) {                                // emergency release of retry in the event
      ++retryReset;                                    // the server never returns a reply to a request
      if (retryReset > 1) {
        retryReset = 0;
        pendingReply = false;
        displayLCD(7);                                 // pending reply failed
        delay(3000);
        displayLCD(10);                                // car information
      }
    }
    if (!pendingReply && !buttonPressed)               // don't want to miss comm reply
    {
      if (!carPresent) 
      {
        scanTag();                                     // check for new car on reader
      }
      if (carPresent) 
      {
        toggle();                                      // toggle LCD display - car info / owner
      }
    }
    tagTimer = loopTimer;                              // reset tag timer 
  }   // end loop timer

  picked = digitalRead(pickPin);                                        // remove car from this location and add to engine
  drop = digitalRead(dropPin);                                          // remove car from engine and add to this location
  assign = digitalRead(assignPin);                                      // link engine at this location and with the computer
  carRelease = digitalRead(releasePin);                                 // reset reader for next car 
  delay(30);                                                            // check for noisy inputs
  picked = digitalRead(pickPin);                                        
  drop = digitalRead(dropPin);                                          
  assign = digitalRead(assignPin);                                      
  carRelease = digitalRead(releasePin);                                 
  
  if (!pendingReply && !buttonPressed && !firstPass) {                                                 // reader not communicating with server

    if (picked == LOW && drop == HIGH && assign == HIGH && carRelease == HIGH && !buttonPressed) {     // test that only one switch has been pressed
      buttonPressed = true;                                                                            // prevent multiply triggers for pressed button
      pickUp();                                                                                        // test for low input (0)
    }
    else if (drop == LOW && picked == HIGH && assign == HIGH && carRelease == HIGH && !buttonPressed) {
      buttonPressed = true;
      dropOff();
    }
    else if (assign == LOW && drop == HIGH && picked == HIGH && carRelease == HIGH && !buttonPressed) {
      buttonPressed = true;
      assignIt();
    }
    else if (carRelease == LOW && drop == HIGH && picked == HIGH && assign == HIGH && !buttonPressed) {
      buttonPressed = true;
      releaseCar();
    }
  }

  if ( drop == HIGH && picked == HIGH && assign == HIGH && carRelease == HIGH && !pendingReply && buttonPressed) {
    buttonPressed = false;                                                                             // reset button block after action has been completed
    delay(10);                                                                                         // regular button deBounce
    if ( drop == LOW || picked == LOW || assign == LOW || carRelease == LOW) {
      buttonPressed = true;                                                                            // someone still holding a button
    }
  } 

  picked = digitalRead(pickPin);                                                                       // remove car from this location and add to engine
  drop = digitalRead(dropPin);                                                                         // remove car from engine and add to this location
  assign = digitalRead(assignPin);                                                                     // link engine at this location and with the computer
  carRelease = digitalRead(releasePin);                                                                // reset reader for next car 


  if(firstPass){
    buttonPressed = false;                                                                             // enable buttons for use (backup for button deBounce)
    firstPass = false;                                                                                 // release start up noise deBounce
  }

  loopTimer =  millis();

} // end of loop


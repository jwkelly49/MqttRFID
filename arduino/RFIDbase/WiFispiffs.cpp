#ifndef tagHeader
#include "RFIDbase.h"
#endif

#define ESP_DRD_USE_SPIFFS true
// JSON configuration file
#define JSON_CONFIG_FILE "/rfid.json"
#define FORMAT_SPIFFS_IF_FAILED true

// **************************************************************

bool loadConfigFile()
// Load existing configuration file
{ 
  // Serial.println("Mounting File System..."); 
    /*
    I implimented the next 5 lines when reader continuously failed to connect
    to the router. It saw the SSID but had a bad IP address. Watch for "Formatted
    the FS" then disconnect and comment out these lines. Use "upload with progrsmmer"
    to reload. Jumper grd to b1 hold in reset button while applying power .... then remove
    the jumper.
    */
    // SPIFFS.format();
    // delay(1000);
    // Serial.println("first Format of the FS");
    // delay(15000);
    // ESP.restart();
  
  if (SPIFFS.begin(true))
  {
    // Serial.println("mounted file system");
    delay(50);
    // Read configuration from FS json
    if (SPIFFS.exists(JSON_CONFIG_FILE))
    {
      // The file exists, reading and loading
      // Serial.println("reading config file");
      File configFile = SPIFFS.open(JSON_CONFIG_FILE, "r");
      if (configFile)
      {
        // Serial.println("Opened configuration file");
        //StaticJsonDocument<512> json;    ........... deprecated
        JsonDocument json;
        DeserializationError error = deserializeJson(json, configFile);
        serializeJsonPretty(json, Serial);
        if (!error)
        {
          // Serial.println("Parsing JSON");
          strcpy(nodeAddress, json["nodeAddress"]);
          thisReader = nodeAddress[0];
          strcpy(hostName, json["hostName"]);
          return true;
        }
        else     
        {
          // Error deserializing JSON data  
          // Serial.println(""); 
          // Serial.println("Failed to parse json config");                                
          // faultRecovery(4);
          createFile();
          return false;
        }
      }
      else
      {
        // config file did not read correctly
        // Serial.println("file did not open");
        // faultRecovery(3);
        createFile();                 
        return false;
      }
    }
    else
    {
      // Error loading JSON data
      // Serial.println("file does not exist");
      createFile();
      JsonDocument json;
      return false;
    }
  }
  else
  {
    // Error mounting file system
    // Serial.println("Failed to mount FS");
    delay(1000);
    if (SPIFFS.format()) {
      // Serial.println("SPIFFS formatted successfully.");
    } else {
      // Serial.println("Failed to format SPIFFS.");
    }
    // delay(3000);
    // Serial.println("Formatted the FS");
    delay(10000);
    // Serial.println("Rebooting");
    ESP.restart();
    //delay(1000);
  }
  // Serial.println("end of load file");
  return false;
}

// **************************************************************
void createFile()
{
  JsonDocument json;
  json["nodeAddress"] = "A";
  json["hostName"] = "10.0.0.13";
  File configFile = SPIFFS.open(JSON_CONFIG_FILE, "w");
  serializeJsonPretty(json, Serial);
  // Serial.println("");
  // if (serializeJson(json, configFile) == 0)
  // {
  //   // Error writing file
  //   Serial.println(F("Failed to create a new file"));
  // }
  // else
  // {
  //   Serial.println(F("successfully created a new file"));
  // }
  // Close file
  configFile.close();
  delay(3000);
  ESP.restart();
  delay(1000);
}


// **************************************************************

void saveConfigFile()
// Save Config in JSON format
{ //Serial.println("start of saveConfigFile ");
  // Serial.println(F("Saving configuration..."));
  SPIFFS.remove(JSON_CONFIG_FILE);
  // Create a JSON document
  //StaticJsonDocument<512> json; ........ deprecated
  JsonDocument json;
  json["nodeAddress"] = nodeAddress;
  json["hostName"] = hostName;
  // Open config file
  File configFile = SPIFFS.open(JSON_CONFIG_FILE, "w");
  if (!configFile)
  {
    // Error, file did not open 
    // Serial.println(F("file did not open for write"));                                     
    configFile.close();
    // faultRecovery(5);
  }
  // Serialize JSON data to write to file
  serializeJsonPretty(json, Serial);
  if (serializeJson(json, configFile) == 0)
  {
    // Error writing file  
    // Serial.println(F("error writing to file"));                                              
    configFile.close();
    // faultRecovery(6);
  }  
  // Close file
  configFile.close();
}

// *********************************************************

void saveParamCallback()
{
  // Serial.println("Entered saving Parameters callBack");
  //saveConfigFile();
}

// *********************************************************

void saveConfigCallback()
{
  //Serial.println("Entered save Configuration callback");
  shouldSaveConfig = true;
}

// **********************************************************

void configModeCallback(WiFiManager *myWiFiManager)
// Called when config mode launched
{
  // Serial.println("Entered Configuration Mode");
 
  // Serial.print("Config SSID: ");
  // Serial.println(myWiFiManager->getConfigPortalSSID());
 
  // Serial.print("Config IP Address: ");
  // Serial.println(WiFi.softAPIP());
}


 



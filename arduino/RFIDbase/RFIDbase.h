#ifndef tagHeader
#define tagHeader
#include "RFIDbase.h"
#include <string.h>
#include <arduino.h>
#include <SPI.h>
#include <MFRC522.h>
#include  "Wire.h"
#include <LiquidCrystal_I2C.h>
#include <FS.h>
#include <SPIFFS.h>
#include <WiFiManager.h>
#include <ArduinoJson.h>
#include <PubSubClient.h>

#define TRIGGER_PIN A6

extern MFRC522 mfrc522;
extern MFRC522::MIFARE_Key key;
extern MFRC522::StatusCode status;
extern LiquidCrystal_I2C lcd;
extern bool carPresent;

extern byte buffer[18];
extern byte size;

extern char thisReader;

extern char globalMessage[100];

extern char carRoadName[17];
extern char carRoadNumber[17];
extern char carType[17];
extern char carColor[17];
extern char carOwner[17];
extern bool badWrite;

extern byte _name;
extern byte _number;
extern byte _type;
extern byte _color;
extern byte _owner;

extern String carID;
extern String assignedEngine;

bool loadConfigFile();
bool authenication(byte block);
void dump_byte_array(byte *buffer, byte bufferSize);
void dumpFullSector(byte sector);
bool showCardID();
int readBlock(byte whichBlock);
void clearValues();
void updateTag();
void updateResult();
int writeBlock(byte whichBlock);
int checkWrite(byte checkBlock);
byte setTrailingBlock(byte block);
void scanTag();
int validMessage(byte* content, unsigned int length);
void parseData();
void processCommands(int cmd, byte* content, unsigned int length);
void processReplies(int returnedCmd, byte* content, unsigned int length);
bool replyStatus(byte* content, unsigned int length);
void pickUp();
void dropOff();
void assignIt();
void releaseCar();
void toggle();
void displayLCD(int value);
void flashLoop();
const byte pickPin = A0;
const byte dropPin = A1;
const byte assignPin = 2;
const byte releasePin = 3;
const byte greenPin = A2;
const byte redPin = A3;
const byte TXcntlPin = 4;
extern bool buttonPressed;
extern bool pendingReply;
extern int retry;
extern PubSubClient mqttClient;
void SetupMqtt();
extern char hostName[50];
extern char nodeAddress[5];
//extern char nodeName[50];
const uint16_t MQTT_PORT = 1883;
void ConnectToMqtt();
void saveConfigFile();
void createFile();
void saveConfigCallback();
void configModeCallback(WiFiManager *myWiFiManager);
void saveParamCallback();
void CallbackMqtt(char* topic, byte* payload, unsigned int length);
void publisher(int pubTrigger, char* myTest, unsigned int length );
extern char publishData[130];
extern char pubBuffer[];
// void faultRecovery(int var);
// extern bool wifiFault;                   // using for fault recovery trigger
void saveConfigFile();
bool loadConfigFile();
extern bool shouldSaveConfig;
void visitingCar();
void onlineRely(byte* content, unsigned int length);
extern WiFiManager wm;
void checkButton();

#endif

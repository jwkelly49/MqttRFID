/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

/**
 *
 * @author jwkel
 */

import databaseStructure.DatabaseHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.*;



public class MqttConnect {

    private static MqttConnect instance; // Singleton instance
    private static MqttClient client;
    private static final String BROKER = "tcp://localhost:1883"; // Broker address
    private static final String CLIENT_ID = "rfidClient"; // Client ID
    static boolean isInitialConnection = false;
    static MqttConnect brokerClient = MqttConnect.getInstance();
    static DatabaseHandler database = DatabaseHandler.getInstance();

    // reference list of topics
    // topic1 -- "rfid/writeTag";
    // topic2 -- "rfid/tagReply";
    // topic3 -- "rfid/request";
    // topic4 -- "rfid/requestReply";
    // topic5 -- "rfid/error";
    // topic6 -- "rfid/readersOnline";
    // topic7 -- "rfid/onlineReply";
    // topic8 -- "rfid/visitor";
    // topic9 -- "rfid/visitorReply";
    
    private MqttConnect() {
//        System.out.println("got to new MqttConnect");
        try{
//            System.out.println("creating client");
            client = new MqttClient(BROKER, CLIENT_ID);
        }catch(MqttException e){
//            System.out.println("MqttConect() failed to make client");
        }
    }
    
    public static MqttConnect getInstance() {
        if (instance == null) {
            instance = new MqttConnect();
        }
        return instance;
    }
    public void myDisconnect(){
        try {
            client.disconnect();
        } catch (MqttException ex) {
            Logger.getLogger(MqttConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void connectToMqtt() {
//        System.out.println("got to class MqttConnect");
        try {
            // Create connection options
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(10);
            connOpts.setKeepAliveInterval(20);
            connOpts.setAutomaticReconnect(true);
            client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
//                System.out.println("connectToMqtt() Connection lost!");
                showAlert();
                cause.printStackTrace();
                
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String data = new String(message.getPayload());
                data = data.replaceAll("[\\n]", ""); // remove newline char
                if (validate(data)) {
                    switch (topic) {
                        case "rfid/tagReply":
//                            System.out.println("Case: tagReply");
                            NewTags.newTagReply(data);
                            break;
                        case "rfid/request":
//                            System.out.println("Case: requestList");
                            Requests.requestList(data);
                            break;
                        case "rfid/error":
//                            System.out.println("Case: errors");
                            Errors.errorList(data);
                            break;
                        case "rfid/onlineReply":
//                            System.out.println("Case: On line readers reply");
                            NewTags.onlineReadersReply(data);
                            break;
                        case "rfid/visitorReply":
//                            System.out.println("Case: visiting car reply");
                            NewTags.visitingCar(data);
                            break;
                        }
                } else {
//                    System.out.println("Invalid command received");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Called when a message published with QoS 1 or 2 is acknowledged by the broker
//                System.out.println("Delivery complete");
            }
        });

            // Connect to the broker
//            System.out.println("Connecting to broker: " + BROKER);
            client.connect(connOpts);
//            System.out.println("Connected");

            // Subscribe to a topic
            // only subscribe to topics that you want data FROM
            // subscribing to a topic that you publish to will
            // cause undesired echo back to this client (program)
            client.subscribe("rfid/tagReply", 0);
            client.subscribe("rfid/request", 0);
            client.subscribe("rfid/error", 0);
            client.subscribe("rfid/onlineReply", 0);
            client.subscribe("rfid/visitorReply", 0);

            // Keep the program running to receive messages
            //Thread.sleep(60000); // Wait for 60 seconds
            // Disconnect
            //client.disconnect();
            //System.out.println("Disconnected");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void testConnection() {
        if (client.isConnected()) {
//            System.out.println("Broker test is connected");
        } else {
//            System.out.println("Broker test is NOT connected");
            showAlert();
        }
    }

    private static void showAlert() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("MQTT Broker connection has been lost.\n"
                + "(Or never started)\n"
                + "Please establish a working broker.\n"
                + "The program will continue to exit until\n"
                + "a broker has been established.\n"
                + "Looking for: localhost:1883");
            alert.showAndWait();
            database.closeConnection();
            brokerClient.myDisconnect();
            System.exit(0);
        });
    }

    public void publisher(String topic, String message) {
        // Publish a message  writeTag
//        System.out.println("Publisher Message = " + message);
//        System.out.println("Publisher topic = " + topic);
        MqttMessage data = new MqttMessage(message.getBytes());
        data.setQos(1);
        try {
            client.publish(topic, data);
//            System.out.println("Message published to topic: " + topic);
        } catch (MqttException e) {
//            System.out.println("publisher catch was triggered ");
            e.printStackTrace();
        }
    }

    // this function only checks the message structure and not the data content
    private static boolean validate(String check) {
        boolean result = false;
        String startBit;
        String address;
        String command;
        startBit = check.substring(0, 1);
        address = check.substring(1, 2);
        command = check.substring(2, 4);
        if (startBit.equals("*")) {  // start bit
            char[] add = new char[1];
            add = address.toCharArray();
            int a = add[0] - 0;
            if (a >= 65 && a <= 89) {     // address A .... Y
                int b = Integer.parseInt(String.valueOf(command)); // proper command
                if (b == 2 || b == 20 || b == 21 || b == 22 || b == 23 || b == 98 || b == 99) {
                    String[] arrOfStr = check.split("~");    // split message to array
                    int elements = arrOfStr.length;          // number of message elements
                    switch (b) {   // test number of elements against number of expected elements per command
                        case 1:
                            // deprecated from serial version
                            break;
                        case 2:      // write to tag Reply         // *A02~carID\n
                        case 20:     // is this an engine request  // *A20~carID\n    
                        case 98:     // reader on line reply       // *A98~thisReader\n
                        case 99:     // error message              // *A99~errorString\n
                            if (elements == 2) {
                                result = true;
                            }
                            break;
                        case 21:    // drop off car this reader    *A21~assignedEngine~carID\n 
                        case 22:    // pick up car from reader     *A22~assignedEngine~carID\n
                            if (elements == 3) {
                                result = true;
                            }
                            break;
                        case 23:
                            if (elements == 7) {
                                result = true;
                            }
                            break;
                        default:
                            result = false;
                            break;
                    }
                }
            }
        }
        return result;
    }
}

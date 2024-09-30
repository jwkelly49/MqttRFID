/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import databaseStructure.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jwkel
 */

public class Requests {
    static DatabaseHandler database = DatabaseHandler.getInstance();
    static MqttConnect brokerClient = MqttConnect.getInstance();

    public static void requestList(String message){
//        System.out.println("got to function requestList()"); 
//        System.out.println("Message: " + message);
        String command = "";
        String reader = "";
        String header = "";
        String car = "";
        String engine = "";
        command = message.substring(2, 4);               // command number as string
        reader =  message.substring(1, 2);               // extract reader address
//        System.out.println("command substring = " + command); 

        String[] arrOfStr = message.split("~");          // split message to array
        for (int i = 0; i < arrOfStr.length; i++) {
            arrOfStr[i] = arrOfStr[i].trim();            // get rid of spaces
            switch (i) {    
                case 0:    
                    header = arrOfStr[0];                // header for sendTo   i.e. *A20
                    break;
                case 1:    
                    engine = arrOfStr[1];                // engine RFID
                    break;
                case 2:    
                    car = arrOfStr[2];                   // car RFID
                    break;
            }
        } 
      
        switch (command) {    
            
            case "20":    // is this an engine              *A20~carID
                Process20(header, engine);
                break;
            case "21":    // drop off car this reader    // *A21~assignedEngine~carID\n 
                Process21(header, reader, car);
                break;
            case "22":    // pick up car from reader     // *A22~assignedEngine~carID\n
                Process22(header,engine, car);
                break;
            default:
//                System.out.println("Unknown REQUEST can't process it!");
                break;
        }         
    }
        
        
    public static void Process20(String returnTo, String isEngine){
        // is this an engine -- used by assign
        String reply = "False";
        String qu = "SELECT * FROM Stock WHERE RFID = '" + isEngine + "'";
//        System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
//        System.out.println("ResultSet = " + rs);
            if (rs != null) {
                try {
                    while (rs.next()){
                        boolean check = rs.getBoolean("locomotive");
                        if(check){
                            reply = "True";
//                            System.out.println("resultSet found match");
                        }
                    }
                } catch (SQLException ex) {
//                    System.out.println("Process20() failed");
                    reply = "False";
                }
            }
            else {
                reply = "False";
//                System.out.println("resultSet was null");
            }
        String replyStatus = returnTo + "~" + reply + "\n";
//        System.out.println("replyStatus = " + replyStatus);
        String topic = "rfid/requestReply";
        brokerClient.publisher(topic,replyStatus);
    }
     
    public static void Process21(String returnTo, String reader, String car){
        // drop this car at reader
        String reply = "False";
        String qu;
        qu = "UPDATE Stock SET ENGINE = " + null + ", Reader = '" + reader + "' WHERE RFID = '" + car + "'";
//        System.out.println("Process 21 drop off qu = " + qu);
        boolean status = database.execAction(qu);
        if (status) {
            reply = "True";
        }
        String replyStatus = returnTo + "~" + reply + "\n";
//        System.out.println("replyStatus = " + replyStatus);
        brokerClient.publisher("rfid/requestReply",replyStatus);
    }
     
    public static void Process22(String returnTo, String engine, String car){
        // attach this car to this engine
        String reply = "False";
        String qu;
        
        qu = "UPDATE Stock SET Reader = " + null + ", ENGINE = '" + engine + "' WHERE RFID = '" +car + "'";
//        System.out.println("Process 22 pick up qu = " + qu);
        boolean status = database.execAction(qu);
        if (status) {
            reply = "True";
        }
        String replyStatus = returnTo + "~" + reply + "\n";
//        System.out.println("replyStatus = " + replyStatus);
        brokerClient.publisher("rfid/requestReply",replyStatus);
    }
}  // end of class


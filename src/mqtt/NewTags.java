/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

//import static commPort.CommPort.pollingPort;
import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.eclipse.paho.client.mqttv3.MqttException;
import programming.CarsController;
import readers.NewReaderController;
import rollingStock.Stock;
import programming.visitors.VisitingController;
import static rollingStock.Stock.getVisitingStockInstance;

/**
 *
 * @author jwkel
 */
public class NewTags {
    static DatabaseHandler database = DatabaseHandler.getInstance();
    static volatile String[] readerReply = new String[25];
    static int index = 0;
    static volatile boolean responseReceived;
    MqttConnect brokerClient = MqttConnect.getInstance();
    static Stock newStock = Stock.getPermanentStockInstance();
    static CommonVars commonVars = CommonVars.getInstance();
    
    public static void newTagReply(String message){
//        System.out.println("got to function newTagReply()"); 
//        System.out.println("Message: " + message);
        Platform.runLater(() -> {
            boolean savingJMRI = commonVars.getSavingJMRI();
            CarsController controller;
            if(!savingJMRI){
                // add car called from Programming menu -- typical way
                controller = main.controllers.MqttRFIDController.getNewCarController();
            } else{
                // add car called from Import JMRI file
                controller = CarsController.getJMRIController();
            }
            String localMessage = message;
//            System.out.println("visiting car Message: " + message);
            String[] arrOfStr = localMessage.split("~");
            // arrOfStr[0] is not used
            newStock.setRFID(arrOfStr[1]);
//            System.out.println("tag reply RFID: " + newStock.getRFID());
            controller.tagReturnedByreader();
            
        });
    }
       
    public static void visitingCar(String message){
        Platform.runLater(() -> {
            VisitingController controller = main.controllers.MqttRFIDController.getVisitingController();
            String localMessage = message;
//            System.out.println("visiting car Message: " + message);
            Stock visitingStock = Stock.getVisitingStockInstance();
            String[] arrOfStr = localMessage.split("~");
            // arrOfStr[0] is not used
            visitingStock.setRFID(arrOfStr[1]);
            visitingStock.setRoadName(arrOfStr[2]);
            visitingStock.setRoadNumber(arrOfStr[3]);
            visitingStock.setColor(arrOfStr[4]);
            visitingStock.setType(arrOfStr[5]);
            visitingStock.setOwner(arrOfStr[6]);
            controller.stockReturnedByreader();
            
        });
    }   
    
    public String[]  queryReaders() throws MqttException{
        String reader = ""; 
        String[] installed = new String[25];
        for (int i = 0; i < installed.length; i++) {
            installed[i] = "^"; 
            readerReply[i] = "^";
        }
        String qu = "SELECT * FROM READERS WHERE installed=true";
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                int i = 0; 
                while (rs.next()){
                    reader = rs.getString("reader");
                    if (!reader.equals("Z")){
                        installed[i] = reader;
//                        System.out.println("reader: " + reader);
                        ++i;
                    }   
                }
            } catch (SQLException ex) {
                 Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (char ch = 'A'; ch <= 'Y'; ch++) {
            String messageContent = "*" + ch + "98~" + "online query\n";
//            System.out.println("sent Message: " + messageContent);
            responseReceived = false;
            try {
                // Wait for the response
                long timeout = 15; // millisecond timeout
                long startTime = System.currentTimeMillis();
                brokerClient.publisher("rfid/readersOnline", messageContent);
                while (!responseReceived && (System.currentTimeMillis() - startTime) < timeout) 
                {
                    Thread.sleep(3); // Sleep for a short period
                }
                if (!responseReceived) {
//                    System.out.println("No response received within the timeout period.");
                }  
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(NewTags.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (int  ch = 0; ch < 25; ch++) {
//            System.out.println("readerReply =  " + readerReply[ch]);
        }
        String[] display = replyCompare(installed);
        return display;
    }
    public static void onlineReadersReply(String message){
//        System.out.println("got to function onlineReadersReply()");
//        System.out.println("Message: " + message);
        char x = message.charAt(5);
        String returned = Character.toString(x);
        readerReply[index] = returned;
        responseReceived = true;
        //System.out.println("reader returned: " + readerReply[index]);
        ++index;
    }
    public static String[] replyCompare(String[] installed ){
        String field, dBase;
        int same = 0, noField = 0, noDbase = 0;
        String[]sendData = new String[2];
        String noFieldDevice ="";
        String noDbaseEntry ="";
        Arrays.sort(installed);
        Arrays.sort(readerReply);
        for(int i=0; i<25; ++i){
            field = readerReply[i];
            dBase = installed[i];
//            System.out.println("field: " + field);
//            System.out.println("dBase: " + dBase);
            if(field.equals(dBase)){
                if (field != "^"){
                    ++same;
                }
            }
            else if (field == "^" && dBase != "^"){
                ++noField;
                noFieldDevice = noFieldDevice + dBase + "  ";
            }
            else if (dBase == "^" && field != "^"){
                ++noDbase;
                noDbaseEntry = noDbaseEntry + field + "  ";
            }
        }
        sendData[0] = noFieldDevice;
        sendData[1] = noDbaseEntry;
       
//        System.out.println("same: " + same);
//        System.out.println("noField: " + noField);
//        System.out.println("noFieldDevice: " + sendData[0]);
//        System.out.println("noDbase: " + noDbase);
//        System.out.println("noDbaseEntry: " + sendData[1]);
        return sendData;
    }
} // end of class

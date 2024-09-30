package main;

import databaseStructure.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import configuration.XML.Config;
import static configuration.XML.Config.configItems;
import java.io.*;
import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import mqtt.MqttConnect;


public class MqttRFID extends Application {
    private static boolean netCheck = true;  //display network OK/Cancel just once
    private static boolean showAlert = true; // false gives no fault message and shutdown
    private static main.controllers.MqttRFIDController controller;
    MqttConnect brokerClient = MqttConnect.getInstance();
    static DatabaseHandler database = DatabaseHandler.getInstance();
    
    @Override
    public void start(Stage stage) throws Exception {
        //DatabaseHandler dataBase = new DatabaseHandler();  
        database.dataBaseStartup();
        File f = new File("configValues.xml");
        if (f.exists ()) {
           Config.xmlToObject("configValues.xml");
        }else{
            Config.objectToXML(); 
            configItems.OpenForm();
            System.exit(0);      // force restart after config change
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/fxml/MqttRFID.fxml"));
        Parent root = loader.load();
        controller = loader.getController(); // Initialize the controller
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Platform.runLater(() -> {
            afterSceneIsShown();
        });
    } 
  
    private void afterSceneIsShown() {
//        System.out.println("Scene is now displayed!");
        brokerClient.connectToMqtt();
        brokerClient.testConnection();
        // Simulate an action event programmatically
        ActionEvent event = new ActionEvent();
        controller.M_ReaderNetwork(event);
    }

    public static void main(String[] args)  throws IOException{
        launch(args);
    }
} // end of class

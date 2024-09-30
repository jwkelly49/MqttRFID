package main.controllers;

import configuration.CommonVars;
import static configuration.XML.Config.configItems;
import databaseStructure.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mqtt.MqttConnect;
import programming.CarsController;
import programming.visitors.VisitingController;

public class MqttRFIDController implements Initializable {
    
    @FXML
    private BorderPane borderPane;
    
    // ************ file menu **************
    @FXML
    private Menu M_File;
    @FXML
    private MenuItem M_About_Intro;
    @FXML
    private MenuItem M_Config;
    @FXML
    private MenuItem M_Import;
    @FXML
    private MenuItem M_Exit;
    
    // ************** Polling Menu ***************
    
    @FXML
    private Menu M_Network;
    @FXML
    private MenuItem M_About_Network;
    
    // ************* locations menu ****************
    @FXML
    private Menu M_Locations;
    @FXML
    private MenuItem M_About_Locations;
    @FXML
    private MenuItem M_List_Cars_Reader;
    @FXML
    private MenuItem M_List_Engines_Reader;
    @FXML
    private MenuItem M_Change_Location;     
    @FXML
    private MenuItem M_View_Tracks_Location;
    @FXML
    private MenuItem M_EditLocation;
    @FXML
    private MenuItem M_DeleteLocation;
    @FXML
    private MenuItem M_AddNewTrack;
    @FXML
    private MenuItem M_EditTrack;
    @FXML
    private MenuItem M_DeleteTrack;
    
// *************** engines menu *********************    
    @FXML
    private Menu M_Engines;
    @FXML
    private MenuItem M_About_Engines;
    @FXML
    private MenuItem M_New_Work_Engine;
    @FXML
    private MenuItem M_Engine_Pick_Drop_Car;
    @FXML
    private MenuItem M_Terminate_Engine;
    @FXML
    private MenuItem M_List_Assigned_Cars_Engines;
    
    
    //  **************** Reader Menu ********************
    @FXML
    private Menu M_Readers;
    @FXML
    private MenuItem M_About_Readers;
    @FXML
    private MenuItem M_Set_Reader;
    @FXML
    private MenuItem M_New_Reader;
    @FXML
    private MenuItem M_Edit_Reader_Text;
    @FXML
    private MenuItem M_Delete_Reader;
    @FXML
    public MenuItem M_Reader_Network;
    
    
    // *****************  Rolling Stock Menu ****************
    @FXML
    private Menu M_Rolling_Stock;
    @FXML
    private MenuItem M_About_Cars;
    @FXML
    private MenuItem M_List_All_Cars;
    @FXML
    private MenuItem M_List_All_Engines;
    @FXML
    private MenuItem M_List_All_Visitors;
    @FXML
    private MenuItem M_Delete_Engine;
    @FXML
    private MenuItem M_Delete_Car;
    @FXML
    private MenuItem M_Delete_Visitor;
    
    // ******************* Programming Menu *******************
    @FXML
    private Menu M_Programming;
    @FXML
    private MenuItem M_About_Programming;    
    @FXML
    private MenuItem M_Add_New_Car;
    @FXML
    private MenuItem M_visiting;
    @FXML
    private MenuItem M_JMRI_Car_Hold;
    @FXML
    private MenuItem M_JMRI_Engine_Hold;
    @FXML
    private MenuItem M_JMRI_Location_Hold;
    
    
    // ***************** JMRI Hold Menu *********************
    @FXML
    private Menu M_Holding;
    @FXML
    private MenuItem M_Show_Jmri_Hold;  
    @FXML
    private MenuItem M_View_JMRI_Cars;
    @FXML
    private MenuItem M_View_JMRI_Engines;
    @FXML
    private MenuItem M_View_JMRI_Locations;
    
    // ******************** labels ********************
    @FXML
    private Label displayLocation;
    @FXML
    private Label displayEngine;
    
    // ******************* Tab Pane ********************
    @FXML
    private TabPane centerTabPane;
    @FXML
    private Tab AboutIntro;
    @FXML
    private Tab AboutPolling;
    @FXML
    private Tab AboutLocations;
    @FXML
    private Tab AboutEngines;
    @FXML
    private Tab AboutReaders;
    @FXML
    private Tab AboutJmriHold;
    @FXML
    private Tab AboutProgramming;
    @FXML
    private Tab AboutRollingStock;
    
    
    // ****************** buttons ******************
    @FXML
    private Button BtnListAllCars;
    @FXML
    private Button btnListAllEngines;
    @FXML
    private Button btnNewLocation;
    @FXML
    private Button btnPickWorkEngine;   
    @FXML
    private Button btnAssignEngineLocation;
    @FXML
    private Button BtnEnginePickDropCar;
    @FXML
    private Button btnListCarsLocation;
    @FXML
    private Button btnListCarsEngine;
    @FXML
    private Button btnTerminateEngine;
    
    //**********************************************************
    
    /**
     * Initializes the controller class.
     */
    
   CommonVars commonVars = CommonVars.getInstance();
   private static VisitingController staticForVisitingController;
   private static CarsController staticForNewCarController;
   static MqttConnect brokerClient = MqttConnect.getInstance();
   static DatabaseHandler database = DatabaseHandler.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       createBindings();
       boolean enable = commonVars.getJmriStatus();
       setMenu(enable);
       
    }

    public void createBindings(){
         displayLocation.textProperty().bind(commonVars.getWorkLocationText());
         displayEngine.textProperty().bind(commonVars.getWorkEngineText());
         bindToJmriStatus(commonVars);
    }
   
   public void bindToJmriStatus(CommonVars commonVars) {
        commonVars.jmriStatusProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
           setMenu(newValue);
        });
    } 
    
   public void setMenu(boolean enable){
       if(enable){
           M_Import.setDisable(false);
           M_JMRI_Car_Hold.setDisable(false);
           M_JMRI_Engine_Hold.setDisable(false);
           M_JMRI_Location_Hold.setDisable(false);
           M_View_JMRI_Cars.setDisable(false);
           M_View_JMRI_Engines.setDisable(false);
           M_View_JMRI_Locations.setDisable(false);
       }else{
           M_Import.setDisable(true);
           M_JMRI_Car_Hold.setDisable(true);
           M_JMRI_Engine_Hold.setDisable(true);
           M_JMRI_Location_Hold.setDisable(true);
           M_View_JMRI_Cars.setDisable(true);
           M_View_JMRI_Engines.setDisable(true);
           M_View_JMRI_Locations.setDisable(true);           
       }
   }
    
 // *********  file actions **************************************
    @FXML
    private void M_ShowIntro(ActionEvent event) {
        centerTabPane.getSelectionModel().select(0);
    }
    @FXML
    private void M_JMRI_Import(ActionEvent event) {
         try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/imports.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML

    private void M_Config_Form(ActionEvent event) {
        configItems.OpenForm();
    }
    
     @FXML
    private void M_Exit_Program(ActionEvent event) {
         database.closeConnection();
         brokerClient.myDisconnect();
         Stage stage = (Stage) borderPane.getScene().getWindow();
         stage.close();
    }
    
    
    // *************** Network Actions **********************
    @FXML
    private void M_ShowNetwork(ActionEvent event) {
         centerTabPane.getSelectionModel().select(1);
    }

    //******************** location actions ***********************
    @FXML
    private void M_ShowLocations(ActionEvent event) {
         centerTabPane.getSelectionModel().select(3);
    }
     @FXML
    private void M_CarsFromReader(ActionEvent event) {
        //System.out.println("got to M_DeleteTrackAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/carsThisLocation.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void M_EnginesFromReader(ActionEvent event) {
        //System.out.println("got to M_DeleteTrackAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/enginesThisLocation.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void M_ChangeLocation(ActionEvent event) {    
    }
    

    @FXML
    private void M_ViewTracksLocation(ActionEvent event) {
        //System.out.println("got to M_ListTracksForLocation Action()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/ListTracksThisLocation.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        @FXML
    private void M_AddNewTrackAction(ActionEvent event) {
        //System.out.println("got to M_AddNewTrackAction");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/addTracks.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_EditTrackAction(ActionEvent event) {
        //System.out.println("got to M_EditTrackAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/editTrack.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void M_DeleteTrackAction(ActionEvent event) {
        //System.out.println("got to M_DeleteTrackAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/locations/deleteTrack.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // ****************** Engines Actions *******************
    
    @FXML
    private void M_ShowEngines(ActionEvent event) {
         centerTabPane.getSelectionModel().select(4);
    }
    @FXML
    private void M_PickNewWorkEngine(ActionEvent event) {
        //System.out.println("got to M_PickNewWorkEngineAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/engines/selectAWorkEngine.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_PickDropCarLocation(ActionEvent event) {
       //System.out.println("got to M_PickDropCarLocationAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/engines/pickupDropoff.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML
    private void M_TerminateEngine(ActionEvent event) {
        //System.out.println("got to M_TerminateEngineAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/engines/TerminateEngine.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_ListCarsCurrentEngine(ActionEvent event) {
        //System.out.println("got to M_ListCarsCurrentEngineAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/engines/listCarsForEngine.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //****************   Reader Actions **************************
    
    @FXML
    private void M_ShowReaders(ActionEvent event) {
         centerTabPane.getSelectionModel().select(5);
    }
    
        @FXML
    private void M_SetActiveReader(ActionEvent event) {
        //System.out.println("got to M_SetActiveReader()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readers/setActiveReader.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void M_AddNewReader(ActionEvent event) {
                //System.out.println("got to M_AddNewReader()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readers/newReader.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_EditReaderText(ActionEvent event) {
        //System.out.println("got to M_EditReaderText()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readers/changeReaderText.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void M_DeleteReader(ActionEvent event) {
        //System.out.println("got to M_DeleteReader()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readers/deleteReader.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void M_ReaderNetwork(ActionEvent event) {
//        System.out.println("got to M_ReaderNetwork()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/readers/readerNetwork.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  // ********************    Rolling Stock Actions ********************
    
        @FXML
    private void M_ShowRollingStock(ActionEvent event) {
         centerTabPane.getSelectionModel().select(6);
    }
    @FXML
    private void M_ListAllCars(ActionEvent event) {
                        //System.out.println("got to M_ListAllCars");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/ListAllCars.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void M_ListAllEngines(ActionEvent event) {
             //System.out.println("got to M_ListAllEngines");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/ListAllEngines.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @FXML
    private void M_ListAllVisitors(ActionEvent event) {
             //System.out.println("got to M_ListAllEngines");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/ListAllGuests.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_DeleteAnEngine(ActionEvent event) {
        //System.out.println("got to M_DeleteAnEngineAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/deleteAnEngine.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_DeleteACar(ActionEvent event) {
        //System.out.println("got to M_DeleteACarAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/deleteACar.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_DeleteAVisitor(ActionEvent event) {
        //System.out.println("got to M_DeleteACarAction()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/rollingStock/deleteAGuest.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //***************** Programming Actions *******************
    
    @FXML
    private void M_ShowProgramming(ActionEvent event) {
         centerTabPane.getSelectionModel().select(2);
    }
    
    @FXML
    private void M_AddNewCar(ActionEvent event) {
                //System.out.println("got to M_AddNewCar");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/programming/cars.fxml"));
            Parent root = loader.load();
            staticForNewCarController = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CarsController getNewCarController() {
        return staticForNewCarController;
    }
    
    @FXML
    private void M_visitingStock(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/programming/visitors/visiting.fxml"));
            Parent root = loader.load();
            staticForVisitingController = loader.getController(); // Store the controller
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static VisitingController getVisitingController() {
        return staticForVisitingController;
    }

    
    @FXML
    private void M_WorkJmriCarHold(ActionEvent event){
                //System.out.println("got to M_WorkJmriCarHold()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRIcars/AddJMRIcars.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_WorkJmriEngineHold(ActionEvent event){
                //System.out.println("got to M_WorkJmriEngineHold()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRIengines/AddJMRIengines.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void M_WorkJmriLocationHold(ActionEvent event){
                        //System.out.println("got to M_WorkJmriLocationHold()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRItracks/AddJMRIlocations.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    
    //******************  JMRI Hold Actions ***************************
    
    @FXML
    private void M_ShowJmriHold(ActionEvent event) {
        centerTabPane.getSelectionModel().select(7);
    }
    
    @FXML
    private void M_ViewJmriCars(ActionEvent event) {
        //System.out.println("got to M_ViewJmriCars()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRIcars/JMRIcars.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void M_ViewJmriEngines(ActionEvent event) {
        //System.out.println("got to M_ViewJmriEngines()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRIengines/JMRIengines.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void M_ViewJmriLocations(ActionEvent event) {
                //System.out.println("got to M_ViewJmriLocations()");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/programming/JMRI/JMRItracks/JMRIlocations.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
//            System.out.println("exception = " + ex);
            Logger.getLogger(MqttRFIDController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

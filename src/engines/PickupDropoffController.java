
package engines;

import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class PickupDropoffController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> roadName;
    @FXML
    private TableColumn<Locals, String> roadNumber;
    @FXML
    private TableColumn<Locals, String> type;
    @FXML
    private TableColumn<Locals, String> color;
    @FXML
    private TableColumn<Locals, String> owner;
    @FXML
    private TableColumn<Locals, String> rfidCol;
    @FXML
    private Button close;
    @FXML
    private Button dropoff;
    @FXML
    private TableView<Locals2> tableViewLocations;
    @FXML
    private TableColumn<Locals2, String> roadName2;
    @FXML
    private TableColumn<Locals2, String> roadNumber2;
    @FXML
    private TableColumn<Locals2, String> type2;
    @FXML
    private TableColumn<Locals2, String> color2;
    @FXML
    private TableColumn<Locals2, String> owner2;
    @FXML
    private TableColumn<Locals2, String> rfid2;
    @FXML
    private Label displayEngineName;
    @FXML
    private Label displayLocationName;
    @FXML
    private Button pickup;
    @FXML
    private Label emptyEngine;
    @FXML
    private Label emptyLocation;

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<PickupDropoffController.Locals> list = FXCollections.observableArrayList();
    ObservableList<PickupDropoffController.Locals2> list2 = FXCollections.observableArrayList();
    String newEngine;
    String newEngineText;
    CommonVars common  = CommonVars.getInstance();
    String currentEngine;
    String currentEngineText;
    String currentLocation;
    String currentLocationText;
    boolean emptyResultSet = true;      // no cars assigned to engine
    boolean emptyResultSet2 = true;    // no cars assigned to location
    String selectedRFID;                      //  movement to/from engine/location

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        currentEngine = common.getWorkEngine();
        currentEngineText = common.getWorkEngineText().get();
        currentLocation = common.getWorkLocation();
        currentLocationText = common.getWorkLocationText().get();
        displayEngineName.setText(currentEngineText);
        displayLocationName.setText(currentLocationText);
        emptyEngine.setVisible(false);       // hide no cars assigned label
        emptyLocation.setVisible(false);    // hide no cars assigned label
        initlCol();
        loadData();
        initlCol2();
        loadData2();
    }

    // *********************** logic for the engines table ******************
    
    private void initlCol(){
        roadName.setCellValueFactory(new PropertyValueFactory<>("roadName"));
        roadNumber.setCellValueFactory(new PropertyValueFactory<>("roadNumber"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        color.setCellValueFactory(new PropertyValueFactory<>("color"));
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        rfidCol.setCellValueFactory(new PropertyValueFactory<>("RFID"));
    }
    
    public static class Locals {
        private final SimpleStringProperty roadName;
        private final SimpleStringProperty roadNumber;
        private final SimpleStringProperty type;
        private final SimpleStringProperty color;
        private final SimpleStringProperty owner;
        private final SimpleStringProperty RFID;
     
        Locals (String roadName, String roadNumber, String type, String color, String owner, String RFID ) {
            this.roadName = new SimpleStringProperty(roadName);
            this.roadNumber = new SimpleStringProperty(roadNumber);
            this.type = new SimpleStringProperty(type);
            this.color = new SimpleStringProperty(color);
            this.owner = new SimpleStringProperty(owner);
            this.RFID = new SimpleStringProperty(RFID);
        }

        public String getRoadName() {
            return roadName.get();
        }

        public String getRoadNumber() {
            return roadNumber.get();
        }
        
        public String getType() {
            return type.get();
        }
        
        public String getEngineRFID() {
            return RFID.get();
        }
} 
    
    private void loadData() {
        list.removeAll(list);
        String qu = "SELECT * FROM STOCK WHERE ENGINE IS NOT NULL";
        //System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
            try {
                while (rs.next()){
                    emptyResultSet = false;
                    emptyEngine.setVisible(false);
                    String name1 = rs.getString("roadName");
                    String number1 = rs.getString("roadNumber");
                    String type1 = rs.getString("type");
                    String color1 = rs.getString("color");
                    String owner1 = rs.getString("owner");
                    String rfid1 = rs.getString("RFID");
                    String ENGINE1 = rs.getString("ENGINE");
                    boolean locomotive = rs.getBoolean("locomotive");
                    if (ENGINE1.equals(currentEngine)){
                            list.add(new PickupDropoffController.Locals(name1, number1,type1, color1, owner1, rfid1) );
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(PickupDropoffController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.getItems().setAll(list);
        if(emptyResultSet){
            emptyEngine.setVisible(true);
            //System.out.println("result set was empty");
        }
    }

    @FXML
    private void tableViewMouseAction(MouseEvent event) {
//        System.out.println("got to mouse action");
        if(!emptyResultSet){
        try{
        PickupDropoffController.Locals item = tableView.getSelectionModel().getSelectedItem();
        String a = item.getRoadName();
        String b = item.getRoadNumber();
        String c = item.getType();
        selectedRFID = item.getEngineRFID();
//        System.out.println("selected stock from engine is name = " + selectedRFID);
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }
    }
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void BtnDropoffAction(ActionEvent event) {
        String qu;
        qu = "UPDATE Stock SET ENGINE = " + null + ", Reader = '" + currentLocation + "' WHERE RFID = '" + selectedRFID + "'";
//        System.out.println("drop off qu = " + qu);
        if (database.execAction(qu)) {
            loadData();
            loadData2();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void BtnPickupAction(ActionEvent event) {
        String qu;
        qu = "UPDATE Stock SET Reader = " + null + ", ENGINE = '" + currentEngine + "' WHERE RFID = '" +selectedRFID + "'";
//        System.out.println("pick up qu = " + qu);
//        System.out.println("current engine  = " + currentEngine);
//        System.out.println("current location = " + currentLocation);
            if (database.execAction(qu)) {
                loadData();
                loadData2();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed");
                alert.showAndWait();
            }
    }
    
    
    // *********************** logic for the location table ******************
    
    private void initlCol2(){
        roadName2.setCellValueFactory(new PropertyValueFactory<>("roadName2"));
        roadNumber2.setCellValueFactory(new PropertyValueFactory<>("roadNumber2"));
        type2.setCellValueFactory(new PropertyValueFactory<>("type2"));
        color2.setCellValueFactory(new PropertyValueFactory<>("color2"));
        owner2.setCellValueFactory(new PropertyValueFactory<>("owner2"));
        rfid2.setCellValueFactory(new PropertyValueFactory<>("rfid2"));
    }
    
    public static class Locals2 {
        private final SimpleStringProperty roadName2;
        private final SimpleStringProperty roadNumber2;
        private final SimpleStringProperty type2;
        private final SimpleStringProperty color2;
        private final SimpleStringProperty owner2;
        private final SimpleStringProperty rfid2;
     
    Locals2 (String roadName2, String roadNumber2, String type2, String color2, String owner2, String rfid2 ) {
        this.roadName2 = new SimpleStringProperty(roadName2);
        this.roadNumber2 = new SimpleStringProperty(roadNumber2);
        this.type2 = new SimpleStringProperty(type2);
        this.color2 = new SimpleStringProperty(color2);
        this.owner2 = new SimpleStringProperty(owner2);
        this.rfid2 = new SimpleStringProperty(rfid2);
    }

    public String getRoadName2() {
        return roadName2.get();
    }

    public String getRoadNumber2() {
        return roadNumber2.get();
    }
        
    public String getType2() {
        return type2.get();
    }
        
    public String getRFID2() {
        return rfid2.get();
    }
} 
    
    private void loadData2() {
        list2.removeAll(list2);
        String qu = "SELECT * FROM STOCK WHERE reader = '" + currentLocation + "'";
//        System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
        try {
            while (rs.next()){
                emptyResultSet2 = false;
                emptyLocation.setVisible(false);
                String name1 = rs.getString("roadName");
                String number1 = rs.getString("roadNumber");
                String type1 = rs.getString("type");
                String color1 = rs.getString("color");
                String owner1 = rs.getString("owner");
                String rfid1 = rs.getString("RFID");
                String ENGINE1 = rs.getString("ENGINE");
                boolean locomotive = rs.getBoolean("locomotive");
                list2.add(new PickupDropoffController.Locals2(name1, number1,type1, color1, owner1, rfid1) );
            }
        } catch (SQLException ex) {
            Logger.getLogger(PickupDropoffController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableViewLocations.getItems().setAll(list2);
        if(emptyResultSet2){
            emptyLocation.setVisible(true);
//            System.out.println("result set was empty");
        }
    }

    @FXML
    private void tableViewLocationMouseAction(MouseEvent event) {
//        System.out.println("got to mouse action #2");
        if(!emptyResultSet2){
            try{
                PickupDropoffController.Locals2 item = tableViewLocations.getSelectionModel().getSelectedItem();
                String a = item.getRoadName2();
                String b = item.getRoadNumber2();
                String c = item.getType2();
                selectedRFID = item.getRFID2();
//                System.out.println("selected stock from location is rfid = " + selectedRFID);
            } catch (Exception e){
//                System.out.println("something went wrong with TableViewMouseAction()");
//                System.out.println(e);
            }
        }
    }

}  // end of controller

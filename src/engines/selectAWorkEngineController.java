
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
import javafx.stage.Stage;
import readers.NewReaderController;
import readers.setActiveReader;

public class selectAWorkEngineController implements Initializable {

    @FXML
    private Label displayEngineName;
    @FXML
    private Button select;
    @FXML
    private Button cancel;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> nameCol;
    @FXML
    private TableColumn<Locals, String> numberCol;
    @FXML
    private TableColumn<Locals, String> typeCol;
    @FXML
    private TableColumn<Locals, String> rfidCol;

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<selectAWorkEngineController.Locals> list = FXCollections.observableArrayList();
    String newEngine;
    String newEngineText;
    CommonVars common  = CommonVars.getInstance();
    String currentEngine;
    String currentEngineText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        currentEngine = common.getWorkEngine();
        currentEngineText = common.getWorkEngineText().get();
        displayEngineName.setText(currentEngineText);
        initlCol();
        loadData();
    }    
    
    private void initlCol(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("roadName"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("roadNumber"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        rfidCol.setCellValueFactory(new PropertyValueFactory<>("RFID"));
    }
    
    public static class Locals {
        private final SimpleStringProperty roadName;
        private final SimpleStringProperty roadNumber;
        private final SimpleStringProperty type;
        private final SimpleStringProperty RFID;
     
     Locals (String roadName, String roadNumber, String type, String RFID ) {
        this.roadName = new SimpleStringProperty(roadName);
        this.roadNumber = new SimpleStringProperty(roadNumber);
        this.type = new SimpleStringProperty(type);
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
        
    public String getRFID() {
        return RFID.get();
    }
      
} 
    
    private void loadData() {
        String qu = "SELECT * FROM Stock WHERE locomotive = 'true'";
        //System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String name = rs.getString("roadName");
                    String number = rs.getString("roadNumber");
                    String type = rs.getString("type");
                    String rfid = rs.getString("RFID");
                    list.add(new selectAWorkEngineController.Locals(name, number,type,rfid) );
                }
            } catch (SQLException ex) {
                Logger.getLogger(selectAWorkEngineController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list);
    }  
    

    @FXML
    private void BtnSelect(ActionEvent event) {
        common.setWorkEngineText(newEngineText);
        common.setWorkEngine(newEngine);
        currentEngineText = common.getWorkEngineText().get();
        currentEngine = common.getWorkEngine();
        displayEngineName.setText(currentEngineText);
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void tableViewMouseAction(MouseEvent event) {
        //System.out.println("got to mouse action");
        try{
        selectAWorkEngineController.Locals item = tableView.getSelectionModel().getSelectedItem();
        newEngineText = item.getRoadName();
        String b = item.getRoadNumber();
        String c = item.getType();
        newEngine = item.getRFID();
        if(newEngineText.equals(currentEngineText)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(currentEngineText + " is already the selected work engine");
            alert.showAndWait();
            newEngine = "";
            return;
        }
        //System.out.println("selected engine name = " + newEngineText + "    number = " + b);
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }
    }
}

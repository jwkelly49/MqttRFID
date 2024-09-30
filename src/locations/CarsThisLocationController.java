
package locations;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CarsThisLocationController implements Initializable {

    @FXML
    private Button cancel;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> roadCol;
    @FXML
    private TableColumn<Locals, String> numberCol;
    @FXML
    private TableColumn<Locals, String> typeCol;
    @FXML
    private TableColumn<Locals, String> colorCol;
    @FXML
    private TableColumn<Locals, String> ownerCol;
    @FXML
    private Label displayLocation;
    @FXML
    private Label emptyLocation;

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<CarsThisLocationController.Locals> list = FXCollections.observableArrayList();
    CommonVars common  = CommonVars.getInstance();
    String activeReader;
    String activeReaderText;
    boolean emptyResultSet;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activeReader = common.getActiveReader();
        activeReaderText = common.getWorkLocationText().get();
        displayLocation.setText(activeReaderText);
        emptyLocation.setVisible(false);    // hide no cars assigned label
        emptyResultSet = true;                   // assume no data in table
        initlCol();
        loadData();
         
    }
    
     private void initlCol(){
        roadCol.setCellValueFactory(new PropertyValueFactory<>("roadName"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("roadNumber"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owner"));
    }  
    
     public static class Locals {
        private final SimpleStringProperty roadName;
        private final SimpleStringProperty roadNumber;
        private final SimpleStringProperty type;
        private final SimpleStringProperty color;
        private final SimpleStringProperty owner;
    
        Locals (String roadName1, String roadNumber1, String type1, String color1, String owner1) {
            this.roadName = new SimpleStringProperty(roadName1);
            this.roadNumber = new SimpleStringProperty(roadNumber1);
            this.type = new SimpleStringProperty(type1);
            this.color = new SimpleStringProperty(color1);
            this.owner = new SimpleStringProperty(owner1);
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
        public String getColor() {
            return color.get();
        }
        public String getOwner() {
            return owner.get();
        }
}
     
    private void loadData() { 
        String qu = "SELECT * FROM Stock WHERE reader = '" + activeReader + "'AND locomotive = 'false'";
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    emptyResultSet = false;
                    emptyLocation.setVisible(false);
                    String color1 = rs.getString("color");
                    String owner1 = rs.getString("owner");
                    String roadName1 = rs.getString("roadName");
                    String roadNumber1 = rs.getString("roadNumber");
                    String type1 = rs.getString("type");
                    list.add(new CarsThisLocationController.Locals(roadName1, roadNumber1, type1, color1, owner1) );
                }
            } catch (SQLException ex) {
                Logger.getLogger(CarsThisLocationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list);
        if(emptyResultSet){
            emptyLocation.setVisible(true);
            //System.out.println("result set was empty");
        }
    }  
     
     
    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    
} // end of controller

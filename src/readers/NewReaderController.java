
package readers;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NewReaderController implements Initializable {

    @FXML
    private TextField readerName;
    @FXML
    private Button cancel;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button install;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> readerCol;
    @FXML
    private TableColumn<Locals, String> textCol;
    @FXML
    private Label fullReaders;

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<NewReaderController.Locals> list = FXCollections.observableArrayList();
    String locName;
    String readerLetter;
    boolean emptyResultSet;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        readerName.setText("");
        fullReaders.setVisible(false);
        emptyResultSet = true;
        loadData();
        initlCol();
    }    

    private void initlCol(){
        readerCol.setCellValueFactory(new PropertyValueFactory<>("reader"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("text3"));
    }    
    
    public static class Locals {
        private final SimpleStringProperty reader;
        private final SimpleStringProperty text3;
    
        Locals (String reader, String text3) {
            this.reader = new SimpleStringProperty(reader);
            this.text3 = new SimpleStringProperty(text3);
        }

        public String getReader() {
            return reader.get();
        }

        public String getText3() {
            return text3.get();
        }
      
}  

    private void loadData() {
        list.removeAll(list); 
        String qu = "SELECT * FROM Readers WHERE installed = false";
        ResultSet rs = database.execQuery(qu);
            try {
                while (rs.next()){
                    String reader = rs.getString("reader");
                    String text = rs.getString("text");
                    if(!"Z".equals(reader)){
                        fullReaders.setVisible(false);
                        emptyResultSet = false;
                        list.add(new NewReaderController.Locals(reader, text) );
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(setActiveReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }

        tableView.getItems().setAll(list); 
        if(emptyResultSet){
            fullReaders.setVisible(true);
            //System.out.println("result set was empty");
        }
    }
    
    @FXML
    private void BtnInstall(ActionEvent event) {
        if(readerLetter == null){
            //System.out.println("saveSelection update = null");
            return;
        }
        locName = readerName.getText();
        saveSelection();
        readerName.setText("");
        fullReaders.setVisible(false);
        emptyResultSet = true;
        loadData();
    }
    
    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    private void saveSelection(){
        String checkName = locName.replace("'", "''"); // escape any single quote characters 
        String qu;
        qu = "UPDATE Readers SET installed = true, text = '" + checkName + "' WHERE reader = '" + readerLetter   + "'" ;
        //System.out.println("qu = " + qu);

        if (database.execAction(qu)) {
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
    private void tableViewMouseAction(MouseEvent event) {
        try{
            NewReaderController.Locals item = tableView.getSelectionModel().getSelectedItem();
            String r = item.getReader();
            String t = item.getText3();
            readerLetter = r;
            locName = t;
            readerName.setText(locName);
            //System.out.println("new reader = " + r + "    Text = " + t);
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }
    }
    

     

    // method storage below this point ***********************************************
    private void loadEngines(){                                    

        String qu = "SELECT * FROM ENGINES"; 
        // this is the database for rolling stock engines 
        //System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String engineRFID = rs.getString("engineRFID");
                    String color = rs.getString("color");
                    String owner = rs.getString("owner");
                    String roadName = rs.getString("roadName");
                    String roadNumber = rs.getString("roadNumber");
                    String type = rs.getString("type");
                    String Reader = rs.getString("Reader");
                    String ENGINE = rs.getString("ENGINE");
                    boolean deletable = rs.getBoolean("deletable");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

     private void loadLocations(){               
        String qu = "SELECT * FROM LOCATIONS"; 
        //System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String text = rs.getString("text");
                    String reader = rs.getString("reader");
                    boolean deletable = rs.getBoolean("deletable");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

    private void loadTracks(){  
        String qu = "SELECT * FROM TRACKS"; 
        //System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);                              
        if (rs != null) {
            try {
                while (rs.next()){
                    String text = rs.getString("text");
                    String reader = rs.getString("reader");
                    boolean deletable = rs.getBoolean("deletable");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }   
    }

    private void loadCars(){                                      
        String qu = "SELECT * FROM CARS"; 
        // this is the database for rolling stock cars 
        // System.out.println("qu = " + qu);
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String carRFID = rs.getString("carRFID");
                    String color = rs.getString("color");
                    String owner = rs.getString("owner");
                    String roadName = rs.getString("roadName");
                    String roadNumber = rs.getString("roadNumber");
                    String type = rs.getString("type");
                    String Reader = rs.getString("Reader");
                    String ENGINE = rs.getString("ENGINE");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            } catch (SQLException ex) {
                Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        } 
    }     
} // END OF FILE

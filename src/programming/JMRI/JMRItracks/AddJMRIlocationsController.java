
package programming.JMRI.JMRItracks;

import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddJMRIlocationsController implements Initializable {

    @FXML
    private Button add;
    @FXML
    private Button cancel;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> idCol;
    @FXML
    private TableColumn<Locals, String> locationCol;
    @FXML
    private Label emptyInventory;
    @FXML
    private ListView<String> listview;
   @FXML
    private Label labelReader;
   
    /**
     * Initializes the controller class.
     */
    
    CommonVars commonVars = CommonVars.getInstance();
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<AddJMRIlocationsController.Locals> list = FXCollections.observableArrayList();
    boolean emptyResultSet;
    String [] readers = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y",};
    String selectedReader;
    String deleteRecord;
           
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emptyInventory.setVisible(false);    // hide no cars assigned label
        emptyResultSet = true;                   // assume no data in table
        loadData();
        initlCol();
        selectTableViewItem();
        fillListView();
    }    

    private void fillListView(){
        listview.getItems().addAll(readers);
        listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedReader = listview.getSelectionModel().getSelectedItem();
                labelReader.setText(selectedReader);
            }      
        });
        listview.getSelectionModel().selectFirst();
    }
    
    private void initlCol(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("locId"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    } 
        
    public static class Locals {
        private final SimpleStringProperty locId;
        private final SimpleStringProperty name;
        Locals (String locId1, String name1) {
            this.locId = new SimpleStringProperty(locId1);
            this.name = new SimpleStringProperty(name1);
        }
        public String getLocId() {
            return locId.get();
        }
        public String getName() {
            return name.get();
        }
    }  

    private void loadData() {
        list.removeAll(list);
        String qu = "SELECT * FROM JMRILocations";
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    emptyResultSet = false;
                    emptyInventory.setVisible(false);
                    //System.out.println("doing while loop");
                    String locId1 = rs.getString("locId");
                    String name1 = rs.getString("name");
                    list.add(new AddJMRIlocationsController.Locals(locId1, name1) );
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddJMRIlocationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list); 
        if(emptyResultSet){
            emptyInventory.setVisible(true);
//            System.out.println("result set was empty");
        }
    }          
    
    @FXML
    private void BtnAdd(ActionEvent event) {
        Locals item = tableView.getSelectionModel().getSelectedItem();
        String ID = item.getLocId();
        String selectedLocation = item.getName();
        String checkName = selectedLocation.replace("'", "''"); // escape any single quote characters
        String qu;
        qu = "UPDATE Readers SET  text = '" + checkName + "' WHERE reader = '" + selectedReader   + "'" ;  
        database.execAction(qu);
        String activeReader = commonVars.getActiveReader();
        if(activeReader.equals(selectedReader)){
            commonVars.setWorkLocationText(selectedLocation);
        }
        qu = "SELECT * FROM JMRITracks WHERE trackID = '" + ID   + "'" ;
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    emptyResultSet = false;
                    emptyInventory.setVisible(false);
                    //String id = rs.getString("trackID");  always use location ID
                    String name = rs.getString("name");
                    boolean deletable = true;
                    checkName = name.replace("'", "''"); // escape any single quote characters
                    qu = "INSERT INTO Tracks VALUES ( '" + checkName + "','" + selectedReader + "','" + deletable + "')";
                    //System.out.println("qu = " + qu);
                    database.execAction(qu); 
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddJMRIlocationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        boolean deleteTracks = true;
        if(emptyResultSet){
            deleteTracks = false;
        }
        deleteFromHold( deleteTracks);
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void tableViewMouseAction(MouseEvent event) {
        //System.out.println("got to mouse action");
        selectTableViewItem();
    }
    
    private void selectTableViewItem(){
        tableView.getSelectionModel().selectFirst();
        tableView.getSelectionModel().getSelectedItem();        
        try{
            AddJMRIlocationsController.Locals item = tableView.getSelectionModel().getSelectedItem();
            String r = item.getLocId();
            deleteRecord = r;   //  hold delete record for delete action
            //System.out.println("locId = " + r );
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }       
    }
        
    public void deleteFromHold(boolean tracks){
        //System.out.println("got to delete from hold");
        if (deleteRecord == null){
            return;
        }
        String qu;
        boolean tracksAlert = true;
        qu = "DELETE FROM JMRILocations  WHERE locID = '" + deleteRecord + "'" ;
        //System.out.println("qu = " + qu);
        if (database.execAction(qu)) {
            if(tracks){
                qu = "DELETE FROM JMRITracks  WHERE trackID = '" + deleteRecord + "'" ;
                if (database.execAction(qu)) {
                    emptyResultSet = true;
                    emptyInventory.setVisible(false);
                    loadData();
                }else{
                    tracksAlert = false;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete records from JMRITracks holding database");
                    alert.showAndWait();
                }
        }
        if(tracksAlert){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Import was Successful");
            alert.showAndWait();
        }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to delete records from JMRILocations holding database");
            alert.showAndWait();
        }
    }         
}

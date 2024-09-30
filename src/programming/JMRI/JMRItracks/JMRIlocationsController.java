
package programming.JMRI.JMRItracks;

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

public class JMRIlocationsController implements Initializable {

    @FXML
    private Button delete;
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

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<JMRIlocationsController.Locals> list = FXCollections.observableArrayList();
    String deleteRecord;
    boolean emptyResultSet;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emptyInventory.setVisible(false);    // hide no cars assigned label
        emptyResultSet = true;                   // assume no data in table
        loadData();
        initlCol();
        selectTableViewItem();
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
                    list.add(new JMRIlocationsController.Locals(locId1, name1) );
                }
            } catch (SQLException ex) {
                Logger.getLogger(JMRIlocationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list); 
        if(emptyResultSet){
            emptyInventory.setVisible(true);
//            System.out.println("result set was empty");
        }
    }         
        
    @FXML
    private void BtnDelete(ActionEvent event) {
        //System.out.println("got to delete action");
         if (deleteRecord == null){
            return;
        }
        String qu;
        qu = "DELETE FROM JMRILocations  WHERE locId = '" + deleteRecord + "'" ;
        //System.out.println("qu = " + qu);
        if (database.execAction(qu)) {
            emptyResultSet = true;
            emptyInventory.setVisible(false);
            loadData();
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
            JMRIlocationsController.Locals item = tableView.getSelectionModel().getSelectedItem();
            String r = item.getLocId();
            deleteRecord = r;   //  hold delete record for delete action
            //System.out.println("locId = " + r );
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }       
    }
    
}

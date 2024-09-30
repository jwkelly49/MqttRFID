
package readers;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class setActiveReader implements Initializable {

    @FXML
    private Button delete;
    @FXML
    private Button cancel;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> readerCol;
    @FXML
    private TableColumn<Locals, String> textCol;

    /**
     * Initializes the controller class.
     */
    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<Locals> list = FXCollections.observableArrayList();
    String deleteRecord;
    CommonVars common  = CommonVars.getInstance();
    String activeReader;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activeReader = common.getActiveReader();
        initlCol();
        loadData();
    }   // end initialize 

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
        String qu = "SELECT * FROM READERS";
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String reader = rs.getString("reader");
                    String text = rs.getString("text");
                    boolean deletable = rs.getBoolean("uninstallable");
                    boolean installed  = rs.getBoolean("installed");
                    if(deletable){
                        if(installed){
                            list.add(new Locals(reader, text) );
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list);
    }    
    
   
    @FXML
    private void BtnDelete(ActionEvent event) {
        //System.out.println("got to delete action");
        String qu;
        qu = "DELETE FROM Readers WHERE reader = '" + deleteRecord   + "'" ;
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
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void tableViewMouseAction(MouseEvent event) {
        //System.out.println("got to mouse action");
        try{
        Locals item = tableView.getSelectionModel().getSelectedItem();
        String r = item.getReader();
        String t = item.getText3();
        deleteRecord = r;   //  hold delete record for delete action
        if(activeReader.equals(deleteRecord)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Can't delete the currently active reader");
                alert.showAndWait();
                deleteRecord = "";
                return;
        }
        //System.out.println("table reader = " + r + "    Text = " + t);
        } catch (Exception e){
//            System.out.println("something went wrong with TableViewMouseAction()");
//            System.out.println(e);
        }
    }
} // end of class

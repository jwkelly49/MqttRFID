
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



public class setActiveReaderController implements Initializable {

    @FXML
    private Button select;
    @FXML
    private Button cancel;
    @FXML
    private TableView<Locals> tableView;
    @FXML
    private TableColumn<Locals, String> readerCol;
    @FXML
    private TableColumn<Locals, String> textCol;
    @FXML
    private Label displayReaderName;
    @FXML
    private Label displayReaderLetter;
    
    /**
     * Initializes the controller class.
     */

    static DatabaseHandler database = DatabaseHandler.getInstance();
    ObservableList<setActiveReaderController.Locals> list = FXCollections.observableArrayList();
    CommonVars common  = CommonVars.getInstance();
    String activeReader = "";
    String activeReaderText = "";    
    String newActiveReader = "";
    String newActiveReaderText = "";    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activeReader = common.getActiveReader();
        activeReaderText = common.getWorkLocationText().get();
        displayReaderLetter.setText(activeReader);
        displayReaderName.setText( activeReaderText);
        initlCol();
        loadData();
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
        String qu = "SELECT * FROM Readers WHERE installed = true";
        ResultSet rs = database.execQuery(qu);
        if (rs != null) {
            try {
                while (rs.next()){
                    String reader = rs.getString("reader");
                    String text = rs.getString("text");
                    if(!"Z".equals(reader)){
                        list.add(new setActiveReaderController.Locals(reader, text) );
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(setActiveReaderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tableView.getItems().setAll(list); 
    }       
     
    @FXML
    private void BtnSelect(ActionEvent event) {
        if (newActiveReader != null){
            common.setActiveReader(newActiveReader);
            activeReader = common.getActiveReader(); // refresh local value
            common.setWorkLocationText(newActiveReaderText);
            common.setWorkLocation(newActiveReader);
            displayReaderLetter.setText(newActiveReader);
            displayReaderName.setText( newActiveReaderText);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText( newActiveReader + " has been selected as the active reader (Location)");
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
        try{
            setActiveReaderController.Locals item = tableView.getSelectionModel().getSelectedItem();
            String r = item.getReader();
            String t = item.getText3();
            newActiveReader = r;
            newActiveReaderText = t;
            if(activeReader.equals(newActiveReader)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText( newActiveReader + " is already the active reader (Location)");
                alert.showAndWait();
                newActiveReader = "";
                newActiveReaderText ="";
                }
//            System.out.println("new active reader = " + r + "    Text = " + t);
        } catch (Exception e){
//                System.out.println("something went wrong with TableViewMouseAction()");
//                System.out.println(e);
        }
    }
    
} // end of controller

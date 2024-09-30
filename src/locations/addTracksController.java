
package locations;

import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class addTracksController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField newTrackName;
    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private Label currentLocationName;

    /**
     * Initializes the controller class.
     */
    
    static DatabaseHandler database = DatabaseHandler.getInstance();
    CommonVars commonVars = CommonVars.getInstance();
    String currentReaderText;
    String currentReader;
    String trackName;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentReaderText = commonVars.getWorkLocationText().get();
        currentReader = commonVars.getActiveReader();
        currentLocationName.setText(currentReaderText);
    }    

    @FXML
    private void BtnSave(ActionEvent event) {
        trackName = newTrackName.getText();
        if(!trackName.isEmpty() ){
        saveSelection();
        }
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
    private void saveSelection(){
        String checkName = trackName.replace("'", "''"); // escape any single quote characters 
        String qu;
        qu = "INSERT INTO TRACKS VALUES ( '" +  checkName + "','" + currentReader + "',true)" ;
        //System.out.println("qu = " + qu);

        if (database.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success -- Track was added");
            alert.showAndWait();
            newTrackName.setText("");
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    } 
    
} // end of controller

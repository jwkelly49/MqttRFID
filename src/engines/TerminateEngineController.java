
package engines;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jwkel
 */
public class TerminateEngineController implements Initializable {

    @FXML
    private Label displayEngineText;
    @FXML
    private Label displayLocationText;
    @FXML
    private Button BtnTerminate;
    @FXML
    private Button BtnCancel;

    /**
     * Initializes the controller class.
     */
    static DatabaseHandler database = DatabaseHandler.getInstance();
    CommonVars common  = CommonVars.getInstance();
    String currentEngine;
    String currentEngineText;
    String currentLocation;
    String currentLocationText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        currentEngine = common.getWorkEngine();
        currentEngineText = common.getWorkEngineText().get();
        currentLocation = common.getWorkLocation();
        currentLocationText = common.getWorkLocationText().get();
        displayEngineText.setText(currentEngineText);
        displayLocationText.setText(currentLocationText);
    }    

    @FXML
    private void BtnTerminateAction(ActionEvent event) {
        String qu;
        qu = "UPDATE Stock SET ENGINE = " + null + ", Reader = '" + currentLocation + "' WHERE ENGINE = '" + currentEngine + "'";
        //System.out.println("terminate qu = " + qu);
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
    private void BntCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }
    
}

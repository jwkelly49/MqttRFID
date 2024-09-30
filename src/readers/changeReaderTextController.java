
package readers;

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
import javafx.stage.Stage;

public class changeReaderTextController implements Initializable {

    @FXML
    private Button save;
    @FXML
    private Button cancel;
    @FXML
    private TextField readerTextChange;

    /**
     * Initializes the controller class.
     */
    static DatabaseHandler database = DatabaseHandler.getInstance();
    CommonVars commonVars = CommonVars.getInstance();
    String currentReaderText;
    String currentReader;
    String textChanged;
    @FXML
    private Label nameEquals;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentReaderText = commonVars.getWorkLocationText().get();
        currentReader = commonVars.getActiveReader();
        readerTextChange.setText(currentReaderText);
        nameEquals.setText(currentReaderText);
    }    


    @FXML
    private void BtnSave(ActionEvent event) {
        textChanged = readerTextChange.getText();
        saveSelection();
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
    
        private void saveSelection(){
        String checkName = textChanged.replace("'", "''"); // escape any single quote characters
        String qu;
        qu = "UPDATE Readers SET  text = '" + checkName + "' WHERE reader = '" + currentReader   + "'" ;
        //System.out.println("qu = " + qu);

        if (database.execAction(qu)) {
            commonVars.setWorkLocationText(textChanged);
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
    
}

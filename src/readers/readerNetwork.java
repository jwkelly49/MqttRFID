/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mqtt.NewTags;

/**
 * FXML Controller class
 *
 * @author jwkel
 */
public class readerNetwork implements Initializable {

    @FXML
    private AnchorPane network;
    @FXML
    private TextArea AddHardware;
    @FXML
    private TextArea DeleteDbase;
    @FXML
    private TextArea noMisMatch;
    @FXML
    private Button Btn_Query_Network;
    @FXML
    private Button Btn_Cancel;
    @FXML
    private TextArea hardwareOnly;
    @FXML
    private TextArea DbaseOnly;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepTextFields();
    }    

    @FXML
    private void TxtAddHardware(MouseEvent event) {
    }

    @FXML
    private void TxtDeleteDbase(MouseEvent event) {
    }

    @FXML
    private void BtnQueryNetwork(ActionEvent event) {
        prepTextFields();
    }

    @FXML
    private void BtnCancel(ActionEvent event) {
        Stage stage = (Stage) Btn_Cancel.getScene().getWindow();
        stage.close();
    }
    public void prepTextFields(){
        NewTags data = new NewTags();
        try{
            String[] matches = data.queryReaders();
            String noField = matches[0];  // no matching reader found on the network
            String noDbase = matches[1];  // no matchung database entry
            int x = noField.length();
            int y = noDbase.length();
//            System.out.println("noField length: " + x);
//            System.out.println("noDbase length: " + y);
            if(x==0 && y==0){
                DeleteDbase.setVisible(false);
                AddHardware.setVisible(false);
                noMisMatch.setVisible(true);
                hardwareOnly.setVisible(false);
                DbaseOnly.setVisible(false);
            
            }else{
                DeleteDbase.setText(noField);
                AddHardware.setText(noDbase);
                noMisMatch.setVisible(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }        
    }
}

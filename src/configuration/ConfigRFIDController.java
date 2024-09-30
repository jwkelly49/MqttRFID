
package configuration;

import configuration.XML.Config;
import static configuration.XML.Config.configItems;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ConfigRFIDController implements Initializable {

    @FXML
    private RadioButton RBntNotUseJMRI;
    @FXML
    private ToggleGroup JMRIstatus;
    @FXML
    private RadioButton RBtnUseJMRI;
    @FXML
    private Pane PaneVisibleAction;
    @FXML
    private TextField TFcarRoster;
    @FXML
    private TextField TFengineRoster;
    @FXML
    private TextField TFlocationRoster;
    @FXML
    private Button BtnCarRoster;
    @FXML
    private Button BtnEngineRoster;
    @FXML
    private Button BtnLocationRoster;
    @FXML
    private Button BtnSubmit;
    @FXML
    private Button BtnCancel;
    @FXML
    private Button BtnCommPort;
    @FXML   
    private TextField TFcomPort;
    
    /**
     * Initializes the controller class.
     */
    
    CommonVars commonVars = CommonVars.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DisplaySettings();
    }    
   
    @FXML
    private void NoJMRIaction(ActionEvent event) {
        PaneVisibleAction.setVisible(false);
        commonVars.setJmriStatus(false);
    }

    @FXML
    private void UseJMRIaction(ActionEvent event) {
        PaneVisibleAction.setVisible(true);
        commonVars.setJmriStatus(true);
    }

    @FXML
    private void CarRosterAtion(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
           TFcarRoster.setText(selectedFile.getAbsolutePath() );
        }
    }

    @FXML
    private void EngineRosterAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
           TFengineRoster.setText(selectedFile.getAbsolutePath() );
        }
    }

    @FXML
    private void LocationRosterAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
           TFlocationRoster.setText(selectedFile.getAbsolutePath() );
        }
    }

    @FXML
    private void SaveAction(ActionEvent event) {
          configItems.setCarRoster(TFcarRoster.getText());
          configItems.setEngineRoster(TFengineRoster.getText());
          configItems.setLocationRoster(TFlocationRoster.getText());
          //configItems.setComPort(TFcomPort.getText());
          boolean check = RBtnUseJMRI.isSelected();
          if(check){
              configItems.setJMRIstatus("true");    
          }else{
              configItems.setJMRIstatus("false");
          }
//          System.out.println("save action =  "+ configItems.getJMRIstatus());
           Config.saveToXML();
          System.exit(0);
    }

    @FXML
    private void CancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
         stage.close();
    }

    @FXML
    private void CommPortAction(ActionEvent event) { }
    
    public void DisplaySettings(){
        try{
          TFcarRoster.setText(configItems.getCarRoster());
          TFengineRoster.setText(configItems.getEngineRoster());
          TFlocationRoster.setText(configItems.getLocationRoster());
          //TFcomPort.setText(configItems.getComPort());
          String check = configItems.getJMRIstatus();
          if("true".equals(check)){
              PaneVisibleAction.setVisible(true);
              RBntNotUseJMRI.setSelected(false);
              RBtnUseJMRI.setSelected(true);
          }else{
              PaneVisibleAction.setVisible(false);
              RBntNotUseJMRI.setSelected(true);
              RBtnUseJMRI.setSelected(false);
          }
        }catch(Exception e){
//            System.out.println("something went wrong with FindPorts() ");
//                System.out.println("Exception --> "+ e.getMessage());
        }
          
    }

}

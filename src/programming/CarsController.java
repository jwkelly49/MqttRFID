
package programming;

import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mqtt.MqttConnect;
import rollingStock.Stock;


/**
 * FXML Controller class
 *
 * @author jwkel
 * 
 * This controller can be opened from 2 different sources.
 * the normal Programmimg/Add new car menu
 * or Programming/add JMRI car/engine menu
 * this means setting a flag addingFromJMRI to manage
 * which controller has called it.
 */
public class CarsController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField TFcarRFID;
    @FXML
    private TextField TFcolor;
    @FXML
    private TextField TFowner;
    @FXML
    private TextField TFroadname;
    @FXML
    private TextField TFroadnumber;
    @FXML
    private TextField TFtype;
    @FXML
    private Button BtnToReader;
    @FXML
    private Button BtnCancel;
    @FXML
    private RadioButton carRadio;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton engineRadio;


    /**
     * Initializes the controller class.
     */
    
    String whichTable = "Car";  // database default selection
    static DatabaseHandler database = DatabaseHandler.getInstance();
    static Stock newStock = Stock.getPermanentStockInstance();
    MqttConnect brokerClient = MqttConnect.getInstance();
    public static String tagTrigger = "idle";
    public static String duplicateTag="";
    public boolean addingFromJMRI = false;
    CommonVars commonVars = CommonVars.getInstance();
    public static CarsController JMRIcarsControl;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        int maxLength = 16;
        TFcolor.textProperty().addListener(new ChangeListener(TFcolor, maxLength));
        TFowner.textProperty().addListener(new ChangeListener(TFowner, maxLength));
        TFroadname.textProperty().addListener(new ChangeListener(TFroadname, maxLength));
        TFroadnumber.textProperty().addListener(new ChangeListener(TFroadnumber, maxLength));
        TFtype.textProperty().addListener(new ChangeListener(TFtype, maxLength));
    }  
    
    @FXML
    private void BtnnSendToReaderAction(ActionEvent event) {
        if (testFields()) {
            newStock.setRFID("unknown");
            newStock.setColor(TFcolor.getText());
            newStock.setOwner(TFowner.getText());
            newStock.setRoadName( TFroadname.getText() );
            newStock.setRoadNumber(TFroadnumber.getText());
            newStock.setType(TFtype.getText());
            // *A02~name~number~type~color~owner\n
            String messageContent = "*A02~" + TFroadname.getText() + "~"
                + TFroadnumber.getText() + "~"
                + TFtype.getText() + "~"
                + TFcolor.getText() + "~"
                + TFowner.getText() + "\n";
            brokerClient.publisher("rfid/writeTag", messageContent);
        }
    }
    
    public void tagReturnedByreader() {
//        System.out.println("got to tageReturnedByreader() ");
        String badTagWrite = newStock.getRFID();
        if(badTagWrite.equals("000000")){
            badTag();
            resetForm();
            if(addingFromJMRI){    // go back to AddJMRIcarsController via loadTextFieldData()
//                System.out.println("got to close");
                commonVars.setSavedJMRI(false);
                BtnCancel.fire();
            }
        }else{
            saveSelection();
        }
    }
    
    public static CarsController getJMRIController(){
        return JMRIcarsControl;
    }
    
    private boolean testFields(){
        boolean fields = true;
        String aa,bb,cc,dd,ee;
        boolean vv,ww,xx,yy,zz;
        aa = TFcolor.getText();
        bb = TFowner.getText();
        cc = TFroadname.getText();
        dd = TFroadnumber.getText();
        ee = TFtype.getText();
        vv = aa.isEmpty();
        ww = bb.isEmpty();
        xx = cc.isEmpty();
        yy = dd.isEmpty();
        zz = ee.isEmpty();
        if (vv || ww|| xx || yy || zz){
            fields = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Empty field");
            alert.setContentText("You must enter data for all fields");
            alert.showAndWait();
        }
        return fields;
    }
    
    @FXML
    private void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void groupClickAction(MouseEvent event) {
        RadioButton which = (RadioButton) toggleGroup.getSelectedToggle();
        String which2 = which.getText();
        if("Car".equals(which2)){
            whichTable = which2;
        }else if("Engine".equals(which2)){
             whichTable = which2;
        } else {
             whichTable = "Car";  // make sure there is always a vaild database table
        }
        //System.out.println("Selected  " + whichTable);
    }

    private void saveSelection(){
//        System.out.println("got to saveSelection() ");
        Date d1 = new Date();
        Date d2 = new Date();
        long milliseconds = 0;            
        String a = newStock.getRFID() ;    // RFID value used to find new entry from commPort.Process2()
        String b = newStock.getRoadName();
        String c = newStock.getRoadNumber();
        String d = newStock.getType();
        String e = newStock.getColor();
        String f = newStock.getOwner();
        String g = "A";       // always insert new car/engine to location A
        String h = null;      // assigned to an engine
        boolean i = true;     // deletable
        boolean j = false;    // locomotive
        if("Engine".equals(whichTable)){
           j = true;
        } 
        boolean k = false;    // visitor
        String qu = "INSERT INTO Stock VALUES ( '" + a + "','" + b + "','" + c + "','" + d + "','" + e + "','" + f + "','" + g + "'," +h + "," + i + "," + j + "," + k +")";
//        System.out.println("New Car qu = " + qu);

        if (database.execAction(qu)) {
//            System.out.println("save action was completed ");
            if(addingFromJMRI){
                commonVars.setSavedJMRI(true);  // used by AddJMRIcarsController  
            }
            success();
            resetForm();
        }
        else {
//            System.out.println("Something went wrong with the save action ");
            duplicate();
            resetForm();
        }
        
        if(addingFromJMRI){    // go back to AddJMRIcarsController via loadTextFieldData()
//            System.out.println("got to close");
            BtnCancel.fire();
        }
 }
    
    public void success(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Success");
        alert.setContentText("The Car/Locomotive\n"
                + "was entered into the database.\n");
        alert.showAndWait();
    }
    
    public void duplicate(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Save action failed");
        alert.setContentText("This RFID tag is already entered in the database.\n"
            + "Or data from the reader was malformed.");
        alert.showAndWait();
    }  
  
    public void badTag(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Write tag failed");
        alert.setContentText("This write operation failed \n"
            + "to write to the RFID tag.");
        alert.showAndWait();
    }  
    
    private void resetForm(){
        TFcarRFID.setText("");
        TFcolor.setText("");
        TFowner.setText("");
        TFroadname.setText("");
        TFroadnumber.setText("");
        TFtype.setText("");
    }

      public void loadTextFieldData(boolean car, String color, String name, String number, String type) throws IOException, InterruptedException {
//      loadTextFieldData only called from AddJMRIcarsController 
//      and AddJMRIenginesController
//      This is a flag to redirect logic flow back to JMRI controllers
        addingFromJMRI = true;
        commonVars.setSavedJMRI(false);  // used by AddJMRIcarsController
        TFcolor.setText(color);
//      owner is unknown and must be added for JMRI imports
        TFowner.setText("??????");
        TFroadname.setText(name);
        TFroadnumber.setText(number);
        TFtype.setText(type);
        if(car){
            toggleGroup.selectToggle(carRadio);
            whichTable = "Car";
        }else{
            toggleGroup.selectToggle(engineRadio);
            whichTable = "Engine";
        }
//        System.out.println("car = " + car );
    }
}

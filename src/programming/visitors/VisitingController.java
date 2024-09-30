
package programming.visitors;

import configuration.CommonVars;
import databaseStructure.DatabaseHandler;
import java.net.URL;
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
 */
public class VisitingController implements Initializable {

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
    private Button BtnFromReader;
    @FXML
    private Button BtnTodbase;
    @FXML
    private Button BtnCancel;
    @FXML
    private RadioButton carRadio;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton engineRadio;
    @FXML
    private RadioButton visitorRadio;
    @FXML
    private RadioButton dbaseRadio;        
    @FXML
    private ToggleGroup toggleGroup1;

    /**
     * Initializes the controller class.
     */
    
    String whichTable = "Car";        // database default selection
    static boolean visitor = true;    // default scene setting
    static boolean locomotive = true; // default scene setting
    static DatabaseHandler database = DatabaseHandler.getInstance();
    public static String duplicateTag="";
    CommonVars commonVars = CommonVars.getInstance();
    MqttConnect brokerClient = MqttConnect.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int maxLength = 16;
        TFcolor.textProperty().addListener(new programming.visitors.ChangeListener(TFcolor, maxLength));
        TFowner.textProperty().addListener(new programming.visitors.ChangeListener(TFowner, maxLength));
        TFroadname.textProperty().addListener(new programming.visitors.ChangeListener(TFroadname, maxLength));
        TFroadnumber.textProperty().addListener(new programming.visitors.ChangeListener(TFroadnumber, maxLength));
        TFtype.textProperty().addListener(new programming.visitors.ChangeListener(TFtype, maxLength));
    }
    
    @FXML
    private void BtnnSendToDbaseAction(ActionEvent event){
        saveSelection();
    }
    
    @FXML
    private void BtnnReadFromReaderAction(ActionEvent event){
        String messageContent = "*A23~visiting cars\n";
        brokerClient.publisher("rfid/visitor", messageContent); 
    }
    
    @FXML
    public void stockReturnedByreader() {
//        System.out.println("got to stock returned by reader");
        Stock visit = Stock.getVisitingStockInstance();
        String carPresent = visit.getRFID();
        if (carPresent.equals("noVisitor")){
            noVisitor();
            resetForm();
        }else{
            TFcarRFID.setText(visit.getRFID());
            TFcolor.setText(visit.getColor());
            TFowner.setText(visit.getOwner());
            TFroadname.setText(visit.getRoadName());
            TFroadnumber.setText(visit.getRoadNumber());
            TFtype.setText(visit.getType());
        }
    }

    @FXML
    private void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void group2ClickAction(MouseEvent event) {
        RadioButton guest = (RadioButton) toggleGroup1.getSelectedToggle();
        String set = guest.getText();
        if("Visitor".equals(set)){
            visitor = true;
//            System.out.println("visitor  ");
        }else if("Permanent".equals(set)){
            visitor = false;
//            System.out.println("Permanent  ");
        } else {
            visitor = true;  // make sure there is always a vaild visitor state
        }
//        System.out.println("visitor = " + visitor);
    }
    
    @FXML
    private void groupClickAction(MouseEvent event) {
        RadioButton which = (RadioButton) toggleGroup.getSelectedToggle();
        String which2 = which.getText();
        if("Car".equals(which2)){
            whichTable = which2;
            locomotive = false;
        }else if("Engine".equals(which2)){
            whichTable = which2;
            locomotive = true;
        } else {
            whichTable = "Car";  // make sure there is always a vaild database table
            locomotive = false;
        }
//        System.out.println("Car/Engine =  " + whichTable);
    }
    
    private void saveSelection(){
        //System.out.println("got to saveSelection() ");                
        String a = TFcarRFID.getText();     
        String b = TFcolor.getText();
        String c = TFowner.getText();
        String d = TFroadname.getText();
        String e = TFroadnumber.getText();
        String f = TFtype.getText();
        String g = "A";                     // always insert new car/engine to location A
        String h = null;                    // assigned to an engine (pulling stock)
        boolean i = true;                   // deletable
        boolean j = locomotive;             // locomotive -- is this a physical engine or a car
        boolean k = visitor;                // visitor temporary entry to database

        String qu = "INSERT INTO Stock VALUES ( '" + a + "','" + b + "','" + c + "','" + d + "','" + e + "','" + f + "','" + g + "'," +h + "," + i + "," + j + "," +k +")";
        //System.out.println("qu = " + qu);
         
        if (database.execAction(qu)) {
//            System.out.println("save action was completed ");
            success();
            resetForm();
        }
        else {
//            System.out.println("Something went wrong with the save action ");
            duplicate();
            resetForm();
        } 
 } // end of save
            
    private void resetForm(){
        TFcarRFID.setText("");
        TFcolor.setText("");
        TFowner.setText("");
        TFroadname.setText("");
        TFroadnumber.setText("");
        TFtype.setText("");
        toggleGroup.selectToggle(engineRadio);
        toggleGroup1.selectToggle(visitorRadio);
    }

    public void noVisitor(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("No Stock");
        alert.setContentText("No car or engine was detected\n"
            + "sitting on Reader A.\n");
        alert.showAndWait();
    }
    
    public void success(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Success");
        alert.setContentText("The visiting Car/Locomotive\n"
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
}

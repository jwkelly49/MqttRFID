
package programming.JMRI;

import static configuration.XML.Config.configItems;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javax.xml.bind.JAXBException;
import static programming.JMRI.JMRIcars.ImportJMRIcars.importCars;
import static programming.JMRI.JMRIengines.ImportJMRIengines.importEngines;
import static programming.JMRI.JMRItracks.ImportJMRItracks.importTracks;

public class ImportsController implements Initializable {

    @FXML
    private RadioButton cars;
    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton engines;
    @FXML
    private RadioButton locations;
    @FXML
    private RadioButton all;
    @FXML
    private Button doImport;
    @FXML
    private Button cancel;

    /**
     * Initializes the controller class.
     */
    private String selection = "Cars Only";    // this radio button is selected by default and may never be clicked so set here for use with import button
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void carsAction(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
        selection = selectedButton.getText();
    }

    @FXML
    private void enginesAction(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
        selection = selectedButton.getText();
    }

    @FXML
    private void locationsAction(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
        selection = selectedButton.getText();
    }

    @FXML
    private void allAction(ActionEvent event) {
        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
        selection = selectedButton.getText();
    }

    @FXML
    private void importAction(ActionEvent event) throws JAXBException {
        getFile();
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
         stage.close();
    }
    
    private void getFile() throws JAXBException{
        String x;
        switch(selection) {
           case "Cars Only":
//               System.out.println("got to cars");
               x = configItems.getCarRoster();
               importCars(x);
               break;
           case "Engines Only":
//               System.out.println("got to engines");
               x = configItems.getEngineRoster();
               importEngines(x);
               break;
           case "Locations Only":
//               System.out.println("got to locations");
               x = configItems.getLocationRoster();
               importTracks(x);
               break;  
        }
    }

} // end class

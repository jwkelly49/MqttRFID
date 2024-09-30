
package programming.JMRI.JMRItracks;

import databaseStructure.DatabaseHandler;
import java.io.File;
import java.util.List;
import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ImportJMRItracks {
    static DatabaseHandler database = DatabaseHandler.getInstance();
    public static void importTracks(String filePath) {
        String qu;
        try {
//            System.out.println("file path = " + filePath);
            File file = new File(filePath);
            // Set the system property to allow external DTDs
            System.setProperty("javax.xml.accessExternalDTD", "");
            JAXBContext jaxbContext = JAXBContext.newInstance(programming.JMRI.JMRItracks.Operations.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Operations operations = (Operations) jaxbUnmarshaller.unmarshal(file);
            Locations data = operations.getAllLocations();
            operations.setAllLocations(data);
            List<Location> myLocations = data.getLocations() ;
             int count = 0;
            for (Location location : myLocations) {
//                System.out.println("after unmarshalling : " + location);
//                System.out.println("ID: " + location.getId());
//                System.out.println("name: " + location.getName());
                String id = location.getId();    
                String name = location.getName();
                String checkName = name.replace("'", "''"); // escape any single quote characters
                 qu = "INSERT INTO JMRILocations VALUES ( '" + checkName + "','" + id  + "')";
//                System.out.println("qu = " + qu);
                database.execAction(qu);
                ++count;
                
                List<Track> locTracks = location.getTracks();
                if(locTracks != null){
                    for(Track track : locTracks) {
//                        System.out.println("Track ID: " +track.getId());
//                        System.out.println("Track name: " + track.getName());
                        //id = track.getId();   //use location id so location and tracks are assigned to the same reader 
                        name = track.getName();
                        checkName = name.replace("'", "''"); // escape any single quote characters
                        qu = "INSERT INTO JMRItracks VALUES ( '" + checkName + "','" + id  + "')";
//                        System.out.println("qu = " + qu);
                        database.execAction(qu);
                    }
                }
//                System.out.println("-----------------------------");
//                System.out.println("-----------------------------");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setHeaderText(null);
                     alert.setContentText("Imported " + count + " locations into JMRI hold successfully");
                     alert.showAndWait();
                     
        } catch (JAXBException e) {
//            System.out.println("Can't find the JMRI tracks / locations file ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Path or File error");
            alert.setContentText("Use File/Configure MqttRFID menu option and set \n"
                + "the correct path for this import action.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }    
}

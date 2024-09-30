
package configuration.XML;

import configuration.CommonVars;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {
//    private String PollingMode;
    private String JMRIMode;
    private String JMRIstatus;
    private String CarRoster;
    private String EngineRoster;
    private String LocationRoster;
//    private String ComPort;
    
    public Config (){
//        PollingMode = "false";
        JMRIMode = "false";
        JMRIstatus =  "false";
        CarRoster = "";
        EngineRoster = "";
        LocationRoster = "";
//        ComPort = "";
    }

    public static void updateVar(){
        String stat = configItems.getJMRIstatus();
        boolean statBool = true;
        if(stat.equals("false")){
            statBool = false;
        }
        commonVars.setJmriStatus(statBool); 
    }
    
    public static Config configItems = new Config();
    static CommonVars commonVars = CommonVars.getInstance();
       
    @Override
    public String toString() {
        return "JMRIMode " + JMRIMode + ", JMRIstatus "+ JMRIstatus + ", CarRoster " + CarRoster + ", EngineRoster " + EngineRoster + ", LocationRoster " + "";
    }
    
    public static void objectToXML(){
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("configValues.xml");
            jaxbMarshaller.marshal(configItems, file);
        }
            catch (JAXBException e){ 
//                 System.out.println("ObjectToXML() failed ");
                 e.printStackTrace();
        }
     }
    
    public static void  xmlToObject(String fileName) {
        String xmlData;
        JAXBContext jaxbContext;
        try{
            jaxbContext = JAXBContext.newInstance(Config.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            xmlData = getContext(fileName);
            StringReader reader = new StringReader(xmlData);
            configItems = (Config) unmarshaller.unmarshal(reader);
            updateVar();
        } catch (JAXBException e) {
//            System.out.println("XMLtoObject() failed ");
            e.printStackTrace();
        }
    }
    
    public static String getContext(String fileName) {
        String filePath = fileName;
        String xmlContent = "";
        try {
            Path path = Paths.get(filePath);
            xmlContent = new String(Files.readAllBytes(path));
        } catch (IOException e) {
//            System.out.println("getContext() failed ");
            e.printStackTrace();
        }
        return xmlContent;
    }
    
    public static void saveToXML(){
          objectToXML();
     }

    public String getJMRIMode() {
        return JMRIMode;
    }

    public void setJMRIMode(String JMRIMode) {
        this.JMRIMode = JMRIMode;
    }

    public String getJMRIstatus() {
        return JMRIstatus;
    }

    public void setJMRIstatus(String JMRIstatus) {
        this.JMRIstatus = JMRIstatus;
    }

    public String getCarRoster() {
        return CarRoster;
    }

    public void setCarRoster(String CarRoster) {
        this.CarRoster = CarRoster;
    }

    public String getEngineRoster() {
        return EngineRoster;
    }

    public void setEngineRoster(String EngineRoster) {
        this.EngineRoster = EngineRoster;
    }

    public String getLocationRoster() {
        return LocationRoster;
    }

    public void setLocationRoster(String LocationRoster) {
        this.LocationRoster = LocationRoster;
    }
     
     public void OpenForm(){
       
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/configuration/ConfigRFID.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
             stage.setResizable(false);
            stage.showAndWait();
            }catch (Exception e) {
//                System.out.println("something went wrong with OpenForm() ");
//                System.out.println("Exception --> "+ e.getMessage());
            } 
    }
    
}  // end of class







package configuration;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;



public class CommonVars {
    private static final CommonVars INSTANCE = new CommonVars();
    private String activeReader;
    private BooleanProperty jmriStatus = new SimpleBooleanProperty();
    private String car;
    private String workEngine;    // RFID tag
    private SimpleStringProperty workEngineText = new SimpleStringProperty();
    private String workLocation;  // reader letter  i.e. 'A'
    SimpleStringProperty workLocationText = new SimpleStringProperty();
    private String workTrack;  // reader letter
    private String workTrackText;
    //private SimpleStringProperty pollingText = new SimpleStringProperty();
    private boolean savedJMRI;
    private boolean savingJMRI;
    
    private CommonVars(){
        activeReader = "A";
        jmriStatus.setValue(false);
        car = "";
        workEngine = "0123456789";
        workEngineText.setValue("Permanent");
        workLocation = "A";                                        // same as active reader
        workLocationText.setValue("Programmer");  // same as active reader text
        workTrack = "A";
        workTrackText = "Programming Track";
        savingJMRI = false;
        savedJMRI = false;
    }
    
    public static CommonVars getInstance(){
        return INSTANCE;
    }
    public String getActiveReader(){
        return activeReader;
    }
    public void setActiveReader(String newreader){
        activeReader = newreader;
    }
    public boolean getJmriStatus(){
        return jmriStatus.get();
    }
     public BooleanProperty jmriStatusProperty() {
        return jmriStatus;
    }

    public void setJmriStatus(boolean newStatus){
        jmriStatus.set(newStatus);
    }
     public boolean getSavedJMRI(){
        return savedJMRI;
    }
    public void setSavedJMRI(boolean newSavedJMRI){
        savedJMRI = newSavedJMRI;
    }
    
    public boolean getSavingJMRI(){
        return savingJMRI;
    }
    public void setSavingJMRI(boolean newSavingJMRI){
        savingJMRI = newSavingJMRI;
    }
    public String getCar(){
        return car;
    }
    public void setCar(String newCar){
        car = newCar;
    }
    public String getWorkEngine(){
        return workEngine;
    }
    public void setWorkEngine(String newEngine){
        workEngine = newEngine;
    }
     public SimpleStringProperty getWorkEngineText(){
        return workEngineText;
    }
    public void setWorkEngineText(String newText){
        workEngineText.set(newText); 
    }
    public String getWorkLocation(){
        return workLocation;
    }
    public void setWorkLocation(String newLocation){
        workLocation = newLocation;
    }
    public SimpleStringProperty getWorkLocationText(){
        return workLocationText;
    }
    public void setWorkLocationText(String newLoc){
        workLocationText.set(newLoc);
    }
    public String getWorkTrack(){
        return workTrack;
    }
    public void setWorkTrack(String newLocation){
        workTrack = newLocation;
    }
    public String getWorkTrackText(){
        return workTrackText;
    }
    public void setWorkTrackText(String newTrk){
        workTrackText = newTrk;
    }

}

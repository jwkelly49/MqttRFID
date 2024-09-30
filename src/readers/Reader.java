/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readers;

/**
 *
 * @author jwkel
 */
public class Reader {
    private String text;     // human name for a reader (location)
    private String reader;
    private boolean installed;
    private boolean uninstallable;
    
    public void Reader(){
        this.text = "";
        this.reader = "";
        this.installed = false;
        this.uninstallable = true;
    } 
    
    public void Reader(String name, String alphaCharacter){
        this.text = name;
        this.reader = alphaCharacter;
        this.installed = false;
        this.uninstallable = true;
    }
    
    // *********** getters/setters ***********************
    
    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }
    public String getReader() {
        return reader;
    }

    public void setReader(String value) {
        this.reader = value;
    }
    public boolean geInstalled() {
        return installed;
    }

    public void setInstalled(boolean value) {
        this.installed = value;
    }
    public boolean getUninstallable() {
        return uninstallable;
    }

    public void setUninstallable(boolean value) {
        this.uninstallable = value;
    }
} //end of class


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locations;

/**
 *
 * @author jwkel
 */
public class Track {
    private String text;  // human name for a track
    private String reader;
    private boolean deletable = true; 
    
    public void Track(String name, String reader){
        this.text = name;
        this.reader = reader;
    }
    
    // *********** setters and getters ******************
    
    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }
    
    public String getName() {
        return reader;
    }

    public void setName(String value) {
        this.reader = value;
    }
} // end of class

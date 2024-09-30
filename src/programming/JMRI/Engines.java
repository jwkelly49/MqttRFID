
package programming.JMRI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Though most attributes are not used the class has been developed
 * to collect all current JMRI values in preparation for future upgrades
 * @author jwkel
 */
@XmlRootElement
public class Engines {
    private String id;                
    private String roadName;                
    private String roadNumber;               
    private String type;             
    private String length;              
    private String weightTons;          
    private String locationId;           
    private String secLocationId;        
    private String moves;                  
    private String date;                  
    private String selected;
    private String model;
    private String hp;
    private String bUnit;
    
    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    @XmlAttribute
    public String getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @XmlAttribute
    public String getWeightTons() {
        return weightTons;
    }

    public void setWeightTons(String weightTons) {
        this.weightTons = weightTons;
    }

    @XmlAttribute
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @XmlAttribute
    public String getSecLocationId() {
        return secLocationId;
    }

    public void setSecLocationId(String secLocationId) {
        this.secLocationId = secLocationId;
    }

    @XmlAttribute
    public String getMoves() {
        return moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    @XmlAttribute
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @XmlAttribute
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    @XmlAttribute
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @XmlAttribute
    public String getHp() {
        return hp;
    }

    public void setHP(String hp) {
        this.hp = hp;
    }

    @XmlAttribute
    public String getBUnit() {
        return bUnit;
    }

    public void setBUnit(String bUnit) {
        this.bUnit = bUnit;
    }

}


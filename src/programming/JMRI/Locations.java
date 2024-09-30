
package programming.JMRI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Though most attributes are not used the class has been developed
 * to collect all current JMRI values in preparation for future upgrades
 * @author jwkel
 */

@XmlRootElement
public class Locations {
    private String id; 
    private String name; 
    private String ops; 
    private String dir; 
    private String switchList; 
    private String comment; 
    private String switchListComment;
    private String yard;
    
    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getOps() {
        return ops;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    @XmlAttribute
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @XmlAttribute
    public String getSwitchList() {
        return switchList;
    }

    public void setSwitchList(String switchList) {
        this.switchList = switchList;
    }

    @XmlAttribute
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlAttribute
    public String getSwitchListComment() {
        return switchListComment;
    }

    public void setSwitchListComment(String switchListComment) {
        this.switchListComment = switchListComment;
    }
    
    @XmlAttribute
    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard;
    }
    
}

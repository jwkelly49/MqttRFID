
package programming.JMRI.JMRItracks;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Track {
    private String id;
    private String name;
    
    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                '}';
    }        
}


package programming.JMRI.JMRItracks;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operations-config")
public class Operations {
     private Locations allLocations;

    public Locations getAllLocations() {
        return allLocations;
    }

    @XmlElement(name = "locations")
    public void setAllLocations(Locations allLocations) {
        this.allLocations = allLocations;
    }   
}

package programming.JMRI.JMRItracks;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class Locations {
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    @XmlElement(name = "location")
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
        
}

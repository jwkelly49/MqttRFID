
package programming.JMRI;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class LocationsList {
     private List<Locations> locations;

    @XmlElement(name = "location")
    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(List<Locations> locations) {
        this.locations = locations;
    }   
}

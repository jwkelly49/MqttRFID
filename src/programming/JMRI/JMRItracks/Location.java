
package programming.JMRI.JMRItracks;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Location {
    private List<Track> tracks;
    private String id;
    private String name;

    public List<Track> getTracks() {
        return tracks;
    }

    @XmlElement(name = "track")
    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
    
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
            ", tracks = '" + tracks + '\'' +
            '}';
    }  
        
}

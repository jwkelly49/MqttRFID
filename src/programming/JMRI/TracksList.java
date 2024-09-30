
package programming.JMRI;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class TracksList {
    private List<Tracks> tracks;

    @XmlElement(name = "engine")
    public List<Tracks> getTracks() {
        return tracks;
    }

    public void setTracks(List<Tracks> tracks) {
        this.tracks = tracks;
    }   
}

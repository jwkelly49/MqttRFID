
package programming.JMRI.JMRIengines;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

final class Engines {
    private List<Engine> engines;

    public List<Engine> getEngines() {
        return engines;
    }

    @XmlElement(name = "engine")
    public void setEngines(List<Engine> engines) {
        this.engines = engines;
    }
    
}


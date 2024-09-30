
package programming.JMRI;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class EnginesList {
    private List<Engines> engines;

    @XmlElement(name = "engine")
    public List<Engines> getEngines() {
        return engines;
    }

    public void setEngines(List<Engines> engines) {
        this.engines = engines;
    }
}

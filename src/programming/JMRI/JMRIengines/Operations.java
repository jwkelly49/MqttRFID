
package programming.JMRI.JMRIengines;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operations-config")
class Operations {
    private Engines allEngines;

    public Engines getAllEngines() {
        return allEngines;
    }

    @XmlElement(name = "engines")
    public void setAllEngines(Engines allEngines) {
        this.allEngines = allEngines;
    }
}


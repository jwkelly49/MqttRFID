
package programming.JMRI.JMRIcars;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operations-config")
class Operations {
    private Cars allCars;

    public Cars getAllCars() {
        return allCars;
    }

    @XmlElement(name = "cars")
    public void setAllCars(Cars allCars) {
        this.allCars = allCars;
    }
}

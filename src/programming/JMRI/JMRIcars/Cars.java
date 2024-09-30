
package programming.JMRI.JMRIcars;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

final class Cars {
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    @XmlElement(name = "car")
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
    
}

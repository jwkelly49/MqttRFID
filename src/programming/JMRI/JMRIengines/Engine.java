
package programming.JMRI.JMRIengines;

import javax.xml.bind.annotation.XmlAttribute;

final class Engine {
    private String id;
    private String roadName;
    private String roadNumber;
    private String type;
    private String model;    // color for car
    
    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getRoadName() {
        return roadName;
    }

    @XmlAttribute(name = "roadName")
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
    
    public String getRoadNumber() {
        return roadNumber;
    }

    @XmlAttribute(name = "roadNumber")
    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }

    public String getType() {
        return type;
    }

    @XmlAttribute(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    @XmlAttribute(name = "model")
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Engine {" +
                "id = '" + id + '\'' +
                ", roadName = '" + roadName + '\'' +
                ", roadNumber = '" + roadNumber + '\'' +
                ", type = '" + type + '\'' +
                ", model = '" + model + '\'' +
                '}';
    }
}


package programming.JMRI.JMRIcars;

import javax.xml.bind.annotation.XmlAttribute;

final class Car {
    private String id;
    private String roadName;
    private String roadNumber;
    private String type;
    private String color;
    
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

    public String getColor() {
        return color;
    }

    @XmlAttribute(name = "color")
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Car{" +
            "id='" + id + '\'' +
            ", roadName='" + roadName + '\'' +
            ", roadNumber='" + roadNumber + '\'' +
            ", type='" + type + '\'' +
            ", color='" + color + '\'' +
            '}';
    }
}

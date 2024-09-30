/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rollingStock;

/**
 *
 * @author jwkel
 */
public class Stock {
    private String rfid;
    private String color;
    private String owner;
    private String roadName;
    private String roadNumber;
    private String type;
    private String reader;       // stock is located at this reader
    private String engine;       // RFID of engine this stock is assigned to.
    private boolean deletable;   // only permanent engine is not deletable
    private boolean locomotive;  // is this a physical engine
    private boolean visitor;
    
    private Stock() {  }
    
    // First singleton instance
    private static class VisitingStock {
        private static final Stock INSTANCE = new Stock();
    }
    
    // Second singleton instance
    private static class PermanentStock {
        private static final Stock INSTANCE = new Stock();
    }
    
    // Static method to get a dedicated visitingStock object
    public static Stock getVisitingStockInstance() {
        return VisitingStock.INSTANCE;
    }
    
    // Static method to get a dedicated permanentStock object
    public static Stock getPermanentStockInstance() {
        return PermanentStock.INSTANCE;
    }
    
    public Stock(String rfid, String roadName, String roadNumber,  String color, String type, String owner){
        this.rfid = rfid;
        this.color = color;
        this.owner = owner;
        this.roadName = roadName;
        this.roadNumber = roadNumber;
        this.type = type;
        this.reader = "A";         // default
        this.engine = null;        // default
        this.deletable = true;     // default
        this.locomotive = false;   // default
        this.visitor = false;      // default
    }
    
    @Override
    public String toString() {
        return "Stock{" +
            "RFID='" + rfid + '\'' +
            '}';
    }
    
    // ************ getters and setters ******************************
    
    public String getRFID() {
        return rfid;
    }

    public void setRFID(String value) {
        this.rfid = value;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String value) {
        this.color = value;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String value) {
        this.owner = value;
    }
    
    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String value) {
        this.roadName = value;
    }
    
    public String getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(String value) {
        this.roadNumber = value;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }
    
    public String getReader() {
        return reader;
    }

    public void setReader(String value) {
        this.reader = value;
    }
    
    public String getEngine() {
        return engine;
    }

    public void setEngine(String value) {
        this.engine = value;
    }
    
    public boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(boolean value) {
        this.deletable = value;
    }
    
    public boolean getLocomotive() {
        return locomotive;
    }

    public void setLocomotive(boolean value) {
        this.locomotive = value;
    }
    
    public boolean getVisitor() {
        return visitor;
    }

    public void setVisitor(boolean value) {
        this.visitor = value;
    }
       
}  // end of class

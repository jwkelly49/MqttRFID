
package databaseStructure;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;


public final class DatabaseHandler {
    private static DatabaseHandler instance; // Singleton instance
    private static final String DB_URL = "jdbc:derby:database; create=true";
    private static String dbUrl = "jdbc:derby:yourDatabase;shutdown=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
   private DatabaseHandler() {
       createConnection();
   }
   
   public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
    
   public void dataBaseStartup(){
       setupStockTable();
       setupTracksTable() ;
       setupReadersTable();
       setupJMRICarsTable();
       setupJMRIEnginesTable();
       setupJMRILocationsTable();
       setupJMRITracksTable();
   }
   
    private void createConnection() {
    try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
         } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
//             System.out.println("create connection failed " + e.getMessage());
            //e.printStackTrace();
         }
     }
    
    public void closeConnection(){
         try {
             System.out.println("got to Database shutdown");
            conn.close();
        } catch (SQLException e) {
                System.out.println("Database shutdown catch triggered.");
                e.printStackTrace();
        }
        
        
    }
    void setupStockTable() {
            /*
            Engines = individual rolling stock locomotives which are deletable.
            MqttRFID requires 1 permanent engine created here with the table
            with deletable set to false.
            */
        String TABLE_NAME = "Stock";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                  //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                   stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                           + "    RFID varchar(80) primary key, \n"
                           + "    color varchar(20) , \n"                  // holds model for an engine
                           + "    owner varchar(20) , \n"
                           + "    roadName varchar(20) , \n"
                           + "    roadNumber varchar(20) , \n"
                           + "    type varchar(20), \n"
                           + "    Reader varchar(2) default 'A' ,\n"       // assigned to a location
                           + "    ENGINE varchar(80) default null , \n"    // or assigned to an ENGINE
                           + "    deletable boolean default true, \n"      // to protect the permanent engine
                           + "    locomotive boolean default false, \n"        // real rolling stock engine
                           + "    visitor boolean default false"           // not permanent rolling stock
                           + ")" );
                   stmt.execute("INSERT INTO STOCK VALUES ( '0123456789','black','MqttRFID','Permanent','1234','Switcher','A',null,false,true,false)");
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... Engines Table error");
        }
        
    }
        
    void setupTracksTable() {
            /*
            Tracks = Names assigned to small sections of the layout. They are meaningful designators
            for portions of a layout used by humans. (i.e.) siding, business, track within a yard, etc.
           They don't have a physical reader and are only indirectly associated with a reader
            by being listed with a location display.
            MqttRFID requires 1 permanent Track setup here with the table.
            */
        String TABLE_NAME = "Tracks";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                  //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                   stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                           + "    text varchar(80) , \n"  // human name for a track
                           + "    reader varchar(20) , \n"
                           + "   deletable boolean default true"
                           + ")" );
                   stmt.execute("INSERT INTO TRACKS VALUES ( 'Programming Track','A',false)");
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... Tracks Table error");
        }
    }  

    void setupReadersTable() {
            /*
            Readers = Physical devicesused to read RFID tags
            MqttRFID requires 1 permanent Reader setup here with the table.
           Legal addresses are capitol A thru Y (Z - reserved) also setup here with the table.
            */
        String TABLE_NAME = "Readers";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                  //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                   stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                           + "    text varchar(80) , \n"     // human name for a reader (location)
                           + "    reader varchar(2) , \n"
                           + "   installed boolean default false, \n"
                           + "   uninstallable boolean default true"
                           + ")" );
                   stmt.execute("INSERT INTO READERS VALUES ( 'Programmer','A',true,false)");
                   stmt.execute("INSERT INTO READERS VALUES ( 'Reserved','Z',true,false)");
                   List<String> readers = Arrays.asList( "B", "C","D","E", "F", "G","H","I", "J", "K","L","M", "N", "O","P","Q", "U", "R","S","T", "U", "V","W","X", "Y");
                   for (String letter : readers) {
                        stmt.execute("INSERT INTO READERS VALUES ( '" + letter +"','" + letter + "',"+"false,true)"); 
                        //System.out.println("INSERT INTO READERS VALUES ( " + letter +"," + letter + ","+"false,true)");
                   }
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... Readers Table error");
        }
    }  
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch(SQLException ex) {
//            System.out.println("Exception at execQuery: dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }
    
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            boolean status = stmt.execute(qu);
//            System.out.println("execAction status = " + status);
            int updateCount = stmt.getUpdateCount();
//            System.out.println("Update count: " + updateCount);
            if(updateCount >= 1){
                status = true;
            }else{
                status = false;
            }
            return status;
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error " + ex.getMessage(), "execAction Error Occurred", JOptionPane.ERROR_MESSAGE);
//            System.out.println("Exception at execAction: dataHandler" + ex.getLocalizedMessage());
            return false;
        }
    }
    
        void setupJMRICarsTable() {
        String TABLE_NAME = "JMRICars";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                    + "    id varchar(20) , \n"
                    + "    color varchar(20) , \n"
                    + "    roadName varchar(80) , \n"
                    + "    roadNumber varchar(20) , \n"
                    + "    type varchar(20)"
                    + ")" );
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... JMRICars Table error");
        }
        
    }
        
        void setupJMRIEnginesTable() {
        String TABLE_NAME = "JMRIEngines";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                    + "    id varchar(20) , \n"
                    + "    roadName varchar(80) , \n"
                    + "    roadNumber varchar(20) , \n"
                    + "    type varchar(20) , \n"
                    + "    model varchar(20)"
                    + ")" );
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... JMRIEngines Table error");
        }
        
    }    
    
        void setupJMRITracksTable() {
        String TABLE_NAME = "JMRITracks";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                    + "    name varchar(80) , \n"
                    + "    trackID varchar(20)"
                    + ")" );
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... JMRITracks Table error");
        }
    }
        
        void setupJMRILocationsTable() {
        String TABLE_NAME = "JMRILocations";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null, TABLE_NAME.toUpperCase(),null );
            if(tables.next()) {
                //System.out.println("Table " + TABLE_NAME +" already exists. Ready to go!");
            }else {
                stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                    + "    name varchar(80) , \n"
                    + "    locID varchar(20)"
                    + ")" );
            }
        }
        catch (SQLException e) {
                 System.err.println(e.getMessage() + " ..... JMRILocations Table error");
        }
    }         
} // end of class



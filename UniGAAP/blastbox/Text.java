package UniGAAP.blastbox;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.sql.DatabaseMetaData;
import java.sql.Date;


public class Text {
    private FileHandler target;
    private DatabaseMetaData meta;
    private String charSet, database, rowName;
    private int text_id;
    private static int ids;

    public Text(String textName, String filePath){
        this.database = "jdbc:sqlite:"+filePath;
        try (Connection conn = DriverManager.getConnection(this.database)) {
            if (conn != null) {
                this.meta = conn.getMetaData();
                System.out.println("INFO::The driver name is " + meta.getDriverName()+".");
                System.out.println("INFO::A new database has been created.");
                String table = "CREATE TABLE IF NOT EXISTS literature (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	name TEXT NOT NULL,\n"
                + " date INTEGER NOT NULL,\n"
                + " charset STRING NOT NULL,\n"
                + " raw BLOB NOT NULL,\n"
                + " normalized BLOB\n"
                + ");";
                try (Connection conn1 = DriverManager.getConnection(this.database);
                        Statement stmt = conn.createStatement()) {
                        stmt.execute(table);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                System.out.println("INFO::Made standard table in new database.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.rowName = textName;
    }
    public void process_file(String fileName){
        byte[] temp1 = null, temp2 = null;
        String query = null;
        this.target = new FileHandler(fileName);
        this.charSet = this.target.getCharset();
        if(this.target.rawTextReady()){
            temp1 = new byte[(int)this.target.getFileLength()];
            this.target.getRawText().get(temp1);
            query = "INSERT INTO literatur(name, date, charset, raw) VALUES(?,?,?,?)";
        }
        if(this.charSet == "UTF-16" && this.target.normalizedTextReady()){
            temp2 = new byte[(int)this.target.getFileLength()];
            this.target.getNormalizedText().get(temp2);
            query = "INSERT INTO literatur(name, date, charset, raw, normalized) VALUES(?,?,?,?,?)";
        }
        try (Connection conn = DriverManager.getConnection(this.database);
            PreparedStatement stm = conn.prepareStatement(query)){
                if(this.charSet == "UTF-16"){
                    Blob blobTemp1 = conn.createBlob(); 
                    Blob blobTemp2 = conn.createBlob();
                    blobTemp1.setBytes(0, temp1);
                    blobTemp2.setBytes(0, temp2);
                    stm.setString(1, this.rowName);
                    stm.setDate(2, new Date(Instant.now().toEpochMilli()));
                    stm.setString(3, this.charSet);
                    stm.setBlob(4, blobTemp1);
                    stm.setBlob(5, blobTemp2);
                } else {
                    Blob blobTemp1 = conn.createBlob(); 
                    blobTemp1.setBytes(0, temp1);
                    stm.setString(1, this.rowName);
                    stm.setDate(2, new Date(Instant.now().toEpochMilli()));
                    stm.setString(3, this.charSet);
                    stm.setBlob(4, blobTemp1);
                }
                stm.executeUpdate();
                System.out.println("INFO::"+this.rowName+" row had data inserted!");
                ids++;
                this.text_id = ids;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifyRowName(String newName){
        String query = "UPDATE literature SET name = "+newName+" WHERE id = "+this.text_id+";";
        try (Connection conn = DriverManager.getConnection(this.database);
        PreparedStatement stm = conn.prepareStatement(query)){
            stm.execute(query);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifyRowCharSet(String newCharSet){
        String query = "UPDATE literature SET charset = "+this.charSet+" WHERE id = "+this.text_id+";";
        try (Connection conn = DriverManager.getConnection(this.database);
        PreparedStatement stm = conn.prepareStatement(query)){
            stm.execute(query);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifyRowRawData(byte[] newRawData){
        String query = "UPDATE literature SET date=(date),raw=(data) WHERE id = "+this.text_id+" VALUES(?,?);";
        try (Connection conn = DriverManager.getConnection(this.database);
        PreparedStatement stm = conn.prepareStatement(query)){
            stm.setDate(1, new Date(Instant.now().toEpochMilli()));
            stm.setBytes(2, newRawData);
            stm.execute(query);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifyRowNormalizedData(byte[] newNormalizedData){
        String query = "UPDATE literature SET date=(date),normalized=(data) WHERE id = "+this.text_id+" VALUES(?,?);";
        try (Connection conn = DriverManager.getConnection(this.database);
        PreparedStatement stm = conn.prepareStatement(query)){
            stm.setDate(1, new Date(Instant.now().toEpochMilli()));
            stm.setBytes(2, newNormalizedData);
            stm.execute(query);
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

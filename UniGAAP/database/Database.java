package UniGAAP.database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
public class Database{
    File database;
    DatabaseMetaData meta;
    Connection conn;
    
    public Database(String fileName){
        this.database = new File(fileName);
        try (this.conn = DriverManager.getConnection("jdbc:sqlite:"+fileName)) {
            if (conn != null) {
                this.meta = conn.getMetaData();
                System.out.println("INFO::The driver name is " + meta.getDriverName()+".");
                System.out.println("INFO::A new database has been created.");
                String table = "CREATE TABLE IF NOT EXISTS literature (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	name TEXT NOT NULL,\n"
                + " date INTEGER NOT NULL,\n"
                + " raw BLOB NOT NULL,\n"
                + " normalized BLOB,\n"
                + "	aux1 BLOB,\n"
                + " aux2 BLOB\n"
                + ");";
                createTable(table);
                System.out.println("INFO::Made standard table in new database.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createTable(String query){
        try (Connection conn = DriverManager.getConnection(database.getAbsolutePath());
                Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void deleteTable(String query){
        try (Connection conn = DriverManager.getConnection(database.getAbsolutePath());
                Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void readData(String query){
        try (Connection conn = DriverManager.getConnection(database.getAbsolutePath());
                Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void writeData(String table, String name, int dateTime, Charset charSet, ByteBuffer data1, ByteBuffer data2){
        String sql = "INSERT INTO ";
        if(!table.isBlank() || !table.isEmpty()){
            sql += table;
        } else {
            return;
        }
        if(!name.isBlank() || !name.isEmpty()){
            sql += name;
        } else {
            return;
        }
        if(dateTime > 0){
            sql += dateTime;
        }
        if(data1 != null && data2 != null){

        } else if(data1 != null && data2 == null){

        } else if(data1 == null && data2 != null){

        }else{

        }


    }
    public void customQuery(String query){
        try (Connection conn = DriverManager.getConnection(database.getAbsolutePath());
                Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
}

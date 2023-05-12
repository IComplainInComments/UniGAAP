package UniGAAP.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
public class Database{
    File database;
    DatabaseMetaData meta;
    Connection conn;
    
    public Database(String fileName){
        this.database = new File(fileName);
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+fileName)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createTable(){

    }
    public void deleteTable(){

    }
    public void query(){

    }
}
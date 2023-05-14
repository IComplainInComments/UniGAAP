package blastbox;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private Thread th;

    public Text(String textName, String filePath) {
        this.database = "jdbc:sqlite:" + filePath;
        try (Connection conn = DriverManager.getConnection(this.database)) {
            if (conn != null) {
                this.meta = conn.getMetaData();
                System.out.println("INFO:: The driver name is " + meta.getDriverName() + ".");
                System.out.println("INFO:: A new database has been created.");
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
                System.out.println("INFO:: Made standard table in new database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        this.rowName = textName;
    }

    public void process_file(String fileName) {
        byte[] temp1 = null, temp2 = null;
        String query = null;
        this.target = new FileHandler(fileName);
        charSet = target.getCharset();
        this.th = new Thread(this.target, fileName);
        temp1 = new byte[(int) this.target.getFileLength()];
        this.target.getRawText().get(temp1);
        query = "INSERT INTO literature(name, date, charset, raw) VALUES(?,?,?,?)";

        /*
         * temp2 = new byte[(int)target.getFileLength()];
         * target.getNormalizedText().get(temp2);
         * query =
         * "INSERT INTO literatur(name, date, charset, raw, normalized) VALUES(?,?,?,?,?)"
         * ;
         */

        try {
            this.th.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            if (charSet == "UTF-16") {
                stm.setString(1, this.rowName);
                stm.setDate(2, new Date(Instant.now().toEpochMilli()));
                stm.setString(3, this.charSet);
                stm.setBytes(4, temp1);
                stm.setBytes(5, temp2);
            } else {
                stm.setString(1, this.rowName);
                stm.setDate(2, new Date(Instant.now().toEpochMilli()));
                stm.setString(3, this.charSet);
                stm.setBytes(4, temp1);
            }
            stm.executeUpdate();
            System.out.println("INFO::" + rowName + " row had data inserted!");
            ids++;
            text_id = ids;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void modifyRowName(String newName) {
        String query = "UPDATE literature SET name = " + newName + " WHERE id = " + this.text_id + ";";
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            stm.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void modifyRowCharSet(String newCharSet) {
        String query = "UPDATE literature SET charset = " + this.charSet + " WHERE id = " + this.text_id + ";";
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            stm.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void modifyRowRawData(byte[] newRawData) {
        String query = "UPDATE literature SET date=(date),raw=(data) WHERE id = " + this.text_id + " VALUES(?,?);";
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setDate(1, new Date(Instant.now().toEpochMilli()));
            stm.setBytes(2, newRawData);
            stm.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void modifyRowNormalizedData(byte[] newNormalizedData) {
        String query = "UPDATE literature SET date=(date),normalized=(data) WHERE id = " + this.text_id
                + " VALUES(?,?);";
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setDate(1, new Date(Instant.now().toEpochMilli()));
            stm.setBytes(2, newNormalizedData);
            stm.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public String getRowName() {
        return this.rowName;
    }

    public String getDate() {
        String query = "SELECT date FROM literature WHERE id=" + this.text_id + ";";
        String date = null;
        ResultSet rs;
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            rs = stm.executeQuery();
            date = rs.getDate("date").toString();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return date;
    }

    public byte[] getRawData() {
        String query = "SELECT raw FROM literature WHERE id=" + this.text_id + ";";
        byte[] data = null;
        ResultSet rs;
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            rs = stm.executeQuery();
            data = rs.getBytes("raw");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return data;
    }

    public byte[] getNormalizedData() {
        String query = "SELECT normalized FROM literature WHERE id=" + this.text_id + ";";
        byte[] data = null;
        ResultSet rs;
        try (Connection conn = DriverManager.getConnection(this.database);
                PreparedStatement stm = conn.prepareStatement(query)) {
            rs = stm.executeQuery();
            data = rs.getBytes("normalized");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return data;
    }

    public String getCharset() {
        return this.charSet;
    }
}

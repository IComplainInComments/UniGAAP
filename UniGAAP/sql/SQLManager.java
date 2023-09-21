package sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Queue;

/**
 * SQLManager object used to control and use the Database.
 * @author IComplainInComments.
 * @version 1.0
 */
public  class SQLManager {
    private static File database;
    private static Queue<DataQuery> QueryQueue;
    private static Queue<OutputQuery> ResultQueue;
    private static DatabaseMetaData meta;
    private static String databaseString;

    /**
     * Constructor for the class.
     * Check if the database exists, if not, makes a new one at the given path.
     * @param path String - Database file path.
     */
    public SQLManager(String path){
        database = new File(path);
        if(database.exists()){
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath())) {
                if (conn != null) {
                    meta = conn.getMetaData();
                    System.out.println("INFO:: Database selected");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            databaseString = "jdbc:sqlite:" + database.getAbsolutePath();
        } else {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath())) {
                if (conn != null) {
                    meta = conn.getMetaData();
                    System.out.println("INFO:: The driver name is " + meta.getDriverName() + ".");
                    System.out.println("INFO:: A new database has been created.");
                    String table = "CREATE TABLE IF NOT EXISTS literature (\n"
                            + "	id INTEGER PRIMARY KEY,\n"
                            + "	name TEXT,\n"
                            + " date INTEGER,\n"
                            + " charset STRING,\n"
                            + " raw BLOB,\n"
                            + " normalized BLOB\n"
                            + ");";
                    try (Connection conn1 = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
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
            databaseString = "jdbc:sqlite:" + database.getAbsolutePath();
        }
    }
    /**
     * Add query to queue to be processed.
     * @param input DataQuery
     * @see DataQuery
     */
    public void enqueue(DataQuery input){
        QueryQueue.add(input);
        processQueue();
    }
    /**
     * Data output from data retreval queries.
     * @return OutputQuery
     */
    public OutputQuery result(){
        return ResultQueue.poll();
    }
    /**
     * Main logic for processing the queue.
     */
    private void processQueue(){
        if(!QueryQueue.isEmpty()){
            QueryType type = QueryQueue.peek().type();
            if(type == QueryType.INPUT){
                processQueryInput((InputQuery)QueryQueue.poll());
            }else if(type == QueryType.OUTPUT){
                processQueryOutput((OutputQuery)QueryQueue.poll());
            }
            processQueue();
        }
    }
    /**
     * Method for processing queries marked as Input Queries.
     * @param data InputQuery
     * @see InputQuery
     */
    private void processQueryInput(InputQuery data){
        SQLDataType temp = data.dataType();
         if(temp == SQLDataType.BLOB){
            String query = data.query();
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(query)) {
                stm.setBytes(1, (byte[])data.getData());
                stm.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else if(temp == SQLDataType.DATE){
            String query = data.query();
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(query)) {
                stm.setDate(1, new Date(Instant.now().toEpochMilli()));
                stm.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else {
            String query = data.query();
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(query)) {
                stm.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * Method for processing queries marked as output quries.
     * @param data OutputQuery
     * @return OutputQuery
     * @see OutputQuery
     */
    private void processQueryOutput(OutputQuery data){
        SQLDataType type = data.dataType();
        OutputQuery result = null;
        if(type == SQLDataType.STRING){
            ResultSet rs;
            String rString = "";
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(data.query())) {
                rs = stm.executeQuery();
                rString = rs.getString(data.getColumn());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            result = new OutputQuery(data.query(), data.getColumn(), data.dataType());
            result.setData(rString);
        }else if(type == SQLDataType.INTEGER){
            ResultSet rs;
            int rInteger = 0;
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(data.query())) {
                rs = stm.executeQuery();
                rInteger = rs.getInt(data.getColumn());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            result = new OutputQuery(data.query(), data.getColumn(), data.dataType());
            result.setData(rInteger);
        }else if(type == SQLDataType.DATE){
            ResultSet rs;
            String rString = "";
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(data.query())) {
                rs = stm.executeQuery();
                rString = rs.getString(data.getColumn());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            result = new OutputQuery(data.query(), data.getColumn(), data.dataType());
            result.setData(rString);
        }else if(type == SQLDataType.BLOB){
            ResultSet rs;
            byte[] rByte = null;
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(data.query())) {
                rs = stm.executeQuery();
                rByte = rs.getBytes(data.getColumn());
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            result = new OutputQuery(data.query(), data.getColumn(), data.dataType());
            result.setData(rByte);
        } else {

        }
        ResultQueue.add(result);
    }

}

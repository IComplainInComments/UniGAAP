package sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Queue;


public  class SQLManager {
    private static File database;
    private static Queue<DataQuery> QueryQueue;
    private static Queue<Object> ResultQueue;
    private static DatabaseMetaData meta;
    private static String databaseString;

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
    public void enqueue(DataQuery input){
        QueryQueue.add(input);
        processQueue();
    }
    public Object result(){
        return ResultQueue.poll();
    }
    private void processQueue(){
        if(!QueryQueue.isEmpty()){
            if(QueryQueue.peek().type() == QueryType.INPUT){
                processQueryInput(QueryQueue.poll());
            }
            if(QueryQueue.peek().type() == QueryType.OUTPUT){
                ResultQueue.add(processQueryOutput(QueryQueue.poll()));
            }
            processQueue();
        }
    }
    private void processQueryInput(DataQuery data){
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
    private Object processQueryOutput(DataQuery data){

    }

}

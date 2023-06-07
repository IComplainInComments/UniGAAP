package sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;

import javax.management.Query;


public  class SQLManager {
    private static File database;
    private static Queue<DataQuery> QueryQueue;
    private static Queue<DataQuery> ResultQueue;
    private static DatabaseMetaData meta;
    private static final String databaseString;

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
    public DataQuery result(){
        return ResultQueue.poll();
    }
    private void processQueue(){
        if(!QueryQueue.isEmpty()){
            if(QueryQueue)
            processQuery(QueryQueue.poll());
            processQueue();
        }
    }
    private void processQuery(DataQuery query){
        SQLDataType temp = query.dataType();
        if(temp == SQLDataType.STRING){
            String query = "UPDATE literature SET name = " + newName + " WHERE id = " + this.text_id + ";";
            try (Connection conn = DriverManager.getConnection(databaseString);
                    PreparedStatement stm = conn.prepareStatement(query)) {
                stm.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else if(temp == SQLDataType.INTEGER){

        } else if(temp == SQLDataType.DATE){

        }else if(temp == SQLDataType.BLOB){

        }
    }

}

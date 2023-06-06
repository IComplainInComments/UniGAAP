package sql;

import java.io.File;
import java.util.Queue;


public  class SQLManager {
    private static File database;
    private static Queue<Query> QueryQueue;
    private static Queue<Query> ResultQueue;

    public SQLManager(String database){
        //TODO: automatically process queries when queue is < 1
    }
    public void enqueue(Query input){
        QueryQueue.add(input);
    }
    public Query result(){
        return ResultQueue.poll();
    }
    private void processQuery(){

    }
}

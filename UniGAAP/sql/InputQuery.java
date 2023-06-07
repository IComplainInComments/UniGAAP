package sql;

public class InputQuery implements DataQuery{
    private String query;
    private SQLDataType contains;
    private Object data;

    public InputQuery(String query, SQLDataType contains, Object data){
        this.query = query;
        this.contains = contains;
        this.data = data;
    }
    public InputQuery(String query, SQLDataType contains){
        this.query = query;
        this.contains = contains;
    }

    @Override
    public String query() {
        return this.query;
    }

    @Override
    public QueryType type() {
        return QueryType.INPUT;
    }
    public Object getData(){
        return this.data;
    }
    public SQLDataType dataType(){
        return this.contains;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }
    
}

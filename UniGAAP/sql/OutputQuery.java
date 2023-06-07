package sql;
public class OutputQuery implements DataQuery{
    private String query;
    private SQLDataType contains;
    Object data;

    public OutputQuery(String query, SQLDataType contains){
        this.query = query;
        this.contains = contains;
    }

    @Override
    public String query() {
        return this.query;
    }

    @Override
    public QueryType type() {
        return QueryType.OUTPUT;
    }
    @Override
    public Object getData() {
        return this.data;
    }
    public void setData(Object data){
        this.data = data;
    }

    @Override
    public SQLDataType dataType() {
        return this.contains;
    }

}
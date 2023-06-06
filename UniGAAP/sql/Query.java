package sql;

public interface Query {
    public String query();
    public QueryType type();
    public SQLDataType dataType();
    public Object getData();
    public void setData(Object data);
}

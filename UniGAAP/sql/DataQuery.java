package sql;

/**
 * DataQuery Interface is used as a way to relate query types to a common type;
 * @author IComplainInComments
 * @version 1.0
 */
public interface DataQuery {
    public String query();
    public QueryType type();
    public SQLDataType dataType();
    public Object getData();
    public void setData(Object data);
}

package sql;
/**
 * OutputQuery class for quering database for a database item (getting something).
 * @author IComplainInComents.
 * @version 1.0
 */
public class OutputQuery implements DataQuery{
    private String query, column;
    private SQLDataType contains;
    Object data;
    /**
     * 
     * @param query
     * @param column
     * @param contains
     */
    public OutputQuery(String query, String column,SQLDataType contains){
        this.query = query;
        this.contains = contains;
        this.column = column;
    }
    /**
     * Set the data the OutputQuery should contain as a result.
     * @param data Object
     */
    public void setData(Object data){
        this.data = data;
    }
    /**
     * Return which column we want our data from.
     * @return String
     */
    public String getColumn(){
        return this.column;
    }
    /**
     * Set which column we want our data from.
     * @param column String
     */
    public void setColumn(String column){
        this.column = column;
    }
    /**
     * Return the query String.
     * @return String
     */
    @Override
    public String query() {
        return this.query;
    }
    /**
     * Return which kind of query this object is,
     * @return QueryType
     * @see QueryType
     */
    @Override
    public QueryType type() {
        return QueryType.OUTPUT;
    }
    /**
     * Return the data this query got from it's query.
     * @return Object
     */
    @Override
    public Object getData() {
        return this.data;
    }
    /**
     * Return what kind of data this query will collect.
     * @return SQLDataType.
     * @see SQLDataTypre.
     */
    @Override
    public SQLDataType dataType() {
        return this.contains;
    }
}
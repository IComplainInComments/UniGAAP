package sql;

/**
 * Input Query class is used for queries to input data into the database
 * @author IComplainInComments
 * @version 1.0
 * 
 */
public class InputQuery implements DataQuery{
    private String query;
    private SQLDataType contains;
    private Object data;

    /**
     * Constructor for the object
     * @param query String
     * @param contains SQLDataType
     * @param data Object
     * @see SQLDataType
     * 
     */
    public InputQuery(String query, SQLDataType contains, Object data){
        this.query = query;
        this.contains = contains;
        this.data = data;
    }
    /**
     * Constructor for the object
     * @param query String
     * @param contains SQLDataType
     * @see SQLDataType
     * 
     */
    public InputQuery(String query, SQLDataType contains){
        this.query = query;
        this.contains = contains;
    }
    /**
     * return the data assigned to the object.
     * @return Object
     * 
     */
    public Object getData(){
        return this.data;
    }
    /**
     * Return the data type the InputQuery data variable contains/
     * @return SQLDataType
     * @see SQLDataType
     */
    public SQLDataType dataType(){
        return this.contains;
    }
    /**
     * Set the data the InputQuery should contain.
     * @param data Object
     */
    @Override
    public void setData(Object data) {
        this.data = data;
    }
    /**
     * Return the query string the IntputQuery should run.
     * @return String
     */
    @Override
    public String query() {
        return this.query;
    }
    /**
     * Return the kind of query being performed on the database
     * @return QueryType
     * @see QueryType
     */
    @Override
    public QueryType type() {
        return QueryType.INPUT;
    }
    
}

package ISIS.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class for all record classes.
 */
public abstract class Record {

    protected HashMap<String, Object> fields = null;

    /**
     * Gets the primary key associated with this record.
     */
    protected int getPkey() throws RecordEmptyFieldException {
        if (this.getField("pkey") == null) {
            throw new RecordEmptyFieldException();
        }
        return (int) this.getField("pkey");
    }

    /**
     * Gets a feild from the field hashmap.
     */
    protected Object getField(String key) {
        if (this.fields == null) {
            return null;
        }
        return this.fields.get(key);
    }

    /**
     * Fetches the record in the database associated with getPkey(), using the fetchMode specified.
     */
    public abstract void fetch() throws SQLException, RecordNotFoundException;

    /**
     * Maps our pkey-based select to a hashmap, leveraging the method in the DB class.
     */
    protected static HashMap<String, Object> mapResultSet(ResultSet rs) throws SQLException, RecordNotFoundException {

        ArrayList<HashMap<String, Object>> result = DB.mapResultSet(rs);
        if (result.size() != 1) { // Pkeys are unique; only one result.
            throw new RecordNotFoundException();
        }
        return result.get(0);
    }

    /**
     * Saves the record in the database. If the record is not in the database it is inserted, otherwise it is updated.
     */
    public abstract void save() throws SQLException;
}

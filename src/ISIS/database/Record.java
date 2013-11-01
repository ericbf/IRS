package ISIS.database;

import ISIS.session.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all record classes.
 */
public abstract class Record {

    private HashMap<String, Field> fields;
    private final String tableName;

    /**
     * Base initializer for a Record.
     */
    protected Record(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the primary key associated with this record.
     */
    protected final int getPkey() throws UninitializedFieldException {
        return (int) this.getFieldValue("pkey");
    }

    /**
     * Fill the fields HashMap with empty fields.
     */
    protected final void initializeFields(HashMap<String, Field> fields) {
        this.fields = fields;
    }

    /**
     * Sets a field's value.
     */
    protected final void setFieldValue(String key, Object value) {
        this.fields.get(key).setValue(value);
    }

    /**
     * Gets a field value from the field hashmap.
     */
    protected final Object getFieldValue(String key) {
        return this.fields.get(key).getValue();
    }

    /**
     * Gets a field from the field hashmap. For internal use.
     */
    private Field getField(String key) {
        return this.fields.get(key);
    }

    /**
     * Maps our pkey-based select to a hashmap, leveraging the method in the DB class. WARNING: All fields are set to
     * modifiable; you have to fix it yourself.
     */
    protected final static HashMap<String, Field> mapResultSet(ResultSet rs) throws SQLException, RecordNotFoundException {

        ArrayList<HashMap<String, Field>> result = DB.mapResultSet(rs);
        if (result.size() != 1) { // Pkeys are unique; only one result.
            throw new RecordNotFoundException();
        }
        return result.get(0);
    }

    /**
     * Fetches the record in the database associated with getPkey(), using the fetchMode specified.
     */
    protected final void fetch() throws SQLException, RecordNotFoundException {
        PreparedStatement stmt = Session.getDB().prepareStatement("SELECT * FROM " + this.tableName + " WHERE pkey=?");
        stmt.setInt(1, this.getPkey());
        HashMap<String, Field> fields = Record.mapResultSet(stmt.executeQuery());
        for (Map.Entry<String, Field> entry : fields.entrySet()) {
            if (this.fields.get(entry.getKey()).isUnmodifiable()) {
                entry.getValue().setUnmodifiable();
            }
        }
        this.fields = fields;
    }

    /**
     * Saves the record in the database. If the record is not in the database it is inserted, otherwise it is updated.
     */
    public final void save() throws SQLException {
        /**
         * Get a list of columns that have been changed.
         */
        ArrayList<String> columns = new ArrayList<>(this.fields.size());
        for (Map.Entry<String, Field> entry : this.fields.entrySet()) {
            if (entry.getValue().getWasChanged()) {
                columns.add(entry.getKey());
            }
        }
        if (this.getField("pkey").getWasInitialized() == false) {
            String sql = "INSERT INTO " + this.tableName + "(" + columns.get(0);
            String params = "?";
            for (int i = 1; i < columns.size(); ++i) {
                sql += ", " + columns.get(i);
                params += ", ?";
            }
            sql += ") VALUES (" + params + ")";

            PreparedStatement stmt = Session.getDB().prepareStatement(sql);
            for (int i = 0; i < columns.size(); ++i) {
                stmt.setObject(i+1, this.getFieldValue(columns.get(i)));
            }
            stmt.executeUpdate();
        } else {
            //Nothing was changed, so no need to save.
            if (columns.isEmpty()) {
                return;
            }
            String sql = "UPDATE " + this.tableName + " SET " + columns.get(0) + "=?";
            for (int i = 1; i < columns.size(); ++i) {
                sql += ", " + columns.get(i) + "=?";
            }
            sql += " WHERE pkey=?";

            PreparedStatement stmt = Session.getDB().prepareStatement(sql);
            for (int i = 0; i < columns.size(); ++i) {
                stmt.setObject(i+1, this.getFieldValue(columns.get(i)));
            }
            stmt.setInt(columns.size() + 1, this.getPkey());
            stmt.executeUpdate();
            if(stmt.getUpdateCount() != 1) {
                throw new RecordNotFoundException(); //no rows were changed, that must mean that the record wasn't found
            }

        }
    }
}

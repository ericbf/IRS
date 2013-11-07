package ISIS.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ISIS.gui.ErrorLogger;
import ISIS.misc.Dates;
import ISIS.session.Session;
import ISIS.user.User;

/**
 * Base class for all record classes.
 */
public abstract class Record {

    private HashMap<String, Field> fields;
    public final String tableName;
    private Dates dates = null;
    private boolean hasDates;

    /**
     * Base initializer for a Record.
     */
    protected Record(String tableName, boolean hasDates) {
	this.tableName = tableName;
	this.hasDates = hasDates;
    }

    /**
     * Gets the primary key associated with this record.
     */
    public final int getPkey() throws UninitializedFieldException {
	return (int) this.getFieldValue("pkey");
    }

    /**
     * Fill the fields HashMap with empty fields.
     */
    protected final void initializeFields(HashMap<String, Field> fields) {
	this.fields = fields;
    }

    /**
     * Sets pkey for a record being fetched. (without setting the field as modified)
     */
    protected final void setPkey(Integer pkey) {
	this.getField("pkey").initField(pkey);
    }

    /**
     * Sets the dates associated with the record.
     */
    public final void setDates(Dates dates) {
	this.dates = dates;
    }

    /**
     * Gets the dates object associated with the record.
     */
    public final Dates getDates() {
	if (this.hasDates) {
	    return this.dates;
	} else {
	    throw new UnsupportedOperationException();
	}
    }

    /**
     * Sets a field's value.
     */
    protected final void setFieldValue(String key, Object value) {
	this.fields.get(key).setValue(value);
    }

    /**
     * Gets a field value from the field hashmap. If the field exists but was not yet fetched,
     */
    public final Object getFieldValue(String key) {
	Object value;
	try {
	    value = this.fields.get(key).getValue();
	} catch (UninitializedFieldException e) {
	    try {
		// check that there is a pkey (will throw
		// uninitializedfieldexception otherwise)
		this.getFieldValue("pkey");

		this.fetch();
		value = this.fields.get(key).getValue();
	    } catch (SQLException | RecordNotFoundException ex) {
		ErrorLogger.error("Failed to fetch record.", true, true);
		throw new UninitializedFieldException();
	    }
	}
	return value;
    }

    /**
     * Gets a field from the field hashmap. For internal use.
     */
    Field getField(String key) {
	return this.fields.get(key);
    }

    /**
     * Maps our pkey-based select to a hashmap, leveraging the method in the DB class. WARNING: All fields are set to
     * modifiable; you have to fix it yourself.
     */
    protected static HashMap<String, Field> mapResultSet(ResultSet rs)
	    throws SQLException, RecordNotFoundException {

	ArrayList<HashMap<String, Field>> result = DB.mapResultSet(rs);
	if (result.size() != 1) { // Pkeys are unique; only one result.
	    throw new RecordNotFoundException();
	}
	return result.get(0);
    }

    /**
     * Fetches the record in the database associated with getPkey(), using the fetchMode specified. TODO: optimize the
     * fetch method so it doesn't fetch columns we have initialized already.
     */
    protected final void fetch() throws SQLException, RecordNotFoundException {
	PreparedStatement stmt = Session.getDB().prepareStatement(
		"SELECT * FROM " + this.tableName + " WHERE pkey=?");
	stmt.setInt(1, this.getPkey());
	HashMap<String, Field> fields = Record
		.mapResultSet(stmt.executeQuery());

	// check which fields were set to be unmodifiable or changed, and set
	// them in the new fields map.
	for (Map.Entry<String, Field> entry : fields.entrySet()) {
	    Field value = this.fields.get(entry.getKey());
	    if (value == null || !value.getWasInitialized()) {
		this.fields.put(entry.getKey(), entry.getValue());
		if (value != null
			&& this.fields.get(entry.getKey()).isUnmodifiable()) {
		    entry.getValue().setUnmodifiable(); // was unmodifiable
		}
	    }
	}

	this.readDates();
    }

    /**
     * Reads the dates columns into a date class.
     */
    private void readDates() throws SQLException {
	if (this.hasDates) {
	    this.dates = new Dates(
		    new Date(((Number) this.getFieldValue("createDate"))
		    .longValue()),
		    new User((Integer) this.getFieldValue("createUser"), false),
		    new Date(((Number) this.getFieldValue("modDate"))
		    .longValue()), new User((Integer) this
		    .getFieldValue("modUser"), false));
	}
    }

    /**
     * Updates the dates field for saving.
     */
    private void updateDates() {
	// if no date information is set, set it now.
	if (this.hasDates && this.dates == null) {
	    this.dates = new Dates();
	}

	// Update date information--if no date information is set, set it now.
	if (this.hasDates) {
	    if (this.dates.modified()) { // modified, so tell the map that we're
		// updating
		this.fields.put("createDate", new Field(true));
		this.fields.put("createUser", new Field(true));
		this.fields.put("modDate", new Field(true));
		this.fields.put("modUser", new Field(true));
		this.setFieldValue("createDate", this.dates.getCreatedDate()
			.getTime());
		this.setFieldValue("createUser", this.dates.getCreatedBy()
			.getPkey());
		this.setFieldValue("modDate", this.dates.getModDate().getTime());
		this.setFieldValue("modUser", this.dates.getModBy().getPkey());
	    }
	}
    }

    /**
     * Saves the record in the database. If the record is not in the database it is inserted, otherwise it is updated.
     */
    public final void save() throws SQLException {
	// update dates
	this.updateDates();

	/**
	 * Get a list of columns that have been changed. Should be all columns if it's a new record.
	 */
	ArrayList<String> columns = new ArrayList<>(this.fields.size());
	for (Map.Entry<String, Field> entry : this.fields.entrySet()) {
	    if (entry.getValue().getWasChanged()) {
		columns.add(entry.getKey());
	    }
	}

	if (this.getField("pkey").getWasInitialized() == false) { // New record
	    // generate sql--something like INSERT INTO tab (col1, col2) VALUES
	    // (?, ?)
            StringBuilder sql = new StringBuilder("INSERT INTO " + this.tableName + "(" + columns.get(0));
	    StringBuilder params = new StringBuilder("?");
	    for (int i = 1; i < columns.size(); ++i) {
		sql.append(", ").append(columns.get(i));
		params.append(", ?");
	    }
	    sql.append(") VALUES (").append(params.toString()).append(")");

	    PreparedStatement stmt = Session.getDB().prepareStatement(sql.toString());

	    // bind columns
	    for (int i = 0; i < columns.size(); ++i) {
		stmt.setObject(i + 1, this.getFieldValue(columns.get(i)));
	    }

	    stmt.executeUpdate(); // do it
	} else { // Existing record
	    // Nothing was changed, so no need to save.
	    if (columns.isEmpty()) {
		return;
	    }
	    // generate sql--something like UPDATE tab SET col1=?, col2=? WHERE
	    // pkey=?
	    String sql = "UPDATE " + this.tableName + " SET " + columns.get(0)
		    + "=?";
	    for (int i = 1; i < columns.size(); ++i) {
		sql += ", " + columns.get(i) + "=?";
	    }
	    sql += " WHERE pkey=?";

	    PreparedStatement stmt = Session.getDB().prepareStatement(sql);

	    // bind columns
	    for (int i = 0; i < columns.size(); ++i) {
		stmt.setObject(i + 1, this.getFieldValue(columns.get(i)));
	    }
	    stmt.setInt(columns.size() + 1, this.getPkey());

	    stmt.executeUpdate();
	    if (stmt.getUpdateCount() != 1) {
		throw new RecordNotFoundException(); // no rows were changed,
		// that must mean that
		// the record wasn't
		// found
	    }

	}
    }
}

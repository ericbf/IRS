package ISIS.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ISIS.database.DB.TableName;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Dates;
import ISIS.session.Session;
import ISIS.user.User;

/**
 * Base class for all record classes.
 */
public abstract class Record {
	
	/**
	 * Maps our pkey-based select to a hashmap, leveraging the method in the DB
	 * class. WARNING: All fields are set to modifiable, but we don't care
	 * anymore
	 */
	protected static HashMap<String, Field> mapResultSet(ResultSet rs)
			throws SQLException, RecordNotFoundException {
		
		ArrayList<HashMap<String, Field>> result = DB.mapResultSet(rs);
		if (result.size() != 1) { // Pkeys are unique; only one result.
			throw new RecordNotFoundException();
		}
		return result.get(0);
	}
	
	private HashMap<String, Field>	fields;
	
	private Dates					dates	= null;
	
	/**
	 * Base initializer for a Record.
	 */
	protected Record() {
		this.fields = new HashMap<>();
	}
	
	/**
	 * Base initializer for a Record whose data has already been retrieved.
	 */
	protected Record(HashMap<String, Field> map) {
		this.initializeFields(map);
		// This condition seems to happen sometimes on (faulty) joins, and
		// obviously we're just going to get errors everywhere if it does.
		if (this.getField("pkey").getValue() == null) {
			throw new RecordNotFoundException(
					"Record has no primary key... Faulty join?");
		}
	}
	
	/**
	 * Fetches the record in the database associated with getPkey(), using the
	 * fetchMode specified. TODO: optimize the fetch method so it doesn't fetch
	 * columns we have initialized already.
	 */
	protected final void fetch() throws SQLException, RecordNotFoundException {
		PreparedStatement stmt = Session.getDB().prepareStatement(
				"SELECT * FROM " + this.getTableName() + " WHERE pkey=?");
		stmt.setInt(1, this.getPkey());
		this.fields = Record.mapResultSet(stmt.executeQuery());
		
		this.readDates();
	}
	
	/**
	 * Gets the dates object associated with the record.
	 */
	public final Dates getDates() {
		if (this.hasDates()) {
			return this.dates;
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * Gets a field from the field hashmap. For internal use.
	 */
	Field getField(String key) {
		return this.fields.get(key);
	}
	
	/**
	 * Gets a field value from the field hashmap. If the field exists but was
	 * not yet fetched,
	 */
	public final Object getFieldValue(String key) {
		Object value;
		if (this.fields.containsKey(key)) {
			value = this.fields.get(key).getValue();
		} else {
			try {
				// check that there is a pkey (will throw
				// UninitializedFieldException otherwise)
				if (key.equals("pkey")) {
					throw new RecordNotFoundException();
				}
				this.getFieldValue("pkey");
				
				this.fetch();
				if (this.fields.containsKey(key)) {
					value = this.fields.get(key).getValue();
				} else {
					throw new NonexistentFieldException(
							"Record does not contain the requested key.");
				}
			} catch (SQLException e) {
				ErrorLogger.error(e, "Failed to retrieve field", true, true);
				throw new UninitializedFieldException();
				// Treat it as if the field wasn't initialized since we can't
				// get it.
			}
		}
		return value;
	}
	
	/**
	 * Gets the primary key associated with this record.
	 */
	public final int getPkey() throws UninitializedFieldException {
		return (int) this.getFieldValue("pkey");
	}
	
	protected abstract TableName getTableName();
	
	protected abstract boolean hasDates();
	
	/**
	 * Fill the fields HashMap with empty fields.
	 */
	public void initializeFields(HashMap<String, Field> fields) {
		this.fields = fields;
	}
	
	public final boolean isActive() {
		try {
			return this.getFieldValue("active") == null
					|| ((int) this.getFieldValue("active")) == 1;
		} catch (NonexistentFieldException e) {
			return true;
			// true if no active field.
		}
	}
	
	public final boolean isChanged() {
		for (Field field : this.fields.values()) {
			if (field.getWasChanged()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Called after saving the record; override it if it is necessary in your
	 * subclass.
	 */
	protected void postSave() throws SQLException {}
	
	/**
	 * Called before saving the record; override it if it is necessary in your
	 * subclass.
	 */
	protected void preSave() {}
	
	/**
	 * Reads the dates columns into a date class.
	 */
	private void readDates() throws SQLException {
		if (this.hasDates()) {
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
	 * Saves the record in the database. If the record is not in the database it
	 * is inserted, otherwise it is updated.
	 */
	public final void save() throws SQLException {
		// update dates
		this.updateDates();
		
		// prep
		this.preSave();
		
		/**
		 * Get a list of columns that have been changed. Should be all columns
		 * if it's a new record.
		 */
		ArrayList<String> columns = new ArrayList<>(this.fields.size());
		for (Map.Entry<String, Field> entry : this.fields.entrySet()) {
			if (entry.getValue().getWasChanged()) {
				columns.add(entry.getKey());
			}
		}
		
		if (!this.fields.containsKey("pkey")) { // New record
			// generate sql--something like INSERT INTO tab (col1, col2) VALUES
			// (?, ?)
			StringBuilder sql = new StringBuilder("INSERT INTO "
					+ this.getTableName() + "(" + columns.get(0));
			StringBuilder params = new StringBuilder("?");
			for (int i = 1; i < columns.size(); ++i) {
				sql.append(", ").append(columns.get(i));
				params.append(", ?");
			}
			sql.append(") VALUES (").append(params.toString()).append(")");
			
			PreparedStatement stmt = Session.getDB().prepareStatement(
					sql.toString());
			
			// bind columns
			for (int i = 0; i < columns.size(); ++i) {
				stmt.setObject(i + 1, this.getFieldValue(columns.get(i)));
			}
			
			stmt.executeUpdate(); // do it
			
			// populate pkey field, for post save stuff
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				this.setPkey(rs.getInt(1));
			} else {
				throw new RecordSaveException(
						"Couldn't verify that record was saved.");
			}
		} else { // Existing record
			// Nothing was changed, so no need to save.
			if (columns.isEmpty()) {
				return;
			}
			// generate sql--something like UPDATE tab SET col1=?, col2=? WHERE
			// pkey=?
			String sql = "UPDATE " + this.getTableName() + " SET "
					+ columns.get(0) + "=?";
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
		// postsave
		this.postSave();
		
		for (Field field : this.fields.values()) {
			field.save(); // sets the field back to not being modified.
		}
		
		Session.updateTable(this.getTableName(), this.getPkey());
	}
	
	/**
	 * Sets the dates associated with the record.
	 */
	public final void setDates(Dates dates) {
		this.dates = dates;
	}
	
	/**
	 * Sets a field's value.
	 */
	protected final void setFieldValue(String key, Object value) {
		if (!this.fields.containsKey(key)) {
			this.fields.put(key, new Field());
		}
		this.fields.get(key).setValue(value);
	}
	
	/**
	 * Sets pkey for a record being fetched. (without setting the field as
	 * modified)
	 */
	protected final void setPkey(Integer pkey) {
		if (!this.fields.containsKey("pkey")) {
			this.fields.put("pkey", new Field());
		}
		this.getField("pkey").initField(pkey);
	}
	
	/**
	 * Updates the dates field for saving.
	 */
	private void updateDates() {
		// if no date information is set, set it now.
		if (this.hasDates() && this.dates == null) {
			this.dates = new Dates();
		}
		
		// Update date information--if no date information is set, set it now.
		if (this.hasDates()) {
			if (this.dates.modified()) { // modified, so tell the map that we're
				// updating
				this.fields.put("createDate", new Field());
				this.fields.put("createUser", new Field());
				this.fields.put("modDate", new Field());
				this.fields.put("modUser", new Field());
				this.setFieldValue("createDate", this.dates.getCreatedDate()
						.getTime());
				this.setFieldValue("createUser", this.dates.getCreatedBy()
						.getPkey());
				this.setFieldValue("modDate", this.dates.getModDate().getTime());
				this.setFieldValue("modUser", this.dates.getModBy().getPkey());
			}
		}
	}
}

package ISIS.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class JunctionTable {

    private class JunctionRecord extends Record {

	public JunctionRecord(String tableName, boolean hasDates, HashMap<String, Field> record) {
	    super(tableName, hasDates);
	    this.initializeFields(record);
	}
    }
    // ArrayList of field arrays that are at least 3 elements long-pkey, record1_fk, record2_fk
    public ArrayList<HashMap<String, Field>> elements = new ArrayList<>();
    private final String tableName;
    //names of the foreign key fields
    private final String type1, type2;
    private boolean hasDates;

    public JunctionTable(String table, ArrayList<HashMap<String, Field>> junctionRecords, String type1, String type2, boolean hasDates) {
	this.tableName = table;
	this.elements = junctionRecords;
	this.type1 = type1;
	this.type2 = type2;
	this.hasDates = hasDates;
    }

    /**
     * Adds a junction record between the two given keys.
     */
    public void addRecord(int record1, int record2) {
	HashMap<String, Field> record = new HashMap<>(3);
	record.put("pkey", new Field(false));
	record.put(this.type1, new Field(false));
	record.put(this.type2, new Field(false));
	this.elements.add(record);
    }

    /**
     * Adds a junction record with your supplied keys.
     */
    public void addRecord(HashMap<String, Field> record) {
	this.elements.add(record);
    }

    /**
     * Saves the records (that have been changed) in this instance.
     */
    public void saveRecords() throws SQLException {
	for (HashMap<String, Field> element : elements) {
	    JunctionRecord record = new JunctionRecord(this.tableName, this.hasDates, element);
	    record.save();
	}
    }
}

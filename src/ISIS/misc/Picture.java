package ISIS.misc;

import ISIS.database.DB.TableName;
import ISIS.database.Record;

/**
 * A picture class for storing a picture and various metadata. This metadata
 * includes a name, a note, and dates associated with the picture. A picture
 * instance is always referenced from another record.
 */
public class Picture extends Record {
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	// public static TableName tableName = TableName.picture;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= false;
	
	/**
	 * Public constructor. Takes a picture database key, and has the option to
	 * populate fields from the database.
	 * 
	 * @param pkey
	 * @param populate
	 */
	public Picture(int pkey, boolean populate) {
		super();
	}
	
	/**
	 * Public constructor. A picture starts with a name, binary representation,
	 * a note, and dates associated with its creation.
	 * 
	 * @param name
	 * @param picture
	 * @param note
	 * @param dates
	 */
	public Picture(String name, byte[] picture, String note, Dates dates) {
		super();
	}
	
	/**
	 * Gets the name associated with the picture.
	 * 
	 * @return
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Gets the note associated with the picture.
	 * 
	 * @return
	 */
	public String getNote() {
		return null;
	}
	
	/**
	 * Gets the picture associated with the picture record.
	 * 
	 * @return
	 */
	public byte[] getPicture() {
		return null;
	}
	
	@Override
	protected TableName getTableName() {
		return null;
	}
	
	@Override
	protected boolean hasDates() {
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.
	}
}

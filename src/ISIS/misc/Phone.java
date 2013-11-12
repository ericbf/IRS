package ISIS.misc;

import java.sql.SQLException;
import java.util.HashMap;

import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;

/**
 * Phone is a generic class for phone numbers. Invariants: The Transaction
 * references exactly one number, one primary status, and one type.
 * 
 * @number != null, primary == true || primary == false, PhoneType != null
 */
public class Phone extends Record {
	/**
	 * The type of the phone number.
	 */
	public static enum PhoneType {
		
		HOME, CELL, FAX
	}
	
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.phone;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= false;
	
	public Phone(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Public constructor. Take a Phone database key, and has the option to
	 * populate the fields from the database.
	 */
	public Phone(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Public constructor. A Phone record starts with a number, primary status,
	 * and type (mobile/home/etc).
	 */
	public Phone(String number, boolean primary, PhoneType type) {
		super();
		this.setFieldValue("number", number);
		this.setFieldValue("primary_num", (primary ? 1 : 0));
		this.setFieldValue("type", type.toString());
	}
	
	/**
	 * Gets the phone number.
	 */
	public String getNumber() {
		return (String) this.getFieldValue("number");
	}
	
	/**
	 * Returns the phone number's primary status.
	 */
	public boolean getPrimary() {
		return ((Integer) this.getFieldValue("primary_num")) == 1;
	}
	
	@Override
	protected TableName getTableName() {
		return Phone.tableName;
	}
	
	/**
	 * Returns the type of the phone number.
	 */
	public PhoneType getType() {
		return PhoneType.valueOf((String) this.getFieldValue("type"));
	}
	
	@Override
	protected boolean hasDates() {
		return Phone.hasDates_;
	}
	
	/**
	 * Sets the phone number's primary status.
	 */
	public void setPrimary(Boolean primary) {
		this.setFieldValue("primary_num", (primary ? 1 : 0));
	}
}

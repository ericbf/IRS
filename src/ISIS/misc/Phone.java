package ISIS.misc;

import ISIS.database.Record;

/**
 * Phone is a generic class for phone numbers. Invariants: The Transaction
 * references exactly one number, one primary status, and one type.
 * 
 * @number != null, primary == true || primary == false, PhoneType != null
 */
public class Phone extends Record {
	/* Fields omitted */
	
	/**
	 * The type of the phone number.
	 */
	public static enum PhoneType {
		
		HOME, CELL, FAX
	}
	
	/**
	 * Public constructor. A Phone record starts with a number, primary status,
	 * and type (mobile/home/etc).
	 */
	public Phone(String number, boolean primary, PhoneType type) {
		super("penis", false);
	}
	
	/**
	 * Gets the phone number.
	 */
	public String getNumber() {
		return null;
	}
	
	/**
	 * Returns the phone number's primary status.
	 */
	public boolean getPrimary() {
		return false;
	}
	
	/**
	 * Returns the type of the phone number.
	 */
	public PhoneType getType() {
		return null;
	}
}

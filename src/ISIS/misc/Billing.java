package ISIS.misc;

import java.util.Date;

import ISIS.database.Record;

/**
 * General purpose class for representing billing information.
 */
public class Billing extends Record {
	/* Fields omitted */
	
	public static enum BillingType {
		
		CREDIT, CASH, OTHER
	}
	
	/**
	 * Public constructor for CREDIT billing.
	 */
	public Billing(Address address, String cardNumber, Date expDate, String CSC) {
		super("Penis", false);
	}
	
	/**
	 * Public constructor for CASH billing.
	 */
	public Billing(Address address) {
		super("Penis", false);
	}
	
	/**
	 * Public constructor for OTHER billing.
	 */
	public Billing(Address address, String note) {
		super("Penis", false);
	}
	
	/**
	 * Gets the billing type associated with the record.
	 */
	public BillingType getBillingType() {
		return null;
	}
	
	/**
	 * Gets the billing address.
	 */
	public Address getAddress() {
		return null;
	}
	
	/**
	 * Gets the credit card number.
	 * 
	 * @pre getBillingType == BillingType.CREDIT
	 */
	public String getCardNumber() {
		return null;
	}
	
	/**
	 * Gets the note.
	 * 
	 * @pre getBillingType() == BillingType.OTHER
	 */
	public String getNote() {
		return null;
	}
}

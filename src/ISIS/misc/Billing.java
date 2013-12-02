package ISIS.misc;

import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * General purpose class for representing billing information.
 */
public class Billing extends Record {
	public static enum BillingType {
		
		CREDIT, CASH, OTHER
	}
	
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.billing;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= false;
	
	Address					address		= null;
	
	/**
	 * Public constructor for CASH or OTHER billing.
	 */
	public Billing(Address address, BillingType billingType) {
		super();
		
		if (billingType.equals(BillingType.CREDIT)) {
			throw new RuntimeException("COMMENTS.");
		}
		this.setFieldValue("type", billingType.toString());
		
		if (address != null) {
			this.setFieldValue("address", address.getPkey());
		}
	}
	
	/**
	 * Public constructor for CREDIT billing.
	 */
	public Billing(Address address, String cardNumber, Date expDate, String CCV) {
		super();
		
		this.setFieldValue("type", BillingType.CREDIT.toString());
		this.address = address;
		if (address != null) {
			this.setFieldValue("address", address.getPkey());
		}
		
		this.setFieldValue("number", cardNumber);
		SimpleDateFormat df = new SimpleDateFormat("MM/yy");
		this.setFieldValue("expiration", df.format(expDate));
		this.setFieldValue("CCV", CCV);
	}
	
	public Billing(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Gets a set of billing info from the DB using the given key.
	 */
	public Billing(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Gets the billing address.
	 */
	public Address getAddress() throws SQLException {
		if (this.address == null) {
            if(this.getFieldValue("address") != null) {
			    return new Address((Integer) this.getFieldValue("address"), false);
            }
            return null;
		} else {
			return this.address;
		}
	}

    public void setAddress(Address address) {
        this.setFieldValue("address", address.getPkey());
    }
	
	/**
	 * Gets the billing type associated with the record.
	 */
	public BillingType getBillingType() {
		return BillingType.valueOf(((String) this.getFieldValue("type")));
	}


    public void setBillingType(BillingType type) {
        this.setFieldValue("type", type.toString());
    }
	
	/**
	 * Gets the credit card number.
	 * 
	 * @pre getBillingType == BillingType.CREDIT
	 */
	public String getCardNumber() {
		if (this.getBillingType().equals(BillingType.CREDIT)) {
			return (String) this.getFieldValue("number");
		} else {
			throw new RuntimeException(
					"Cannot get card number unless the billing info is a card!");
		}
	}
	
	/**
	 * Gets the credit card number.
	 * 
	 * @pre getBillingType == BillingType.CREDIT
	 */
	public String getCCV() {
		if (this.getBillingType().equals(BillingType.CREDIT)) {
			return (String) this.getFieldValue("CCV");
		} else {
			throw new RuntimeException(
					"Cannot get CCV unless the billing info is a card!");
		}
	}
	
	public Date getExpiration() {
		try {
			Date date = new SimpleDateFormat("MM/yy", Locale.ENGLISH)
					.parse((String) this.getFieldValue("expiration"));
			return date;
		} catch (ParseException e) {
			ErrorLogger.error(e, "Failed to parse expiration date.", true,
					false);
			throw new RuntimeException("Failed to retrieve date");
		}
	}

    /**
     * Allows you to set the active status of the Customer.
     */
    public void setActive(boolean active) {
        this.setFieldValue("active", ((active) ? 1 : 0));
    }
	
	@Override
	protected TableName getTableName() {
		return Billing.tableName;
	}
	
	@Override
	protected boolean hasDates() {
		return Billing.hasDates_;
	}
        
    @Override
    public String toString()  {
        return this.getBillingType().toString();
    }
}

package ISIS.misc;

import java.sql.SQLException;
import java.util.HashMap;

import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;

/**
 * General purpose class for representing postal addresses.
 */
public class Address extends Record {
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.address;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= false;
	
	/**
	 * Public constructor.
	 * 
	 * @param active
	 * @param primary
	 * @param country
	 * @param title
	 * @param zip
	 * @param state
	 * @param city
	 * @param county
	 * @param streetAddress
	 */
	public Address(boolean active, boolean primary, String country,
			String title, String zip, String state, String city, String county,
			String streetAddress) {
		super();
		this.setFieldValue("active", (active ? 1 : 0));
		this.setFieldValue("primary_status", (primary ? 1 : 0));
		this.setFieldValue("title", title);
		this.setFieldValue("country", country);
		this.setFieldValue("county", county);
		this.setFieldValue("zip", zip);
		this.setFieldValue("state", state);
		this.setFieldValue("city", city);
		this.setFieldValue("st_address", streetAddress);
	}
	
	public Address(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Public constructor. Take an address database key, and has the option to
	 * populate the fields from the database.
	 * 
	 * @param pkey
	 * @param populate
	 * @throws SQLException
	 */
	public Address(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Gets the record's active status.
	 * 
	 * @return
	 */
	public Boolean getActive() {
		return ((Integer) this.getFieldValue("active")) == 1;
	}
	
	/**
	 * Gets the city.
	 * 
	 * @return
	 */
	public String getCity() {
		return (String) this.getFieldValue("city");
	}
	
	/**
	 * Gets the country.
	 * 
	 * @return
	 */
	public String getCountry() {
		return (String) this.getFieldValue("country");
	}
	
	/**
	 * Gets the county.
	 * 
	 * @return
	 */
	public String getCounty() {
		return (String) this.getFieldValue("county");
	}
	
	/**
	 * gets the record's primary status
	 * 
	 * @return
	 */
	public boolean getPrimary() {
		return ((Integer) this.getFieldValue("primary_status")) == 1;
	}
	
	/**
	 * Gets the state.
	 * 
	 * @return
	 */
	public String getState() {
		return (String) this.getFieldValue("state");
	}
	
	/**
	 * Gets the street address.
	 * 
	 * @return
	 */
	public String getStreetAddress() {
		return (String) this.getFieldValue("st_address");
	}
	
	@Override
	protected TableName getTableName() {
		return Address.tableName;
	}
	
	/**
	 * Gets the record's title.
	 * 
	 * @return
	 */
	public String getTitle() {
		return (String) this.getFieldValue("title");
	}
	
	/**
	 * Gets the ZIP.
	 * 
	 * @return
	 */
	public String getZIP() {
		return (String) this.getFieldValue("zip");
	}
	
	@Override
	protected boolean hasDates() {
		return Address.hasDates_;
	}
	
	/**
	 * Sets the record's active status.
	 * 
	 * @param active
	 */
	public void setActive(Boolean active) {
		this.setFieldValue("active", (active ? 1 : 0));
	}
	
	/**
	 * Sets the record's primary status
	 * 
	 * @param primary
	 */
	public void setPrimary(boolean primary) {
		this.setFieldValue("primary", (primary ? 1 : 0));
		
	}
	
	@Override
	public String toString() {
		return this.getStreetAddress();
	}
}

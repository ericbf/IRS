package ISIS.customer;

import ISIS.database.Record;
import ISIS.misc.Address;
import ISIS.misc.Dates;
import ISIS.misc.Phone;
import ISIS.misc.Picture;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A Customer is the entity that intends to purchase products from the client. A
 * customer record consists of a status field, an email address, a password, an
 * address, a note, a phone number, a first and last name, an initiating user,
 * and, optionally, a picture. Invariants: The Customer record references
 * exactly one set of associated dates.
 * 
 * @dates != null The Customer record references a customer status field.
 * @active == true || active == false The customer record has a primary key
 *         field that is always set.
 * @pkey > 0 The customer record name fields are always set.
 * @fname != null, lname != null, mname != null
 */
public class Customer extends Record {
	/* Fields omitted */
	
	/**
	 * Public constructor. A Customer starts with a name and a status.
	 */
	public Customer(String fname, String lname, String mname, boolean active) {
		super("Penis", true);
	}
	
	/**
	 * Public constructor. Take a Customer database key, and has the option to
	 * populate the fields from the database.
	 */
	public Customer(int pkey, boolean populate) {
		super("Penis", true);
	}
	
	/**
	 * Allows you to set the active status of the Customer.
	 */
	public void setActive(boolean active) {}
	
	/**
	 * Gets the active status of the Customer.
	 */
	public boolean isActive() {
		return false;
	}
	
	/**
	 * Gets the Customer's first name.
	 */
	public String getFirstName() {
		return null;
	}
	
	/**
	 * Gets the Customer's last name.
	 */
	public String getLastName() {
		return null;
	}
	
	/**
	 * Sets the Customer's note field.
	 */
	public void setNote(String note) {}
	
	/**
	 * Gets the Customer's note field.
	 */
	public String getNote() {
		return null;
	}
	
	/**
	 * Sets the Customer's password.
	 */
	public void setPassword(String password) {}
	
	/**
	 * Gets the Customer's password.
	 */
	public String getPassword() {
		return null;
	}
	
	/**
	 * Sets the Customer's email address.
	 */
	public void setEmail(String email) {}
	
	/**
	 * Gets the Customer's email address.
	 */
	public String getEmail() {
		return null;
	}
	
	/**
	 * Adds an address to the customer record.
	 */
	public void addAddress(Address address) {}
	
	/**
	 * Remove an address from the customer record.
	 * 
	 * @pre getAddresses().contains(address) == true
	 * @post getAddresses().contains(address) == false
	 */
	public void removeAddress(Address address) {}
	
	/**
	 * Gets all addresses associated with the customer record.
	 */
	public ArrayList<Address> getAddresses() {
		return null;
	}
	
	/**
	 * Adds a phone number to the customer record.
	 * 
	 * @pre getPhoneNums().contains(phone) == false
	 * @post getPhoneNums().contains(phone) == true
	 */
	public void addPhoneNum(Phone phone) {}
	
	/**
	 * Removes a phone number from the customer record.
	 * 
	 * @pre getPhoneNums().contains(phone) == true
	 * @post getPhoneNums().contains(phone) == false
	 */
	public void removePhoneNum(Phone phone) {}
	
	/**
	 * Gets all phone numbers and information associated with the numbers from
	 * the customer record.
	 */
	public ArrayList<Phone> getPhoneNums() {
		return null;
	}
	
	/**
	 * Adds a picture to the customer record.
	 * 
	 * @pre getPictures().contains(picture) == false
	 * @pre getPictures().contains(picture) == true
	 */
	public void addPicture(Picture picture) {}
	
	/**
	 * Removes a picture from the Customer record.
	 * 
	 * @pre getPictures().contains(picture) == true
	 * @pre getPictures().contains(picture) == false
	 */
	public void removePicture(Picture picture) {}
	
	/**
	 * Gets the pictures associated with the customer record.
	 */
	public ArrayList<Picture> getPictures(Picture picture) {
		return null;
	}
}

package ISIS.customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.database.DB;
import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.database.RecordSaveException;
import ISIS.database.UninitializedFieldException;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Address;
import ISIS.misc.Billing;
import ISIS.misc.Phone;
import ISIS.misc.Picture;
import ISIS.session.Session;

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
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName		tableName				= TableName.customer;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean		hasDates_				= true;
	
	boolean						addressesInitialized	= false,
			numbersInitialized = false, billingInitialized;
	private ArrayList<Address>	addresses				= new ArrayList<Address>();
	private ArrayList<Address>	addressesToSave			= new ArrayList<Address>();
	private ArrayList<Address>	addressesToRemove		= new ArrayList<Address>();
	private ArrayList<Billing>	billing					= new ArrayList<Billing>();
	private ArrayList<Billing>	billingToSave			= new ArrayList<Billing>();
	private ArrayList<Billing>	billingToRemove			= new ArrayList<Billing>();
	private ArrayList<Phone>	numbers					= new ArrayList<Phone>();
	private ArrayList<Phone>	numbersToSave			= new ArrayList<Phone>();
	private ArrayList<Phone>	numbersToRemove			= new ArrayList<Phone>();
	
	public Customer(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Public constructor. Take a Customer database key, and has the option to
	 * populate the fields from the database.
	 */
	public Customer(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Public constructor. A Customer starts with a name and a status.
	 * 
	 * @pre - receives all required parameters
	 * @post - returns object set to those records
	 */
	public Customer(String fname, String lname, String email, String note,
			String password, boolean active) {
		super();
		
		this.setFieldValue("fname", fname);
		this.setFieldValue("lname", lname);
		this.setFieldValue("email", email);
		this.setFieldValue("note", note);
		this.setFieldValue("password", password);
		this.setActive(active);
	}
	
	/**
	 * Public constructor. A Customer starts with a name and a status.
	 * 
	 * @pre - receives all required parameters
	 * @post - returns object set to those records
	 */
	public void addAddress(Address address) {
		this.addresses.add(address);
		this.addressesToSave.add(address);
	}
	
	/**
	 * Adds an address to the customer record.
	 * 
	 * @pre - none
	 * @post - new billing address saved and related to customer record
	 */
	public void addBilling(Billing billing) {
		this.billing.add(billing);
		this.billingToSave.add(billing);
	}
	
	/**
	 * Adds a phone number to the customer record.
	 * 
	 * @pre getPhoneNums().contains(phone) == false
	 * @post getPhoneNums().contains(phone) == true
	 */
	public void addPhoneNum(Phone phone) {
		this.numbers.add(phone);
		this.numbersToSave.add(phone);
	}
	
	/**
	 * Adds a picture to the customer record.
	 * 
	 * @pre getPictures().contains(picture) == false
	 * @pre getPictures().contains(picture) == true
	 */
	public void addPicture(Picture picture) {}
	
	/**
	 * Gets all addresses associated with the customer record.
	 * 
	 * @pre - receives a customer record
	 * @post - returns all addresses associated with the customer records
	 */
	public ArrayList<Address> getAddresses() throws SQLException {
		if (this.addressesInitialized) {
			return this.addresses;
		}
		try {
			this.getPkey(); // check if the record has ever been saved.
		} catch (UninitializedFieldException e) {
			return this.addresses; // it hasn't been; nothing to find.
		}
		String sql = "SELECT a.* FROM customer_address AS ca LEFT JOIN address AS a ON ca.address=a.pkey WHERE ca.customer=?";
		try {
			PreparedStatement stmt = Session.getDB().prepareStatement(sql);
			stmt.setInt(1, this.getPkey());
			ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
					.executeQuery());
			for (HashMap<String, Field> result : results) {
				this.addresses.add(new Address(result));
			}
			this.addressesInitialized = true;
			return this.addresses;
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to retrieve addresses.", true, true);
			throw e;
		}
	}
	
	/**
	 * Gets all addresses associated with the customer record.
	 * 
	 * @pre - none
	 * @post - returns all billing addresses associated with customer record
	 */
	public ArrayList<Billing> getBilling() throws SQLException {
		if (this.billingInitialized) {
			return this.billing;
		}
		try {
			this.getPkey(); // check if the record has ever been saved.
		} catch (UninitializedFieldException e) {
			return this.billing; // it hasn't been; nothing to find.
		}
		String sql = "SELECT b.* FROM customer_billing AS cb LEFT JOIN billing AS b ON cb.billing=b.pkey WHERE cb.customer=?";
		try {
			PreparedStatement stmt = Session.getDB().prepareStatement(sql);
			stmt.setInt(1, this.getPkey());
			ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
					.executeQuery());
			for (HashMap<String, Field> result : results) {
				this.billing.add(new Billing(result));
			}
			this.billingInitialized = true;
			return this.billing;
		} catch (SQLException e) {
			ErrorLogger
					.error(e, "Failed to retrieve billing info.", true, true);
			throw e;
		}
	}
	
	/**
	 * Gets the Customer's email address.
	 * 
	 * @pre - none
	 * @post - returns customer's email address
	 */
	public String getEmail() {
		return (String) this.getFieldValue("email");
	}
	
	/**
	 * Gets the Customer's first name.
	 * 
	 * @pre- none
	 * @post - return first name
	 */
	public String getFirstName() {
		return (String) this.getFieldValue("fname");
	}
	
	/**
	 * Gets the Customer's last name.
	 * 
	 * @pre - none
	 * @post - returns last name
	 */
	public String getLastName() {
		return (String) this.getFieldValue("lname");
	}
	
	/**
	 * Gets the Customer's note field.
	 * 
	 * @pre - none
	 * @post - returns note field
	 */
	public String getNote() {
		return (String) this.getFieldValue("note");
	}
	
	/**
	 * Gets the Customer's password.
	 * 
	 * @pre - none
	 * @post - returns customer password
	 */
	public String getPassword() {
		return (String) this.getFieldValue("password");
	}
	
	/**
	 * Gets all phone numbers and information associated with the numbers from
	 * the customer record. Returns empty list on failure.
	 * 
	 * @pre - none
	 * @post - returns list of related numbers, or empty list if none found
	 */
	public ArrayList<Phone> getPhoneNums() throws SQLException {
		if (this.numbersInitialized) {
			return this.numbers;
		}
		try {
			this.getPkey(); // check if the record has ever been saved.
		} catch (UninitializedFieldException e) {
			return this.numbers; // it hasn't been; nothing to find.
		}
		String sql = "SELECT p.* FROM customer_phone AS cp LEFT JOIN phone AS p ON cp.phone=p.pkey "
				+ "WHERE cp.customer=?";
		try {
			PreparedStatement stmt = Session.getDB().prepareStatement(sql);
			stmt.setInt(1, this.getPkey());
			ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
					.executeQuery());
			for (HashMap<String, Field> result : results) {
				this.numbers.add(new Phone(result));
			}
			return this.numbers;
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to retrieve phone numbers.", true,
					true);
			throw e;
		}
	}
	
	/**
	 * Gets the pictures associated with the customer record.
	 * 
	 * @pre - none
	 * @post - returns picture associated with customer
	 */
	public ArrayList<Picture> getPictures(Picture picture) {
		return null;
	}
	
	/**
	 * Returns the primary address, or null if there are no addresses. If no
	 * address is marked primary, then one is picked arbitrarily.
	 * 
	 * @pre - none
	 * @post - return primary address if one exists, null if none exists
	 */
	public Address getPrimaryAddress() throws SQLException {
		ArrayList<Address> addresses = this.getAddresses();
		if (addresses.size() == 0) {
			return null;
		}
		for (Address address : addresses) {
			if (address.getPrimary()) {
				return address;
			}
		}
		return addresses.get(0);
	}
	
	/**
	 * Gets the primary phone number. Returns null if there are no numbers, or
	 * an arbitrary number if there is no primary. pre - none post - return
	 * primary phone number if one exists, null if none exists
	 */
	public Phone getPrimaryNum() throws SQLException {
		for (Phone number : this.getPhoneNums()) {
			if (number.getPrimary()) {
				return number;
			}
		}
		if (this.getPhoneNums().size() == 0) {
			return null;
		} else {
			return this.getPhoneNums().get(0);
		}
	}
	
	@Override
	protected TableName getTableName() {
		return Customer.tableName;
	}
	
	@Override
	protected boolean hasDates() {
		return Customer.hasDates_;
	}
	
	@Override
	protected void postSave() throws SQLException {
		// delete removed numbers
		if (this.numbersToRemove.size() > 0) {
			String sql = "DELETE FROM customer_phone WHERE phone IN ("
					+ DB.preparedArgsBuilder(this.numbersToRemove.size(), "?")
					+ ") AND customer=?";
			try {
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				for (; i < (this.numbersToRemove.size() + 1); ++i) {
					stmt.setInt(i, this.numbersToRemove.get(i - 1).getPkey());
				}
				stmt.setInt(i, this.getPkey());
				stmt.executeUpdate();
				numbersToRemove.clear();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Could not remove phone numbers.", true,
						true);
				throw e;
			}
		}
		
		// save any new phone numbers
		if (this.numbersToSave.size() > 0) {
			try {
				String sql = "INSERT INTO customer_phone (customer, phone) VALUES "
						+ DB.preparedArgsBuilder(this.numbersToSave.size(),
								"(?, ?)");
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				while (i < ((this.numbersToSave.size()) * 2 + 1)) {
					stmt.setInt(i++, this.getPkey());
					try {
						this.numbersToSave.get(i / 2 - 1).save();
					} catch (SQLException e) {
						ErrorLogger.error(e, "Saving a phone number failed.",
								true, true);
						throw e;
					}
					stmt.setInt(i++, this.numbersToSave.get(i / 2 - 1)
							.getPkey());
				}
				stmt.executeUpdate();
				Session.updateTable(TableName.customer_phone, null);
				numbersToSave.clear();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Could not save phone numbers.", true,
						true);
				throw e;
			}
		}
		
		// delete removed addresses
		if (this.addressesToRemove.size() > 0) {
			String sql = "DELETE FROM customer_address WHERE address IN ("
					+ DB.preparedArgsBuilder(this.addressesToRemove.size(), "?")
					+ ") AND customer=?";
			try {
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				for (; i < (this.addressesToRemove.size() + 1); ++i) {
					stmt.setInt(i, this.addressesToRemove.get(i - 1).getPkey());
				}
				stmt.setInt(i, this.getPkey());
				stmt.executeUpdate();
				addressesToRemove.clear();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Could not remove addresses.", true, true);
				throw e;
			}
		}
		
		// save any new addresses
		if (this.addressesToSave.size() > 0) {
			try {
				String sql = "INSERT INTO customer_address (customer, address) VALUES "
						+ DB.preparedArgsBuilder(this.addressesToSave.size(),
								"(?, ?)");
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				while (i < ((this.addressesToSave.size()) * 2 + 1)) {
					stmt.setInt(i++, this.getPkey());
					try {
						this.addressesToSave.get(i / 2 - 1).save();
					} catch (SQLException e) {
						ErrorLogger.error(e, "Saving an address failed.", true,
								true);
						throw e;
					}
					stmt.setInt(i++, this.addressesToSave.get(i / 2 - 1)
							.getPkey());
				}
				stmt.executeUpdate();
				Session.updateTable(TableName.customer_address, null);
				addressesToSave.clear();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Could not save addresses.", true, true);
				throw e;
			}
		}
		
		// delete removed billing
		if (this.billingToRemove.size() > 0) {
			String sql = "DELETE FROM customer_billing WHERE billing IN ("
					+ DB.preparedArgsBuilder(this.billingToRemove.size(), "?")
					+ ") AND customer=?";
			try {
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				for (; i < (this.billingToRemove.size() + 1); ++i) {
					stmt.setInt(i, this.billingToRemove.get(i - 1).getPkey());
				}
				stmt.setInt(i, this.getPkey());
				stmt.executeUpdate();
				billingToRemove.clear();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Could not remove billing info.", true,
						true);
				throw e;
			}
		}
		
		// save any new addresses
		if (this.billingToSave.size() > 0) {
			try {
				String sql = "INSERT INTO customer_billing (customer, billing) VALUES "
						+ DB.preparedArgsBuilder(this.billingToSave.size(),
								"(?, ?)");
				PreparedStatement stmt = Session.getDB().prepareStatement(sql);
				int i = 1;
				while (i < ((this.billingToSave.size()) * 2 + 1)) {
					stmt.setInt(i++, this.getPkey());
					try {
						this.billingToSave.get(i / 2 - 1).save();
					} catch (SQLException e) {
						ErrorLogger.error(e, "Saving a billing record failed.",
								true, true);
						throw e;
					}
					stmt.setInt(i++, this.billingToSave.get(i / 2 - 1)
							.getPkey());
				}
				stmt.executeUpdate();
				Session.updateTable(TableName.customer_billing, null);
				billingToSave.clear();
			} catch (SQLException e) {
				ErrorLogger
						.error(e, "Could not save billing info.", true, true);
				throw e;
			}
		}
	}
	
	/**
	 * Remove an address from the customer record.
	 * 
	 * @pre getAddresses().contains(address) == true
	 * @post getAddresses().contains(address) == false
	 */
	public void removeAddress(Address address) {
		if (!this.addressesInitialized) {
			try {
				this.getAddresses();
			} catch (SQLException e) {
				ErrorLogger
						.error(e, "Failed to populate addresses", true, true);
				return;
			}
		}
		if (this.addresses.contains(address)) {
			this.addresses.remove(address);
			try { // check if address is stored, if not don't add it to the list
					// to be removed from db
				address.getPkey(); // throws uninit'd field
				this.addressesToRemove.add(address);
			} catch (UninitializedFieldException e) {}
		} else {
			throw new RecordSaveException(
					"Could not remove phone number: couldn't find phone number to remove.");
		}
	}
	
	/**
	 * Remove an address from the customer record.
	 * 
	 * @pre getAddresses().contains(address) == true
	 * @post getAddresses().contains(address) == false
	 */
	public void removeBilling(Billing billing) {
		if (!this.billingInitialized) {
			try {
				this.getBilling();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Failed to populate billing information.",
						true, true);
				return;
			}
		}
		if (this.billing.contains(billing)) {
			this.billing.remove(billing);
			try { // check if address is stored, if not don't add it to the list
					// to be removed from db
				billing.getPkey(); // throws uninit'd field
				this.billingToRemove.add(billing);
			} catch (UninitializedFieldException e) {}
		} else {
			throw new RecordSaveException(
					"Could not remove billing info: couldn't find billing info to remove.");
		}
	}
	
	/**
	 * Removes a phone number from the customer record.
	 * 
	 * @pre getPhoneNums().contains(phone) == true
	 * @post getPhoneNums().contains(phone) == false
	 */
	public void removePhoneNum(Phone phone) {
		if (this.numbers.contains(phone)) {
			this.numbers.remove(phone);
			try { // check if number is stored, if not don't add it to the list
					// to be removed from db
				phone.getPkey(); // throws uninit'd field
				this.numbersToRemove.add(phone);
			} catch (UninitializedFieldException e) {}
		} else {
			throw new RecordSaveException(
					"Could not remove phone number: couldn't find phone number to remove.");
		}
	}
	
	/**
	 * Removes a picture from the Customer record.
	 * 
	 * @pre getPictures().contains(picture) == true
	 * @pre getPictures().contains(picture) == false
	 */
	public void removePicture(Picture picture) {}
	
	/**
	 * Allows you to set the active status of the Customer. pre - none post -
	 * set active status = true
	 */
	public void setActive(boolean active) {
		this.setFieldValue("active", ((active) ? 1 : 0));
	}
	
	/**
	 * Sets the Customer's email address.
	 * 
	 * @pre - none
	 * @post - customer email set
	 */
	public void setEmail(String email) {
		this.setFieldValue("email", email);
	}
	
	/**
	 * Sets the Customer's note field.
	 * 
	 * @pre - none
	 * @post - customer note field set
	 */
	public void setNote(String note) {
		this.setFieldValue("note", note);
	}
	
	/**
	 * Sets the Customer's password.
	 * 
	 * @pre - none
	 * @post - customer password firel set
	 */
	public void setPassword(String password) {
		this.setFieldValue("password", password);
	}
}

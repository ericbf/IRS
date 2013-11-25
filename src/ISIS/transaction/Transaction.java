package ISIS.transaction;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.database.UninitializedFieldException;
import ISIS.gui.ErrorLogger;
import ISIS.item.Item;
import ISIS.misc.Address;
import ISIS.misc.Billing;
import ISIS.session.Session;

/**
 * A Transaction is the exchange of goods, legal tender, or rendering of
 * services between the client and a customer. A Transaction consists of a
 * customer, an intiating user, a set of items and information pertaining to the
 * items, billing information, various date/time information, and shipping
 * information. Invariants: The Transaction references exactly one customer
 * record.
 * 
 * @customer != null The Transaction references exactly one user record.
 * @user != null The Transaction references exactly one set of associated dates.
 * @dates != null The transaction has a primary key field that is always set.
 * @pkey > 0
 */
public class Transaction extends Record {
	/**
	 * TransactionStatus is a enumeration describing the status of a
	 * transaction.
	 */
	public static enum TransactionStatus {
		
		ACTIVE, CLOSED, BILLED, UNDER_REVIEW, ABORTED
	}
	
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName					tableName			= TableName.transaction_;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean					hasDates_			= true;
	
	Customer								customer			= null;
	Billing									billing				= null;
	Address									address				= null;
	Transaction								originalTransaction	= null;
	boolean									itemsInitialized	= false;
	
	private ArrayList<TransactionLineItem>	items				= new ArrayList<TransactionLineItem>();
	
	/**
	 * Public constructor. A Transaction starts with a user and a customer.
	 * These attributes cannot be changed.
	 */
	public Transaction(Customer customer) {
		super();
		
		this.setFieldValue("customer", customer.getPkey());
		this.setFieldValue("status", TransactionStatus.ACTIVE);
		this.setFieldValue("modified", 0);
		this.setStatus(TransactionStatus.ACTIVE);
		this.setFieldValue("type", "normal");
	}
	
	public Transaction(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Public constructor. Takes a transaction database key, and has the option
	 * to populate the fields from the database.
	 */
	public Transaction(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
		
		this.customer = this.getCustomer();
	}
	
	/**
	 * Public constructor. A return Transaction starts with a modifying user and
	 * a transaction. This attributes cannot be changed.
	 * 
	 * @pre originalSale.getStatus() == status.Finalized
	 * @post originalSale.getModified() == true
	 */
	public Transaction(Transaction originalSale) {
		super();
		
		this.originalTransaction = originalSale;
		this.setFieldValue("parent_transaction",
				this.originalTransaction.getPkey());
	}
	
	/**
	 * Adds an item to this transaction.
	 * 
	 * @pre getItems().contains(item) == false
	 * @post getItems().contains(item) == true
	 */
	public void addItem(Item item, BigDecimal price, BigDecimal adjustment,
			BigDecimal quantity, String note) {
		if (this.items.contains(item)) {
			throw new RuntimeException(
					"The transaction already contains this item.");
		}
		this.items.add(new TransactionLineItem(this, item, price, adjustment,
				quantity, note));
	}
	
	/**
	 * Completes this transaction. No finalized transaction can be modified.
	 * 
	 * @pre getStatus() != status.Finalized
	 * @pre getAddress() != null
	 * @pre getBilling() != null
	 * @pre getItems().size() > 0
	 * @post getStatus() == TransactionStatus.CLOSED
	 */
	public void finalizeTransaction() {
		this.setStatus(TransactionStatus.CLOSED);
	}
	
	/**
	 * Gets the address associated with this transaction.
	 */
	public Address getAddress() throws SQLException {
		if (this.address != null) {
			return this.address;
		}
		try {
			this.getPkey();
		} catch (UninitializedFieldException e) {
			return null; // the transaction has never been saved, so no address
							// has been set.
		}
		try {
			this.address = new Address((Integer) this.getFieldValue("address"),
					false);
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to retrieve address.", true, true);
			throw e;
		}
		return this.address;
	}
	
	/**
	 * Gets the billing information associated with this transaction.
	 */
	public Billing getBilling() throws SQLException {
		if (this.billing != null) {
			return this.billing;
		}
		try {
			this.getPkey();
		} catch (UninitializedFieldException e) {
			return null; // the transaction has never been saved, so no billing
							// has been set.
		}
		try {
			this.billing = new Billing((Integer) this.getFieldValue("billing"),
					false);
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to retrieve address.", true, true);
			throw e;
		}
		return this.billing;
	}
	
	/**
	 * Gets customer associated with this transaction.
	 */
	public Customer getCustomer() throws SQLException {
		return new Customer((Integer) this.getFieldValue("customer"), false);
	}
	
	/**
	 * Gets information associated with all involved items in this transaction.
	 */
	public ArrayList<TransactionLineItem> getItems() throws SQLException {
		if (!this.itemsInitialized) {
			try {
				this.getPkey();
			} catch (UninitializedFieldException e) {
				return this.items;
			}
			try {
				PreparedStatement stmt = Session.getDB().prepareStatement(
						"SELECT * FROM transaction_item WHERE transaction_=?");
				stmt.setInt(1, this.getPkey());
				for (HashMap<String, Field> row : DB.mapResultSet(stmt
						.executeQuery())) {
					this.items.add(new TransactionLineItem(row));
				}
			} catch (SQLException e) {
				ErrorLogger.error(e, "Failed to retrieve transaction's items.",
						true, true);
				throw e;
			}
			
		}
		return this.items;
	}
	
	/**
	 * Gets a list of returns that have been made on the transaction, or the
	 * transaction that this return is being made on as well as returns
	 * associated with it. If the transaction is not associated with a return,
	 * an empty list is returned.
	 */
	public Transaction getParentTransaction() throws SQLException {
		if (this.getFieldValue("parent_transaction") == null) {
			return null;
		}
		return new Transaction(
				(Integer) this.getFieldValue("parent_transaction"), false);
	}
	
	/**
	 * Gets status associated with this transaction.
	 */
	public TransactionStatus getStatus() {
		if (this.getFieldValue("status") == null) {
			return null;
		}
		return TransactionStatus.valueOf((String) this.getFieldValue("status"));
	}
	
	@Override
	protected TableName getTableName() {
		return Transaction.tableName;
	}
	
	@Override
	protected boolean hasDates() {
		return Transaction.hasDates_;
	}
	
	/**
	 * Checks whether any returns have been made that reference this
	 * transaction.
	 * 
	 * @pre getStatus() == status.Finalized
	 */
	public boolean isModified() {
		return ((Integer) this.getFieldValue("modified")) == 1;
	}
	
	/**
	 * Modifies attributes of an item associated with this transaction.
	 * 
	 * @pre getItems().contains(item) == true
	 */
	public void modItem(Item item, BigDecimal adjustment, BigDecimal quantity,
			String note) {
		TransactionLineItem itemToMod = this.items
				.get(this.items.indexOf(item));
		itemToMod.setAdjustment(adjustment);
		itemToMod.setQuantity(quantity);
		itemToMod.setDescription(note);
	}
	
	@Override
	protected void postSave() throws SQLException {
		// save any new LineItems.
		for (TransactionLineItem item : this.items) {
			item.save();
		}
	}
	
	/**
	 * Removes an item from this transaction.
	 * 
	 * @pre getItems().contains(item) == true
	 * @post getItems().contains(item) == false
	 */
	public void removeItem(Item item) {
		this.items.remove(item);
	}
	
	/**
	 * Sets the address for this transaction.
	 */
	public void setAddress(Address address) {
		this.setFieldValue("address", address.getPkey());
		this.address = address;
	}
	
	/**
	 * Sets the billing information for this transaction.
	 */
	public void setBilling(Billing billing) throws SQLException {
		this.setFieldValue("billing", billing.getPkey());
		this.billing = billing;
	}
	
	/**
	 * Sets the transaction's status.
	 */
	public void setStatus(TransactionStatus status) {
		this.setFieldValue("status", status.toString());
	}
}

package ISIS.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.item.Item;

/**
 * Attributes of an item as it relates to a transaction -- e.g. amount being
 * bought, a discount, or a note.
 */
public class TransactionLineItem extends Record {
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.transaction_item;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= true;
	
	/**
	 * Public constructor. Creates new instance of TransactionLineItem
	 * 
	 * @param map
	 */
	public TransactionLineItem(HashMap<String, Field> map) {
		super(map);
	}
	
	/**
	 * Public constructor. Take a transaction_item database key, and has the
	 * option to populate the fields from the database.
	 * 
	 * @param pkey
	 * @param populate
	 * @throws SQLException
	 */
	public TransactionLineItem(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Public constructor. Initializes all fields of the object.
	 * 
	 * @param transaction
	 * @param item
	 * @param price
	 * @param adjustment
	 * @param quantity
	 * @param description
	 */
	public TransactionLineItem(Transaction transaction, Item item,
			BigDecimal price, BigDecimal adjustment, BigDecimal quantity,
			String description) {
		super();
		this.setFieldValue("transaction_", transaction.getPkey());
		this.setFieldValue("item", item.getPkey());
		this.setFieldValue("price", price.toString());
		this.setFieldValue("adjustment", adjustment.toString());
		this.setFieldValue("quantity", quantity.toString());
		this.setFieldValue("description", description);
	}
	
	/**
	 * For checking if an item is already in the transaction.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TransactionLineItem) && !(obj instanceof Item)) {
			throw new RuntimeException("This will never happen.");
		}
		if (obj instanceof TransactionLineItem) {
			return super.equals(obj);
		}
		return ((this.getItem().getPkey()) == (((Item) obj).getPkey()));
	}
	
	/**
	 * Gets an adjustment associated with the transaction and item, e.g. a
	 * discount.
	 * 
	 * @return
	 */
	public BigDecimal getAdjustment() {
		return new BigDecimal((String) this.getFieldValue("adjustment"));
	}
	
	/**
	 * Gets the note associated with the transaction and this item.
	 * 
	 * @return
	 */
	public String getDescription() {
		return (String) this.getFieldValue("description");
	}
	
	/**
	 * Gets the item.
	 * 
	 * @return
	 */
	public Item getItem() {
		try {
			return new Item((Integer) this.getFieldValue("item"), true);
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to fetch item", true, true);
			return null;
		}
	}
	
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return new BigDecimal((String) this.getFieldValue("price"));
	}
	
	/**
	 * Gets the quantity of the item involved in the transaction.
	 * 
	 * @return
	 */
	public BigDecimal getQuantity() {
		return new BigDecimal((String) this.getFieldValue("quantity"));
	}
	
	@Override
	protected TableName getTableName() {
		return TransactionLineItem.tableName;
	}
	
	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return (Transaction) this.getFieldValue("transaction_");
	}
	
	@Override
	protected boolean hasDates() {
		return TransactionLineItem.hasDates_;
	}
	
	/**
	 * @param adjustment
	 *            the adjustment to set
	 */
	public void setAdjustment(BigDecimal adjustment) {
		this.setFieldValue("adjustment", adjustment.toString());
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.setFieldValue("description", description);
	}
	
	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.setFieldValue("quantity", quantity.toString());
	}
}

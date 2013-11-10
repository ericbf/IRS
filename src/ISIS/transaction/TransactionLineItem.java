package ISIS.transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.item.Item;

/**
 * Attributes of an item as it relates to a transaction -- e.g. amount being
 * bought, a discount, or a note.
 */
public class TransactionLineItem extends Record {
	/* Fields omitted */
	
	/**
	 * Public constructor. Initializes all fields of the object.
	 */
	public TransactionLineItem(Transaction transaction, Item item,
			BigDecimal price, BigDecimal adjustment, BigDecimal quantity,
			String description) {
		super("transaction_item", true);
		this.initializeFields(this.getFields());
		this.setFieldValue("transaction_", transaction);
		this.setFieldValue("item", item);
		this.setFieldValue("price", price);
		this.setFieldValue("adjustment", adjustment);
		this.setFieldValue("quantity", quantity);
		this.setFieldValue("description", description);
	}
	
	/**
	 * Public constructor. Take a transaction_item database key, and has the
	 * option to populate the fields from the database.
	 */
	public TransactionLineItem(int pkey, boolean populate) throws SQLException {
		super("transaction_item", true);
		this.initializeFields(this.getFields());
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	private HashMap<String, Field> getFields() {
		HashMap<String, Field> fields = new HashMap<>(6);
		fields.put("pkey", new Field(false));
		fields.put("transaction_", new Field(false));
		fields.put("item", new Field(false));
		fields.put("price", new Field(false));
		fields.put("adjustment", new Field(true));
		fields.put("quantity", new Field(true));
		fields.put("description", new Field(true));
		return fields;
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
	 * Gets the item.
	 */
	public Item getItem() {
		return (Item) this.getFieldValue("item");
	}
	
	/**
	 * Gets an adjustment associated with the transaction and item, e.g. a
	 * discount.
	 */
	public BigDecimal getAdjustment() {
		return (BigDecimal) this.getFieldValue("adjustment");
	}
	
	/**
	 * Gets the quantity of the item involved in the transaction.
	 */
	public BigDecimal getQuantity() {
		return (BigDecimal) this.getFieldValue("quantity");
	}
	
	/**
	 * Gets the note associated with the transaction and this item.
	 */
	public String getDescription() {
		return (String) this.getFieldValue("description");
	}
	
	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return (Transaction) this.getFieldValue("transaction_");
	}
	
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return (BigDecimal) this.getFieldValue("price");
	}
	
	/**
	 * @param adjustment
	 *            the adjustment to set
	 */
	public void setAdjustment(BigDecimal adjustment) {
		this.setFieldValue("adjustment", adjustment);
	}
	
	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.setFieldValue("quantity", quantity);
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.setFieldValue("description", description);
	}
}

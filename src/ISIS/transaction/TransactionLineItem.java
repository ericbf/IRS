package ISIS.transaction;

import java.math.BigDecimal;

import ISIS.item.Item;

/**
 * Attributes of an item as it relates to a transaction -- e.g. amount being
 * bought, a discount, or a note.
 */
public class TransactionLineItem {
	/* Fields omitted */
	
	/**
	 * Public constructor. Initializes all fields of the object.
	 */
	public TransactionLineItem(Item item, BigDecimal adjustment,
			BigDecimal quantity, String note) {}
	
	/**
	 * Gets the item.
	 */
	public Item getItem() {
		return null;
	}
	
	/**
	 * Gets an adjustment associated with the transaction and item, e.g. a
	 * discount.
	 */
	public BigDecimal getAdjustment() {
		return null;
	}
	
	/**
	 * Gets the quantity of the item involved in the transaction.
	 */
	public BigDecimal getQuantity() {
		return null;
	}
	
	/**
	 * Gets the note associated with the transaction and this item.
	 */
	public String getnote() {
		return null;
	}
}

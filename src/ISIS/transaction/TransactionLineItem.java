package ISIS.transaction;

import ISIS.item.Item;
import java.math.BigDecimal;

/**
 * Attributes of an item as it relates to a transaction -- e.g. amount being
 * bought, a discount, or a note.
 */
public class TransactionLineItem {
	/* Fields omitted */

	/**
	 * Public constructor. Initializes all fields of the object.
	 */
	public TransactionLineItem(Item item, BigDecimal adjustment, BigDecimal quantity, String note) {
	}

	/**
	 * Gets the item.
	 */
	public Item getItem() {
	}

	/**
	 * Gets an adjustment associated with the transaction and item, e.g. a
	 * discount.
	 */
	public BigDecimal getAdjustment() {
	}

	/**
	 * Gets the quantity of the item involved in the transaction.
	 */
	public BigDecimal getQuantity() {
	}

	/**
	 * Gets the note associated with the transaction and this item.
	 */
	public String getnote() {
	}
}

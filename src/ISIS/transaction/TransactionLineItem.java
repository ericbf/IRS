package ISIS.transaction;

import ISIS.database.Record;
import ISIS.item.Item;

import java.math.BigDecimal;

/**
 * Attributes of an item as it relates to a transaction -- e.g. amount being
 * bought, a discount, or a note.
 */
public class TransactionLineItem extends Record {
    /* Fields omitted */

    /**
     * Public constructor. Initializes all fields of the object.
     */
    public TransactionLineItem(Item item, BigDecimal adjustment, BigDecimal quantity, String note) {
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
        return (((int) this.getItem().getPkey()) == ((int) ((Item) obj).getPkey()))
    }

    /**
     * Gets the item.
     */
    public Item getItem() {
        return this.getF
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

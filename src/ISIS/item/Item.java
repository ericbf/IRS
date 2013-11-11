package ISIS.item;

import java.math.BigDecimal;
import java.util.ArrayList;

import ISIS.database.Record;
import ISIS.misc.Picture;
import java.sql.SQLException;

/**
 * A good available and currently managed by the client. An item record consists
 * of a name, a SKU, a price, an on hand quantity, a unit of measure, a quantity
 * at which it should be reordered, a manufacturer cost, and pictures.
 * Invariants: The Transaction references exactly one customer record.
 * 
 * @customer != null The Transaction references exactly one user record.
 * @user != null The Transaction references exactly one set of associated dates.
 * @dates != null The transaction has a primary key field that is always set.
 * @pkey > 0
 */
public class Item extends Record {
	/* Fields omitted */
	
	/**
	 * Public constructor. Takes an Item database key, and has the option to
	 * populate the fields from the database.
	 */
	public Item(int pkey, boolean populate) throws SQLException {
		super("item", true);
                
                this.setPkey(pkey);
                if (populate) {
                    this.fetch();
                }
	}
	
	/**
	 * Public constructor. An item starts with all fields populated.
	 */
	public Item(String name, String SKU, String description, BigDecimal price,
			BigDecimal onHandQty, BigDecimal ReorderQty, String UOM,
			BigDecimal cost) {
		super("item", true);
                
                this.setFieldValue("name", name);
                this.setFieldValue("SKU", SKU);
                this.setFieldValue("description", description);
                this.setFieldValue("price", price);
                this.setFieldValue("onHandQty", onHandQty);
                this.setFieldValue("ReorderQty", ReorderQty);
                this.setFieldValue("Uom", UOM);
                this.setFieldValue("cost", cost);
	}
	
	/**
	 * Adds a picture to the item.
	 */
	public void addPicture(Picture picture) {}
	
	/**
	 * Gets the cost of the item.
	 */
	public BigDecimal getCost() {
		return null;
	}
	
	/**
	 * Gets the item's description.
	 */
	public String getDescription() {
		return null;
	}
	
	/**
	 * Gets the item's name.
	 */
	public String getName() {
		return null;
	}
	
	/**
	 * Gets the on hand quantity of the item.
	 */
	public BigDecimal getOnHandQty() {
		return null;
	}
	
	/**
	 * Gets the pictures associated with the item.
	 */
	public ArrayList<Picture> getPictures() {
		return null;
	}
	
	/**
	 * Returns a list of previous versions of the item, or an empty list if
	 * there are none.
	 */
	public ArrayList<Item> getPreviousVersions() {
		return null;
	}
	
	/**
	 * Gets the item's price.
	 */
	public BigDecimal getPrice() {
		return null;
	}
	
	/**
	 * Gets the reorder quantity of the item.
	 */
	public BigDecimal getReorderQuantity() {
		return null;
	}
	
	/**
	 * Gets the item's SKU.
	 */
	public int getSKU() {
		return 0;
	}
	
	/**
	 * Sets the unit of measure of the item (e.g. pounds).
	 */
	public String getUOM() {
		return null;
	}
	
	/**
	 * Checks if this record is the latest version of the associated item.
	 */
	public boolean isLatestVersion() {
		return false;
	}
	
	/**
	 * Sets the cost of the item.
	 */
	public void setCost(BigDecimal cost) {}
	
	/**
	 * Changes the item's description.
	 */
	public void setDescription(String description) {}
	
	/**
	 * Changes the item's name.
	 */
	public void setName(String name) {}
	
	/**
	 * Sets the on hand quantity of the item.
	 */
	public void setOnHandQty(BigDecimal quantity) {}
	
	/**
	 * Sets the item's price.
	 */
	public void setPrice(BigDecimal price) {}
	
	/**
	 * Sets the on hand quantity at which the item should be reordered.
	 */
	public void setReorderQty(BigDecimal quantity) {}
}

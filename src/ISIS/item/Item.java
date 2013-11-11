package ISIS.item;

import ISIS.database.Field;
import ISIS.database.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

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
    public static String tableName =  "";
    public static boolean hasDates_ = true;

	/**
	 * Constructor to pass a pre-populated HashMap of the fields.
	 * 
	 * @param map
	 */
	public Item(HashMap<String, Field> map) {
		super();
		this.initializeFields(map);
	}

    /**
	 * Public constructor. Takes an Item database key, and has the option to
	 * populate the fields from the database.
	 */
	public Item(int pkey, boolean populate) {
		super();
	}

	/**
	 * Public constructor. An item starts with all fields populated.
	 */
	public Item(String name, String SKU, String description, BigDecimal price,
			BigDecimal onHandQty, BigDecimal ReorderQty, String UOM,
			BigDecimal cost) {
		super();
	}

    @Override
    protected String getTableName() {
        return Item.tableName;
    }

    @Override
    protected boolean hasDates() {
        return Item.hasDates_;
    }

	// /**
	// * Adds a picture to the item.
	// */
	// public void addPicture(Picture picture) {
	//
	// } TODO: implement this?
	
	/**
	 * Gets the cost of the item.
	 */
	public BigDecimal getCost() {
		return (BigDecimal) this.getFieldValue("cost");
	}
	
	/**
	 * Gets the item's description.
	 */
	public String getDescription() {
		return (String) this.getFieldValue("description");
	}
	
	/**
	 * Gets the item's name.
	 */
	public String getName() {
		return (String) this.getFieldValue("name");
	}
	
	/**
	 * Gets the on hand quantity of the item.
	 */
	public BigDecimal getOnHandQty() {
		return (BigDecimal) this.getFieldValue("onhand_qty");
	}
	
	// /**
	// * Gets the pictures associated with the item.
	// */
	// public ArrayList<Picture> getPictures() {
	// return (ArrayList<Picture>) this.getFieldValue("reorder_qty");
	// } TODO: implement this?
	
	/**
	 * Returns a list of previous versions of the item, or an empty list if
	 * there are none.
	 */
	public ArrayList<Item> getPreviousVersions() {
		// TODO: get previous versions
		return null;
	}
	
	/**
	 * Gets the item's price.
	 */
	public BigDecimal getPrice() {
		return (BigDecimal) this.getFieldValue("price");
	}
	
	/**
	 * Gets the reorder quantity of the item.
	 */
	public BigDecimal getReorderQuantity() {
		return (BigDecimal) this.getFieldValue("reorder_qty");
	}
	
	/**
	 * Gets the item's SKU.
	 */
	public int getSKU() {
		return (int) this.getFieldValue("uom");
	}
	
	/**
	 * Sets the unit of measure of the item (e.g. pounds).
	 */
	public String getUOM() {
		return (String) this.getFieldValue("uom");
	}
	
	/**
	 * Checks if this record is the latest version of the associated item.
	 */
	public boolean isLatestVersion() {
		return (boolean) this.getFieldValue("latest");
	}
	
	/**
	 * Sets the cost of the item.
	 */
	public void setCost(BigDecimal cost) {
		this.setFieldValue("cost", cost);
	}
	
	/**
	 * Changes the item's description.
	 */
	public void setDescription(String description) {
		this.setFieldValue("description", description);
	}
	
	/**
	 * Changes the item's name.
	 */
	public void setName(String name) {
		this.setFieldValue("name", name);
	}
	
	/**
	 * Sets the on hand quantity of the item.
	 */
	public void setOnHandQty(BigDecimal onHandQty) {
		this.setFieldValue("onhand_qty", onHandQty);
	}
	
	/**
	 * Sets the item's price.
	 */
	public void setPrice(BigDecimal price) {
		this.setFieldValue("price", price);
	}
	
	/**
	 * Sets the on hand quantity at which the item should be reordered.
	 */
	public void setReorderQty(BigDecimal reOrderQty) {
		this.setFieldValue("reorder_qty", reOrderQty);
	}
}

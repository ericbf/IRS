package ISIS.item;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;

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
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.item;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= true;
	
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
	 * 
	 * @param pkey
	 * @param populate
	 * @throws SQLException
	 */
	public Item(int pkey, boolean populate) throws SQLException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Public constructor. An item starts with all fields populated.
	 * 
	 * @param name
	 * @param SKU
	 * @param description
	 * @param price
	 * @param onHandQty
	 * @param ReorderQty
	 * @param UOM
	 * @param cost
	 * @param active
	 */
	public Item(String name, String SKU, String description, BigDecimal price,
			BigDecimal onHandQty, BigDecimal ReorderQty, String UOM,
			BigDecimal cost, boolean active) {
		super();
		this.setName(name);
		this.setFieldValue("SKU", SKU);
		this.setDescription(description);
		this.setPrice(price);
		this.setOnHandQty(onHandQty);
		this.setReorderQty(ReorderQty);
		this.setFieldValue("uom", UOM);
		this.setCost(cost);
		this.setActive(active);
	}
	
	/**
	 * Gets the cost of the item.
	 * 
	 * @return
	 */
	public BigDecimal getCost() {
		return new BigDecimal((String) this.getFieldValue("cost"));
	}
	
	/**
	 * Gets the item's description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return (String) this.getFieldValue("description");
	}
	
	/**
	 * Gets the item's name.
	 * 
	 * @return
	 */
	public String getName() {
		return (String) this.getFieldValue("name");
	}
	
	/**
	 * Gets the on hand quantity of the item.
	 * 
	 * @return
	 */
	public BigDecimal getOnHandQty() {
		return new BigDecimal((String) this.getFieldValue("onhand_qty"));
	}
	
	/**
	 * Gets the item's price.
	 * 
	 * @return
	 */
	public BigDecimal getPrice() {
		return new BigDecimal((String) this.getFieldValue("price"));
	}
	
	/**
	 * Gets the reorder quantity of the item.
	 * 
	 * @return
	 */
	public BigDecimal getReorderQuantity() {
		return new BigDecimal((String) this.getFieldValue("reorder_qty"));
	}
	
	/**
	 * Gets the item's SKU.
	 * 
	 * @return
	 */
	public String getSKU() {
		return (String) this.getFieldValue("SKU");
	}
	
	@Override
	protected TableName getTableName() {
		return Item.tableName;
	}
	
	/**
	 * Sets the unit of measure of the item (e.g. pounds).
	 * 
	 * @return
	 */
	public String getUOM() {
		return (String) this.getFieldValue("uom");
	}
	
	@Override
	protected boolean hasDates() {
		return Item.hasDates_;
	}
	
	/**
	 * Set whether this is the active record
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.setFieldValue("active", (active ? 1 : 0));
	}
	
	/**
	 * Sets the cost of the item.
	 * 
	 * @param cost
	 */
	public void setCost(BigDecimal cost) {
		this.setFieldValue("cost", cost.toString());
	}
	
	/**
	 * Changes the item's description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.setFieldValue("description", description);
	}
	
	/**
	 * Changes the item's name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.setFieldValue("name", name);
	}
	
	/**
	 * Sets the on hand quantity of the item.
	 * 
	 * @param onHandQty
	 */
	public void setOnHandQty(BigDecimal onHandQty) {
		this.setFieldValue("onhand_qty", onHandQty.toString());
	}
	
	/**
	 * Sets the item's price.
	 * 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		this.setFieldValue("price", price.toString());
	}
	
	/**
	 * Sets the on hand quantity at which the item should be reordered.
	 * 
	 * @param reOrderQty
	 */
	public void setReorderQty(BigDecimal reOrderQty) {
		this.setFieldValue("reorder_qty", reOrderQty.toString());
	}
}

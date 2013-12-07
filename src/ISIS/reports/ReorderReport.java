/**
 * 
 */
package ISIS.reports;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.gui.ErrorLogger;
import ISIS.html.objects.Cell;
import ISIS.html.objects.Table;
import ISIS.item.Item;
import ISIS.session.Session;

/**
 * @author eric
 */
public class ReorderReport extends Report {
	ArrayList<Item>	items;
	
	/**
	 * @param title
	 */
	public ReorderReport() {
		super("Reorder Report");
		
		this.items = new ArrayList<>();
		
		try {
			PreparedStatement st = Session
					.getDB()
					.prepareStatement(
							"SELECT * FROM item WHERE onhand_qty<reorder_qty AND active=1;");
			for (HashMap<String, Field> record : DB.mapResultSet(st
					.executeQuery())) {
				this.items.add(new Item(record));
			}
		} catch (SQLException e) {
			ErrorLogger.error("Failed to fetch inventory info", true, true);
		}
		
		this.populateBuilder();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.reports.Report#populateBuilder()
	 */
	@Override
	public void populateBuilder() {
		Table t = new Table(3, 0);
		Cell[] cells;
		int x;
		
		cells = t.addRow();
		t.setHeaderRow(0);
		cells[x = 0].add("Item Name");
		cells[++x].add("On-hand Quantity");
		cells[++x].add("Reorder Quantity");
		
		for (Item item : this.items) {
			cells = t.addRow();
			cells[x = 0].add(item.getName());
			cells[++x].add(item.getOnHandQty().toString());
			cells[++x].add(item.getReorderQuantity().toString());
		}
		
		this.b.add(t);
	}
	
}

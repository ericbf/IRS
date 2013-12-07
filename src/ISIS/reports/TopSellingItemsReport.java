/**
 * 
 */
package ISIS.reports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.gui.ErrorLogger;
import ISIS.html.objects.Cell;
import ISIS.html.objects.Table;
import ISIS.item.Item;
import ISIS.session.Session;

/**
 * @author eric
 */
public class TopSellingItemsReport extends Report {
	ArrayList<Item>			items;
	HashMap<Item, Float>	grosses;
	
	/**
	 * @param title
	 */
	public TopSellingItemsReport() {
		super("Reorder Report");
		
		this.items = new ArrayList<>();
		this.grosses = new HashMap<>();
		
		try {
			PreparedStatement st = Session
					.getDB()
					.prepareStatement(
							"SELECT item, sum(cast(price AS real)*quantity) AS gross FROM transaction_item GROUP BY item ORDER BY gross;");
			ResultSet x = st.executeQuery();
			
			while (x.next()) {
				Item item = new Item(x.getInt("item"), true);
				this.grosses.put(item, x.getFloat("gross"));
				this.items.add(item);
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
		cells[++x].add("Price");
		cells[++x].add("Gross");
		
		for (Item item : this.items) {
			cells = t.addRow();
			cells[x = 0].add(item.getName());
			cells[++x].add(item.getPrice().toString());
			cells[++x]
					.add(String.format("%.2f", (float) this.grosses.get(item)));
		}
		
		this.b.add(t);
	}
	
}

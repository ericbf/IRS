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
public class TopSellingReport extends Report {
	ArrayList<Item>			items;
	HashMap<Item, Float>	counts;
	HashMap<Item, Integer>	transactions;
	
	/**
	 * @param title
	 */
	public TopSellingReport() {
		super("Top Selling Report");
		
		this.items = new ArrayList<>();
		this.counts = new HashMap<>();
		this.transactions = new HashMap<>();
		
		try {
			PreparedStatement st = Session
					.getDB()
					.prepareStatement(
							"SELECT item, sum(cast(quantity AS real)) AS sold_cnt, count(item) AS order_cnt FROM transaction_item GROUP BY item ORDER BY sold_cnt DESC;");
			ResultSet x = st.executeQuery();
			int num = 0;
			while (x.next() && num++ < 10) {
				Item item = new Item(x.getInt("item"), true);
				this.counts.put(item, x.getFloat("sold_cnt"));
				this.transactions.put(item, x.getInt("order_cnt"));
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
		Table t = new Table(4, 0);
		Cell[] cells;
		int x;
		
		cells = t.addRow();
		t.setHeaderRow(0);
		cells[x = 0].add("Item Name");
		cells[++x].add("Price");
		cells[++x].add("Sold Count");
		cells[++x].add("Order Count");
		
		for (Item item : this.items) {
			cells = t.addRow();
			cells[x = 0].add(item.getName());
			cells[++x].add(String
					.format("$%,.2f", item.getPrice().floatValue()));
			cells[++x].add(String.format("%,.2f %s",
					(float) this.counts.get(item), item.getUOM()));
			cells[++x].add(String.format("%,d", this.transactions.get(item)));
		}
		
		this.b.add(t);
	}
	
}

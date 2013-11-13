package ISIS.gui.item;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.IRSTableModel;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * List of items. Allows you to query and act on items.
 */
public class ListItems extends ListView<Item> {
	private static final long	serialVersionUID	= 1L;
	private JButton				editButton;
	
	/**
	 * Constructs new Customer list view.
	 */
	public ListItems(SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int buttonNameSel = 0;
		JButton addButton = new JButton(this.buttonNames[buttonNameSel++]);
		this.editButton = new JButton(this.buttonNames[buttonNameSel++]);
		// JButton activeButton = new
		// JButton(this.buttonNames[buttonNameSel++]);
		// TODO: toggle button
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ListCustomers.this.splitPane.push(new AddEditCustomer(
				// ListCustomers.this.splitPane),
				// SplitPane.LayoutType.HORIZONTAL);
				ListItems.this.splitPane.push(new AddEditItem(
						ListItems.this.splitPane),
						SplitPane.LayoutType.HORIZONTAL, ListItems.this);
			}
		});
		
		this.editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected = ListItems.this.table.getSelectedRow();
				
				if (selected == -1) {
					selected = ListItems.this.selected;
					if (selected == -1) return;
					ListItems.this.table.setRowSelectionInterval(selected,
							selected);
				}
				
				int pkey = ListItems.this.keys.get(selected);
				
				try {
					// ListItems.this.splitPane.push(new AddEditItem(
					// ListItems.this.splitPane, pkey),
					// SplitPane.LayoutType.HORIZONTAL);
					ListItems.this.splitPane.push(new AddEditItem(
							ListItems.this.splitPane, pkey),
							SplitPane.LayoutType.HORIZONTAL, ListItems.this);
					
				} catch (SQLException ex) {
					ErrorLogger.error(ex, "Failed to open the item record.",
							true, true);
				}
			}
		});
		
		int x = 0, y = 0;
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.gridy = y;
		this.add(addButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		this.add(this.editButton, c);
		
		// c = new GridBagConstraints();
		// c.fill = GridBagConstraints.BOTH;
		// c.gridx = x++;
		// this.add(activeButton, c);
		// TODO: toggle button
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.weightx = 1;
		this.add(this.searchField, c);
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Item item = (Item) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = item.getSKU();
				array[i++] = item.getName();
				array[i++] = item.getPrice();
				array[i++] = item.getOnHandQty();
				array[i++] = item.getUOM();
				
				super.addRow(array);
				ListItems.this.keys.add(item.getPkey());
			}
		});
		this.tableModel.setColumnTitles("SKU", "name", "price", "qty", "UOM");
		this.fillTable();
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		this.add(new JScrollPane(this.table), c);
	}
	
	@Override
	protected void actionHandlerActionForSearchField() {
		this.editButton.doClick();
	}
	
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.item;
	}
	
	@Override
	protected ArrayList<Item> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<Item> items = new ArrayList<Item>(results.size());
		for (HashMap<String, Field> result : results) {
			items.add(new Item(result));
		}
		return items;
	}
}

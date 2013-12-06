package ISIS.gui.simplelists;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.IRSTableModel;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.gui.address.AddEditAddress;
import ISIS.misc.Address;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListAddress extends SimpleListView<Address> {
	private static final long	serialVersionUID	= 1L;
	private final Customer		customer;
	private JButton				viewButton, selectButton;
	
	/**
	 * Lists all addresses associated with the customer record.
	 * 
	 * @param splitPane
	 * @param pusher
	 * @param customer
	 * @param key
	 * @param selectMode
	 * @pre - select mode == true.
	 * @post - returns and lists all info concerned with listing an address into
	 *       the table view for use.
	 */
	public ListAddress(SplitPane splitPane, View pusher, Customer customer,
			Integer key, boolean selectMode) {
		super(splitPane, pusher, false, "SELECT a.* FROM address AS a left "
				+ "join customer_address AS ca ON a.pkey=ca.address WHERE "
				+ "ca.customer=?", key);
		
		this.customer = customer;
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Address address = (Address) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = address.getTitle();
				array[i++] = address.getStreetAddress(); // TODO: flesh this out
				
				super.addRow(array);
				ListAddress.this.keys.add(address.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Title", "Address");
		int x = 0;
		int y = 0;
		GridBagConstraints c = new GridBagConstraints();
		
		if (selectMode) { // we're selecting an address.
			this.selectButton = new JButton("Select");
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(this.selectButton, c);
		} else { // we're adding/removing addresses.
			JButton addButton = new JButton("Add");
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ListAddress.this.splitPane.push(new AddEditAddress(
							ListAddress.this.splitPane,
							ListAddress.this.customer),
							SplitPane.LayoutType.HORIZONTAL,
							ListAddress.this.pusher);
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(addButton, c);
			JButton deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int selected = ListAddress.this.table.getSelectedRow();
					if (selected == -1) {
						return;
					}
					
					int pkey = ListAddress.this.keys.get(selected);
					try {
						ListAddress.this.customer.removeAddress(new Address(
								pkey, true));
						ListAddress.this.customer.save();
					} catch (SQLException ex) {
						ErrorLogger.error(ex,
								"Failed to delete address record.", true, true);
					}
					ListAddress.this.fillTable();
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(deleteButton, c);
			this.viewButton = new JButton("View");
			this.viewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int selected = ListAddress.this.table.getSelectedRow();
					if (selected == -1) {
						return;
					}
					
					int pkey = ListAddress.this.keys.get(selected);
					try {
						ListAddress.this.splitPane.push(new AddEditAddress(
								ListAddress.this.splitPane,
								ListAddress.this.customer, pkey),
								SplitPane.LayoutType.HORIZONTAL,
								ListAddress.this.pusher);
						ListAddress.this.customer.save();
					} catch (SQLException ex) {
						ErrorLogger.error(ex,
								"Failed to delete address record.", true, true);
					}
					ListAddress.this.fillTable();
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(this.viewButton, c);
		}
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		c.weightx = 1;
		this.add(new JScrollPane(this.table), c);
		
		this.fillTable();
	}
	
	/**
	 * Public constructor.
	 * 
	 * @return
	 * @pre - selected == -1
	 * @post - returns -1 or this.keys.get(selected)
	 */
	public int getSelectedPkey() {
		int selected = this.table.getSelectedRow();
		if (selected == -1) {
			return -1;
		}
		return this.keys.get(selected);
	}
	
	/**
	 * @pre - none
	 * @post - returns DB.TableName.customer_address
	 */
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.customer_address;
	}
	
	/**
	 * @pre - Results from a DB query are given.
	 * @post - Puts the results into a collection the table view can use.
	 */
	@Override
	protected ArrayList<Address> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<Address> addresses = new ArrayList<Address>(results.size());
		for (HashMap<String, Field> result : results) {
			addresses.add(new Address(result));
		}
		return addresses;
	}
	
	/**
	 * @param listener
	 * @pre - none
	 * @post - Button is clicked.
	 */
	public void setSelectAction(ActionListener listener) {
		this.selectButton.addActionListener(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableItemAction()
	 */
	@Override
	protected void tableItemAction() {
		if (this.selectButton == null) {
			this.viewButton.doClick();
		}
	}
}

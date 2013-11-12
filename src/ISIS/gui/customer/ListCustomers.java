package ISIS.gui.customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.misc.Phone;

/**
 * List of customers. Allows you to query and act on customers.
 */
public class ListCustomers extends ListView<Customer> {
	private static final long	serialVersionUID	= 1L;
	private JButton				editButton;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Customer list view.
	 * 
	 * @throws SQLException
	 */
	public ListCustomers(SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int buttonNameSel = 0;
		JButton addButton = new JButton(this.buttonNames[buttonNameSel++]);
		this.editButton = new JButton(this.buttonNames[buttonNameSel++]);
		JButton activeButton = new JButton(this.buttonNames[buttonNameSel++]);
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ListCustomers.this.splitPane.push(new AddEditCustomer(
				// ListCustomers.this.splitPane),
				// SplitPane.LayoutType.HORIZONTAL);
				ListCustomers.this.splitPane.push(new AddEditCustomer(
						ListCustomers.this.splitPane),
						SplitPane.LayoutType.HORIZONTAL, ListCustomers.this);
			}
		});
		
		this.editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected = ListCustomers.this.table.getSelectedRow();
				
				if (selected == -1) {
					selected = ListCustomers.this.selected;
					if (selected == -1) return;
					ListCustomers.this.table.setRowSelectionInterval(selected,
							selected);
				}
				
				int pkey = ListCustomers.this.keys.get(selected);
				
				try {
					// ListCustomers.this.splitPane.push(new AddEditCustomer(
					// ListCustomers.this.splitPane, pkey),
					// SplitPane.LayoutType.HORIZONTAL);
					ListCustomers.this.splitPane
							.push(new AddEditCustomer(
									ListCustomers.this.splitPane, pkey),
									SplitPane.LayoutType.HORIZONTAL,
									ListCustomers.this);
				} catch (SQLException ex) {
					ErrorLogger.error(ex,
							"Failed to open the customer record.", true, true);
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
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		this.add(activeButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.weightx = 1;
		this.add(this.searchField, c);
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				try {
					Customer customer = (Customer) record;
					Object[] array = new Object[this.getColumnCount()];
					int col = 0;
					array[col++] = customer.getPkey();
					array[col++] = customer.getFirstName() + " "
							+ customer.getLastName();
					Phone num = customer.getPrimaryNum();
					if (num != null) {
						array[col] = num.getNumber();
					}
					col++;
					
					array[col++] = customer.getEmail();
					if (customer.getPrimaryAddress() != null) {
						array[col] = customer.getPrimaryAddress().getZIP();
					}
					col++;
					// don't add row until we successfully retrieve data.
					super.addRow(array);
					// don't add customer key to list until we know adding is a
					// success.
					ListCustomers.this.keys.add(customer.getPkey());
				} catch (SQLException e) {
					ErrorLogger
							.error(e, "Failed to display a row.", true, true);
				}
			}
		});
		
		this.tableModel.setColumnTitles("id", "name", "phone", "email", "zip");
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		this.add(new JScrollPane(this.table), c);
		
		this.fillTable();
	}
	
	@Override
	protected void actionHandlerActionForSearchField() {
		this.editButton.doClick();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableName()
	 */
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.customer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#mapResults(java.util.ArrayList)
	 */
	@Override
	protected ArrayList<Customer> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<Customer> customers = new ArrayList<>(results.size());
		for (HashMap<String, Field> result : results) {
			customers.add(new Customer(result));
		}
		return customers;
	}
	
}

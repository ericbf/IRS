package ISIS.gui.customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.gui.ErrorLogger;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.session.Session;

/**
 * List of customers. Allows you to query and act on customers.
 */
public class ListCustomers extends ListView<Customer> {
	private static final long	serialVersionUID	= 1L;
	
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
		
		JButton addButton = new JButton("Add"), editButton = new JButton("Edit"), activeButton = new JButton(
				"Toggle Active");
		
		int x = 0, y = 0;
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.gridy = y;
		this.add(addButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		this.add(editButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		this.add(activeButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.weightx = 1;
		this.add(this.searchField, c);
		
		TableColumn column = new TableColumn();
		column.setHeaderValue("ID");
		this.table.addColumn(column);
		column = new TableColumn();
		column.setHeaderValue("Name");
		this.table.addColumn(column);
		column = new TableColumn();
		column.setHeaderValue("Phone");
		this.table.addColumn(column);
		this.tableModel.setColumnTitles("id", "name", "phone");
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		this.add(new JScrollPane(this.table), c);
		
		try {
			this.fillTable();
		} catch (SQLException e) {
			ErrorLogger
					.error(e, "Error populating customer table.", true, true);
			e.printStackTrace();
		}
	}
	
	private void fillTable() throws SQLException {
		String searchFieldText = this.searchField.getText();
		String sqlQuery = "SELECT pkey,fname||lname AS name,'' AS phone FROM (SELECT docid FROM customer_search WHERE customer_search MATCH ?) LEFT JOIN customer ON docid = pkey";
		// TODO add phone
		PreparedStatement stmt = Session.getDB().prepareStatement(sqlQuery);
		stmt.setString(1, searchFieldText);
		ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
				.executeQuery());
		this.records = new ArrayList<Customer>();
		for (HashMap<String, Field> map : results)
			this.records.add(new Customer(map));
		this.populateTable();
	}
	
	private void populateTable() {
		for (Customer c : this.records)
			this.tableModel.addRow(c);
	}
}

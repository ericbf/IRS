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
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.session.Session;

/**
 * List of customers. Allows you to query and act on customers.
 */
public class ListCustomers extends ListView<Customer> {
<<<<<<< HEAD
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
	
	@Override
	protected void fillTable() throws SQLException {
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
		for (Customer c : this.records)
			this.tableModel.addRow(c);
	}
	
	// private void populateTable() {
	// for (Customer c : this.records)
	// this.tableModel.addRow(c);
	// }
=======

    private static final long serialVersionUID = 1L;

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

	this.setTableModel(new IRSTableModel() {
	    @Override
	    public void addRow(Record record) {
		Customer customer = (Customer) record;
		Object[] array = new Object[3];

		array[0] = customer.getPkey();
		array[1] = customer.getFirstName() + " " + customer.getLastName();
		array[2] = "Phone number";

		super.addRow(array);
	    }
	});

	this.tableModel.setColumnIdentifiers(new String[]{"id", "name", "phone"});
	this.tableModel.setColumnTitles("id", "name", "phone");

	c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.gridy = ++y;
	c.gridwidth = x;
	c.gridx = x = 0;
	c.weighty = 1;
	tableModel.setColumnCount(3);
	this.add(new JScrollPane(this.table), c);

	this.fillTable();
    }

    @Override
    protected void fillTable() {
	String searchFieldText = this.searchField.getText();
	PreparedStatement stmt = null;
	try {
	    if (searchFieldText.length() >= 1) {
		String search = searchFieldText + " ";
		search = search.replaceFirst("^\\s+", "");
		search = search.replaceAll("\\s+", "* ");
		String sqlQuery = "SELECT c.*, 'phone' AS phone FROM (SELECT docid FROM customer_search WHERE customer_search MATCH ?) "
			+ "LEFT JOIN customer AS c ON docid=c.pkey";
		stmt = Session.getDB().prepareStatement(sqlQuery);
		stmt.setString(1, search);
	    } else {
		String sqlQuery = "SELECT c.*, 'phone' AS phone FROM customer AS c";
		stmt = Session.getDB().prepareStatement(sqlQuery);
	    }
	    // TODO add phone
	    ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
		    .executeQuery());
	    this.records = new ArrayList<>();
	    for (HashMap<String, Field> map : results) {
		this.records.add(new Customer(map));
	    }
	    this.populateTable();
	} catch (SQLException e) {
	    ErrorLogger
		    .error(e, "Error populating customer table.", true, true);
	}
    }

    private void populateTable() {
	this.table.removeAll();
	this.tableModel.setRowCount(0);
	for (Customer c : this.records) {
	    this.tableModel.addRow(c);
	}
	this.tableModel.getDataVector();
    }
>>>>>>> e47a2962ff19f452b557d22f013fa2587089daa4
}

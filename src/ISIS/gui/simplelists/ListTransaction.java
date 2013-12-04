/**
 * 
 */
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
import ISIS.gui.SplitPane.LayoutType;
import ISIS.gui.View;
import ISIS.gui.report.ReportViewer;
import ISIS.gui.transaction.AddEditTransaction;
import ISIS.reports.Invoice;
import ISIS.transaction.Transaction;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListTransaction extends SimpleListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	Customer					customer;
	
	/**
	 * Lists all Transactions associated with the customer record.
	 * 
	 * @pre - none
	 * @post - returns and lists all info concerned with listing a transaction
	 *       into the table view for use.
	 */
	public ListTransaction(SplitPane splitPane, View pusher, Customer customer,
			boolean selectMode) {
		super(splitPane, pusher, false, "SELECT t.* FROM transaction_ AS t "
				+ "WHERE t.customer=?", customer.getPkey());
		
		this.customer = customer;
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Transaction transaction = (Transaction) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				try {
					array[i++] = transaction.getDates().getModDate();
				} catch (SQLException e) {
					ErrorLogger.error(e, "Failed to fetch transaction's date.",
							true, true);
				}
				array[i++] = transaction.getStatus();
				
				super.addRow(array);
				ListTransaction.this.keys.add(transaction.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Date", "Status");
		
		int x = 0;
		int y = 0;
		GridBagConstraints c = new GridBagConstraints();
		
		if (selectMode) { // we're selecting a transaction.
			@SuppressWarnings("unused")
			JButton select;
			// TODO: THIS
		} else { // we're adding/removing transactions.
			JButton addButton = new JButton("Add");
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ListTransaction.this.splitPane.push(new AddEditTransaction(
							ListTransaction.this.splitPane,
							ListTransaction.this.customer),
							SplitPane.LayoutType.HORIZONTAL,
							ListTransaction.this.pusher);
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(addButton, c);
			
			JButton invoiceButton = new JButton("Invoice");
			invoiceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (ListTransaction.this.selected != -1
							|| (ListTransaction.this.selected = ListTransaction.this.table
									.getSelectedRow()) != -1) {
						try {
							ListTransaction.this.splitPane.push(
									new ReportViewer(
											new Invoice(
													new Transaction(
															ListTransaction.this.keys
																	.get(ListTransaction.this.selected),
															true)),
											ListTransaction.this.splitPane),
									SplitPane.LayoutType.HORIZONTAL,
									ListTransaction.this.pusher);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(invoiceButton, c);
			
			JButton editButton = new JButton("Edit");
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (ListTransaction.this.selected != -1
							|| (ListTransaction.this.selected = ListTransaction.this.table
									.getSelectedRow()) != -1) {
						try {
							ListTransaction.this.splitPane.push(
									new AddEditTransaction(
											ListTransaction.this.splitPane,
											ListTransaction.this.keys
													.get(ListTransaction.this.selected)),
									SplitPane.LayoutType.HORIZONTAL,
									ListTransaction.this.pusher);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridy = ++y;
			c.gridwidth = x;
			c.gridx = x = 0;
			c.weightx = 1;
			this.add(editButton, c);
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
	
	/*
	 * @pre - none
	 * @post - returns DB.TableName.transaction_
	 */
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.transaction_;
	}
	
	/*
	 * @pre - Results from a DB query are given.
	 * @post - Puts the results into a collection the table view can use.
	 */
	@Override
	protected ArrayList<Transaction> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<Transaction> addresses = new ArrayList<Transaction>(
				results.size());
		for (HashMap<String, Field> result : results) {
			addresses.add(new Transaction(result));
		}
		return addresses;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableItemAction()
	 */
	@Override
	protected void tableItemAction() {
		try {
			this.splitPane.push(new AddEditTransaction(this.splitPane,
					this.keys.get(this.selected)), LayoutType.HORIZONTAL,
					this.pusher);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

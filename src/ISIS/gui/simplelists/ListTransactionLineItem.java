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

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.IRSTableModel;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.transaction.Transaction;
import ISIS.transaction.TransactionLineItem;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListTransactionLineItem extends
		SimpleListView<TransactionLineItem> {
	private static final long	serialVersionUID	= 1L;
	Transaction					transaction;
	JButton						remove;
	
	/**
	 * Lists all Transaction Line Items associated with the customer record.
	 * 
	 * @pre - none
	 * @post - returns and lists all info concerned with listing a transaction
	 *       line item into the table view for use.
	 */
	public ListTransactionLineItem(SplitPane splitPane, View pusher,
			Transaction transaction) {
		super(
				splitPane,
				pusher,
				false,
				"SELECT ti.* FROM transaction_item AS ti WHERE ti.transaction_=?",
				transaction.getPkey());
		
		this.transaction = transaction;
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				TransactionLineItem ti = (TransactionLineItem) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = ti.getItem().getName();
				array[i++] = ti.getPrice();
				array[i++] = ti.getQuantity();
				
				super.addRow(array);
				ListTransactionLineItem.this.keys.add(ti.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Item", "Price", "Qty");
		
		int x = 0;
		int y = 0;
		GridBagConstraints c;
		
		this.remove = new JButton("Remove");
		this.remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected = ListTransactionLineItem.this.table
						.getSelectedRow();
				if (selected == -1) {
					return;
				}
				
				int pkey = ListTransactionLineItem.this.keys.get(selected);
				try {
					ListTransactionLineItem.this.transaction
							.removeItem(new TransactionLineItem(pkey, true));
				} catch (SQLException ex) {
					ErrorLogger.error(ex,
							"Failed to remove item from transaction.", true,
							true);
				}
				try {
					ListTransactionLineItem.this.transaction.save();
				} catch (SQLException e1) {
					ErrorLogger.error(e1, "Failed to save transaction", true,
							true);
				}
			}
		});
		c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		this.add(this.remove, c);
		
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
	 * @pre - none
	 * @post - returns DB.TableName.transaction_item
	 */
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.transaction_item;
	}
	
	/**
	 * @pre - Results from a DB query are given.
	 * @post - Puts the results into a collection the table view can use.
	 */
	@Override
	protected ArrayList<TransactionLineItem> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<TransactionLineItem> ti = new ArrayList<TransactionLineItem>(
				results.size());
		for (HashMap<String, Field> result : results) {
			ti.add(new TransactionLineItem(result));
		}
		return ti;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableItemAction()
	 */
	@Override
	protected void tableItemAction() {}
}

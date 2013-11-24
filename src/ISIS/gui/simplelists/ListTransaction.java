/**
 * 
 */
package ISIS.gui.simplelists;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.*;
import ISIS.gui.customer.AddEditTransaction;
import ISIS.transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListTransaction extends SimpleListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
    Customer customer;
	
	public ListTransaction(SplitPane splitPane, View pusher, Customer customer,
			boolean selectMode) {
		super(splitPane, pusher, false, "SELECT t.* FROM transaction_ AS t "
				+ "LEFT JOIN customer AS c ON t.customer=c.pkey AND c.pkey=?",
				customer.getPkey());

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
                } catch(SQLException e) {
                    ErrorLogger.error(e, "Failed to fetch transaction's date.", true, true);
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
							ListTransaction.this.splitPane, ListTransaction.this.customer),
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
	
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.transaction_;
	}
	
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
}

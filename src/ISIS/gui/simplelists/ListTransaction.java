/**
 * 
 */
package ISIS.gui.simplelists;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.IRSTableModel;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.gui.customer.AddEditTransaction;
import ISIS.transaction.Transaction;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListTransaction extends SimpleListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	
	public ListTransaction(SplitPane splitPane, View pusher, Integer key,
			boolean selectMode) {
		super(splitPane, pusher, false, "SELECT t.* FROM transaction_ AS t "
				+ "LEFT JOIN customer AS c ON t.customer=c.pkey AND c.pkey=?",
				key);
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Transaction transaction = (Transaction) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = transaction.getDates().getModDate();
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
							ListTransaction.this.splitPane),
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

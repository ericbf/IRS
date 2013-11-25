/**
 *
 */
package ISIS.gui.simplelists;

import java.awt.GridBagConstraints;
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
import ISIS.transaction.Transaction;
import ISIS.transaction.TransactionLineItem;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListTransactionLineItem extends
		SimpleListView<TransactionLineItem> {
	private static final long	serialVersionUID	= 1L;
	Transaction					transaction;
	JButton						edit;
	
	public ListTransactionLineItem(SplitPane splitPane, View pusher,
			Transaction transaction) {
		super(
				splitPane,
				pusher,
				false,
				"SELECT ti.* FROM transaction_item AS ti "
						+ "LEFT JOIN transaction_ AS t ON ti.transaction_=t.pkey AND t.pkey=?",
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
				
				super.addRow(array);
				ListTransactionLineItem.this.keys.add(ti.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Name", "Price");
		
		int x = 0;
		int y = 0;
		GridBagConstraints c;
		
		JButton remove;
		this.edit = new JButton();
		// TODO: THIS
		
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
	protected void tableItemAction() {
		this.edit.doClick();
	}
}

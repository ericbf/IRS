/**
 *
 */
package ISIS.gui.simplelists;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.*;
import ISIS.transaction.Transaction;
import ISIS.transaction.TransactionLineItem;

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
public class ListTransactionLineItem extends
		SimpleListView<TransactionLineItem> {
	private static final long	serialVersionUID	= 1L;
	Transaction					transaction;
	JButton						remove;
	
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
                int selected = ListTransactionLineItem.this.table.getSelectedRow();
                if (selected == -1) {
                    return;
                }

                int pkey = ListTransactionLineItem.this.keys.get(selected);
                try {
                    ListTransactionLineItem.this.transaction.removeItem(new TransactionLineItem(pkey, false));
                } catch(SQLException ex) {
                    ErrorLogger.error(ex, "Failed to add item to transaction.", true, true);
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
	}
}

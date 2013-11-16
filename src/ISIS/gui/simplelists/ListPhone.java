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
import ISIS.misc.Phone;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListPhone extends SimpleListView<Phone> {
	private static final long	serialVersionUID	= 1L;
	
	public ListPhone(SplitPane splitPane, View pusher, Integer key,
			boolean selectMode) {
		super(splitPane, pusher, false, "SELECT * FROM phone AS p LEFT "
				+ "JOIN customer_phone AS cp ON p.pkey=cp.phone WHERE "
				+ "cp.customer=?", key);
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Phone transaction = (Phone) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = transaction.getType();
				array[i++] = transaction.getNumber();
				
				super.addRow(array);
				ListPhone.this.keys.add(transaction.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Type", "Number");
		
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
					ListPhone.this.splitPane.push(new AddEditTransaction(
							ListPhone.this.splitPane),
							SplitPane.LayoutType.HORIZONTAL,
							ListPhone.this.pusher);
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
		return DB.TableName.customer_address; // only customers should have an
												// address list
	}
	
	@Override
	protected ArrayList<Phone> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		ArrayList<Phone> addresses = new ArrayList<Phone>(results.size());
		for (HashMap<String, Field> result : results) {
			addresses.add(new Phone(result));
		}
		return addresses;
	}
}

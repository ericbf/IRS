/**
 * 
 */
package ISIS.gui.simplelists;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.*;
import ISIS.gui.customer.AddEditPhone;
import ISIS.misc.Phone;

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
public class ListPhone extends SimpleListView<Phone> {
	private static final long	serialVersionUID	= 1L;
    private final Customer customer;
	
	public ListPhone(SplitPane splitPane, View pusher, Customer customer) {
		super(splitPane, pusher, false, "SELECT p.* FROM phone AS p LEFT "
				+ "JOIN customer_phone AS cp ON p.pkey=cp.phone WHERE "
				+ "cp.customer=?", customer.getPkey());

        this.customer = customer;
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Phone phone = (Phone) record;
				Object[] array = new Object[this.getColumnCount()];
				int i = 0;
				
				array[i++] = phone.getType();
				array[i++] = phone.getNumber();
				
				super.addRow(array);
				ListPhone.this.keys.add(phone.getPkey());
			}
		});
		this.tableModel.setColumnTitles("Type", "Number");
		
		int x = 0;
		int y = 0;
		GridBagConstraints c = new GridBagConstraints();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListPhone.this.splitPane.push(new AddEditPhone(ListPhone.this.splitPane,
                        ListPhone.this.customer), SplitPane.LayoutType.HORIZONTAL, ListPhone.this.pusher);
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weightx = 1;
        this.add(addButton, c);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = ListPhone.this.table.getSelectedRow();
                if (selected == -1) {
                    return;
                }

                int pkey = ListPhone.this.keys.get(selected);
                try {
                    ListPhone.this.customer.getPhoneNums();
                    ListPhone.this.customer.removePhoneNum(new Phone(
                            pkey, true));
                    ListPhone.this.customer.save();
                } catch (SQLException ex) {
                    ErrorLogger.error(ex, "Failed to delete address record.", true, true);
                }
                ListPhone.this.fillTable();
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weightx = 1;
        this.add(deleteButton, c);

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
		return DB.TableName.customer_phone;
		// only customers should have a phone list
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
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableItemAction()
	 */
	@Override
	protected void tableItemAction() {
		// TODO Auto-generated method stub
		
	}
}

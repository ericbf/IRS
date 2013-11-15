package ISIS.gui.address;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.IRSTableModel;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.misc.Address;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This should NEVER be pushed, only embedded.
 */
public class ListAddress extends SimpleListView<Address> {
    public ListAddress(SplitPane splitPane, View pusher, Integer key, boolean selectMode) {
        super(splitPane, pusher, false, "SELECT a.* FROM address AS a left join customer_address AS ca ON a.pkey=ca.address WHERE ca" +
                ".customer=?", key);

        this.setTableModel(new IRSTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void addRow(Record record) {
                Address address = (Address) record;
                Object[] array = new Object[this.getColumnCount()];
                int i = 0;

                array[i++] = address.getTitle();
                array[i++] = address.getStreetAddress(); // TODO: flesh this out

                super.addRow(array);
                ListAddress.this.keys.add(address.getPkey());
            }
        });
        this.tableModel.setColumnTitles("Title", "Address");

        int x = 0;
        int y = 0;
        GridBagConstraints c = new GridBagConstraints();

        if (selectMode) { //we're selecting an address.
            JButton select;
            //TODO: THIS
        } else { //we're adding/removing addresses.
            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ListAddress.this.splitPane.push(new AddEditAddress(ListAddress.this.splitPane), SplitPane.LayoutType.HORIZONTAL,
                                                    ListAddress.this.pusher);
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
        return DB.TableName.customer_address; // only customers should have an address list
    }

    @Override
    protected ArrayList<Address> mapResults(ArrayList<HashMap<String, Field>> results) {
        ArrayList<Address> addresses = new ArrayList<Address>(results.size());
        for (HashMap<String, Field> result : results) {
            addresses.add(new Address(result));
        }
        return addresses;
    }
}

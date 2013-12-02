package ISIS.gui.simplelists;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.*;
import ISIS.gui.customer.AddEditBilling;
import ISIS.misc.Billing;

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
public class ListBilling extends SimpleListView<Billing> {
    private static final long	serialVersionUID	= 1L;
    private final Customer customer;
    private JButton viewButton, selectButton = null;

    public ListBilling(SplitPane splitPane, View pusher, Customer customer,
                       Integer key) {
        super(splitPane, pusher, false, "SELECT b.* FROM billing AS b left "
                + "join customer_billing AS cb ON b.pkey=cb.billing WHERE "
                + "cb.customer=?", key);

        this.customer = customer;
        this.setTableModel(new IRSTableModel() {
            private static final long	serialVersionUID	= 1L;

            @Override
            public void addRow(Record record) {
                Billing billing = (Billing) record;
                Object[] array = new Object[this.getColumnCount()];
                int i = 0;

                array[i++] = billing.getBillingType();
                try {
                    if(billing.getAddress() != null) {
                        array[i++] = billing.getAddress().getZIP();
                    } else {
                        array[i++] = "";
                    }
                } catch (SQLException e) {
                    ErrorLogger.error(e, "Failed to fetch address.", true, true);
                }
                array[i++] = billing.getCardNumber();

                super.addRow(array);
                ListBilling.this.keys.add(billing.getPkey());
            }
        });
        this.tableModel.setColumnTitles("Type", "ZIP", "Card#");
        int x = 0;
        int y = 0;
        GridBagConstraints c = new GridBagConstraints();

        this.selectButton = new JButton("Select");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weightx = 1;
        this.add(this.selectButton, c);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListBilling.this.splitPane.push(new AddEditBilling(
                        ListBilling.this.splitPane,
                        ListBilling.this.customer),
                                                SplitPane.LayoutType.HORIZONTAL,
                                                ListBilling.this.pusher);
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
                int selected = ListBilling.this.table.getSelectedRow();
                if (selected == -1) {
                    return;
                }

                int pkey = ListBilling.this.keys.get(selected);
                try {
                    ListBilling.this.customer.removeBilling(new Billing(pkey, true));
                    ListBilling.this.customer.save();
                } catch (SQLException ex) {
                    ErrorLogger.error(ex, "Failed to delete billing info record.", true, true);
                }
                ListBilling.this.fillTable();
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weightx = 1;
        this.add(deleteButton, c);
        this.viewButton = new JButton("View");
        this.viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = ListBilling.this.table.getSelectedRow();
                if (selected == -1) {
                    return;
                }

                int pkey = ListBilling.this.keys.get(selected);
                try {
                    ListBilling.this.splitPane.push(new AddEditBilling(
                            ListBilling.this.splitPane,
                            ListBilling.this.customer, pkey),
                                                    SplitPane.LayoutType.HORIZONTAL,
                                                    ListBilling.this.pusher);
                    ListBilling.this.customer.save();
                } catch (SQLException ex) {
                    ErrorLogger.error(ex,
                                      "Failed to delete address record.", true, true);
                }
                ListBilling.this.fillTable();
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weightx = 1;
        this.add(this.viewButton, c);

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
        return DB.TableName.billing;
    }

    @Override
    protected ArrayList<Billing> mapResults(
            ArrayList<HashMap<String, Field>> results) {
        ArrayList<Billing> billing = new ArrayList<Billing>(results.size());
        for (HashMap<String, Field> result : results) {
            billing.add(new Billing(result));
        }
        return billing;
    }

    /*
     * (non-Javadoc)
     * @see ISIS.gui.ListView#tableItemAction()
     */
    @Override
    protected void tableItemAction() {
        if(this.selectButton != null) {
            this.viewButton.doClick();
        }
    }

    public int getSelectedPkey() {
        int selected = this.table.getSelectedRow();
        if(selected == -1) {
            return -1;
        }
        return this.keys.get(selected);
    }

    public void setSelectAction(ActionListener listener) {
        this.selectButton.addActionListener(listener);
    }
}

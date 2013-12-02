package ISIS.gui.transaction;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;
import ISIS.transaction.TransactionLineItem;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddEditTransactionLineItem extends AddEditView {
    private static final long	serialVersionUID	= 1L;
    private final Item				item;
    private TransactionLineItem lineItem;
    private HintField itemName, price, adjustment, quantity, description;
    private final Customer		customer;

    /**
     * Public constructor: returns new instance of add/edit customer view.
     *
     * @wbp.parser.constructor
     */
    public AddEditTransactionLineItem(SplitPane splitPane, Customer customer, Item item)
            throws SQLException {
        super(splitPane);
        this.lineItem = null;
        this.item = item;
        this.customer = customer;
        this.populateElements();

        this.itemName.setText(item.getName());
        this.price.setText(item.getPrice().toString());
    }

    /**
     * Discards any modifications.
     */
    @Override
    public void cancel() {}

    /*
     * (non-Javadoc)
     * @see ISIS.gui.View#getCurrentRecord()
     */
    @Override
    public Record getCurrentRecord() {

        //TODO: this
        return null;
    }

    /*
     * (non-Javadoc)
     * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
     */
    @Override
    public boolean isAnyFieldDifferentFromDefault() {

        //TODO: this
        boolean same = true;
        return !same;
    }

    /**
     * Draws all necessary components on the window.
     */
    private void populateElements() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;
        int x = 0, y = 0;

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Item name"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.itemName = new HintField(), c);
        this.itemName.setEnabled(false);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Price"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.price = new HintField(), c);
        this.price.setEnabled(false);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Adjustment"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.quantity = new HintField("0.0"), c);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Quantity"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.quantity = new HintField("0.0"), c);
        
        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Description"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.description = new HintField("Description"), c);

    }

    @Override
    protected void postSave() throws SQLException {
        if (this.customer != null) {
            this.customer.save();
        }
    }

}

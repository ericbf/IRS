package ISIS.gui.customer;

import ISIS.customer.Customer;
import ISIS.gui.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * View for adding and editing customers.
 */
public class AddEditCustomer extends View {
    private static final long serialVersionUID = 1L;
    JCheckBox active;
    JTextField password, fname, lname, email;
    JTextArea note;
    JList transactions, addresses, phones;
    Customer customer;

    /**
     * Public constructor: returns new instance of add/edit customer view.
     */
    public AddEditCustomer(SplitPane splitPane) {
        super(splitPane);
        populateElements();
        this.customer = null;
    }

    /**
     * Public constructor: returns new instance of add/edit customer view.
     */
    public AddEditCustomer(SplitPane splitPane, int pkey) throws SQLException {
        super(splitPane);
        populateElements();
        this.customer = new Customer(pkey, true);

        this.active.setSelected(customer.isActive());
        this.password.setText(customer.getPassword());
        this.fname.setText(customer.getFirstName());
        this.lname.setText(customer.getLastName());
        this.email.setText(customer.getEmail());
        this.note.setText(customer.getNote());
        this.deactivateFields();
    }

    /**
     * Draws all necessary components on the window.
     */
    private void populateElements() {
        this.setLayout(new GridBagLayout());
        int x = 0, y = 0;
        GridBagConstraints c;

        // active
        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Active"), c);
        this.active = new JCheckBox("", true);
        c.weightx = 2;
        c.gridx++;
        this.add(this.active, c);

        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        this.add(new JLabel("Password"), c);
        this.password = new JTextField();
        c.weightx = 2;
        c.gridx++;
        this.add(this.password, c);

        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        this.add(new JLabel("First name"), c);
        this.fname = new JTextField();
        c.weightx = 2;
        c.gridx++;
        this.add(this.fname, c);
        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        this.add(new JLabel("Last name"), c);
        this.lname = new JTextField();
        c.weightx = 2;
        c.gridx++;
        this.add(this.lname, c);

        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        this.add(new JLabel("Email"), c);
        this.email = new JTextField();
        c.weightx = 2;
        c.gridx++;
        this.add(this.email, c);

        c.weightx = 0;
        c.gridx = x;
        c.gridy = y++;
        this.add(new JLabel("Note"), c);
        this.note = new JTextArea();
        c.weightx = 2;
        c.weighty = 2;
        c.gridx++;
        this.add(this.note, c);

        addLists(1, y - 1);
    }

    /**
     * add the lists to the right hand side of the view.
     *
     * @param currentx the current x position in the grid.
     * @param currenty the current y position in the grid.
     */
    private void addLists(int currentx, int currenty) {
        JPanel rightSide = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = currentx + 1;
        c.gridy = 0;
        c.weightx = 1;
        c.gridheight = currenty;
        this.add(rightSide, c);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
    }

    /**
     * Disables fields that can no longer be changed.
     */
    private void deactivateFields() {
        this.fname.setEditable(false);
        this.lname.setEditable(false);
    }

    /**
     * This view needs to be saved.
     */
    @Override
    public boolean needsSave() {
        return true;
    }

    /**
     * Saves the customer.
     */
    @Override
    public void save() throws SQLException {
        if (this.customer == null) {
            this.customer = new Customer(this.fname.getText(), this.lname.getText(), this.email.getText(), this.note.getText(), this.password.getText(), this.active.isSelected());

        } else {
            this.customer.setActive(this.active.isSelected());
            this.customer.setEmail(this.email.getText());
            this.customer.setPassword(this.password.getText());
            this.customer.setNote(this.note.getText());
        }
        this.customer.save();
    }

    /**
     * Discards any modifications.
     */
    @Override
    public void cancel() {

    }

    @Override
    public void close() throws CloseCanceledException {
        if ((new ConfirmCloseDialog().show())) {
            try {
                this.save();
            } catch (SQLException e) {
                ErrorLogger.error(e, "Failed to save. Canceling close.", true, true);
                throw new CloseCanceledException();
            }
        } else {
            //do nothing
        }
    }
}

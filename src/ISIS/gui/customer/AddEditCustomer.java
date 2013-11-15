package ISIS.gui.customer;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.gui.address.ListAddress;
import ISIS.misc.Address;
import ISIS.misc.Phone;
import ISIS.transaction.Transaction;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * View for adding and editing customers.
 */
public class AddEditCustomer extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	JCheckBox					active;
	HintField					password, fname, lname, email;
	JTextArea					note;
	JList<Transaction>			transactions;
	JList<Address>				addresses;
	JList<Phone>				phones;
	Customer					customer;
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 */
	public AddEditCustomer(SplitPane splitPane) {
		super(splitPane);
		this.populateElements();
		this.customer = null;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditCustomer(SplitPane splitPane, int pkey) throws SQLException {
		super(splitPane);
		this.customer = new Customer(pkey, true);
		this.populateElements();

		this.active.setSelected(this.customer.isActive());
		this.password.setText(this.customer.getPassword());
		this.fname.setText(this.customer.getFirstName());
		this.lname.setText(this.customer.getLastName());
		this.email.setText(this.customer.getEmail());
		this.note.setText(this.customer.getNote());
		this.disableFields(this.fname, this.lname);
	}
	
	/**
	 * add the lists to the right hand side of the view.
	 * 
	 * @param desiredX
	 *            The desired x position in the grid.
	 * @param currentY
	 *            The current y position in the grid.
	 */
	@SuppressWarnings("unused")
	private void addLists(int desiredX, int currentY) {
        if (this.customer == null) {
            // if the customer hasn't been saved, we can't do this as things are now
            // TODO: FIX
           return;
        }
		JPanel rightSide = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        int y = 0;
		c.gridx = desiredX;
		c.gridy = y++;
        c.weightx = 1;
        c.weighty = 1;
		c.gridheight = currentY;
		this.add(rightSide, c);

        int x = 0;
        c = new GridBagConstraints();
        c.gridy = y = 0;
        c.gridx = x;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        rightSide.add(new ListAddress(this.splitPane, this, this.customer.getPkey(), false), c);
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
		if (this.customer == null) {
			if (!this.isAnyFieldDifferentFromDefault()) return null;
			this.customer = new Customer(this.fname.getText(),
					this.lname.getText(), this.email.getText(),
					this.note.getText(), this.password.getText(),
					this.active.isSelected());
			
		} else {
			this.customer.setActive(this.active.isSelected());
			this.customer.setEmail(this.email.getText());
			this.customer.setPassword(this.password.getText());
			this.customer.setNote(this.note.getText());
		}
		return this.customer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldsDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		return !(this.active.isSelected() && this.email.getText().isEmpty()
				&& this.password.getText().isEmpty() && this.note.getText()
				.isEmpty());
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
		this.add(new JLabel("Active"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.active = new JCheckBox("", true), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Password"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.password = new HintField("Password"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("First name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.fname = new HintField("First Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Last name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.lname = new HintField("Last Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Email"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.email = new HintField("Email"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = x++;
		c.gridy = y;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("Note"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.note = new JTextArea(), c);
		this.note.setBorder(new EtchedBorder());
		
		this.addLists(2, y);
	}
}

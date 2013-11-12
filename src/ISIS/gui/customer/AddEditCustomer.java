package ISIS.gui.customer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import ISIS.customer.Customer;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.misc.Address;
import ISIS.misc.Phone;
import ISIS.transaction.Transaction;

/**
 * View for adding and editing customers.
 */
public class AddEditCustomer extends View {
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
		this.populateElements();
		this.customer = new Customer(pkey, true);
		
		this.active.setSelected(this.customer.isActive());
		this.password.setText(this.customer.getPassword());
		this.fname.setText(this.customer.getFirstName());
		this.lname.setText(this.customer.getLastName());
		this.email.setText(this.customer.getEmail());
		this.note.setText(this.customer.getNote());
		this.deactivateFields();
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
		
		// this.addLists(2, y);
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
		JPanel rightSide = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = desiredX;
		c.gridy = 0;
		c.weightx = 1;
		c.gridheight = currentY;
		this.add(rightSide, c);
	}
	
	/**
	 * Disables fields that can no longer be changed.
	 */
	private void deactivateFields() {
		this.fname.setEditable(false);
		this.lname.setEditable(false);
		this.fname.setForeground(Color.gray);
		this.lname.setForeground(Color.gray);
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
		this.customer.save();
	}
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
}

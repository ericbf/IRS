package ISIS.gui.customer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.NumberHintField;
import ISIS.gui.SplitPane;
import ISIS.misc.Phone;

public class AddEditPhone extends AddEditView {
	private static final long			serialVersionUID	= 1L;
	private Phone						phone;
	private NumberHintField				phoneNo;
	final JComboBox<Phone.PhoneType>	type				= new JComboBox<>(
																	Phone.PhoneType
																			.values());
	private final Customer				customer;
	
	/**
	 * @pre - receive SPlitPane and Customer objects
	 * @post - draw AddEditPhone members in view
	 */
	public AddEditPhone(SplitPane splitPane, Customer customer) {
		super(splitPane);
		this.populateElements();
		this.phone = null;
		this.customer = customer;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 * @pre - recieve required parameters
	 * @post - returns new view for add/edit phone numbers
	 */
	public AddEditPhone(SplitPane splitPane, Customer customer, int pkey)
			throws SQLException {
		super(splitPane);
		this.phone = new Phone(pkey, true);
		this.customer = customer;
		this.populateElements();
		
		this.phoneNo.setText(this.phone.getNumber());
		this.type.setSelectedItem(this.phone.getType());
	}
	
	/**
	 * Discards any modifications.
	 * 
	 * @pre - none
	 * @post - none, override stub
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 * @pre - none
	 * @post - return Record object of current record
	 */
	@Override
	public Record getCurrentRecord() {
		if (!this.isAnyFieldDifferentFromDefault()) {
			return null;
		}
		if (this.phone == null) {
			this.phone = new Phone(this.phoneNo.getText(), false,
					(Phone.PhoneType) this.type.getSelectedItem());
			this.customer.addPhoneNum(this.phone);
			// TODO: fixme
			this.disableFields(this.phoneNo);
			this.type.setEnabled(false);
		}
		return this.phone;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 * @pre - none
	 * @post - bool returned indicating if any difference detected
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.phoneNo.getText().isEmpty();
		return !same;
	}
	
	/**
	 * Draws all necessary components on the window.
	 * 
	 * @pre - none
	 * @post - window populated with elements
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
		this.add(new JLabel("Type"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.type, c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Phone Number"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.phoneNo = new NumberHintField("Phone #"), c);
	}
	
	/**
	 * @pre - none
	 * @post - Save customer record if not null
	 */
	@Override
	protected void postSave() throws SQLException {
		if (this.customer != null) {
			this.customer.save();
		}
	}
	
}

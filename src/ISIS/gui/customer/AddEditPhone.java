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
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
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
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.phoneNo.getText().isEmpty();
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
	
	@Override
	protected void postSave() throws SQLException {
		if (this.customer != null) {
			this.customer.save();
		}
	}
	
}

package ISIS.gui.address;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.misc.Address;

public class AddEditAddress extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private Address				address;
	private HintField			title, city, state, county, country,
			st_address, zip;
	private final Customer		customer;
	private JCheckBox			active;
	
	public AddEditAddress(SplitPane splitPane, Customer customer) {
		super(splitPane);
		this.populateElements();
		this.address = null;
		this.customer = customer;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditAddress(SplitPane splitPane, Customer customer, int pkey)
			throws SQLException {
		super(splitPane);
		this.address = new Address(pkey, true);
		this.customer = customer;
		this.populateElements();
		
		this.active.setSelected(this.address.getActive());
		this.title.setText(this.address.getTitle());
		this.city.setText(this.address.getCity());
		this.state.setText(this.address.getState());
		this.county.setText(this.address.getCounty());
		this.country.setText(this.address.getCountry());
		this.st_address.setText(this.address.getStreetAddress());
		this.zip.setText(this.address.getZIP());
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
		if (this.address == null) {
			this.address = new Address(this.active.isSelected(), false,
					this.country.getText(), this.title.getText(),
					this.zip.getText(), this.state.getText(),
					this.city.getText(), this.county.getText(),
					this.st_address.getText());
			this.customer.addAddress(this.address);
			// TODO: fixme
			this.disableFields(this.country, this.country, this.title,
					this.zip, this.state, this.city, this.county,
					this.st_address);
		} else {
			this.address.setActive(this.active.isSelected());
		}
		return this.address;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		boolean same = true;
		same &= this.active.isSelected();
		same &= this.country.getText().isEmpty();
		same &= this.title.getText().isEmpty();
		same &= this.zip.getText().isEmpty();
		same &= this.state.getText().isEmpty();
		same &= this.city.getText().isEmpty();
		same &= this.county.getText().isEmpty();
		same &= this.st_address.getText().isEmpty();
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
		this.add(new JLabel("Active"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.active = new JCheckBox("", true), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Title"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.title = new HintField("Title"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Country"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.country = new HintField("Country"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("State"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.state = new HintField("State"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("County"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.county = new HintField("County"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("City"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.city = new HintField("City"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Zip"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.zip = new HintField("Zip"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("St. Address"), c);
		
		c = new GridBagConstraints();
		c.weightx = .3;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.st_address = new HintField("Street Address"), c);
		
	}
	
	@Override
	public void save() throws SQLException {
		super.save();
		if (this.customer != null) {
			this.customer.save();
		}
	}
}

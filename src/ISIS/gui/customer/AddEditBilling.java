package ISIS.gui.customer;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
//import ISIS.misc.Address;
import ISIS.misc.Billing;


import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddEditBilling extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private Billing				billing;
	private HintField		address, CCV, number, type, expiration;	
                
                //title, city, state, county, country, st_address, zip;
	private final Customer		customer;
	private JCheckBox			active;
	
	public AddEditBilling(SplitPane splitPane, Customer customer) {
		super(splitPane);
		this.populateElements();
		this.billing = null;
		this.customer = customer;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditBilling(SplitPane splitPane, Customer customer, int pkey)
			throws SQLException {
		super(splitPane);
		this.billing = new Billing(pkey, true);
		this.customer = customer;
		this.populateElements();
		
                this.address.setText(this.billing.getAddress().toString());
                this.type.setText(this.billing.getBillingType().toString());
                this.number.setText(this.billing.getCardNumber());
                this.CCV.setText(this.billing.getCCV());
                this.expiration.setText(this.billing.getExpiration().toString());
	/*	this.active.setSelected(this.billing.getActive());
		this.title.setText(this.billing.getTitle())getTitle;
		this.city.setText(this.billing.getCity());
		this.state.setText(this.billing.getState());
		this.county.setText(this.billing.getCounty());
		this.country.setText(this.billing.getCountry());
		this.st_address.setText(this.billing.getStreetAddress());
		this.zip.setText(this.billing.getZIP());
	*/
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
		if (this.billing == null) {
                    /*
                    * This only has four of the elements from the constructor on
                    * Billing.java. I believe I need to do another line with the
                    * other two elements in the other constructor on Billing.java?
                    *
                    *
                    *
                    * Also, I don't know what addAddress and setActive were for
                    * from when I copied this from AddEditAddress.java. I changed
                    * addAddress to addBilling but I don't think that's right. 
                    * I don't know if they're even needed for AddEditBilling.java. 
                    */
			this.billing = new Billing(this.address.getText(),
                                this.number.getText(), this.CCV.getText(), 
                                this.expiration.getText());
				/*	this.country.getText(), this.title.getText(),
					this.zip.getText(), this.state.getText(),
					this.city.getText(), this.county.getText(),
					this.st_billing.getText());
			*/
                        this.customer.addBilling(this.billing);
			// TODO: fixme
			this.disableFields(this.address, this.number, this.CCV,
					this.expiration);
		} else {
			this.billing.setActive(this.active.isSelected());
		}
		return this.billing;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.active.isSelected()
                                && this.address.getText().isEmpty()
                                && this.type.getText().isEmpty()
                                && this.number.getText().isEmpty()
                                && this.CCV.getText().isEmpty()
                                && this.expiration.getText().isEmpty();
                        
                        /*&& this.country.getText().isEmpty()
				&& this.title.getText().isEmpty()
				&& this.zip.getText().isEmpty()
				&& this.state.getText().isEmpty()
				&& this.city.getText().isEmpty()
				&& this.county.getText().isEmpty()
				&& this.st_billing.getText().isEmpty();
                        */
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
		this.add(new JLabel("Billing Type"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.type = new HintField("Billing Type"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Address"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.address = new HintField("Address"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Card Number"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.number = new HintField("Card Number"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Expiration Date"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.expiration = new HintField("Expiration Date"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("CCV"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.CCV = new HintField("CCV"), c);
		/* Copied and pasted from AddEditAddress.java
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
		*/
	}

    @Override
    protected void postSave() throws SQLException {
        if (this.customer != null) {
            this.customer.save();
        }
    }
	
}

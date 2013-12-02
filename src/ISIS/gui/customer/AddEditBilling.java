package ISIS.gui.customer;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.ErrorLogger;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.gui.simplelists.ListAddress;
import ISIS.misc.Address;
import ISIS.misc.Billing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import ISIS.misc.Address;

public class AddEditBilling extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private Billing				billing;
    private JComboBox   type = new JComboBox(Billing.BillingType.values());
	private HintField		address, CCV, number, expiration;
    private Address billing_address;
                
                //title, city, state, county, country, st_address, zip;
	private final Customer		customer;
	private JCheckBox			active;
	
	public AddEditBilling(SplitPane splitPane, Customer customer) {
        super(splitPane);
        this.customer = customer;
        this.populateElements();
        this.billing = null;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditBilling(SplitPane splitPane, Customer customer, int pkey)
			throws SQLException {
		super(splitPane);
        this.customer = customer;
        this.billing = new Billing(pkey, true);
		this.populateElements();

        if(this.billing.getAddress() != null) {
            this.address.setText(this.billing.getAddress().getStreetAddress());
        }
        this.number.setText(this.billing.getCardNumber());
        this.CCV.setText(this.billing.getCCV());
        this.expiration.setText(this.billing.getExpiration().toString());
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
            try {
                Date exp = new SimpleDateFormat("MM/yy", Locale.ENGLISH).parse(this.expiration.getText());
                this.billing = new Billing(this.billing_address,
                                    this.number.getText(), exp, this.CCV.getText());
            } catch (ParseException e) {
                ErrorLogger.error(e, "Failed to parse exp date.", true, true);
                return null;
            }
				/*	this.country.getText(), this.title.getText(),
					this.zip.getText(), this.state.getText(),
					this.city.getText(), this.county.getText(),
					this.st_billing.getText());
			*/
            this.customer.addBilling(this.billing);
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
                                && (this.type.getSelectedItem() == Billing.BillingType.CASH)
                                && this.number.getText().isEmpty()
                                && this.CCV.getText().isEmpty()
                                && this.expiration.getText().isEmpty();
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
		this.add(this.type, c);
		
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
		this.add(this.address = new HintField(""), c);
		
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


        final ListAddress listAddress = new ListAddress(this.splitPane, this,
                                                  this.customer, this.customer.getPkey(), true);
        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JLabel("Addresses"), c);
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        this.add(listAddress, c);

        listAddress.setSelectAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pkey = listAddress.getSelectedPkey();
                if (pkey == -1) {
                    return;
                }
                try {
                    AddEditBilling.this.billing_address = new Address(pkey, true);
                    AddEditBilling.this.address.setText(AddEditBilling.this.billing_address.getStreetAddress());
                } catch (SQLException ex) {
                    ErrorLogger.error(ex, "Failed to select address.", true, true);
                }
            }
        });

        this.address.setEnabled(false);

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
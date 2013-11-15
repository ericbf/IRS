package ISIS.gui.customer;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.address.ListAddress;

/**
 * View for adding and editing customers.
 */
public class AddEditCustomer extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	JCheckBox					active;
	HintField					password, fname, lname, email;
	JTextArea					note;
	// JList<Transaction> transactions;
	// JList<Address> addresses;
	// JList<Phone> phones;
	Customer					customer;
	JPanel						otherListsContainer;
	CardLayout					otherListsCardLayout;
	JButton						addresses, transactions, phones;
	ArrayList<JButton>			cardLayoutViewButtons;
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 */
	public AddEditCustomer(SplitPane splitPane) {
		super(splitPane);
		this.populateElements();
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
	}
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.AddEditView#newWasSaved()
	 */
	@Override
	protected void doSaveRecordAction() {
		if (this.customer != null && !this.wasSavedOrAlreadySetUp) {
			@SuppressWarnings("rawtypes")
			SimpleListView l;
			
			l = new ListAddress(this.splitPane, this, this.customer.getPkey(),
					false);
			this.otherListsContainer.add(l);
			this.otherListsCardLayout.addLayoutComponent(l, "addresses");
			this.addresses.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AddEditCustomer.this.otherListsCardLayout.show(
							AddEditCustomer.this.otherListsContainer,
							"addresses");
				}
			});
			
			// l = new ListTransactions(this.splitPane, this,
			// this.customer.getPkey(), false);
			// this.otherListsContainer.add(l);
			// this.otherListsCardLayout.addLayoutComponent(l, "transactions");
			// this.transactions.addActionListener(new ActionListener() {
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// AddEditCustomer.this.otherListsCardLayout.show(
			// AddEditCustomer.this.otherListsContainer,
			// "transactions");
			// }
			// });
			// // TODO: add other view here
			//
			// l = new ListPhones(this.splitPane, this, this.customer.getPkey(),
			// false);
			// this.otherListsContainer.add(l);
			// this.otherListsCardLayout.addLayoutComponent(l, "phones");
			// this.transactions.addActionListener(new ActionListener() {
			// @Override
			// public void actionPerformed(ActionEvent e) {
			// AddEditCustomer.this.otherListsCardLayout.show(
			// AddEditCustomer.this.otherListsContainer,
			// "phones");
			// }
			// });
			// // TODO: add other view here
			
			this.disableFields(this.lname, this.fname);
			for (JButton b : this.cardLayoutViewButtons) {
				b.setEnabled(true);
				b.setToolTipText(null);
			}
		} else if (this.customer == null) {
			for (JButton b : this.cardLayoutViewButtons) {
				b.setEnabled(false);
				b.setToolTipText("Save this record to access this area");
			}
		}
		
	}
	
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
		this.cardLayoutViewButtons = new ArrayList<>();
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
		
		int depth = y;
		
		c = new GridBagConstraints();
		c.gridx = x = 2;
		c.gridy = y = 0;
		// c.weightx = .25;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.addresses = new JButton("Addresses"), c);
		this.cardLayoutViewButtons.add(this.addresses);
		
		c = new GridBagConstraints();
		c.gridx = ++x;
		c.gridy = y;
		// c.weightx = .25;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.transactions = new JButton("Transactions"), c);
		this.cardLayoutViewButtons.add(this.transactions);
		
		c = new GridBagConstraints();
		c.gridx = ++x;
		c.gridy = y;
		// c.weightx = .25;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.phones = new JButton("Phones"), c);
		this.cardLayoutViewButtons.add(this.phones);
		
		c = new GridBagConstraints();
		c.gridheight = depth;
		c.gridwidth = x - 1;
		// c.weightx = .5;
		c.weighty = 1;
		c.gridx = 2;
		c.gridy = ++y;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.otherListsContainer = new JPanel(
				this.otherListsCardLayout = new CardLayout()), c);
		this.otherListsContainer.setOpaque(false);
		this.otherListsContainer.setPreferredSize(new Dimension(this
				.getPreferredSize().width / 2, this.getPreferredSize().height));
		
		for (JButton b : this.cardLayoutViewButtons) {
			b.setFont(new Font("Small", Font.PLAIN, 11));
		}
		
		this.doSaveRecordAction();
	}
}

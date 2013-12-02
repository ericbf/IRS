package ISIS.gui.customer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.ListButtonListener;
import ISIS.gui.SimpleListView;
import ISIS.gui.SplitPane;
import ISIS.gui.WrapLayout;
import ISIS.gui.simplelists.ListAddress;
import ISIS.gui.simplelists.ListPhone;
import ISIS.gui.simplelists.ListTransaction;

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
	JToggleButton				addresses, transactions, phones;
	ArrayList<JToggleButton>	cardLayoutViewButtons;
	protected static double		dividerRatio		= 0;
	
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
			this.wasSavedOrAlreadySetUp = true;
			
			@SuppressWarnings("rawtypes")
			SimpleListView l;
			
			// Add the other lists to the JPanel and register with the layout
			this.otherListsContainer.add(l = new ListAddress(this.splitPane,
					this, this.customer, this.customer.getPkey(), false));
			this.otherListsCardLayout.addLayoutComponent(l, "addresses");
			// next
			this.otherListsContainer.add(l = new ListTransaction(
					this.splitPane, this, this.customer, false));
			this.otherListsCardLayout.addLayoutComponent(l, "transactions");
			// next
			this.otherListsContainer.add(l = new ListPhone(this.splitPane,
					this, this.customer, false));
			this.otherListsCardLayout.addLayoutComponent(l, "phones");
			
			// Add action listeners to the buttons
			this.addresses.addActionListener(new ListButtonListener(
					this.otherListsCardLayout, this.otherListsContainer,
					"addresses"));
			// next
			this.transactions.addActionListener(new ListButtonListener(
					this.otherListsCardLayout, this.otherListsContainer,
					"transactions"));
			// next
			this.phones.addActionListener(new ListButtonListener(
					this.otherListsCardLayout, this.otherListsContainer,
					"phones"));
			
			// Disable the uneditable fields
			this.disableFields(this.lname, this.fname);
			
			// Enable buttons to select a list, reset tooltip
			for (JToggleButton b : this.cardLayoutViewButtons) {
				b.setEnabled(true);
				b.setToolTipText(null);
			}
			this.cardLayoutViewButtons.get(0).setSelected(true);
		} else if (this.customer == null) {
			// Disable buttons to select a list if the record isn't yet saved
			for (JToggleButton b : this.cardLayoutViewButtons) {
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
			if (!this.isAnyFieldDifferentFromDefault()) {
				return null;
			}
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
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.active.isSelected()
				&& this.email.getText().isEmpty()
				&& this.password.getText().isEmpty()
				&& this.note.getText().isEmpty();
		return !same;
	}
	
	/**
	 * Draws all necessary components on the window.
	 */
	private void populateElements() {
		this.setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void doLayout() {
				this.setDividerLocation((int) (this.getWidth() * (AddEditCustomer.dividerRatio == 0 ? .55
						: AddEditCustomer.dividerRatio)));
				super.doLayout();
			}
			
			@Override
			public void setDividerLocation(int location) {
				AddEditCustomer.dividerRatio = location
						/ (double) this.getWidth();
				super.setDividerLocation(location);
			}
		};
		split.setOpaque(false);
		split.setBorder(null);
		JPanel main = new JPanel(new GridBagLayout());
		main.setOpaque(false);
		GridBagConstraints c;
		this.cardLayoutViewButtons = new ArrayList<>();
		int x = 0, y = 0;
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("Active"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.active = new JCheckBox("", true), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("Password"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.password = new HintField("Password"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("First name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.fname = new HintField("First Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("Last name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.lname = new HintField("Last Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("Email"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.email = new HintField("Email"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = x++;
		c.gridy = y;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		main.add(new JLabel("Note"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(this.note = new JTextArea(), c);
		this.note.setBorder(new EtchedBorder());
		
		split.setLeftComponent(main);
		
		JPanel otherArea = new JPanel(new BorderLayout());
		otherArea.setOpaque(false);
		JPanel buttonHolder = new JPanel(new WrapLayout());
		buttonHolder.setOpaque(false);
		
		// Add buttons for the cards (Other lists)
		buttonHolder.add(this.addresses = new JToggleButton("Addresses"), c);
		buttonHolder.add(this.transactions = new JToggleButton("Transactions"),
				c);
		buttonHolder.add(this.phones = new JToggleButton("Phones"), c);
		
		// Add buttons to the buttons ArrayList
		this.cardLayoutViewButtons.add(this.addresses);
		this.cardLayoutViewButtons.add(this.transactions);
		this.cardLayoutViewButtons.add(this.phones);
		
		// Add the button holder at the top of the right section
		otherArea.add(buttonHolder, BorderLayout.NORTH);
		
		// Add the JPanel(card layout) to the right section center
		otherArea.add(this.otherListsContainer = new JPanel(
				this.otherListsCardLayout = new CardLayout()),
				BorderLayout.CENTER);
		this.otherListsContainer.setOpaque(false);
		
		split.setRightComponent(otherArea);
		split.setResizeWeight(.5);
		
		this.add(split, BorderLayout.CENTER);
		
		ButtonGroup group = new ButtonGroup();
		for (JToggleButton b : this.cardLayoutViewButtons) {
			b.setFont(new Font("Small", Font.PLAIN, 11));
			group.add(b);
		}
		
		this.doSaveRecordAction();
	}
	//
	// /**
	// * Draws all necessary components on the window.
	// */
	// private void populateElements() {
	// this.setLayout(new GridBagLayout());
	// GridBagConstraints c;
	// this.cardLayoutViewButtons = new ArrayList<>();
	// int x = 0, y = 0;
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.gridx = x++;
	// c.gridy = y;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(new JLabel("Active"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.active = new JCheckBox("", true), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.gridx = x++;
	// c.gridy = y;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(new JLabel("Password"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.password = new HintField("Password"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.gridx = x++;
	// c.gridy = y;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(new JLabel("First name"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.fname = new HintField("First Name"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.gridx = x++;
	// c.gridy = y;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(new JLabel("Last name"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.lname = new HintField("Last Name"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.gridx = x++;
	// c.gridy = y;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(new JLabel("Email"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.email = new HintField("Email"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 0;
	// c.weighty = 1;
	// c.gridx = x++;
	// c.gridy = y;
	// c.anchor = GridBagConstraints.NORTH;
	// c.fill = GridBagConstraints.HORIZONTAL;
	// this.add(new JLabel("Note"), c);
	//
	// c = new GridBagConstraints();
	// c.weightx = 1;
	// c.weighty = 1;
	// c.gridx = x--;
	// c.gridy = y++;
	// c.fill = GridBagConstraints.BOTH;
	// this.add(this.note = new JTextArea(), c);
	// this.note.setBorder(new EtchedBorder());
	//
	// JPanel otherArea = new JPanel(new BorderLayout());
	// otherArea.setOpaque(false);
	// JPanel buttonHolder = new JPanel(new WrapLayout());
	// buttonHolder.setOpaque(false);
	//
	// // Add buttons for the cards (Other lists)
	// buttonHolder.add(this.addresses = new JToggleButton("Addresses"), c);
	// buttonHolder.add(this.transactions = new JToggleButton("Transactions"),
	// c);
	// buttonHolder.add(this.phones = new JToggleButton("Phones"), c);
	//
	// // Add buttons to the buttons ArrayList
	// this.cardLayoutViewButtons.add(this.addresses);
	// this.cardLayoutViewButtons.add(this.transactions);
	// this.cardLayoutViewButtons.add(this.phones);
	//
	// // Add the button holder at the top of the right section
	// otherArea.add(buttonHolder, BorderLayout.NORTH);
	//
	// // Add the JPanel(card layout) to the right section center
	// otherArea.add(this.otherListsContainer = new JPanel(
	// this.otherListsCardLayout = new CardLayout()),
	// BorderLayout.CENTER);
	// this.otherListsContainer.setOpaque(false);
	//
	// c = new GridBagConstraints();
	// c.fill = GridBagConstraints.BOTH;
	// c.weightx = .6;
	// c.weighty = 1;
	// c.gridheight = y;
	// c.gridx = 2;
	// c.gridy = 0;
	// this.add(otherArea, c);
	//
	// ButtonGroup group = new ButtonGroup();
	// for (JToggleButton b : this.cardLayoutViewButtons) {
	// b.setFont(new Font("Small", Font.PLAIN, 11));
	// group.add(b);
	// }
	//
	// this.doSaveRecordAction();
	// }
	
}

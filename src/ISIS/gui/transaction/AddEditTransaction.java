package ISIS.gui.transaction;

import ISIS.customer.Customer;
import ISIS.gui.*;
import ISIS.gui.item.SearchListItems;
import ISIS.gui.simplelists.ListAddress;
import ISIS.gui.simplelists.ListBilling;
import ISIS.gui.simplelists.ListTransactionLineItem;
import ISIS.misc.Address;
import ISIS.transaction.Transaction;
import ISIS.transaction.Transaction.TransactionStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * View for adding and editing customers.
 */
public class AddEditTransaction extends AddEditView {
	private static final long			serialVersionUID	= 1L;
	JCheckBox							returnTransaction;
	final JComboBox<TransactionStatus>	status				= new JComboBox<>(
																	TransactionStatus
																			.values());
	HintField							address, billing, total;
	Transaction							transaction;
	Customer							customer;
	JPanel								otherListsContainer;
	CardLayout							otherListsCardLayout;
	JToggleButton						address_select, billing_select,
			item_select;
	ArrayList<JToggleButton>			cardLayoutViewButtons;
	static double						dividerRatio		= 0;
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 */
	public AddEditTransaction(SplitPane splitPane, Customer customer) {
		super(splitPane);
		this.customer = customer;
		this.transaction = new Transaction(this.customer);
		try {
			this.transaction.save();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to save a new transaction.", true,
					true);
			throw new RuntimeException(e);
		}
		this.populateElements();
		
		this.status.setSelectedItem(this.transaction.getStatus());
	}
	
	/**
	 * Public constructor: returns new instance of add/edit transaction view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditTransaction(SplitPane splitPane, int pkey)
			throws SQLException {
		super(splitPane);
		this.transaction = new Transaction(pkey, true);
		
		try {
			this.customer = this.transaction.getCustomer();
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch customer.", e);
		}
		this.populateElements();
		try {
			this.returnTransaction.setSelected(this.transaction
					.getParentTransaction() != null);
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch parent transaction.", e);
		}
		
		this.status.setSelectedItem(this.transaction.getStatus());
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
		
		@SuppressWarnings("rawtypes")
		ListView l;

        final ListAddress listAddress;
		// Add the other lists to the JPanel and register with the layout
		this.otherListsContainer.add(listAddress = new ListAddress(this.splitPane, this,
				this.customer, this.customer.getPkey(), true));
		this.otherListsCardLayout.addLayoutComponent(listAddress, "Address");
		
		// next TODO
		this.otherListsContainer.add(l = new ListBilling(this.splitPane, this,
                                                         this.customer, this.customer.getPkey()));
		this.otherListsCardLayout.addLayoutComponent(l, "Billing");
		
		// next
		this.otherListsContainer.add(l = new SearchListItems(this.splitPane,
				this, this.customer, this.transaction));
		this.otherListsCardLayout.addLayoutComponent(l, "Items");
		
		// Add action listeners to the buttons
		this.address_select
				.addActionListener(new ListButtonListener(
						this.otherListsCardLayout, this.otherListsContainer,
						"Address"));
        listAddress.setSelectAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pkey = listAddress.getSelectedPkey();
                if (pkey == -1) {
                    return;
                }
                try {
                    AddEditTransaction.this.transaction.setAddress(new Address(pkey, true));
                    AddEditTransaction.this.transaction.save();
                    AddEditTransaction.this.reloadAddress();
                } catch (SQLException ex) {
                    ErrorLogger.error(ex, "Failed to add item to transaction.", true, true);
                }
            }
        });
		// next TODO
		this.billing_select
		.addActionListener(new ListButtonListener(
		this.otherListsCardLayout, this.otherListsContainer,
		"Billing"));
		
		// next
		this.item_select.addActionListener(new ListButtonListener(
				this.otherListsCardLayout, this.otherListsContainer, "Items"));
		
		// Enable buttons to select a list, reset tooltip
		for (JToggleButton b : this.cardLayoutViewButtons) {
			b.setEnabled(true);
			b.setToolTipText(null);
		}
		this.cardLayoutViewButtons.get(0).setSelected(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 */
	@Override
	public Transaction getCurrentRecord() {
		if (this.transaction == null) {
			if (!this.isAnyFieldDifferentFromDefault()) {
				return null;
			}
			this.transaction = new Transaction(this.customer);
			this.transaction.setStatus((TransactionStatus) this.status.getSelectedItem());
			this.transaction.setStatus((TransactionStatus) this.status.getSelectedItem());
		} else {
			this.transaction.setStatus((TransactionStatus) this.status.getSelectedItem());
		}
		return this.transaction;
	}

    public void reloadAddress() {
        try {
            if(this.transaction.hasAddress()) {
                this.address.setText(this.transaction.getAddress().getStreetAddress() + this.transaction.getAddress().getZIP());
            }
        } catch (SQLException e) {
            ErrorLogger.error(e, "Failed to update address", true, true);
        }
    }

	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldsDifferentFromDefault()
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		return true;
		// TODO: This
		// return !(this.active.isSelected() && this.email.getText().isEmpty()
		// && this.password.getText().isEmpty() && this.note.getText()
		// .isEmpty());
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
				this.setDividerLocation((int) (this.getWidth() * (AddEditTransaction.dividerRatio == 0 ? .55
						: AddEditTransaction.dividerRatio)));
				super.doLayout();
			}
			
			@Override
			public void setDividerLocation(int location) {
				AddEditTransaction.dividerRatio = location
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
//		main.add(new JLabel("Return"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
//		main.add(this.returnTransaction = new JCheckBox("", true), c);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        main.add(new JLabel("Status"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        main.add(this.status, c);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        main.add(new JLabel("Address"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        main.add(this.address=new HintField(), c);
        this.address.setEnabled(false);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		main.add(new JLabel("Items"), c);
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		main.add(new ListTransactionLineItem(this.splitPane, this,
				this.transaction), c);
		
		split.setLeftComponent(main);
		
		JPanel otherArea = new JPanel(new BorderLayout());
		otherArea.setOpaque(false);
		JPanel buttonHolder = new JPanel(new WrapLayout());
		buttonHolder.setOpaque(false);
		
		// Add buttons for the cards (Other lists)
		buttonHolder.add(this.address_select = new JToggleButton("Address"), c);
		buttonHolder.add(this.billing_select = new JToggleButton("Billing"), c);
		buttonHolder.add(this.item_select = new JToggleButton("Items"), c);
		
		// Add buttons to the buttons ArrayList
		this.cardLayoutViewButtons.add(this.address_select);
		this.cardLayoutViewButtons.add(this.billing_select);
		this.cardLayoutViewButtons.add(this.item_select);
		
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
}
package ISIS.gui.transaction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.DoubleHintField;
import ISIS.gui.HintArea;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;
import ISIS.transaction.Transaction;
import ISIS.transaction.TransactionLineItem;

public class AddEditTransactionLineItem extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private final Item			item;
	private TransactionLineItem	lineItem			= null;
	private final Transaction	transaction;
	private HintField			itemName;
	private HintArea			description;
	private DoubleHintField		price, adjustment, quantity;
	private final Customer		customer;
	private boolean				failed;
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @param splitPane
	 * @param customer
	 * @param transaction
	 * @param item
	 * @throws SQLException
	 * @wbp.parser.constructor
	 */
	public AddEditTransactionLineItem(SplitPane splitPane, Customer customer,
			Transaction transaction, Item item) throws SQLException {
		super(splitPane);
		this.lineItem = null;
		this.transaction = transaction;
		this.item = item;
		this.customer = customer;
		this.populateElements();
		
		this.itemName.setText(item.getName());
		this.price.setText(item.getPrice().toString());
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
		if (this.lineItem == null) {
			this.lineItem = new TransactionLineItem(this.transaction,
					this.item, new BigDecimal(this.price.getText()),
					new BigDecimal(this.adjustment.getText()), new BigDecimal(
							this.quantity.getText()),
					this.description.getText());
		} else {
			this.lineItem.setDescription(this.description.getText());
			this.lineItem.setAdjustment(new BigDecimal(this.adjustment
					.getText()));
			this.lineItem.setQuantity(new BigDecimal(this.quantity.getText()));
		}
		return this.lineItem;
	}
	
	/**
	 * Even if nothing was changed from the defaults, we still want to save it.
	 * 
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		return true;
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
		this.add(new JLabel("Item name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.itemName = new HintField()).make(), c);
		this.itemName.setEnabled(false);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Price"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.price = new DoubleHintField()).make(), c);
		this.price.setEnabled(false);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Quantity"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.quantity = new DoubleHintField("Quantity")).make(), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Adjustment"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.adjustment = new DoubleHintField("Adjustment")).make(),
				c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Description"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add((this.description = new HintArea("Description")).make(), c);
		
		this.quantity.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (AddEditTransactionLineItem.this.item
						.getOnHandQty()
						.compareTo(
								new BigDecimal(
										AddEditTransactionLineItem.this.quantity
												.getText())) < 0) {
					AddEditTransactionLineItem.this.failed = true;
					JOptionPane
							.showMessageDialog(AddEditTransactionLineItem.this,
									"Don't have enough stock on-hand to sell this many");
					AddEditTransactionLineItem.this.quantity
							.requestFocusInWindow();
					AddEditTransactionLineItem.this.quantity.selectAll();
				} else {
					AddEditTransactionLineItem.this.failed = AddEditTransactionLineItem.this.item == null;
				}
			}
		});
	}
	
	/**
	 * @pre - this.customer != null == true
	 * @post - saves the customer.
	 */
	@Override
	protected void postSave() throws SQLException {
		if (this.customer != null) {
			this.customer.save();
		}
		if (this.item != null) {
			this.item.setOnHandQty(this.item.getOnHandQty().subtract(
					new BigDecimal(this.quantity.getText())));
			this.item.save();
		}
	}
	
	/**
	 * Backup to the focus listener: prevents save if not enough on-hand
	 */
	@Override
	protected void preSave() throws SQLException {
		if (this.failed) {
			this.failed = false;
			throw new SQLException("Can't put more items than on-hand");
		}
	}
	
}

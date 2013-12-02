package ISIS.gui.transaction;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;
import ISIS.transaction.Transaction;
import ISIS.transaction.TransactionLineItem;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddEditTransactionLineItem extends AddEditView {
	private static final long		serialVersionUID	= 1L;
	private final Item				item;
	private TransactionLineItem		lineItem = null;
	private final Transaction		transaction;
	private HintField				itemName, price, adjustment, quantity,
			description;
	private final Customer			customer;
	private ArrayList<HintField>	constrainedFields;
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
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
        if(this.lineItem == null) {
		    this.lineItem = new TransactionLineItem(
				this.transaction, this.item, new BigDecimal(
						this.price.getText()), new BigDecimal(
						this.adjustment.getText()), new BigDecimal(
						this.quantity.getText()), this.description.getText());
        } else {
            this.lineItem.setDescription(this.description.getText());
            this.lineItem.setAdjustment(new BigDecimal(this.adjustment.getText()));
            this.lineItem.setQuantity(new BigDecimal(this.quantity.getText()));
        }
        return this.lineItem;
	}
	
	/*
	 * Even if nothing was changed from the defaults, we still want to save it.
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
		this.constrainedFields = new ArrayList<>();
		
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
		this.add(this.itemName = new HintField(), c);
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
		this.add(this.price = new HintField(), c);
		this.price.setEnabled(false);
		
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
		this.add(this.adjustment = new HintField("Adjustment", "0.00"), c);
		this.constrainedFields.add(this.adjustment);
		
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
		this.add(this.quantity = new HintField("Quantity", "0.00"), c);
		this.constrainedFields.add(this.quantity);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Description"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.description = new HintField("Description"), c);
		
		for (final HintField field : this.constrainedFields) {
			((PlainDocument) field.getDocument())
					.setDocumentFilter(AddEditView.numberFilter);
			field.addFocusListener(new FocusListener() {
				
				@Override
				public void focusGained(FocusEvent e) {}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (field.getText().isEmpty()) {
						field.setText("0.00");
					}
					if (field.getText().matches("[0-9]*\\.")) {
						field.setText(field.getText() + "0");
					}
					if (field.getText().matches("\\.[0-9]*")) {
						field.setText("0" + field.getText());
					}
					if (field.getText().matches("[0-9]*\\.[0-9]")) {
						field.setText(field.getText() + "0");
					}
					field.setCaretPosition(0);
				}
			});
		}
	}
	
	@Override
	protected void postSave() throws SQLException {
		if (this.customer != null) {
			this.customer.save();
		}
	}
	
}

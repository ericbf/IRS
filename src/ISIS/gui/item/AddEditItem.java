package ISIS.gui.item;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.BadInputException;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

/**
 * View for adding and editing items.
 */
public class AddEditItem extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private JCheckBox			active;
	private HintField			SKU, name, price, stock, UOM;
	private Item				item;
	private JTextArea			description;
	
	/**
	 * Public constructor: returns new instance of add/edit item view.
	 */
	public AddEditItem(SplitPane splitPane) {
		super(splitPane);
		this.populateElements();
		this.item = null;
	}
	
	/**
	 * Public constructor: returns new instance of add/edit customer view.
	 * 
	 * @wbp.parser.constructor
	 */
	public AddEditItem(SplitPane splitPane, int pkey) throws SQLException {
		super(splitPane);
		this.populateElements();
		this.item = new Item(pkey, true);
		
		this.active.setSelected(this.item.isActive());
		this.SKU.setText(this.item.getSKU());
		this.name.setText(this.item.getName());
		this.price.setText(this.item.getPrice().toString());
		this.stock.setText(this.item.getOnHandQty().toString());
		this.UOM.setText(this.item.getUOM());
		this.description.setText(this.item.getDescription());
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
	public Record getCurrentRecord() throws BadInputException {
		BigDecimal price, onhand, reorder, cost;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		// TODO: VALIDATION
		if (this.isAnyFieldDifferentFromDefault()) return null;
		try {
			price = new BigDecimal(this.price.getText());
		} catch (NumberFormatException e) {
			throw new BadInputException("Bad input for price (\""
					+ this.price.getText() + "\"); must be a number.");
		}
		try {
			onhand = new BigDecimal(this.stock.getText());
		} catch (NumberFormatException e) {
			throw new BadInputException("Bad input for onhand quantity (\""
					+ this.price.getText() + "; must be a number.");
		}
		try {
			reorder = new BigDecimal("0.0"); // TODO: fix me
		} catch (NumberFormatException e) {
			throw new BadInputException("Bad input for reorder quantity (\""
					+ this.price.getText() + "; must be a number.");
		}
		try {
			cost = new BigDecimal("0.0"); // TODO: fix me
		} catch (NumberFormatException e) {
			throw new BadInputException("Bad input for cost (\""
					+ this.price.getText() + "; must be a number.");
		}
		if (this.item == null) {
			
			this.item = new Item(this.name.getText(), this.SKU.getText(),
					this.description.getText(), price, onhand, reorder,
					this.UOM.getText(), cost, this.active.isSelected());
			
		} else {
			this.item.setName(this.name.getText());
			this.item.setDescription(this.description.getText());
			this.item.setPrice(price);
			this.item.setOnHandQty(onhand);
			this.item.setReorderQty(reorder);
			this.item.setCost(cost);
			// TODO: disable UOM, SKU fields if editing (we don't allow that to
			// be changed)
			this.item.setActive(this.active.isSelected());
		}
		return this.item;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		return this.active.isSelected() && this.SKU.getText().isEmpty()
				&& this.name.getText().isEmpty()
				&& this.description.getText().isEmpty()
				&& this.price.getText().isEmpty()
				&& this.stock.getText().isEmpty()
				&& this.UOM.getText().isEmpty();
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
		this.add(new JLabel("SKU"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.SKU = new HintField("SKU"), c);
		
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
		this.add(new JLabel("Name"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.name = new HintField("Name"), c);
		
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
		this.add(this.price = new HintField("Price"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Stock"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.stock = new HintField("amount"), c);
		
		c = new GridBagConstraints();
		c.weightx = .3;
		c.gridx = x-- + 1;
		c.gridy = y++;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.UOM = new HintField("UOM"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.weighty = 1;
		c.gridx = x++;
		c.gridy = y;
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("Description"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.description = new JTextArea(), c);
		this.description.setBorder(new EtchedBorder());
	}
}

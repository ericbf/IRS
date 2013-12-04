package ISIS.gui.item;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.DoubleHintField;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

/**
 * View for adding and editing items.
 */
public class AddEditItem extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private JCheckBox			active;
	private HintField			SKU, name, UOM;
	private DoubleHintField		price, stock, cost, reorder;
	private Item				item;
	private JTextArea			description;
	
	/**
	 * Public constructor: returns new instance of add/edit item view.
         * @pre - SPlitPane to update received
         * @post SplitPane populated with new AddEditItem vie
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
         * @pre - receive SPlitPane object and item pkey
         * @post - draw Item members in view
	 */
	public AddEditItem(SplitPane splitPane, int pkey) throws SQLException {
		super(splitPane);
		this.populateElements();
		this.item = new Item(pkey, true);
		
		this.active.setSelected(this.item.isActive());
		this.SKU.setText(this.item.getSKU());
		this.name.setText(this.item.getName());
		this.price.setText(this.item.getPrice().toString());
		this.cost.setText(this.item.getCost().toString());
		this.reorder.setText(this.item.getReorderQuantity().toString());
		this.stock.setText(this.item.getOnHandQty().toString());
		this.UOM.setText(this.item.getUOM());
		this.description.setText(this.item.getDescription());
		this.disableFields(this.SKU, this.UOM);
	}
	
	/**
	 * Discards any modifications.
         * 
         * @pre - none
         * @post - none, override stub
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
        @pre - none
        @post - returns item mebers for current record
	 */
	@Override
	public Record getCurrentRecord() {
		BigDecimal price = new BigDecimal(this.price.getText());
		BigDecimal onhand = new BigDecimal(this.stock.getText());
		BigDecimal reorder = new BigDecimal(this.reorder.getText());
		BigDecimal cost = new BigDecimal(this.cost.getText());
		if (!this.isAnyFieldDifferentFromDefault()) {
			return null;
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
			this.item.setActive(this.active.isSelected());
		}
		return this.item;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
        @pre - none
        @post - returns bool indicating if any fields differ from default values
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		boolean same = this.active.isSelected() && this.SKU.getText().isEmpty()
				&& this.name.getText().isEmpty()
				&& this.description.getText().isEmpty()
				&& this.UOM.getText().isEmpty()
				&& this.price.getText().equals("0.00")
				&& this.stock.getText().equals("0.00")
				&& this.cost.getText().equals("0.00")
				&& this.reorder.getText().equals("0.00");
		return !same;
	}
	
	/**
	 * Draws all necessary components on the window.
         * 
         * @pre - none
         * @post - draws members for current object in view
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
		this.add(this.price = new DoubleHintField("Retail price"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Cost"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.cost = new DoubleHintField("Restock cost"), c);
		
		c = new GridBagConstraints();
		c.weightx = 0;
		c.gridx = x++;
		c.gridy = y;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JLabel("Reorder"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		this.add(this.reorder = new DoubleHintField("Reorder amount"), c);
		
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
		this.add(this.stock = new DoubleHintField("Current Stock"), c);
		
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

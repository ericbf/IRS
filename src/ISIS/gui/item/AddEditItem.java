package ISIS.gui.item;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import ISIS.database.Record;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.item.Item;

/**
 * View for adding and editing items.
 */
public class AddEditItem extends View {
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
	public Record getCurrentRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getTemporaryRecord()
	 */
	@Override
	public Record getTemporaryRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * This view needs to be saved.
	 */
	@Override
	public boolean needsSave() {
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
		this.add(new JLabel("SKU"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
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
		c.anchor = GridBagConstraints.CENTER;
		this.add(this.stock = new HintField("amount"), c);
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.gridx = x--;
		c.gridy = y++;
		c.anchor = GridBagConstraints.EAST;
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
		c.fill = GridBagConstraints.BOTH;
		this.add(this.description = new JTextArea(), c);
		this.description.setBorder(new EtchedBorder());
	}
	
	/**
	 * Saves the item.
	 */
	@Override
	public void save() {}
}

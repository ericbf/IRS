package ISIS.gui.item;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.text.PlainDocument;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.HintField;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

/**
 * View for adding and editing items.
 */
public class AddEditItem extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	private JCheckBox			active;
	private HintField			SKU, name, UOM, price, stock, cost, reorder;
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
		this.cost.setText(this.item.getCost().toString());
		this.reorder.setText(this.item.getReorderQuantity().toString());
		this.stock.setText(this.item.getOnHandQty().toString());
		this.UOM.setText(this.item.getUOM());
		this.description.setText(this.item.getDescription());
		this.disableFields(this.SKU, this.UOM);
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
		BigDecimal price = new BigDecimal(this.price.getText());
		BigDecimal onhand = new BigDecimal(this.stock.getText());
		BigDecimal reorder = new BigDecimal(this.reorder.getText());
		BigDecimal cost = new BigDecimal(this.cost.getText());
		if (!this.isAnyFieldDifferentFromDefault()) return null;
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
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		boolean same = true;
		same &= this.active.isSelected();
		same &= this.SKU.getText().isEmpty();
		same &= this.name.getText().isEmpty();
		same &= this.description.getText().isEmpty();
		same &= this.UOM.getText().isEmpty();
		same &= this.price.getText().equals("0.00");
		same &= this.stock.getText().equals("0.00");
		same &= this.cost.getText().equals("0.00");
		same &= this.reorder.getText().equals("0.00");
		return !same;
	}
	
	/**
	 * Draws all necessary components on the window.
	 */
	private void populateElements() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		ArrayList<HintField> constrainedFields = new ArrayList<>();
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
		this.add(this.price = new HintField(null, "0.00"), c);
		constrainedFields.add(this.price);
		this.price.setToolTipText("Price");
		this.price.setHintEnabled(false);
		
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
		this.add(this.cost = new HintField(null, "0.00"), c);
		constrainedFields.add(this.cost);
		this.cost.setToolTipText("Cost");
		this.cost.setHintEnabled(false);
		
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
		this.add(this.reorder = new HintField(null, "0.00"), c);
		constrainedFields.add(this.reorder);
		this.reorder.setToolTipText("Quantity before reordering");
		this.reorder.setHintEnabled(false);
		
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
		this.add(this.stock = new HintField(null, "0.00"), c);
		constrainedFields.add(this.stock);
		this.stock.setToolTipText("On-hand quantity");
		this.stock.setHintEnabled(false);
		
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
		
		for (final HintField field : constrainedFields) {
			((PlainDocument) field.getDocument())
					.setDocumentFilter(AddEditView.numberFilter);
			field.addFocusListener(new FocusListener() {
				
				@Override
				public void focusGained(FocusEvent e) {}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (field.getText().isEmpty()) field.setText("0.00");
					if (field.getText().matches("[0-9]*\\."))
						field.setText(field.getText() + "0");
					if (field.getText().matches("\\.[0-9]*"))
						field.setText("0" + field.getText());
					if (field.getText().matches("[0-9]*\\.[0-9]"))
						field.setText(field.getText() + "0");
					field.setCaretPosition(0);
				}
			});
		}
	}
}

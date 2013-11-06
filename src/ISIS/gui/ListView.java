package ISIS.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import ISIS.database.Record;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class ListView<E extends Record> extends View {
	private static final long	serialVersionUID	= 1L;
	protected JTable			table;
	protected HintField			searchField;
	protected ArrayList<E>		records;
	protected IRSTableModel		tableModel;
	
	public ListView(SplitPane splitPane) {
		super(splitPane);
		this.table = new JTable();
		this.table.setModel(this.tableModel = new IRSTableModel());
		this.searchField = new HintField("Enter query to search...");
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setOpaque(false);
	}
	
	/**
	 * This type of view needs not be saved.
	 */
	@Override
	public boolean needsSave() {
		return false;
	}
	
	/**
	 * Save is not supported.
	 */
	@Override
	public void save() {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	/**
	 * Cancel is not supported.
	 */
	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	protected class HintField extends JTextField {
		private static final long	serialVersionUID	= 1L;
		private boolean				hintShown;
		private boolean				showHint;
		private String				hint;
		
		public HintField() {
			super();
			this.addFocusListener(this.new Listener());
			this.hintShown = true;
		}
		
		public HintField(String hint) {
			super(hint);
			this.hint = hint;
			this.addFocusListener(this.new Listener());
		}
		
		public void setShowHint(boolean b) {
			this.showHint = b;
		}
		
		public HintField(Document dcmnt, String notifyAction, int TOP) {
			super(dcmnt, notifyAction, TOP);
			this.addFocusListener(this.new Listener());
		}
		
		@Override
		public String getText() {
			if (!this.hintShown) return super.getText();
			else return "";
		}
		
		class Listener implements FocusListener {
			@Override
			public void focusGained(FocusEvent fe) {
				HintField.this.selectAll();
				if (HintField.this.hintShown) HintField.this.setText("");
				HintField.this.hintShown = false;
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				HintField.this.setCaretPosition(0);
				if (HintField.this.getText().length() == 0) {
					HintField.this.hintShown = true;
					HintField.this.setText(HintField.this.hint);
				}
			}
		}
	}
	
	public class IRSTableModel extends DefaultTableModel {
		private String[]	columnTitles;
		
		public void setColumnTitles(String... titles) {
			this.columnTitles = titles;
		}
		
		public void addRow(Record record) {
			Object[] array = new Object[this.getColumnCount()];
			
			for (int i = 0; i < this.getColumnCount(); i++) {
				array[i] = record.getFieldValue(this.columnTitles[i]);
			}
			
			super.addRow(array);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}

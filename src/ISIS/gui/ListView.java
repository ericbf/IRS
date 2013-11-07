package ISIS.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

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
	protected String[]			buttonNames			= { "Add", "Edit",
			"Toggle Active"						};
	
	public ListView(SplitPane splitPane) {
		super(splitPane);
		this.table = new JTable();
		this.searchField = new HintField("Enter query to search...");
		this.searchField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				ListView.this.fillTable();
			}
		});
		this.setFocusCycleRoot(true);
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public Component getFirstComponent(Container aContainer) {
				return ListView.this.searchField;
			}
		});
	}
	
	protected void setTableModel(IRSTableModel model) {
		this.tableModel = model;
		this.table.setModel(model);
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
	
	/**
	 * Fills the table.
	 */
	protected abstract void fillTable();
}

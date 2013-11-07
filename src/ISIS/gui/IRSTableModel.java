/**
 * 
 */
package ISIS.gui;

import javax.swing.table.DefaultTableModel;

import ISIS.database.Record;

public abstract class IRSTableModel extends DefaultTableModel {
	private static final long	serialVersionUID	= 1L;
	String[]					columnTitles;
	
	public void setColumnTitles(String... titles) {
		this.columnTitles = titles;
	}
	
	public abstract void addRow(Record record);
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
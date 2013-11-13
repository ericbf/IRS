/**
 * 
 */
package ISIS.gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import ISIS.database.Record;

public abstract class IRSTableModel extends DefaultTableModel {
	private static final long	serialVersionUID	= 1L;
	public static final Color	DEFAULT_COLOR		= new Color(255, 255, 255,
															0);
	public static final Color	INACTIVE_COLOR		= new Color(220, 220, 220);
	
	String[]					columnTitles;
	ArrayList<Color>			rowColors;
	
	public abstract void addRow(Record record);
	
	@Override
	public final boolean isCellEditable(int row, int column) {
		return false;
	}
	
	public final void setColorAt(int row, Color color) {
		this.rowColors.set(row, color);
	}
	
	public final void setColumnTitles(String... titles) {
		this.columnTitles = titles;
		this.setColumnIdentifiers(titles);
		this.setColumnCount(titles.length);
	}
}
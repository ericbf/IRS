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
	public static final Color	INACTIVE_COLOR		= new Color(240, 200, 200);
	
	String[]					columnTitles;
	ArrayList<Color>			rowColors;
	
	/**
	 * @param record
	 * @pre - receives a Record
	 * @post - row added to record
	 */
	public abstract void addRow(Record record);
	
	/**
	 * @pre - receives row & column
	 * @post - returns true cell is editable
	 */
	@Override
	public final boolean isCellEditable(int row, int column) {
		return false;
	}
	
	/**
	 * @param row
	 * @param color
	 * @pre - receives row & color
	 * @post -sets color value for row
	 */
	public final void setColorAt(int row, Color color) {
		this.rowColors.set(row, color);
	}
	
	/**
	 * @param titles
	 * @pre - receives Title strings
	 * @post - sets column titles to received strings
	 */
	public final void setColumnTitles(String... titles) {
		this.columnTitles = titles;
		this.setColumnIdentifiers(titles);
		this.setColumnCount(titles.length);
	}
}
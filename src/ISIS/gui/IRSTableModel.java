/**
 * 
 */
package ISIS.gui;

import ISIS.database.Record;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public abstract class IRSTableModel extends DefaultTableModel {
	private static final long	serialVersionUID	= 1L;
	String[]					columnTitles;
    ArrayList<Color> rowColors;
    public static Color defaultColor = Color.WHITE;
	
	public final void setColumnTitles(String... titles) {
		this.columnTitles = titles;
		this.setColumnIdentifiers(titles);
		this.setColumnCount(titles.length);
	}

    public final void setColorAt(int row, Color color) {
        this.rowColors.set(row, color);
    }
	
	public abstract void addRow(Record record);
	
	@Override
	public final boolean isCellEditable(int row, int column) {
		return false;
	}
}
package ISIS.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ISIS.database.Record;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class ListView<E extends Record> extends View {

    private static final long serialVersionUID = 1L;
    protected JTable table;
    protected HintField searchField;
    protected ArrayList<E> records;
    protected IRSTableModel tableModel;

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

    protected class HintField extends JTextField {

	private static final long serialVersionUID = 1L;
	private boolean hintShown;
	private boolean showHint;
	private String hint;

	public HintField() {
	    this("");
	}

	public HintField(String hint) {
	    super(hint);
	    this.hint = hint;
	    this.hintShown = true;
	    this.showHint = true;
	    this.addFocusListener(this.new Listener());
	}

	public void setShowHint(boolean b) {
	    this.showHint = b;
	}

	@Override
	public String getText() {
	    if (!this.hintShown) {
		return super.getText();
	    } else {
		return "";
	    }
	}

	class Listener implements FocusListener {

	    @Override
	    public void focusGained(FocusEvent fe) {
		HintField.this.selectAll();
		if (HintField.this.hintShown) {
		    HintField.this.setText("");
		}
		HintField.this.hintShown = false;
	    }

	    @Override
	    public void focusLost(FocusEvent fe) {
		HintField.this.setCaretPosition(0);
		if (HintField.this.getText().length() == 0) {
		    HintField.this.hintShown = true;
		    if (HintField.this.showHint) {
			HintField.this.setText(HintField.this.hint);
		    }
		}
	    }
	}
    }

    public abstract class IRSTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnTitles;

	public void setColumnTitles(String... titles) {
	    this.columnTitles = titles;
	}

	public abstract void addRow(Record record);

	@Override
	public boolean isCellEditable(int row, int column) {
	    return false;
	}
    }
}

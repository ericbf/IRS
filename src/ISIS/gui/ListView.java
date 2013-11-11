package ISIS.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.session.Session;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class ListView<E extends Record> extends View {
	private static final long		serialVersionUID	= 1L;
	protected JTable				table;
	protected HintField				searchField;
	protected ArrayList<E>			records;
	protected IRSTableModel			tableModel;
	protected String[]				buttonNames			= { "Add", "Edit",
			"Toggle Active", "View", "Generate Nonfinal Invoice(s)",
			"Close and Generate Invoice"				};
	protected ArrayList<Integer>	keys				= new ArrayList<>();
	protected int					selected;
	
	public ListView(SplitPane splitPane) {
		super(splitPane);
		this.table = new JTable();
		this.selected = -1;
		this.searchField = new HintField("Enter query to search...");
		this.searchField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				ListView.this.fillTable();
			}
		});
		this.searchField.addKeyListener(new KeyListener() {
			private final int	DOWN	= 40, UP = 38;
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case DOWN:
						if (ListView.this.table.getRowCount() > 0) {
							int downSel = ListView.this.table.getSelectedRow();
							if (meta) {
								downSel = ListView.this.table.getRowCount() - 1;
								ListView.this.table.setRowSelectionInterval(
										downSel, downSel);
							} else if (downSel != -1) {
								downSel += downSel + 1 < ListView.this.table
										.getRowCount() ? 1 : 0;
								ListView.this.table.setRowSelectionInterval(
										downSel, downSel);
							} else {
								ListView.this.table.setRowSelectionInterval(0,
										0);
							}
						}
						int sel;
						if ((sel = ListView.this.table.getSelectedRow()) != -1)
							ListView.this.selected = sel;
						break;
					case UP:
						if (ListView.this.table.getRowCount() > 0) {
							int upSel = ListView.this.table.getSelectedRow();
							if (meta) {
								ListView.this.table.setRowSelectionInterval(0,
										0);
							} else if (upSel != -1) {
								upSel -= upSel > 0 ? 1 : 0;
								ListView.this.table.setRowSelectionInterval(
										upSel, upSel);
							} else {
								upSel = ListView.this.table.getRowCount() - 1;
								ListView.this.table.setRowSelectionInterval(
										upSel, upSel);
							}
						}
						int selPasser;
						if ((selPasser = ListView.this.table.getSelectedRow()) != -1)
							ListView.this.selected = selPasser;
						break;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ListView.this.actionHandlerActionForSearchField();
			}
		});
		this.searchField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				ListView.this.table.setFocusable(false);
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				ListView.this.table.setFocusable(true);
			}
		});
		this.table.addKeyListener(new KeyListener() {
			private final int	DOWN	= 40, UP = 38;
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case DOWN:
						if (ListView.this.table.getRowCount() > 0) {
							if (meta) {
								int rowCount = ListView.this.table
										.getRowCount() - 1;
								ListView.this.table.setRowSelectionInterval(
										rowCount, rowCount);
							} else if (ListView.this.table.getSelectedRow() == -1) {
								ListView.this.table.setRowSelectionInterval(0,
										0);
							}
						}
						int sel;
						if ((sel = ListView.this.table.getSelectedRow()) != -1)
							ListView.this.selected = sel;
						break;
					case UP:
						if (ListView.this.table.getRowCount() > 0) {
							if (meta) {
								ListView.this.table.setRowSelectionInterval(0,
										0);
							} else if (ListView.this.table.getSelectedRow() == -1) {
								int rowCount = ListView.this.table
										.getRowCount() - 1;
								ListView.this.table.setRowSelectionInterval(
										rowCount, rowCount);
							}
						}
						int selPasser;
						if ((selPasser = ListView.this.table.getSelectedRow()) != -1)
							ListView.this.selected = selPasser;
						break;
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		this.table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int sel;
				if ((sel = ListView.this.table.getSelectedRow()) != -1)
					ListView.this.selected = sel;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		this.setFocusCycleRoot(true);
		this.setOpaque(false);
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public Component getFirstComponent(Container aContainer) {
				return ListView.this.searchField;
			}
		});
	}
	
	protected abstract void actionHandlerActionForSearchField();
	
	/**
	 * Cancel is not supported.
	 */
	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	protected void fillTable() {
		String searchFieldText = this.searchField.getText();
		try {
			PreparedStatement stmt;
			if (searchFieldText.length() >= 1) {
				String search = searchFieldText + " ";
				// remove leading whitespace
				search = search.replaceFirst("^\\s+", "");
				// replaces whitespace with wildcards then a space.
				search = search.replaceAll("\\s+", "* ");
				// these aren't indexed anyway, so...
				search = search.replaceAll("([\\(\\)])", "");
				search = search.replaceAll("\\\"", ""); // TODO: actually fix
				String sql = "SELECT i.* FROM (SELECT pkey AS row FROM item_search WHERE item_search MATCH ?) "
						+ "LEFT JOIN"
						+ " "
						+ this.tableName()
						+ " AS i ON row=i.pkey";
				stmt = Session.getDB().prepareStatement(sql);
				stmt.setString(1, search);
			} else {
				String sqlQuery = "SELECT i.* from item AS i";
				stmt = Session.getDB().prepareStatement(sqlQuery);
			}
			ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
					.executeQuery());
			this.records = this.mapResults(results);
			this.populateTable();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Error populating item table.", true, true);
		}
	}
	
	protected abstract boolean hasDates();
	
	protected abstract ArrayList<E> mapResults(
			ArrayList<HashMap<String, Field>> results);
	
	/**
	 * This type of view needs not be saved.
	 */
	@Override
	public boolean needsSave() {
		return false;
	}
	
	private void populateTable() {
		this.table.removeAll();
		this.keys.clear();
		this.tableModel.setRowCount(0);
		for (E i : this.records) {
			this.tableModel.addRow(i);
		}
	}
	
	/**
	 * Save is not supported.
	 */
	@Override
	public void save() throws SQLException {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	protected void setTableModel(IRSTableModel model) {
		this.tableModel = model;
		this.table.setModel(model);
		this.table.setFocusable(false);
	}
	
	protected abstract String tableName();
}

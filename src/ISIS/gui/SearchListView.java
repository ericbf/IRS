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

import javax.swing.AbstractAction;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.session.Session;

/**
 * Abstract class for views that consist of a list that can be searched.
 */
public abstract class SearchListView<E extends Record> extends ListView<E> {
	private static final long	serialVersionUID		= 1L;
	protected HintField			searchField;
	protected String[]			buttonNames				= { "Add", "Edit",
			"Toggle Active", "View", "Generate Nonfinal Invoice(s)",
			"Close and Generate Invoice"				};
	protected int				selected;
	private String				lastSearchFieldValue	= " ";
	
	public SearchListView(SplitPane splitPane) {
		super(splitPane, false);
		this.setBorder(new EmptyBorder(4, 0, 10, 5));
		
		this.selected = -1;
		this.searchField = new HintField("Enter query to search...");
		this.searchField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				SearchListView.this.fillTable();
			}
		});
		this.searchField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if (SearchListView.this.table.getRowCount() > 0) {
							int downSel = SearchListView.this.table
									.getSelectedRow();
							if (meta) {
								downSel = SearchListView.this.table
										.getRowCount() - 1;
								SearchListView.this.table
										.setRowSelectionInterval(downSel,
												downSel);
							} else if (downSel != -1) {
								downSel += downSel + 1 < SearchListView.this.table
										.getRowCount() ? 1 : 0;
								SearchListView.this.table
										.setRowSelectionInterval(downSel,
												downSel);
							} else {
								SearchListView.this.table
										.setRowSelectionInterval(0, 0);
							}
						}
						int sel;
						if ((sel = SearchListView.this.table.getSelectedRow()) != -1)
							SearchListView.this.selected = sel;
						break;
					case KeyEvent.VK_UP:
						if (SearchListView.this.table.getRowCount() > 0) {
							int upSel = SearchListView.this.table
									.getSelectedRow();
							if (meta) {
								SearchListView.this.table
										.setRowSelectionInterval(0, 0);
							} else if (upSel != -1) {
								upSel -= upSel > 0 ? 1 : 0;
								SearchListView.this.table
										.setRowSelectionInterval(upSel, upSel);
							} else {
								upSel = SearchListView.this.table.getRowCount() - 1;
								SearchListView.this.table
										.setRowSelectionInterval(upSel, upSel);
							}
						}
						int selPasser;
						if ((selPasser = SearchListView.this.table
								.getSelectedRow()) != -1)
							SearchListView.this.selected = selPasser;
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
				SearchListView.this.actionHandlerActionForSearchField();
			}
		});
		this.searchField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {
				SearchListView.this.table.setFocusable(false);
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				SearchListView.this.table.setFocusable(true);
			}
		});
		this.table.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						if (SearchListView.this.table.getRowCount() > 0) {
							if (meta) {
								int rowCount = SearchListView.this.table
										.getRowCount() - 1;
								SearchListView.this.table
										.setRowSelectionInterval(rowCount,
												rowCount);
							} else if (SearchListView.this.table
									.getSelectedRow() == -1) {
								SearchListView.this.table
										.setRowSelectionInterval(0, 0);
							}
						}
						int sel;
						if ((sel = SearchListView.this.table.getSelectedRow()) != -1)
							SearchListView.this.selected = sel;
						break;
					case KeyEvent.VK_UP:
						if (SearchListView.this.table.getRowCount() > 0) {
							if (meta) {
								SearchListView.this.table
										.setRowSelectionInterval(0, 0);
							} else if (SearchListView.this.table
									.getSelectedRow() == -1) {
								int rowCount = SearchListView.this.table
										.getRowCount() - 1;
								SearchListView.this.table
										.setRowSelectionInterval(rowCount,
												rowCount);
							}
						}
						int selPasser;
						if ((selPasser = SearchListView.this.table
								.getSelectedRow()) != -1)
							SearchListView.this.selected = selPasser;
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
				if ((sel = SearchListView.this.table.getSelectedRow()) != -1) {
					SearchListView.this.selected = sel;
					if (e.getClickCount() == 2) {
						SearchListView.this.actionHandlerActionForSearchField();
					}
				}
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
		this.table.getActionMap().put("Enter", new AbstractAction() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				SearchListView.this.actionHandlerActionForSearchField();
			}
		});
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public Component getFirstComponent(Container aContainer) {
				return SearchListView.this.searchField;
			}
		});
	}
	
	protected abstract void actionHandlerActionForSearchField();
	
	@Override
	protected final void doFillTable() {
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
				String sql = "SELECT i.* FROM (SELECT content AS row FROM "
						+ this.getTableName() + "_search WHERE "
						+ this.getTableName() + "_search MATCH ?) "
						+ "LEFT JOIN " + this.getTableName()
						+ " AS i ON row=i.pkey";
				stmt = Session.getDB().prepareStatement(sql);
				stmt.setString(1, search);
			} else {
				String sqlQuery = "SELECT i.* from " + this.getTableName()
						+ " AS i";
				stmt = Session.getDB().prepareStatement(sqlQuery);
			}
			ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt
					.executeQuery());
			this.records = this.mapResults(results);
			this.populateTable();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Error populating list.", true, true);
		}
	}
	
	@Override
	protected final void fillTable() {
		String searchFieldText = this.searchField.getText();
		if (searchFieldText.equals(this.lastSearchFieldValue)) {
			return;
		}
		this.lastSearchFieldValue = searchFieldText;
		this.doFillTable();
	}
	
}

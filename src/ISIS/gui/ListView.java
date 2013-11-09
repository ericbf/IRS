package ISIS.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import ISIS.database.Record;

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
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
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
		});
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ListView.this.actionHandlerActionForSearchField();
			}
		});
		this.table.addKeyListener(new KeyListener() {
			private final int	DOWN	= 40, UP = 38;
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
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
		});
		this.table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int sel;
				if ((sel = ListView.this.table.getSelectedRow()) != -1)
					ListView.this.selected = sel;
			}
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
	
	protected void setTableModel(IRSTableModel model) {
		this.tableModel = model;
		this.table.setModel(model);
		this.table.setFocusable(false);
	}
	
	protected abstract void actionHandlerActionForSearchField();
	
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
	public void save() throws SQLException {
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

package ISIS.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultFocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
			"Toggle Active", "View", "Generate Nonfinal Invoice(s)",
			"Close and Generate Invoice"			};
	
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
		
		this.searchField.addKeyListener(new KeyListener() {
			private final int	BACKSPACE	= 8, DOWN = 40, UP = 38;
			private HintField	searchField	= ListView.this.searchField;
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;
				// System.out.println(e.getKeyText(e.getKeyCode()) + " "
				// + e.getKeyCode() + " " + e.getExtendedKeyCode());
				switch (e.getKeyCode()) {
					case BACKSPACE:
						if (meta) {
							int caretPosition = this.searchField
									.getCaretPosition();
							if (caretPosition < this.searchField.getText()
									.length()) {
								this.searchField.setText(this.searchField
										.getText().substring(caretPosition));
								this.searchField.setCaretPosition(0);
							} else this.searchField.setText("");
						}
						break;
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
						break;
				}
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
						|| (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;
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
						break;
				}
			}
		});
		this.setFocusCycleRoot(true);
		this.setBorder(new EmptyBorder(4, 0, 10, 5));
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
	
	public class HintField extends JTextField {
		private static final long	serialVersionUID	= 1L;
		private boolean				hintShown;
		private boolean				showHint;
		private String				hint;
		
		public HintField() {
			this("");
		}
		
		public HintField(String hint) {
			super(hint);
			this.hint = hint;
			this.hintShown = true;
			this.showHint = true;
			this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");
			this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");
			this.getInputMap().put(
					KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
							java.awt.event.InputEvent.META_DOWN_MASK), "none");
			this.getInputMap().put(
					KeyStroke.getKeyStroke(KeyEvent.VK_UP,
							java.awt.event.InputEvent.META_DOWN_MASK), "none");
			this.addFocusListener(this.new Listener());
		}
		
		public void setShowHint(boolean b) {
			this.showHint = b;
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
				if (HintField.this.hintShown) {
					HintField.this.setText("");
				}
				HintField.this.hintShown = false;
				ListView.this.table.setFocusable(false);
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
				ListView.this.table.setFocusable(true);
			}
		}
	}
}

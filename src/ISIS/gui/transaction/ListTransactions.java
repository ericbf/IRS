package ISIS.gui.transaction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.IRSTableModel;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.transaction.Transaction;

/**
 * List of transactions. Allows you to query and act on transactions.
 */
public class ListTransactions extends ListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	private JButton				viewButton;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Transaction list view.
	 */
	public ListTransactions(SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int buttonNameSel = 3;
		this.viewButton = new JButton(this.buttonNames[buttonNameSel++]);
		JButton generateButton = new JButton(this.buttonNames[buttonNameSel++]);
		
		int x = 0, y = 0;
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.gridy = y;
		this.add(this.viewButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		this.add(generateButton, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x++;
		c.weightx = 1;
		this.add(this.searchField, c);
		
		this.setTableModel(new IRSTableModel() {
			private static final long	serialVersionUID	= 1L;
			
			@Override
			public void addRow(Record record) {
				Transaction transaction = (Transaction) record;
				Object[] array = new Object[this.getColumnCount()];
				
				array[0] = transaction.getPkey();
				array[1] = "";
				array[2] = "";
				
				super.addRow(array);
			}
		});
		this.tableModel.setColumnTitles("customer", "other", "headers", "date",
				"status");
		this.fillTable();
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		this.add(new JScrollPane(this.table), c);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#actionHandlerActionForSearchField()
	 */
	@Override
	protected void actionHandlerActionForSearchField() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#tableName()
	 */
	@Override
	protected String getTableName() {
		return "transaction";
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#hasDates()
	 */
	@Override
	protected boolean hasDates() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#mapResults(java.util.ArrayList)
	 */
	@Override
	protected ArrayList<Transaction> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		// TODO Auto-generated method stub
		return null;
	}
}

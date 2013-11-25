package ISIS.gui.transaction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.IRSTableModel;
import ISIS.gui.SearchListView;
import ISIS.gui.SplitPane;
import ISIS.gui.SplitPane.LayoutType;
import ISIS.gui.customer.AddEditTransaction;
import ISIS.gui.report.ReportView;
import ISIS.reports.Invoice;
import ISIS.transaction.Transaction;

/**
 * List of transactions. Allows you to query and act on transactions.
 */
public class SearchListTransactions extends SearchListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	private JButton				viewButton;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Transaction list view.
	 */
	public SearchListTransactions(SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		
		int buttonNameSel = 3;
		this.viewButton = new JButton(this.buttonNames[buttonNameSel++]);
		this.viewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected;
				if ((selected = SearchListTransactions.this.table
						.getSelectedRow()) != -1) {
					try {
						SearchListTransactions.this.splitPane.push(
								new AddEditTransaction(
										SearchListTransactions.this.splitPane,
										SearchListTransactions.this.keys
												.get(selected)),
								LayoutType.HORIZONTAL,
								SearchListTransactions.this);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		JButton generateButton = new JButton(this.buttonNames[buttonNameSel++]);
		generateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selected;
				if ((selected = SearchListTransactions.this.table
						.getSelectedRow()) != -1) {
					try {
						SearchListTransactions.this.splitPane.push(
								new ReportView(new Invoice(new Transaction(
										SearchListTransactions.this.keys
												.get(selected), true)),
										SearchListTransactions.this.splitPane),
								LayoutType.HORIZONTAL,
								SearchListTransactions.this);
					} catch (SQLException e1) {
						ErrorLogger.error("Could not fetch transaction", false,
								true);
						e1.printStackTrace();
					}
				}
			}
		});
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
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridy = ++y;
		c.gridwidth = x;
		c.gridx = x = 0;
		c.weighty = 1;
		this.add(new JScrollPane(this.table), c);
		
		// this.fillTable();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.SearchListView#actionHandlerActionForSearchField()
	 */
	@Override
	protected void tableItemAction() {
		this.viewButton.doClick();
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.SearchListView#tableName()
	 */
	@Override
	protected DB.TableName getTableName() {
		return DB.TableName.transaction_;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.SearchListView#mapResults(java.util.ArrayList)
	 */
	@Override
	protected ArrayList<Transaction> mapResults(
			ArrayList<HashMap<String, Field>> results) {
		// TODO Auto-generated method stub
		return new ArrayList<Transaction>();
	}
}

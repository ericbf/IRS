package ISIS.gui.transaction;

import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.transaction.Transaction;

/**
 * List of transactions. Allows you to query and act on transactions.
 */
public class ListTransactions extends ListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Transaction list view.
	 */
	public ListTransactions(SplitPane splitPane) {
		super(splitPane);
	}
	
	@Override
	protected void fillTable() {
		throw new UnsupportedOperationException("Not supported yet.");
		// To change body of generated methods, choose Tools | Templates.
	}
}

package ISIS.gui.customer;

import java.sql.SQLException;

import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.transaction.Transaction;

/**
 * List of transactions. Allows you to query and act on transactions.
 */
<<<<<<< HEAD
public class ListTransactions extends ListView<Transaction> {
	private static final long	serialVersionUID	= 1L;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Transaction list view.
	 */
	public ListTransactions(SplitPane splitPane) {
		super(splitPane);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.ListView#fillTable()
	 */
	@Override
	protected void fillTable() throws SQLException {
		// TODO Auto-generated method stub
		
	}
=======
public class ListTransactions extends ListView {

    private static final long serialVersionUID = 1L;

    /* Fields omitted */
    /**
     * Constructs new Transaction list view.
     */
    public ListTransactions(SplitPane splitPane) {
	super(splitPane);
    }

    @Override
    protected void fillTable() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
>>>>>>> e47a2962ff19f452b557d22f013fa2587089daa4
}

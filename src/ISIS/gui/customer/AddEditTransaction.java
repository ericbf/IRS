package ISIS.gui.customer;

import ISIS.database.Record;
import ISIS.gui.SplitPane;
import ISIS.gui.View;

/**
 * View for adding a transaction.
 */
public class AddEditTransaction extends View {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Public constructor: returns an instance of the add/edit transaction view.
	 */
	public AddEditTransaction(SplitPane splitPane) {
		super(splitPane);
	}
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 */
	@Override
	public Record getCurrentRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getTemporaryRecord()
	 */
	@Override
	public Record getTemporaryRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * The transaction needs to be saved.
	 */
	@Override
	public boolean needsSave() {
		return true;
	}
	
	/**
	 * This saves the transaction.
	 */
	@Override
	public void save() {}
}

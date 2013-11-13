package ISIS.gui.customer;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.SplitPane;
import ISIS.transaction.Transaction;

/**
 * View for adding a transaction.
 */
public class AddEditTransaction extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	Transaction					transaction;
	
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
		return this.transaction;
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
}

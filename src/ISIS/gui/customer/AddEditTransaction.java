package ISIS.gui.customer;

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
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
}

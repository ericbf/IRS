package ISIS.gui.customer;

import ISIS.gui.View;

/**
 * View for adding a transaction.
 */
public class AddEditTransaction extends View {

	/**
	 * Public constructor: returns an instance of the add/edit transaction view.
	 */
	public AddEditTransaction() {
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
	public void save() {
	}

	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {
	}
}

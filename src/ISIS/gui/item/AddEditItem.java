package ISIS.gui.item;

import ISIS.gui.View;

/**
 * View for adding and editing items.
 */
public class AddEditItem extends View {
	
	/**
	 * Public constructor: returns new instance of add/edit item view.
	 */
	public AddEditItem() {}
	
	/**
	 * This view needs to be saved.
	 */
	@Override
	public boolean needsSave() {
		return true;
	}
	
	/**
	 * Saves the item.
	 */
	@Override
	public void save() {}
	
	/**
	 * Discards any modifications.
	 */
	@Override
	public void cancel() {}
}

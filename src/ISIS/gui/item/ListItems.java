package ISIS.gui.item;

import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

/**
 * List of items. Allows you to query and act on items.
 */
public class ListItems extends ListView<Item> {
	
	private static final long	serialVersionUID	= 1L;
	
	/* Fields omitted */
	/**
	 * Constructs new Customer list view.
	 */
	public ListItems(SplitPane splitPane) {
		super(splitPane);
	}
	
	@Override
	protected void fillTable() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}

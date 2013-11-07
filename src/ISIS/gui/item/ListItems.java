package ISIS.gui.item;

import ISIS.gui.ListView;
import ISIS.gui.SplitPane;

/**
 * List of items. Allows you to query and act on items.
 */
public class ListItems extends ListView {

    private static final long serialVersionUID = 1L;

    /* Fields omitted */
    /**
     * Constructs new Customer list view.
     */
    public ListItems(SplitPane splitPane) {
	super(splitPane);
    }

    @Override
    protected void fillTable() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

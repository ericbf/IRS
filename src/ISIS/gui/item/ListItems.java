package ISIS.gui.item;

import java.sql.SQLException;

import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

/**
 * List of items. Allows you to query and act on items.
 */
<<<<<<< HEAD
public class ListItems extends ListView<Item> {
	private static final long	serialVersionUID	= 1L;
	
	/* Fields omitted */
	
	/**
	 * Constructs new Customer list view.
	 */
	public ListItems(SplitPane splitPane) {
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
>>>>>>> e47a2962ff19f452b557d22f013fa2587089daa4
}

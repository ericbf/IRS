package ISIS.gui;

import javax.swing.AbstractAction;

public abstract class TableUpdateListener extends AbstractAction {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * @pre - none, constructor
	 * @post - TableUpdateListener instantiated
	 */
	public TableUpdateListener() {
		super();
	}
	
	/**
	 * @pre - object received
	 * @post - key for object received is returned
	 */
	protected int getPkey() {
		return ((Integer) this.getValue("pkey"));
	}
	
	/**
	 * @pre - int received
	 * @post - pkey for object set to int
	 */
	public void setKey(Integer key) {
		this.putValue("pkey", key);
	}
}

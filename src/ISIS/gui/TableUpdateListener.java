package ISIS.gui;

import javax.swing.AbstractAction;

public abstract class TableUpdateListener extends AbstractAction {
	private static final long	serialVersionUID	= 1L;
	
	public TableUpdateListener() {
		super();
	}
	
	protected int getPkey() {
		return ((Integer) this.getValue("pkey"));
	}
	
	public void setKey(Integer key) {
		this.putValue("pkey", key);
	}
}

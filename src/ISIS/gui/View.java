package ISIS.gui;

import java.sql.SQLException;

import javax.swing.JPanel;

/**
 * Abstract class for all views.
 */
public abstract class View extends JPanel {
	private static final long	serialVersionUID	= 1L;
	protected SplitPane			splitPane;
	
	/**
	 * Base constructor for views not in a split pane.
	 */
	public View() {
		this(null);
	}
	
	/**
	 * Base constructor.
	 */
	public View(SplitPane splitPane) {
		this.splitPane = splitPane;
		this.setOpaque(false);
	}
	
	/**
	 * Returns whether or not this view is in a split pane.
	 * 
	 * @return
	 */
	protected final boolean inSplitPane() {
		if (this.splitPane != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the split pane in which this view is contained.
	 * 
	 * @return
	 */
	protected final SplitPane getSplitPane() {
		if (!this.inSplitPane()) {
			throw new UnsupportedOperationException("Not supported.");
		}
		return this.splitPane;
	}
	
	/**
	 * Returns whether this view needs to be saved. This method must be
	 * implemented.
	 */
	public abstract boolean needsSave();
	
	/**
	 * A method for saving the contents of the implemented view. This method
	 * must be implemented.
	 */
	public abstract void save() throws SQLException;
	
	/**
	 * A method for canceling the implemented view. This method must be
	 * implemented.
	 */
	public abstract void cancel();
	
	/**
	 * Overridden for windows where cleanup is necessary, but save and cancel do
	 * not apply.
	 */
	public void close() throws CloseCanceledException {}
}

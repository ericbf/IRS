package ISIS.gui;

import javax.swing.JPanel;

/**
 * Abstract class for all views.
 */
public abstract class View extends JPanel {
	
	private SplitPane	splitPane;
	
	public View() {
		splitPane = null;
	}
	
	/**
	 * Base constructor.
	 */
	public View(SplitPane splitPane) {
		this.splitPane = splitPane;
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
		if (!inSplitPane()) {
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
	public abstract void save();
	
	/**
	 * A method for canceling the implemented view. This method must be
	 * implemented.
	 */
	public abstract void cancel();
	
	/**
	 * Overridden for windows where cleanup is necessary, but save and cancel do
	 * not apply.
	 */
	public void close() {}
}

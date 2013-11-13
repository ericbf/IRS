package ISIS.gui;

import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ISIS.database.Record;

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
		this.setBorder(new EmptyBorder(4, 0, 10, 5));
	}
	
	/**
	 * A method for canceling the implemented view. This method must be
	 * implemented.
	 */
	public abstract void cancel();
	
	/**
	 * Overridden for windows where cleanup is necessary, but save and cancel do
	 * not apply.
	 */
	public void close() throws CloseCanceledException {
		if (this.needsSave()
				&& (this.getCurrentRecord() == null
						&& this.getTemporaryRecord() == null || this
						.getCurrentRecord() != null
						&& this.getTemporaryRecord() != null
						&& this.getCurrentRecord().isChanged(
								this.getTemporaryRecord()))) {
			if ((new ConfirmCloseDialog().show(this.splitPane))) {
				try {
					this.save();
				} catch (SQLException e) {
					ErrorLogger.error(e, "Failed to save. Canceling close.",
							true, true);
					throw new CloseCanceledException();
				}
			} else {
				// do nothing
			}
		}
	}
	
	/**
	 * Return the record for this view if it has one, or null if it doesn't have
	 * one.
	 * 
	 * @return
	 */
	public abstract Record getCurrentRecord();
	
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
	 * Return a version of the record that has been updated as per the changes
	 * physically made in the view. If this view doesn't own a record, return
	 * null.
	 * 
	 * @return
	 */
	public abstract Record getTemporaryRecord();
	
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
	 * Returns whether this view needs to be saved. This method must be
	 * implemented.
	 */
	public abstract boolean needsSave();
	
	/**
	 * A method for saving the contents of the implemented view. This method
	 * must be implemented.
	 */
	public abstract void save() throws SQLException;
}

/**
 * 
 */
package ISIS.gui;

import java.sql.SQLException;

/**
 * @author eric
 */
public abstract class AddEditView extends View {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Link to super
	 */
	public AddEditView(SplitPane splitPane) {
		super(splitPane);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#needsSave()
	 */
	@Override
	public final boolean needsSave() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#save()
	 */
	@Override
	public final void save() throws SQLException {
        try {
		    this.getCurrentRecord().save();
        } catch (BadInputException e) {
            ErrorLogger.error(e, "", false, true);
        }
	}
}

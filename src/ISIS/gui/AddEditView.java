/**
 * 
 */
package ISIS.gui;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.border.EmptyBorder;

import ISIS.session.Session;

/**
 * @author eric
 */
public abstract class AddEditView extends View {
	
	private static final long	serialVersionUID	= 1L;
	protected boolean			wasSavedOrAlreadySetUp;
	
	/**
	 * Link to super
	 * 
	 * @pre - none
	 * @post - new splitPane created
	 */
	public AddEditView(SplitPane splitPane) {
		super(splitPane);
		this.setBorder(new EmptyBorder(4, 0, 10, 5));
	}
	
	/**
	 * Used to disable any unchangeable fields
	 * 
	 * @param fields
	 * @pre - 1 or more HintFields received
	 * @post - unchangeable fields are disabled
	 */
	protected final void disableFields(HintField... fields) {
		this.wasSavedOrAlreadySetUp = true;
		for (HintField field : fields) {
			field.setEditable(false);
			field.setForeground(Color.gray);
		}
	}
	
	/**
	 * Use this to activate the necessary fields after the first save. Override
	 * to give action.
	 * 
	 * @pre - none
	 * @post - none - abstract
	 */
	protected void doSaveRecordAction() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#needsSave()
	 * @pre - none
	 * @post - none - abstract for override
	 */
	@Override
	public final boolean needsSave() {
		return true;
	}
	
	protected void postSave() throws SQLException {}
	
	/**
	 * Override if you have actions that should be taken before saving.
	 */
	protected void preSave() throws SQLException {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#save()
	 * @pre - none
	 * @post - none - abstract
	 */
	@Override
	public final void save() throws SQLException {
		try {
			Session.getDB().startTransaction();
			this.preSave();
			if (this.isAnyFieldDifferentFromDefault()) {
				if (this.getCurrentRecord() == null) {
					ErrorLogger.error("Nothing to save!?", true, false);
					Session.getDB().rollbackTransaction();
					return;
				}
				this.getCurrentRecord().save();
			}
			if (!this.wasSavedOrAlreadySetUp) {
				this.doSaveRecordAction();
				this.wasSavedOrAlreadySetUp = true;
			}
			this.postSave();
			Session.getDB().closeTransaction();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to save...", true, false);
			Session.getDB().rollbackTransaction();
		}
	}
}

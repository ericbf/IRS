package ISIS.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ConfirmCloseDialog {
	public ConfirmCloseDialog() {}
	
	/**
	 * Returns true if the thing should be saved.
	 * 
	 * @param parent
	 * @return
	 * @throws CloseCanceledException
	 * @pre - button was clicked
	 * @post - returns true if save should be performed
	 */
	public boolean show(Component parent) throws CloseCanceledException {
		int result = JOptionPane.showConfirmDialog(parent,
				"Would you like to save?");
		if (result == JOptionPane.YES_OPTION) {
			return true;
		} else if (result == JOptionPane.NO_OPTION) {
			return false;
		} else if (result == JOptionPane.CANCEL_OPTION) {
			throw new CloseCanceledException();
		} else {
			throw new RuntimeException("Save dialog screwed up.");
		}
	}
}

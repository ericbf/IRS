package ISIS.gui;

import javax.swing.*;

public class ConfirmCloseDialog {
    public ConfirmCloseDialog () {
    }

    /**
     * Returns true if the thing should be saved.
     */
    public boolean show() throws CloseCanceledException {
        int result = JOptionPane.showConfirmDialog(null, "Would you like to save?");
        if (result == JOptionPane.YES_OPTION) {
            return true;
        } else if(result == JOptionPane.NO_OPTION) {
            return false;
        } else if(result == JOptionPane.CANCEL_OPTION) {
            throw new CloseCanceledException();
        } else {
            throw new RuntimeException("Save dialog screwed up.");
        }
    }
}

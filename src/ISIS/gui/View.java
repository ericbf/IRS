package ISIS.gui;

import javax.swing.JPanel;

/**
 * Abstract class for all views.
 */
public abstract class View extends JPanel {

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
    public void close() {
    }
}

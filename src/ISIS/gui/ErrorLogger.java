package ISIS.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 */
public class ErrorLogger {

    public static void error(String error, boolean severe, boolean show) {
	new ErrorLogger(error, severe, show);
    }

    private ErrorLogger(String error, boolean severe, boolean show) {
	if (show) {
	    JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
	log(error, severe ? Level.SEVERE : Level.INFO);
    }

    private static void log(String message, Level level) {
	Logger.getLogger("ISIS").log(level, message);
    }
}

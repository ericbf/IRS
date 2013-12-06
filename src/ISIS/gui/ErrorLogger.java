package ISIS.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * 
 */
public class ErrorLogger {
	
	private static boolean	debug	= false;
	
	/**
	 * @pre - exception object received
	 * @post - error message logged
	 */
	public static void error(Exception e, String error, boolean severe,
			boolean show) {
		if (debug) {
			StringWriter excBuffer = new StringWriter();
			e.printStackTrace(new PrintWriter(excBuffer));
			e.printStackTrace();
			new ErrorLogger(error + "\n" + e.getMessage() + "\nStack trace:\n"
					+ excBuffer.toString(), severe, show);
		} else {
			new ErrorLogger(error + "\n" + e.getMessage(), severe, show);
		}
	}
	
	/**
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	public static void error(String error, boolean severe, boolean show) {
		new ErrorLogger(error, severe, show);
	}
	
	/**
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	private static void log(String message, Level level) {
		Logger.getLogger("ISIS").log(level, message);
	}
	
	/**
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	private ErrorLogger(String error, boolean severe, boolean show) {
		if (show) {
			JOptionPane.showMessageDialog(null, error, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		log(error, severe ? Level.SEVERE : Level.INFO);
	}
}

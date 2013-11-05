package ISIS.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 */
public class ErrorLogger {

    private static boolean debug = true;

    public static void error(String error, boolean severe, boolean show) {
        new ErrorLogger(error, severe, show);
    }

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

    private ErrorLogger(String error, boolean severe, boolean show) {
        if (show) {
            JOptionPane.showMessageDialog(null, error, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        log(error, severe ? Level.SEVERE : Level.INFO);
    }

    private static void log(String message, Level level) {
        Logger.getLogger("ISIS").log(level, message);
    }
}

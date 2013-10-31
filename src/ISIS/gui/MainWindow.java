package ISIS.gui;

import ISIS.session.Session;
import javax.swing.JFrame;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {

    public static void main(String args[]) {
	System.out.println("Hello world");
	Session.getDB();
    }

    public MainWindow() {
    }
}

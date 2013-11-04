package ISIS.gui;

import ISIS.database.RecordNotFoundException;
import ISIS.gui.customer.ListCustomers;
import ISIS.gui.item.ListItems;
import ISIS.session.Session;
import ISIS.user.AuthenticationException;
import ISIS.user.User;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {

    private SplitPane inventoryPane;
    private SplitPane customerPane;
    private SplitPane reportPane;

    public static void main(String args[]) {

	Session.getDB(); // opens the db

	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		MainWindow frame = new MainWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setMinimumSize(new Dimension(400, 400));
		frame.setVisible(true);
	    }
	});

	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	    @Override
	    public void run() {
		Session.getDB().close();
	    }
	}, "Shutdown-thread"));
	try {
	    Session.baseSession();
	} catch (SQLException ex) {
	    System.out.println("well shit");
	}

	try {
	    if (!User.userExists("jdickhead")) {
		User testUser = new User("jdickhead", true, "penismonger", "Janet", "Dickhead", "This is a note.");
		testUser.save();
		Session.endCurrentSession();
		System.out.println("OK!");
		User janet = new User(2, true);
		System.out.println("Got janet.");
		System.out.println("Active: " + janet.getActive());
		System.out.println("Password correct: " + janet.checkPassword("penismonger"));
		System.out.println("Password incorrect: " + !janet.checkPassword("penismongerz"));
		System.out.println("ID: " + janet.getEmployeeID());
		System.out.println("Fname: " + janet.getFirstName() + "\nLname: " + janet.getLastName());
		System.out.println("Note: " + janet.getNote());
		System.out.println("Date: " + janet.getDates().getCreatedDate());
		janet.setNote("This is a new note.");
		System.out.println("Set note to: " + janet.getNote());
		janet.save();
		System.out.println("Starting session with janet..");
	    } else {
		System.out.println("Janet already exists");
	    }
	    try {
		Session.startNewSession("jdickhead", "penismonger");
	    } catch (AuthenticationException e) {
		System.err.println("failed");
	    }
	    try {
		User testUser = new User(2, true); // if you change the pkey, it won't be able to find janet
		System.out.println("Note: " + testUser.getNote());
	    } catch (RecordNotFoundException e) {
		ErrorLogger.error("Could not find janet.", true, true);
	    }

	} catch (SQLException | RecordNotFoundException ex) {
	    ErrorLogger.error(ex, "something went wrong lel", true, true);
	}
    }

    public MainWindow() {
	super("IRS");
	inventoryPane = new SplitPane();
	inventoryPane.push(new ListItems(inventoryPane), SplitPane.LayoutType.HORIZONTAL);
	customerPane = new SplitPane();
	customerPane.push(new ListCustomers(inventoryPane), SplitPane.LayoutType.HORIZONTAL);
//	reportPane = new SplitPane();
//	reportPane.push(new ListItems(), SplitPane.LayoutType.HORIZONTAL);
	JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
	tabs.add("Inventory", inventoryPane);
	tabs.add("Customers", customerPane);
	this.setContentPane(tabs);
    }
}

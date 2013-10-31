package ISIS.gui;

import ISIS.database.RecordNotFoundException;
import ISIS.gui.customer.ListCustomers;
import ISIS.gui.item.ListItems;
import ISIS.session.Session;
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
            User testUser = new User("jdickhead", true, "penismonger", "Janet", "Dickhead", "This is a note.");
            testUser.save();
            User janet = new User(1, true);
            System.out.println("Got janet.");
            System.out.println("Active: " + janet.getActive());
            System.out.println("Password correct: " + janet.checkPassword("penismonger"));
            System.out.println("Password incorrect: " + janet.checkPassword("penismonger"));
            System.out.println("ID: " + janet.getEmployeeID());
            System.out.println("Fname: " + janet.getFirstName() + "Lname: " + janet.getLastName());
            System.out.println("Note: " + janet.getNote());
            
            janet.setNote("This is a new note.");
            System.out.println("Set note to: " + janet.getNote());
            janet.save();
            testUser = new User(1, true);
            System.out.println("Note: " + testUser.getNote());
           
        } catch (SQLException | RecordNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
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

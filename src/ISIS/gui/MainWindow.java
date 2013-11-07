package ISIS.gui;

import ISIS.database.RecordNotFoundException;
import ISIS.gui.customer.ListCustomers;
import ISIS.gui.item.ListItems;
import ISIS.gui.transaction.ListTransactions;
import ISIS.session.Session;
import ISIS.user.AuthenticationException;
import ISIS.user.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private SplitPane inventoryPane;
    private SplitPane customerPane;
    private SplitPane transactionPane;

    // private SplitPane reportPane;

    public MainWindow() {
        super("IRS");
        this.inventoryPane = new SplitPane();
        this.inventoryPane.push(new ListItems(this.inventoryPane), SplitPane.LayoutType.HORIZONTAL);

        this.customerPane = new SplitPane();
        this.customerPane.push(new ListCustomers(this.inventoryPane), SplitPane.LayoutType.HORIZONTAL);

        this.transactionPane = new SplitPane();
        this.transactionPane.push(new ListTransactions(this.transactionPane), SplitPane.LayoutType.HORIZONTAL);

        // this.reportPane = new SplitPane();
        // this.reportPane.push(new ReportSelectorView(this.reportPane),
        // SplitPane.LayoutType.HORIZONTAL);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.add("Customers", this.customerPane);
        tabs.add("Inventory", this.inventoryPane);
        tabs.add("Transactions", this.transactionPane);
        this.setContentPane(tabs);
    }

    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            ErrorLogger.error(e, "Couldn't set look and feel.", false, false);
        }

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
            System.out.println("well darn");
        }

        try {
            if (!User.userExists("jdickhead")) {
                Session.getDB().sampleData();
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
                User testUser = new User(2, true); // if you change the pkey, it
                // won't be able to find
                // janet
                System.out.println("Note: " + testUser.getNote());
            } catch (RecordNotFoundException e) {
                ErrorLogger.error("Could not find janet.", true, true);
            }

        } catch (SQLException | RecordNotFoundException ex) {
            ErrorLogger.error(ex, "something went wrong lel", true, true);
        }
    }
}

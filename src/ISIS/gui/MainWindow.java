package ISIS.gui;

import ISIS.customer.Customer;
import ISIS.database.RecordNotFoundException;
import ISIS.gui.customer.ListCustomers;
import ISIS.gui.item.ListItems;
import ISIS.gui.transaction.ListTransactions;
import ISIS.item.Item;
import ISIS.misc.Address;
import ISIS.misc.Phone;
import ISIS.session.Session;
import ISIS.user.AuthenticationException;
import ISIS.user.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {
	
	private static final long	serialVersionUID	= 1L;
	
	public static void main(String args[]) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			ErrorLogger.error(e, "Couldn't set look and feel.", false, false);
		}
		
		Session.getDB(); // opens the db
		
		try {
			Session.baseSession();
		} catch (SQLException ex) {
			System.out.println("well darn");
		}
		
		try {
			if (!User.userExists("jmichhead")) {
				sampledata();
				User testUser = new User("jmichhead", true, "sammichmonger",
						"Janet", "michhead", "This is a note.");
				testUser.save();
				Session.endCurrentSession();
				System.out.println("OK!");
				User janet = new User(2, true);
				System.out.println("Got janet.");
				System.out.println("Active: " + janet.getActive());
				System.out.println("Password correct: "
						+ janet.checkPassword("sammichmonger"));
				System.out.println("Password incorrect: "
						+ !janet.checkPassword("sammichmongerz"));
				System.out.println("ID: " + janet.getEmployeeID());
				System.out.println("Fname: " + janet.getFirstName()
						+ "\nLname: " + janet.getLastName());
				System.out.println("Note: " + janet.getNote());
				System.out
						.println("Date: " + janet.getDates().getCreatedDate());
				janet.setNote("This is a new note.");
				System.out.println("Set note to: " + janet.getNote());
				janet.save();
				System.out.println("Starting session with janet..");
			} else {
				System.out.println("Janet already exists");
			}
			try {
				Session.startNewSession("jmichhead", "sammichmonger");
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
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow frame = new MainWindow();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setMinimumSize(new Dimension(800, 400));
				frame.setVisible(true);
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				Session.getDB().close();
			}
		}, "Shutdown-thread"));
	}
	
	private static void sampledata() throws SQLException {
		Customer customer = new Customer("Joe", "Doe", "sammich@sammich.info",
				"This is a note.", "this is a password?", true);
		customer.addPhoneNum(new Phone("404040404", false, Phone.PhoneType.HOME));
		customer.addPhoneNum(new Phone("987654321", false, Phone.PhoneType.HOME));
		customer.addPhoneNum(new Phone("123456789", true, Phone.PhoneType.HOME));
		customer.save();
		customer = new Customer("Sammich", "Bob", "whuh@what.com",
				"This is a note.", "this is a password?", true);
		Phone asdf = new Phone("301231213", true, Phone.PhoneType.HOME);
		customer.addPhoneNum(asdf);
		customer.save();
		customer.removePhoneNum(asdf);
		customer.save();
		customer = new Customer("Jizzle", "Dizzle", "cookies@gmail.com",
				"This is a note.", "this is a password?", true);
		customer.addPhoneNum(new Phone("56565656", true, Phone.PhoneType.HOME));
		customer.addAddress(new Address(true, true, "mars", "aliens", "9001",
				"state", "city", "county", "this is pretty unique huh"));
		customer.save();
		Item item = new Item("new item", "12345", "sammiches", new BigDecimal(
				1234.5), new BigDecimal(1234.5), new BigDecimal(1234.5), "LBS",
				new BigDecimal(1234.5), true);
		item.save();
		item = new Item("new itemsessesesse", "54321", "nope", new BigDecimal(
				1234.5), new BigDecimal(1234.5), new BigDecimal(1234.5), "LBS",
				new BigDecimal(1234.5), true);
		item.save();
	}
	
	private SplitPane	inventoryPane;
	
	// private SplitPane reportPane;
	
	private SplitPane	customerPane;
	
	private SplitPane	transactionPane;
	
	public MainWindow() {
		super("IRS");
		this.inventoryPane = new SplitPane();
		this.inventoryPane.push(new ListItems(this.inventoryPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		// this.inventoryPane.push(new ListItems(this.inventoryPane),
		// SplitPane.LayoutType.HORIZONTAL);
		
		this.customerPane = new SplitPane();
		this.customerPane.push(new ListCustomers(this.customerPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		// this.customerPane.push(new ListCustomers(this.customerPane),
		// SplitPane.LayoutType.HORIZONTAL);
		
		this.transactionPane = new SplitPane();
		this.transactionPane.push(new ListTransactions(this.transactionPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		// this.transactionPane.push(new ListTransactions(this.transactionPane),
		// SplitPane.LayoutType.HORIZONTAL);
		
		// this.reportPane = new SplitPane();
		// this.reportPane.push(new ReportSelectorView(this.reportPane),
		// SplitPane.LayoutType.HORIZONTAL);
		
		// new BorderLayout()
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
		tabs.setFocusable(false);
		tabs.add("Customers", this.customerPane);
		tabs.add("Inventory", this.inventoryPane);
		tabs.add("Transactions", this.transactionPane);
		this.setContentPane(tabs);
	}
}

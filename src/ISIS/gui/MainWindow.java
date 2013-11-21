package ISIS.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ISIS.customer.Customer;
import ISIS.database.RecordNotFoundException;
import ISIS.gui.customer.SearchListCustomers;
import ISIS.gui.item.SearchListItems;
import ISIS.gui.report.ReportSelectorView;
import ISIS.gui.transaction.SearchListTransactions;
import ISIS.item.Item;
import ISIS.misc.Address;
import ISIS.misc.Phone;
import ISIS.session.Session;
import ISIS.user.AuthenticationException;
import ISIS.user.User;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {
	
	private static final long	serialVersionUID	= 1L;
	
	public static void main(String args[]) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			ErrorLogger.error(e, "Couldn't set look and feel.", false, false);
		} catch (InstantiationException e) {
			ErrorLogger.error(e, "Couldn't set look and feel.", false, false);
		} catch (IllegalAccessException e) {
			ErrorLogger.error(e, "Couldn't set look and feel.", false, false);
		} catch (UnsupportedLookAndFeelException e) {
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
			
		} catch (SQLException ex) {
			ErrorLogger.error(ex, "something went wrong lel", true, true);
		} catch (RecordNotFoundException ex) {
			ErrorLogger.error(ex, "something went wrong lel", true, true);
		}
		final JDialog splash = new JDialog();
		
		URL loading_URL = MainWindow.class.getClassLoader().getResource(
				"ISIS/misc/Loading.gif");
		Splash splashPanel = new Splash(loading_URL);
		splash.setContentPane(splashPanel);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		splash.setSize((splashPanel.image.getWidth(splashPanel)),
				(splashPanel.image.getHeight(splashPanel)));
		splash.setLocation(
				(int) (dimension.getWidth() - splash.getWidth()) / 2,
				(int) (dimension.getHeight() - splash.getHeight()) / 2);
		splash.setUndecorated(true);
		splash.setAlwaysOnTop(true);
		splash.setVisible(true);
		Timer dispose = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				splash.setVisible(false);
				splash.dispose();
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
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
					}
				});
			}
		});
		dispose.setRepeats(false);
		dispose.start();
		
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
				"This is a note.", "this is a password?", false);
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
	
	private SplitPane	reportPane;
	
	private SplitPane	customerPane;
	
	private SplitPane	transactionPane;
	
	public MainWindow() {
		super("IRS");
		this.inventoryPane = new SplitPane();
		this.inventoryPane.push(new SearchListItems(this.inventoryPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		
		this.customerPane = new SplitPane();
		this.customerPane.push(new SearchListCustomers(this.customerPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		
		this.transactionPane = new SplitPane();
		this.transactionPane.push(new SearchListTransactions(
				this.transactionPane), SplitPane.LayoutType.HORIZONTAL, null);
		
		this.reportPane = new SplitPane();
		this.reportPane.push(new ReportSelectorView(this.reportPane),
				SplitPane.LayoutType.HORIZONTAL, null);
		
		JTabbedPane tabs = new JTabbedPane(SwingConstants.LEFT);
		tabs.setFocusable(false);
		tabs.add("Customers", this.customerPane);
		tabs.add("Inventory", this.inventoryPane);
		tabs.add("Transactions", this.transactionPane);
		tabs.add("Reports", this.reportPane);
		this.setContentPane(tabs);
	}
}

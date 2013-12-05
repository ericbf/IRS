package ISIS.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.WindowConstants;

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
			ErrorLogger.error(ex, "well darn.", true, true);
			System.exit(1);
		}
		
		try {
			if (!User.userExists("user")) {
				sampledata();
				User testUser = new User("user", true, "password", "Janet",
						"michhead", "This is a note.");
				testUser.save();
				Session.endCurrentSession();
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
		splash.setSize((splashPanel.image.getWidth(splashPanel) / 2),
				(splashPanel.image.getHeight(splashPanel) / 2));
		splash.setUndecorated(true);
		splash.setAlwaysOnTop(true);
		splash.setLocationRelativeTo(null);
		splash.setVisible(true);
		Timer dispose = new Timer(1500, new ActionListener() {
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
								// Login frame =
								new Login();
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
	
	/*
	 * @pre - Need Sample Data populated
	 * @post - sample data was populated
	 */
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
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent e) {}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					MainWindow.this.customerPane.popAllButFirst();
					MainWindow.this.inventoryPane.popAllButFirst();
					MainWindow.this.transactionPane.popAllButFirst();
					System.exit(0);
				} catch (CloseCanceledException e1) {}
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
	}
}

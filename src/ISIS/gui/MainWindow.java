package ISIS.gui;

import ISIS.gui.customer.ListCustomers;
import ISIS.gui.item.ListItems;
import ISIS.session.Session;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Class for main window. No public methods available.
 */
public class MainWindow extends JFrame {

    private SplitPane inventoryPane;
    private SplitPane customerPane;
    private SplitPane reportPane;

    public static void main(String args[]) {

	Session.getDB(); // opens the db
	MainWindow frame = new MainWindow();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setMinimumSize(new Dimension(400, 400));
	frame.setVisible(true);
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

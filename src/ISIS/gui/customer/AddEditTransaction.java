package ISIS.gui.customer;

import ISIS.customer.Customer;
import ISIS.gui.*;
import ISIS.gui.simplelists.ListAddress;
import ISIS.transaction.Transaction;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * View for adding and editing customers.
 */
public class AddEditTransaction extends AddEditView {
    private static final long	serialVersionUID	= 1L;
    JCheckBox					returnTransaction;
    final JComboBox<String>     status = new JComboBox<>(new String[]{"Status1", "status2"});
    HintField					address, billing, total;
    Transaction                 transaction;
    Customer                    customer;
    JPanel						otherListsContainer;
    CardLayout					otherListsCardLayout;
    JToggleButton				address_select, billing_select, items;
    ArrayList<JToggleButton>	cardLayoutViewButtons;
    static double				dividerRatio		= 0;

    /**
     * Public constructor: returns new instance of add/edit customer view.
     */
    public AddEditTransaction(SplitPane splitPane, Customer customer) {
        super(splitPane);
        this.populateElements();

        this.customer = customer;
        status.setSelectedItem("status2");
    }

    /**
     * Public constructor: returns new instance of add/edit customer view.
     *
     * @wbp.parser.constructor
     */
    public AddEditTransaction(SplitPane splitPane, int pkey) throws SQLException {
        super(splitPane);
        this.transaction = new Transaction(pkey, true);
        this.populateElements();

        try {
            this.customer = this.transaction.getCustomer();
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch customer.", e);
        }
        try {
            this.returnTransaction.setSelected(this.transaction.getParentTransaction() != null);
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch parent transaction.", e);
        }

        status.setSelectedItem("status2");
    }

    /**
     * Discards any modifications.
     */
    @Override
    public void cancel() {}

    /*
     * (non-Javadoc)
     * @see ISIS.gui.AddEditView#newWasSaved()
     */
    @Override
    protected void doSaveRecordAction() {
        if (this.transaction != null && !this.wasSavedOrAlreadySetUp) {
            this.wasSavedOrAlreadySetUp = true;

            @SuppressWarnings("rawtypes")
            SimpleListView l;

            // Add the other lists to the JPanel and register with the layout
            this.otherListsContainer.add(l = new ListAddress(this.splitPane,
                                                             this, this.customer, this.customer.getPkey(), true));
            this.otherListsCardLayout.addLayoutComponent(l, "Address");
            // next
//            this.otherListsContainer.add(l = new ListBill(
//                    this.splitPane, this, this.customer.getPkey(), false));
//            this.otherListsCardLayout.addLayoutComponent(l, "Billing");
            // next
//            this.otherListsContainer.add(l = new SearchListItems(this.splitPane,
//                                                           this, this.customer.getPkey(), false));
//            this.otherListsCardLayout.addLayoutComponent(l, "Items");

            // Add action listeners to the buttons
            this.address_select.addActionListener(new ListButtonListener(this.otherListsCardLayout, this.otherListsContainer, "Address"));
            // next
//            this.billing_select.addActionListener(new ListButtonListener(
//                    this.otherListsCardLayout, this.otherListsContainer,
//                    "Billing"));
            // next
            this.items.addActionListener(new ListButtonListener(
                    this.otherListsCardLayout, this.otherListsContainer,
                    "Items"));

            // Enable buttons to select a list, reset tooltip
            for (JToggleButton b : this.cardLayoutViewButtons) {
                b.setEnabled(true);
                b.setToolTipText(null);
            }
            this.cardLayoutViewButtons.get(0).setSelected(true);
        } else if (this.customer == null) {
            // Disable buttons to select a list if the record isn't yet saved
            for (JToggleButton b : this.cardLayoutViewButtons) {
                b.setEnabled(false);
                b.setToolTipText("Save this record to access this area");
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see ISIS.gui.View#getCurrentRecord()
     */
    @Override
    public Transaction getCurrentRecord() {
        if (this.transaction == null) {
            if (!this.isAnyFieldDifferentFromDefault()) {
                return null;
            }
            this.transaction = new Transaction(customer);
//            this.transaction.setStatus();
            //TODO: This

        } else {
//            this.transaction.setStatus();
            //TODO: this
        }
        return this.transaction;
    }

    /*
     * (non-Javadoc)
     * @see ISIS.gui.View#isAnyFieldsDifferentFromDefault()
     */
    @Override
    public Boolean isAnyFieldDifferentFromDefault() {
        return null;
        //TODO: This
//        return !(this.active.isSelected() && this.email.getText().isEmpty()
//                && this.password.getText().isEmpty() && this.note.getText()
//                .isEmpty());
    }

    /**
     * Draws all necessary components on the window.
     */
    private void populateElements() {
        this.setLayout(new BorderLayout());
        JSplitPane split = new JSplitPane() {
            private static final long	serialVersionUID	= 1L;

            @Override
            public void doLayout() {
                this.setDividerLocation((int) (this.getWidth() * (AddEditCustomer.dividerRatio == 0 ? .55
                        : AddEditCustomer.dividerRatio)));
                super.doLayout();
            }

            @Override
            public void setDividerLocation(int location) {
                AddEditCustomer.dividerRatio = location
                        / (double) this.getWidth();
                super.setDividerLocation(location);
            }
        };
        split.setOpaque(false);
        split.setBorder(null);
        JPanel main = new JPanel(new GridBagLayout());
        main.setOpaque(false);
        GridBagConstraints c;
        this.cardLayoutViewButtons = new ArrayList<>();
        int x = 0, y = 0;

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        main.add(new JLabel("Return"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        main.add(this.returnTransaction = new JCheckBox("", true), c);

        c = new GridBagConstraints();
        c.weightx = 0;
        c.gridx = x++;
        c.gridy = y;
        c.fill = GridBagConstraints.BOTH;
        main.add(new JLabel("Status"), c);

        c = new GridBagConstraints();
        c.weightx = 1;
        c.gridx = x--;
        c.gridy = y++;
        c.fill = GridBagConstraints.BOTH;
        main.add(this.status, c);

        split.setLeftComponent(main);

        JPanel otherArea = new JPanel(new BorderLayout());
        otherArea.setOpaque(false);
        JPanel buttonHolder = new JPanel(new WrapLayout());
        buttonHolder.setOpaque(false);

        // Add buttons for the cards (Other lists)
        buttonHolder.add(this.address_select = new JToggleButton("Address"), c);
        buttonHolder.add(this.billing_select = new JToggleButton("Billing"),
                         c);
        buttonHolder.add(this.items = new JToggleButton("Items"), c);

        // Add buttons to the buttons ArrayList
        this.cardLayoutViewButtons.add(this.address_select);
        this.cardLayoutViewButtons.add(this.billing_select);
        this.cardLayoutViewButtons.add(this.items);

        // Add the button holder at the top of the right section
        otherArea.add(buttonHolder, BorderLayout.NORTH);

        // Add the JPanel(card layout) to the right section center
        otherArea.add(this.otherListsContainer = new JPanel(
                this.otherListsCardLayout = new CardLayout()),
                      BorderLayout.CENTER);
        this.otherListsContainer.setOpaque(false);

        split.setRightComponent(otherArea);
        split.setResizeWeight(.5);

        this.add(split, BorderLayout.CENTER);

        ButtonGroup group = new ButtonGroup();
        for (JToggleButton b : this.cardLayoutViewButtons) {
            b.setFont(new Font("Small", Font.PLAIN, 11));
            group.add(b);
        }

        this.doSaveRecordAction();
    }
}

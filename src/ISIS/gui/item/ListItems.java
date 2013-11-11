package ISIS.gui.item;

import ISIS.customer.Customer;
import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.IRSTableModel;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.item.Item;
import ISIS.session.Session;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * List of items. Allows you to query and act on items.
 */
public class ListItems extends ListView<Item> {
    private static final long serialVersionUID = 1L;
    private JButton editButton;

	/* Fields omitted */

    /**
     * Constructs new Customer list view.
     */
    public ListItems(SplitPane splitPane) {
        super(splitPane);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c;

        int buttonNameSel = 0;
        JButton addButton = new JButton(this.buttonNames[buttonNameSel++]);
        this.editButton = new JButton(this.buttonNames[buttonNameSel++]);
        JButton activeButton = new JButton(this.buttonNames[buttonNameSel++]);

        int x = 0, y = 0;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x++;
        c.gridy = y;
        this.add(addButton, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x++;
        this.add(this.editButton, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x++;
        this.add(activeButton, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x++;
        c.weightx = 1;
        this.add(this.searchField, c);

        this.setTableModel(new IRSTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void addRow(Record record) {
                Item item = (Item) record;
                Object[] array = new Object[this.getColumnCount()];

                array[0] = item.getPkey();
                array[1] = "";
                array[2] = "";

                super.addRow(array);
            }
        });
        this.tableModel.setColumnTitles("id", "other", "header", "here");
        this.fillTable();

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = ++y;
        c.gridwidth = x;
        c.gridx = x = 0;
        c.weighty = 1;
        this.add(new JScrollPane(this.table), c);
    }

    @Override
    protected void fillTable() {
        String searchFieldText = this.searchField.getText();
        PreparedStatement stmt = null;
        try {
            if (searchFieldText.length() >= 1) {
                String search = searchFieldText + " ";
                // remove leading whitespace
                search = search.replaceFirst("^\\s+", "");
                // replaces whitespace with wildcards then a space.
                search = search.replaceAll("\\s+", "* ");
                // these aren't indexed anyway, so...
                search = search.replaceAll("([\\(\\)])", "");
                search = search.replaceAll("\\\"", ""); // TODO: actually fix
                String sqlQuery = "SELECT i.* FROM (SELECT pkey AS row FROM item_search WHERE item_search MATCH ?) " + "LEFT JOIN" +
                        " item AS i ON row=i.pkey";
                stmt = Session.getDB().prepareStatement(sqlQuery);
                stmt.setString(1, search);
            } else {
                String sqlQuery = "SELECT i.* from item AS i";
                stmt = Session.getDB().prepareStatement(sqlQuery);
            }
            // TODO add phone
            ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt.executeQuery());
            this.records = new ArrayList<>();
            for (HashMap<String, Field> map : results) {
                this.records.add(new Item(map));
            }
            this.populateTable();
        } catch (SQLException e) {
            ErrorLogger.error(e, "Error populating customer table.", true, true);
        }
    }

    private void populateTable() {
        this.table.removeAll();
        this.keys.clear();
        this.tableModel.setRowCount(0);
        for (Customer c : this.records) {
            this.tableModel.addRow(c);
        }
    }

    @Override
    protected void actionHandlerActionForSearchField() {
        this.editButton.doClick();
    }
}

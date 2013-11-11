package ISIS.gui.item;

import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.IRSTableModel;
import ISIS.gui.ListView;
import ISIS.gui.SplitPane;
import ISIS.item.Item;

import javax.swing.*;
import java.awt.*;
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
    protected void actionHandlerActionForSearchField() {
        this.editButton.doClick();
    }

    @Override
    protected ArrayList<Item> mapResults(ArrayList<HashMap<String, Field>> results) {
        ArrayList<Item> items = new ArrayList<>(results.size());
        for (HashMap<String, Field> result : results) {
            items.add(new Item(result));
        }
        return items;
    }

    @Override
    protected boolean hasDates () {
        return true;
    }

    @Override
    protected String tableName() {
        return "item";
    }
}

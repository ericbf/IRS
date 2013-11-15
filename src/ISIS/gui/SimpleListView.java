package ISIS.gui;

import ISIS.database.DB;
import ISIS.database.Record;
import ISIS.session.Session;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SimpleListView<E extends Record> extends ListView<E> {
    private final String sql;
    private final Integer key;
    protected final View pusher;

    public SimpleListView(SplitPane splitPane, View pusher, boolean multiSelect, String sql, Integer key) {
        super(splitPane, multiSelect);

        this.pusher = pusher;
        this.sql = sql;
        this.key = key;

        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void fillTable() {
        //no preprocessing to do.
        this.doFillTable();
    }

    @Override
    protected void doFillTable() {
        try {
            PreparedStatement stmt = Session.getDB().prepareStatement(this.sql);
            stmt.setInt(1, this.key);
            this.records = mapResults(DB.mapResultSet(stmt.executeQuery()));

            this.populateTable();
        } catch (SQLException e) {
            ErrorLogger.error(e, "Error populating list.", true, true);
        }
    }

    public enum ListType {
        ADD_SELECT, ADD_EDIT_REMOVE
    }
}

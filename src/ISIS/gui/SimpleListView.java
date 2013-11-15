package ISIS.gui;

import java.awt.GridBagLayout;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ISIS.database.DB;
import ISIS.database.Record;
import ISIS.session.Session;

public abstract class SimpleListView<E extends Record> extends ListView<E> {
	public enum ListType {
		ADD_SELECT, ADD_EDIT_REMOVE
	}
	
	private static final long	serialVersionUID	= 1L;
	private final String		sql;
	private final Integer		key;
	
	protected final View		pusher;
	
	public SimpleListView(SplitPane splitPane, View pusher,
			boolean multiSelect, String sql, Integer key) {
		super(splitPane, multiSelect);
		
		this.pusher = pusher;
		this.sql = sql;
		this.key = key;
		
		this.setLayout(new GridBagLayout());
	}
	
	@Override
	protected void doFillTable() {
		try {
			PreparedStatement stmt = Session.getDB().prepareStatement(this.sql);
			stmt.setInt(1, this.key);
			this.records = this
					.mapResults(DB.mapResultSet(stmt.executeQuery()));
			
			this.populateTable();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Error populating list.", true, true);
		}
	}
	
	@Override
	protected void fillTable() {
		// no preprocessing to do.
		this.doFillTable();
	}
}

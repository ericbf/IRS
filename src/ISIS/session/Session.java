package ISIS.session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.database.DB;
import ISIS.gui.ErrorLogger;
import ISIS.gui.TableUpdateListener;
import ISIS.user.AuthenticationException;
import ISIS.user.User;

/**
 * Various information and methods that is associated with a session. This
 * includes a logged in user, settings, and a connection to the database.
 */
public class Session {
	
	private static Session	session	= null;
	private static DB		db		= null;
	
	/**
	 * Only for initialization of program.
	 */
	public static void baseSession() throws SQLException {
		// login as base user
		Session.session = new Session(new User(1, false));
	}
	
	/**
	 * Gets the current session.
	 */
	public static Session getCurrentSession() {
		return Session.session;
	}
	
	/**
	 * Starts a new session, using the given user.
	 */
	public static void startNewSession(String username, String password)
			throws SQLException, AuthenticationException {
		if (Session.session != null) {
			endCurrentSession();
		}
		
		User user = new User(username, password);
		if (!user.getActive()) {
			throw new AuthenticationException("User is not active.",
					AuthenticationException.exceptionType.ACTIVE);
		}
		Session.session = new Session(user);
	}
	
	/**
	 * Notifies listeners that the record with the specified key was changed in
	 * some way.
	 */
	public static void updateTable(DB.TableName tableName, Integer pkey) {
		if (Session.tableListeners.containsKey(tableName)) {
			for (TableUpdateListener listener : Session.tableListeners
					.get(tableName)) {
				synchronized (listener) {
					listener.setKey(pkey);
					listener.actionPerformed(null);
				}
			}
		}
	}
	
	private User															user;
	
	private static HashMap<DB.TableName, ArrayList<TableUpdateListener>>	tableListeners	= new HashMap<DB.TableName, ArrayList<TableUpdateListener>>();
	
	/**
	 * Ends the current session.
	 */
	public static void endCurrentSession() {
		Session.session = null;
	}
	
	/**
	 * Gets a reference to the database.
	 */
	public static DB getDB() {
		if (Session.db == null) {
			Session.db = new DB("test.db");
			return Session.db;
		} else {
			return Session.db;
		}
	}
	
	/**
	 * Watch a database table for updates.
	 */
	public static void watchTable(DB.TableName tableName,
			TableUpdateListener listener) {
		if (Session.tableListeners.containsKey(tableName)) {
			Session.tableListeners.get(tableName).add(listener);
		} else {
			ArrayList<TableUpdateListener> listeners = new ArrayList<TableUpdateListener>();
			listeners.add(listener);
			Session.tableListeners.put(tableName, listeners);
		}
	}
	
	private Session(User user) {
		this.user = user;
	}
	
	/**
	 * Retrieves a setting for this user. If there is no instance of the given
	 * setting for this Session's user, it checks if there is a default setting.
	 * If there is no default setting, it returns null.
	 * 
	 * @param key
	 * @return The setting as an Object, or null if not found
	 */
	public Object getSetting(String key) {
		try {
			PreparedStatement s = Session.db
					.prepareStatement("SELECT * FROM setting WHERE key=\""
							+ key + "\" AND user=?");
			s.setInt(1, this.user.getEmployeeID());
			// There should only be one
			try {
				return DB.mapResultSet(s.executeQuery()).get(0).get("value")
						.getValue();
			} catch (NullPointerException e) {
				return null;
			}
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to get setting", true, true);
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUser() {
		return this.user;
	}
	
	/**
	 * Sets a default setting that is used for Users that have not set the
	 * setting.
	 */
	public void setDefaultSetting(String key, Object value) {
		try {
			PreparedStatement s = Session.db
					.prepareStatement("INSERT OR IGNORE INTO setting (key, value, user) VALUES (\""
							+ key + "\",\"" + value + "\",?)");
			s.setInt(1, this.user.getEmployeeID());
			s.execute();
		} catch (SQLException e) {
			ErrorLogger.error(e, "Failed to set default setting", true, true);
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets a setting for the Session's user, under the given key. If the passed
	 * value is null, the setting is removed from the database
	 * 
	 * @param key
	 * @param value
	 *            If null, deletes the setting
	 */
	public void setSetting(String key, Object value) {
		if (this.getSetting(key) == null) {
			this.setDefaultSetting(key, value);
		} else if (value == null) {
			try {
				PreparedStatement s = Session.db
						.prepareStatement("DELETE FROM setting WHERE key="
								+ key + "\" AND user=?");
				s.setInt(1, this.user.getEmployeeID());
				s.execute();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Failed to delete setting", true, true);
				e.printStackTrace();
			}
		} else {
			
			try {
				PreparedStatement s = Session.db
						.prepareStatement("UPDATE OR IGNORE setting SET value=\""
								+ value
								+ "\" WHERE key=\""
								+ key
								+ "\" AND user=?");
				s.setInt(1, this.user.getEmployeeID());
				s.execute();
			} catch (SQLException e) {
				ErrorLogger.error(e, "Failed to set setting", true, true);
				e.printStackTrace();
			}
		}
	}
}

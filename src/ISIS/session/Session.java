package ISIS.session;

import ISIS.database.DB;
import ISIS.gui.TableUpdateListener;
import ISIS.user.AuthenticationException;
import ISIS.user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Various information and methods that is associated with a session. This
 * includes a logged in user, settings, and a connection to the database.
 */
public class Session {
	
	private static Session	session	= null;
	private static DB		db		= null;
	private User			user;
    private static HashMap<DB.TableName, ArrayList<TableUpdateListener>> tableListeners = new HashMap<>();
	
	private Session(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets a setting for the Session's user, under the given key.
	 */
	public void setSetting(String key, Object value) {}
	
	/**
	 * Sets a default setting that is used for Users that have not set the
	 * setting.
	 */
	public static void setDefaultSetting(String key, Object value) {}
	
	/**
	 * Retrieves a setting for this user. If there is no instance of the given
	 * setting for this Session's user, it checks if there is a default setting.
	 * If there is no default setting, it returns null.
	 */
	public Object getSetting(String key) {
		return null;
	}
	
	/**
	 * Starts a new session, using the given user.
	 */
	public static void startNewSession(String username, String password)
			throws SQLException, AuthenticationException {
		if (session != null) {
			endCurrentSession();
		}
		
		User user = new User(username, password);
		if (!user.getActive()) {
			throw new AuthenticationException("User is not active.",
					AuthenticationException.exceptionType.ACTIVE);
		}
		session = new Session(user);
	}
	
	/**
	 * Only for initialization of program.
	 */
	public static void baseSession() throws SQLException {
		// login as base user
		session = new Session(new User(1, false));
	}
	
	/**
	 * Ends the current session.
	 */
	public static void endCurrentSession() {
		session = null;
	}
	
	/**
	 * Gets the current session.
	 */
	public static Session getCurrentSession() {
		return session;
	}

    /**
     * Watch a database table for updates.
     */
    public static void watchTable(DB.TableName tableName, TableUpdateListener listener) {
        if(Session.tableListeners.containsKey(tableName)) {
            Session.tableListeners.get(tableName).add(listener);
        } else {
            ArrayList<TableUpdateListener> listeners = new ArrayList<>();
            listeners.add(listener);
            Session.tableListeners.put(tableName, listeners);
        }
    }

    /**
     * Notifies listeners that the record with the specified key was changed in some way.
     */
    public static void updateTable(DB.TableName tableName, Integer pkey) {
        if(Session.tableListeners.containsKey(tableName)) {
            for(TableUpdateListener listener : Session.tableListeners.get(tableName)) {
                synchronized (listener) {
                        listener.setKey(pkey);
                        listener.actionPerformed(null);
                }
            }
        }
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
}

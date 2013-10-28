package ISIS.session;

import ISIS.Transactions.User;
import ISIS.database.DB;

/**
 * Various information and methods that is associated with a session. This
 * includes a logged in user, settings, and a connection to the database.
 */
public class Session {
    /* Field omitted. */

    private Session(User user) {
    }

    public User getUser() {
    }

    /**
     * Sets a setting for the Session's user, under the given key.
     */
    public void setSetting(String key, Object value) {
    }

    /**
     * Sets a default setting that is used for Users that have not set the
     * setting.
     */
    public static void setDefaultSetting(String key, Object value) {
    }

    /**
     * Retrieves a setting for this user. If there is no instance of the given
     * setting for this Session's user, it checks if there is a default setting.
     * If there is no default setting, it returns null.
     */
    public Object getSetting(String key) {
    }

    /**
     * Starts a new session, using the given user.
     */
    public static Session startNewSession(User user) {
    }

    /**
     * Ends the current session.
     */
    public static void endCurrentSession() {
    }

    /**
     * Gets the current session.
     */
    public static Session getCurrentSession() {
    }

    /**
     * Gets a reference to the database.
     */
    public static DB getDB() {
    }
}

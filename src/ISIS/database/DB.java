package ISIS.database;

import ISIS.gui.ErrorLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages third party relational database software used to manage and organize data used to implement functionalities
 * in IRS. Makes available functionality to execute statements, queries, and updates.
 */
public final class DB {
    /* Fields omitted */

    private Connection connection;
    private static int timeout = 100;

    /**
     * Public constructor. Opens the database from the specified location. The specified file must be a valid database.
     *
     * @pre new File(DBLocation).exists() == true
     * @post isOpen == true
     */
    public DB(String DBLocation) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            ErrorLogger.error("Driver not found.", true, true);
            System.exit(1);
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            initializeDB();
        } catch (SQLException ex) {
            ErrorLogger.error(ex.getLocalizedMessage(), true, true);
            System.exit(1);
        }
    }

    /**
     * Initialize tables
     */
    public void initializeDB() throws SQLException {
        //40 characters for hash, 5 for salt
        executeUpdate("CREATE TABLE IF NOT EXISTS user (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
                + "username VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(44) NOT NULL, fname VARCHAR(255) NOT NULL, "
                + "lname VARCHAR(255) NOT NULL, note TEXT NOT NULL)");
    }

    /**
     * For creating tables and stuff.
     */
    private void executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    /**
     * Checks if the database is open.
     */
    public boolean isOpen() {
        try {
            return connection.isValid(timeout);
        } catch (SQLException ex) {
            ErrorLogger.error(ex.getLocalizedMessage(), false, false);
            return false;
        }
    }

    /**
     * Closes the database.
     *
     * @pre isOpen == true
     * @post isOpen == false
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ErrorLogger.error(ex.getLocalizedMessage(), false, false);
        }
    }

    /**
     * Creates and returns a prepared statement given the given sql.
     *
     * @pre isOpen == true
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public static ArrayList<HashMap<String, Object>> mapResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        ArrayList<HashMap<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(md.getColumnCount());
            for (int i = 1; i <= md.getColumnCount(); ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * Starts a transaction in the database.
     *
     * @pre isOpen == true
     * @pre transactionActive() == false
     * @post transactionActive() == true
     */
    public void startTransaction() {
    }

    /**
     * Closes a transaction in the database.
     *
     * @pre isOpen == true
     * @pre transactionActive() == true
     * @post transactionActive() == false
     */
    public void closeTransaction() {
    }

    /**
     * Checks if a transaction is active.
     *
     * @pre isOpen == true
     */
    public boolean transactionActive() {
        return false;
    }
}

package ISIS.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import ISIS.gui.ErrorLogger;

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
            ErrorLogger.error(ex, "Failed to open or initialize database.", true, true);
            System.exit(1);
        }
    }

    /**
     * Initialize tables
     */
    public void initializeDB() throws SQLException {
        String datesSql = "createDate BIGINT NOT NULL, createUser INT REFERENCES user(pkey), modDate BIGINT NOT NULL, "
                + "modUser INT REFERENCES user(pkey)";

        // user
        // 40 characters for hash, 4 for salt
        executeUpdate("CREATE TABLE IF NOT EXISTS user (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
                + "username VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(44) NOT NULL, fname VARCHAR(255) NOT NULL, "
                + "lname VARCHAR(255) NOT NULL, note TEXT NOT NULL, "
                + datesSql + ")");
        // add base user (pkey = 1)
        executeUpdate("INSERT OR IGNORE INTO user (pkey, active, username, password, fname, lname, note, createDate, modDate) "
                + "VALUES (1, 0, 'base', '0', 'fname', 'lname', 'note', 0, 0)");

        // address
        executeUpdate("CREATE TABLE IF NOT EXISTS address (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, title VARCHAR(255) NOT NULL, "
                + "city VARCHAR(255) NOT NULL, state VARCHAR(255) NOT NULL, zip VARCHAR(10) NOT NULL, county VARCHAR(255) NOT NULL, "
                + "country VARCHAR(3) NOT NULL, st_address TEXT NOT NULL, type VARCHAR(255) NOT NULL, " + datesSql + ")");
        //address-search
        String address_search_columns = "st_address, zip, city, county, state, country, type";
        String address_search_columns_temp = "new." + address_search_columns.replaceAll("\\s", " new.");
        //view representing data inside address_search
        executeUpdate("CREATE VIEW IF NOT EXISTS address_search_view AS SELECT pkey AS docid, " + address_search_columns + " FROM address");
        //virtual table for searching addresses
        executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS address_search USING fts4(content=\"address_search_view\", " + address_search_columns + ")");
        //triggers to populate virtual table
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_insert AFTER INSERT ON address BEGIN\n"
                + "  INSERT INTO address_search(docid, " + address_search_columns + ") VALUES "
                + "(new.rowid, " + address_search_columns_temp + ");\n"
                + "END;");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_update BEFORE UPDATE ON address BEGIN\n"
                + "  DELETE FROM address_search WHERE docid=old.pkey;\n"
                + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_update_after AFTER UPDATE ON address BEGIN\n"
                + "  INSERT INTO address_search(docid, " + address_search_columns + ") VALUES "
                + "(new.rowid, " + address_search_columns_temp + ");\n"
                + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_delete BEFORE DELETE ON address BEGIN\n"
                + "  DELETE FROM address_search WHERE docid=old.pkey;\n"
                + "END;\n");


        // billing
        executeUpdate("CREATE TABLE IF NOT EXISTS billing (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
                + "number VARCHAR(255), expiration VARCHAR(5), CCV VARCHAR(5) NOT NULL, "
                + "address INT REFERENCES address(pkey), " + datesSql + ")");

        // customer
        executeUpdate("CREATE TABLE IF NOT EXISTS customer (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
                + "password VARCHAR(255) NOT NULL, fname VARCHAR(255) NOT NULL, lname VARCHAR(255) NOT NULL, "
                + "email TEXT NOT NULL, note TEXT NOT NULL, " + datesSql + ")");
        // customer-address
        executeUpdate("CREATE TABLE IF NOT EXISTS customer_address (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, "
                + "address INT REFERENCES address(pkey) NOT NULL, " + datesSql + ")");
        // customer-billing
        executeUpdate("CREATE TABLE IF NOT EXISTS customer_billing (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, "
                + "billing INT REFERENCES billing(pkey) NOT NULL, " + datesSql + ")");
        // customer-search
        String customer_search_columns = "fname, lname, email, note";
        String customer_search_columns_temp = "new." + customer_search_columns.replaceAll("\\s", " new.");
        //view representing data inside address_search
        executeUpdate("CREATE VIEW IF NOT EXISTS customer_search_view AS SELECT pkey AS docid, " + customer_search_columns + " FROM customer");
        //virtual table for searching addresses
        executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS customer_search USING fts4(content=\"customer_search_view\", " + customer_search_columns + ")");
        //triggers to populate virtual table
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_insert AFTER INSERT ON customer BEGIN\n"
                + "  INSERT INTO customer_search(docid, " + customer_search_columns + ") VALUES "
                + "(" + customer_search_columns_temp + ");\n"
                + "END;");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update BEFORE UPDATE ON customer BEGIN\n"
                + "  DELETE FROM customer_search WHERE docid=old.pkey;\n"
                + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update_after AFTER UPDATE ON customer BEGIN\n"
                + "  INSERT INTO customer_search(docid, " + customer_search_columns + ") VALUES "
                + "(" + customer_search_columns_temp + ");\n"
                + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_delete BEFORE DELETE ON customer BEGIN\n"
                + "  DELETE FROM customer_search WHERE docid=old.pkey;\n"
                + "END;\n");

        // item
        executeUpdate("CREATE TABLE IF NOT EXISTS item (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
                + "name VARCHAR(255) NOT NULL, SKU VARCHAR(255) NOT NULL, price VARCHAR(30) NOT NULL, onhand_qty VARCHAR(30) NOT NULL, "
                + "cost VARCHAR(30) NOT NULL, description TEXT NOT NULL, uom VARCHAR(10), reorder_qty VARCHAR(30) NOT NULL, lastest BOOLEAN NOT NULL, " + datesSql + ")");

        //transaction
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_ (pkey INTEGER PRIMARY KEY, status VARCHAR(20) NOT NULL, "
                + "type VARCHAR(20) NOT NULL, modified BOOLEAN NOT NULL, parent_transaction INT REFERENCES transaction_(pkey), "
                + datesSql + ")");
        //transaction-item
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_item (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, "
                + "item INT REFERENCES item(pkey) NOT NULL, price VARCHAR(30) NOT NULL, adjustment VARCHAR(30) NOT NULL, description TEXT, "
                + datesSql + ")");
        //transaction-address
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_address (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, "
                + "address INT REFERENCES address(pkey) NOT NULL, " + datesSql + ")");
        //transaction-billing
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_billing (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, "
                + "billing INT REFERENCES billing(pkey) NOT NULL, " + datesSql + ")");

        //TODO: add indices
        //TODO: keywords

    }

    /**
     * For creating tables and stuff. Returns number of affected rows.
     */
    private int executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
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

    public static ArrayList<HashMap<String, Field>> mapResultSet(ResultSet rs)
            throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        ArrayList<HashMap<String, Field>> rows = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Field> row = new HashMap<>(md.getColumnCount());
            for (int i = 1; i <= md.getColumnCount(); ++i) {
                Field field = new Field(true);
                field.initField(rs.getObject(i));
                row.put(md.getColumnName(i), field);
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

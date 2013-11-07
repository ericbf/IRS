package ISIS.database;

import ISIS.customer.Customer;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages third party relational database software used to manage and organize data used to implement functionalities
 * in IRS. Makes available functionality to execute statements, queries, and updates.
 */
public final class DB {
    /* Fields omitted */

    private static int timeout = 100;
    private Connection connection;

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

    public static ArrayList<HashMap<String, Field>> mapResultSet(ResultSet rs) throws SQLException {
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
     * Initialize tables
     */
    public void initializeDB() throws SQLException {
        String datesSql = "createDate BIGINT NOT NULL, createUser INT REFERENCES user(pkey), modDate BIGINT NOT NULL, " + "modUser INT REFERENCES user(pkey)";

        // user
        // 40 characters for hash, 4 for salt
        executeUpdate("CREATE TABLE IF NOT EXISTS user (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, " + "username VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(44) NOT NULL, fname VARCHAR(255) NOT NULL, " + "lname VARCHAR(255) NOT NULL, note TEXT NOT NULL, " + datesSql + ")");
        // add base user (pkey = 1)
        executeUpdate("INSERT OR IGNORE INTO user (pkey, active, username, password, fname, lname, note, createDate, modDate) " + "VALUES (1, 0, 'base', '0', 'fname', 'lname', 'note', 0, 0)");

        // settings
        // null user for global setting
        executeUpdate("CREATE TABLE IF NOT EXISTS setting (pkey INTEGER PRIMARY KEY, key VARCHAR(255) NOT NULL, value TEXT NOT NULL, " + "user INT REFERENCES user(pkey))");

        //phone
        executeUpdate("CREATE TABLE IF NOT EXISTS phone (pkey INTEGER PRIMARY KEY, primary_num BOOLEAN NOT NULL, " +
                              "type VARCHAR(255) NOT NULL, " + "number VARCHAR(255) NOT NULL, " + datesSql + ")");

        // address
        executeUpdate("CREATE TABLE IF NOT EXISTS address (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, title VARCHAR(255) NOT NULL, " + "city VARCHAR(255) NOT NULL, state VARCHAR(255) NOT NULL, zip VARCHAR(10) NOT NULL, county VARCHAR(255) NOT NULL, " +
                              "country VARCHAR(3) NOT NULL, st_address TEXT NOT NULL, type VARCHAR(255) NOT NULL, " + datesSql + ")");
        // address-search
        String address_search_columns = "st_address, zip, city, county, state, country, type";
        String address_search_columns_temp = "new." + address_search_columns.replaceAll("\\s", " new.");
        // view representing data inside address_search
        executeUpdate("CREATE VIEW IF NOT EXISTS address_search_view AS SELECT pkey AS docid, " + address_search_columns + " FROM address");
        // virtual table for searching addresses
        executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS address_search USING fts3(content=\"address_search_view\", " + address_search_columns + ")");
        // triggers to populate virtual table
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_insert AFTER INSERT ON address BEGIN\n" + "  INSERT INTO address_search(docid, " + address_search_columns + ") VALUES " + "(new.rowid, " + address_search_columns_temp + ");\n" + "END;");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_update BEFORE UPDATE ON address BEGIN\n" + "  DELETE FROM address_search WHERE docid=old.pkey;\n" + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_update_after AFTER UPDATE ON address BEGIN\n" + "  INSERT INTO address_search(docid, " + address_search_columns + ") VALUES " + "(new.rowid, " + address_search_columns_temp + ");\n" + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS address_search_delete BEFORE DELETE ON address BEGIN\n" + "  DELETE FROM address_search WHERE docid=old.pkey;\n" + "END;\n");

        // billing
        executeUpdate("CREATE TABLE IF NOT EXISTS billing (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, " + "number VARCHAR(255), expiration VARCHAR(5), CCV VARCHAR(5) NOT NULL, " + "address INT REFERENCES address(pkey), " + datesSql + ")");

        // customer
        executeUpdate("CREATE TABLE IF NOT EXISTS customer (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, " + "password VARCHAR(255) NOT NULL, fname VARCHAR(255) NOT NULL, lname VARCHAR(255) NOT NULL, " + "email TEXT NOT NULL, note TEXT NOT NULL, " + datesSql + ")");
        // customer-phone
        executeUpdate("CREATE TABLE IF NOT EXISTS customer_phone (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer" +
                              "(pkey) NOT NULL, " + "phone INT REFERENCES phone(pkey) NOT NULL)");
        // customer-address
        executeUpdate("CREATE TABLE IF NOT EXISTS customer_address (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, " + "address INT REFERENCES address(pkey) NOT NULL)");
        // customer-billing
        executeUpdate("CREATE TABLE IF NOT EXISTS customer_billing (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, " + "billing INT REFERENCES billing(pkey) NOT NULL)");
        // customer-search
        String customer_search_columns = "pkey, fname, lname, email, note";
        String customer_search_columns_temp = "new." + customer_search_columns.replaceAll("\\s", " new.");
        // view representing data inside customer_search
        executeUpdate("CREATE VIEW IF NOT EXISTS customer_search_view AS SELECT pkey AS docid, " + customer_search_columns + " FROM customer");
        // virtual table for searching customers
        executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS customer_search USING fts3(content=\"customer_search_view\", " + customer_search_columns + ")");
        // triggers to populate virtual table
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_insert AFTER INSERT ON customer BEGIN\n" + "  INSERT INTO customer_search(docid, " + customer_search_columns + ") VALUES " + "(new.rowid, " + customer_search_columns_temp + ");\n" + "END;");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update BEFORE UPDATE ON customer BEGIN\n" + "  DELETE FROM customer_search WHERE docid=old.pkey;\n" + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update_after AFTER UPDATE ON customer BEGIN\n" + "  INSERT INTO customer_search(docid, " + customer_search_columns + ") VALUES " + "(new.rowid, " + customer_search_columns_temp + ");\n" + "END;\n");
        executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_delete BEFORE DELETE ON customer BEGIN\n" + "  DELETE FROM customer_search WHERE docid=old.pkey;\n" + "END;\n");
        // sample data
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (1, 1, \"hello pass\", \"hello name\", \"lname\", \"email\", \"note\", 0,0)");
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (2, 1, \"person 1 world\", \"Joe\", \"Dickhead\", \"a\", \"a\", 0, 0)");
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (3, 1, \"person 2 world\", \"hello world name111\", \"hello world\", \"a\", \"a\", 0, 0)");
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (4, 1, \"person 3 world\", \"hello world name333\", \"hello world\", \"a\", \"a\", 0, 0)");
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (5, 1, \"person 4 world\", \"hello world name444\", \"hello world\", \"a\", \"a\", 0, 0)");
        //        executeUpdate("INSERT OR IGNORE INTO customer (pkey, active, password, fname, lname, email, note, createDate, modDate) " + "VALUES (6, 1, \"person 5 world\", \"hello world name555\", \"hello world\", \"a\", \"a\", 0, 0)");


        // item
        executeUpdate("CREATE TABLE IF NOT EXISTS item (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, " + "name VARCHAR(255) NOT NULL, SKU VARCHAR(255) NOT NULL, price VARCHAR(30) NOT NULL, onhand_qty VARCHAR(30) NOT NULL, " +
                              "cost VARCHAR(30) NOT NULL, description TEXT NOT NULL, uom VARCHAR(10), reorder_qty VARCHAR(30) NOT NULL, lastest BOOLEAN NOT NULL, " + datesSql + ")");

        // transaction
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_ (pkey INTEGER PRIMARY KEY, status VARCHAR(20) NOT NULL, " + "type VARCHAR(20) NOT NULL, modified BOOLEAN NOT NULL, parent_transaction INT REFERENCES transaction_(pkey), " + datesSql + ")");
        // transaction-item
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_item (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, " + "item INT REFERENCES item(pkey) NOT NULL, price VARCHAR(30) NOT NULL, adjustment VARCHAR(30) NOT NULL, description TEXT, " + datesSql + ")");
        // transaction-address
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_address (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, " + "address INT REFERENCES address(pkey) NOT NULL)");
        // transaction-billing
        executeUpdate("CREATE TABLE IF NOT EXISTS transaction_billing (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, " + "billing INT REFERENCES billing(pkey) NOT NULL)");

        // TODO: add indices
        // TODO: keywords

    }

    public void sampleData() throws SQLException {
        Customer customer = new Customer("Joe", "Doe", "sammich@penis.info", "This is a note.", "this is a password?", true);
        customer.addPhoneNum(new Phone("404040404", true, Phone.PhoneType.HOME));
        customer.save();
        customer = new Customer("Sammich", "Bob", "whuh@what.com", "This is a note.", "this is a password?", true);
        customer.addPhoneNum(new Phone("301231213", true, Phone.PhoneType.HOME));
        customer.save();
        customer = new Customer("Jizzle", "Dizzle", "cookies@gmail.com", "This is a note.", "this is a password?", true);
        customer.addPhoneNum(new Phone("56565656", true, Phone.PhoneType.HOME));
        customer.save();
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
        return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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

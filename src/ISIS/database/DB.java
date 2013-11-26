package ISIS.database;

import ISIS.gui.ErrorLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages third party relational database software used to manage and organize
 * data used to implement functionalities in IRS. Makes available functionality
 * to execute statements, queries, and updates.
 */
public final class DB {
	/* Fields omitted */
	
	public static enum TableName {
		transaction_item, transaction_, customer_address, customer_billing,
		customer_phone, item, phone, customer, setting, billing, user, address
	}
	
	private static int	timeout	= 100;
	
	public static ArrayList<HashMap<String, Field>> mapResultSet(ResultSet rs)
			throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		ArrayList<HashMap<String, Field>> rows = new ArrayList<HashMap<String, Field>>();
		while (rs.next()) {
			HashMap<String, Field> row = new HashMap<String, Field>(
					md.getColumnCount());
			for (int i = 1; i <= md.getColumnCount(); ++i) {
				Field field = new Field();
				field.initField(rs.getObject(i));
				row.put(md.getColumnName(i), field);
			}
			rows.add(row);
		}
		return rows;
	}
	
	/**
	 * For building a preparedstatement's sql.
	 */
	public static String preparedArgsBuilder(int argCount, String argFormat) {
		if (argCount == 0) {
			return "";
		}
		StringBuilder args = new StringBuilder();
		args.append(argFormat);
		for (int i = 1; i < argCount; ++i) {
			args.append(", ").append(argFormat);
		}
		return args.toString();
	}
	
	private Connection	connection;
	
	/**
	 * Public constructor. Opens the database from the specified location. The
	 * specified file must be a valid database.
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
			this.connection = DriverManager
					.getConnection("jdbc:sqlite:test.db");
			this.initializeDB();
		} catch (SQLException ex) {
			ErrorLogger.error(ex, "Failed to open or initialize database.",
					true, true);
			System.exit(1);
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
			this.connection.close();
		} catch (SQLException ex) {
			ErrorLogger.error(ex.getLocalizedMessage(), false, false);
		}
	}
	
	/**
	 * Closes a transaction in the database.
	 * 
	 * @pre isOpen == true
	 * @pre transactionActive() == true
	 * @post transactionActive() == false
	 */
	public void closeTransaction() throws SQLException {
        connection.setAutoCommit(true);
    }

    /**
     * Rolls back all changes that have been made inside of a transaction.
     */
    public void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            ErrorLogger.error(e, "Failed to roll back your changes!!!!", true, true);
        }
    }
	
	/**
	 * For creating tables and stuff. Returns number of affected rows.
	 */
	private int executeUpdate(String sql) throws SQLException {
		Statement statement = this.connection.createStatement();
		return statement.executeUpdate(sql);
	}
	
	/**
	 * Initialize tables
	 */
	public void initializeDB() throws SQLException {
		String datesSql = "createDate BIGINT NOT NULL, createUser INT REFERENCES user(pkey), modDate BIGINT NOT NULL, "
				+ "modUser INT REFERENCES user(pkey)";
		
		// user
		// 40 characters for hash, 4 for salt
		this.executeUpdate("CREATE TABLE IF NOT EXISTS user (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
				+ "username VARCHAR(255) UNIQUE NOT NULL, password VARCHAR(44) NOT NULL, fname VARCHAR(255) NOT NULL, "
				+ "lname VARCHAR(255) NOT NULL, note TEXT NOT NULL, "
				+ datesSql + ")");
		// add base user (pkey = 1)
		this.executeUpdate("INSERT OR IGNORE INTO user (pkey, active, username, password, fname, lname, note, createDate, modDate) "
				+ "VALUES (1, 0, 'base', '0', 'fname', 'lname', 'note', 0, 0)");
		
		// settings
		// null user for global setting
		this.executeUpdate("CREATE TABLE IF NOT EXISTS setting (pkey INTEGER PRIMARY KEY, key VARCHAR(255) NOT NULL, value TEXT NOT NULL, "
				+ "user INT REFERENCES user(pkey))");
		
		// phone
		this.executeUpdate("CREATE TABLE IF NOT EXISTS phone (pkey INTEGER PRIMARY KEY, primary_num BOOLEAN NOT NULL, "
				+ "type VARCHAR(255) NOT NULL, "
				+ "number VARCHAR(255) NOT NULL)");
		
		// address
		this.executeUpdate("CREATE TABLE IF NOT EXISTS address (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
				+ "primary_status BOOLEAN NOT NULL, title VARCHAR(255) NOT NULL, "
				+ "city VARCHAR(255) NOT NULL, "
				+ "state VARCHAR(255) NOT NULL, county VARCHAR(255) NOT NULL, country VARCHAR(3) NOT NULL, "
				+ "st_address TEXT NOT NULL, zip VARCHAR(255) NOT NULL)");
		
		// billing
		this.executeUpdate("CREATE TABLE IF NOT EXISTS billing (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
				+ "number VARCHAR(255), "
				+ "expiration VARCHAR(5), CCV VARCHAR(5), "
				+ "address INT REFERENCES address(pkey), "
				+ "type VARCHAR(50))");
		
		// customer
		this.executeUpdate("CREATE TABLE IF NOT EXISTS customer (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
				+ "password VARCHAR(255) NOT NULL, fname VARCHAR(255) NOT NULL, lname VARCHAR(255) NOT NULL, "
				+ "email TEXT NOT NULL, note TEXT NOT NULL, " + datesSql + ")");
		// customer-phone
		this.executeUpdate("CREATE TABLE IF NOT EXISTS customer_phone (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer"
				+ "(pkey) NOT NULL, "
				+ "phone INT REFERENCES phone(pkey) NOT NULL)");
		// customer-address
		this.executeUpdate("CREATE TABLE IF NOT EXISTS customer_address (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, "
				+ "address INT REFERENCES address(pkey) NOT NULL)");
		// customer-billing
		this.executeUpdate("CREATE TABLE IF NOT EXISTS customer_billing (pkey INTEGER PRIMARY KEY, customer INT REFERENCES customer(pkey) NOT NULL, "
				+ "billing INT REFERENCES billing(pkey) NOT NULL)");
		
		// customer-search
		String customer_search_columns = "pkey, fname, lname, email, note";
		String phoneNoSql = "SELECT group_concat(number, ' ') FROM customer_phone AS cp LEFT JOIN phone AS p ON cp.phone=p.pkey WHERE cp.customer=";
		String addressSqlColumns = "title || ' ' || city || ' ' || state || ' ' || zip || ' ' || county || ' ' || st_address";
		String addressSql = "SELECT group_concat("
				+ addressSqlColumns
				+ ", ' ') FROM customer_address AS ca LEFT JOIN address AS a ON "
				+ "ca.address=a.pkey WHERE ca.customer=";
		String customer_search_insert = "INSERT INTO customer_search SELECT csv.* FROM customer_search_view AS csv WHERE csv.pkey=";
		// view representing data inside customer_search
		this.executeUpdate("CREATE VIEW IF NOT EXISTS customer_search_view AS SELECT pkey AS docid, "
				+ customer_search_columns
				+ ", "
				+ "("
				+ phoneNoSql
				+ "customer.pkey), ("
				+ addressSql
				+ "customer.pkey) FROM customer");
		// virtual table for searching customers
		this.executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS customer_search USING fts3(content=\"customer_search_view\", "
				+ "" + customer_search_columns + ", phone, address)");
		
		// triggers to populate virtual table
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_insert AFTER INSERT ON customer BEGIN\n"
				+ customer_search_insert + "new.rowid; END;");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update_before BEFORE UPDATE ON customer BEGIN\n"
				+ "DELETE FROM "
				+ "customer_search WHERE pkey=old.pkey;\nEND;\n");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_update_after AFTER UPDATE ON customer BEGIN\n"
				+ customer_search_insert + "new.rowid;\nEND;\n");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_delete BEFORE DELETE ON customer BEGIN\n"
				+ "  DELETE FROM "
				+ "customer_search WHERE pkey=old.pkey;\n"
				+ "END;\n");
		
		// update virtual table when grouped columns are updated
		for (String junction : new String[] { "customer_phone",
				"customer_address" }) {
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction
					+ "_insert_before BEFORE INSERT ON "
					+ junction
					+ " BEGIN\nDELETE FROM customer_search WHERE content=new.customer;\nEND;");
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction + "_insert_after AFTER INSERT ON " + junction
					+ " BEGIN\n" + customer_search_insert + "new"
					+ ".customer;" + "\nEND;");
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction
					+ "_update_before BEFORE UPDATE ON "
					+ junction
					+ " BEGIN\nDELETE FROM customer_search WHERE content=old.customer;\nEND;\n");
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction + "_update_after AFTER UPDATE ON " + junction
					+ " BEGIN\n" + customer_search_insert
					+ "old.customer;\nEND;\n");
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction
					+ "_delete_before BEFORE DELETE ON "
					+ junction
					+ " BEGIN\nDELETE FROM customer_search WHERE content=old.customer;\nEND;");
			this.executeUpdate("CREATE TRIGGER IF NOT EXISTS customer_search_"
					+ junction + "_delete_after AFTER DELETE ON " + junction
					+ " " + "BEGIN\n" + customer_search_insert
					+ "old.customer;\nEND;");
		}
		
		// item
		this.executeUpdate("CREATE TABLE IF NOT EXISTS item (pkey INTEGER PRIMARY KEY, active BOOLEAN NOT NULL, "
				+ "name VARCHAR(255) NOT NULL, SKU VARCHAR(255) NOT NULL, price VARCHAR(30) NOT NULL, onhand_qty VARCHAR(30) NOT NULL, "
				+ "cost VARCHAR(30) NOT NULL, description TEXT NOT NULL, uom VARCHAR(10), reorder_qty VARCHAR(30) NOT NULL, "
				+ datesSql + ")");
		// item searching
		String item_search_columns = "name, SKU, price, description, uom";
		String item_search_insert = "INSERT INTO item_search SELECT csv.* FROM item_search_view AS csv WHERE csv.docid=";
		
		// view representing data inside item_search
		this.executeUpdate("CREATE VIEW IF NOT EXISTS item_search_view AS SELECT pkey AS docid, "
				+ item_search_columns + " FROM item");
		// virtual table for searching items
		this.executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS item_search USING fts3(content=\"item_search_view\", "
				+ customer_search_columns + ")");
		
		// triggers to populate virtual table
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS item_search_insert AFTER INSERT ON item BEGIN\n"
				+ item_search_insert + "new.rowid; END;");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS item_search_update_before BEFORE UPDATE ON item BEGIN\n"
				+ "DELETE FROM "
				+ "item_search WHERE content=old.pkey;\nEND;\n");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS item_search_update_after AFTER UPDATE ON item BEGIN\n"
				+ item_search_insert + "new.rowid;\nEND;\n");
		this.executeUpdate("CREATE TRIGGER IF NOT EXISTS item_search_delete BEFORE DELETE ON item BEGIN\n"
				+ "  DELETE FROM "
				+ "item_search WHERE content=old.pkey;\n"
				+ "END;\n");
		
		// transaction
		this.executeUpdate("CREATE TABLE IF NOT EXISTS transaction_ (pkey INTEGER PRIMARY KEY, status VARCHAR(20) NOT NULL, "
				+ "customer INT REFERENCES customer(pkey), "
				+ "type VARCHAR(20) NOT NULL, modified BOOLEAN NOT NULL, "
				+ "parent_transaction INT REFERENCES transaction_"
				+ "(pkey), address INT REFERENCES address(pkey), billing INT REFERENCES billing(pkey), "+datesSql+")");
		// transaction-item
		this.executeUpdate("CREATE TABLE IF NOT EXISTS transaction_item (pkey INTEGER PRIMARY KEY, transaction_ INT REFERENCES transaction_(pkey) NOT NULL, "
				+ "item INT REFERENCES item(pkey) NOT NULL, price VARCHAR(30) NOT NULL, adjustment VARCHAR(30) NOT NULL, description TEXT, "
				+ datesSql + ")");
		
		// TODO: keywords
		// TODO: add indices
		
	}
	
	/**
	 * Checks if the database is open.
	 */
	public boolean isOpen() {
		try {
			return this.connection.isValid(DB.timeout);
		} catch (SQLException ex) {
			ErrorLogger.error(ex.getLocalizedMessage(), false, false);
			return false;
		}
	}
	
	/**
	 * Creates and returns a prepared statement given the given sql.
	 * 
	 * @pre isOpen == true
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
	}
	
	/**
	 * Starts a transaction in the database.
	 * 
	 * @pre isOpen == true
	 * @pre transactionActive() == false
	 * @post transactionActive() == true
	 */
	public void startTransaction() throws SQLException {
        if(connection.getAutoCommit() == false) {
            ErrorLogger.error("We were already inside a transaction!?\nChanges rolled back.", true, true);
            this.rollbackTransaction();
        }
        connection.setAutoCommit(false);
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

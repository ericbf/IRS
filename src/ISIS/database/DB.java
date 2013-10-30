package ISIS.database;

/**
 * Manages third party relational database software used to manage and organize
 * data used to implement functionalities in IRS. Makes available functionality
 * to execute statements, queries, and updates.
 */
public class DB {
	/* Fields omitted */

	/**
	 * Public constructor. Opens the database from the specified location. The
	 * specified file must be a valid database.
	 *
	 * @pre new File(DBLocation).exists() == true
	 * @post isOpen == true
	 */
	public DB(String DBLocation) {
	}

	/**
	 * Checks if the database is open.
	 */
	public boolean isOpen() {
	}

	/**
	 * Closes the database.
	 *
	 * @pre isOpen == true
	 * @post isOpen == false
	 */
	public void close() {
	}

	/**
	 * Creates and returns a prepared statement given the given sql.
	 *
	 * @pre isOpen == true
	 */
	public PreparedStatment prepareStatement(String sql) {
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
	}
}

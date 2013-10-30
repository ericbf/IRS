package ISIS.user;

import ISIS.database.Record;
import ISIS.misc.Dates;
import ISIS.misc.Picture;
import java.util.ArrayList;

/**
 * The class representing the person who is employed by the client and is using
 * IRS. It consists of information about the user, such as his or her username,
 * employee id, password, and etcetera.
 *
 * Invariants:
 *
 * The Customer references exactly one set of associated dates.
 *
 * @dates != null
 *
 * The transaction has a primary key field that is always set.
 * @pkey > 0
 */
public class User extends Record {
	/* Fields omitted */

	/**
	 * Public constructor. A User starts with all fields populated.
	 *
	 * @post this.password == hash_function(password)
	 */
	public User(int employeeID, String username, boolean active, String password, String fname, String lname, String note) {
	}

	/**
	 * Public constructor. Takes a User database key, and has the option to
	 * populate the fields from the database.
	 */
	public User(int pkey, boolean populate) {
	}

	/**
	 * Get the employee's ID.
	 */
	public int getEmployeeID() {
	}

	/**
	 * Sets the User's active status.
	 */
	public void setActive(boolean active) {
	}

	/**
	 * Gets the User's active status.
	 */
	public boolean getActive() {
	}

	/**
	 * Gets the User's username.
	 */
	public String getUsername() {
	}

	/**
	 * Sets the password for this user.
	 *
	 * @post this.password == hash_function(password)
	 */
	public void setPassword(String password) {
	}

	/**
	 * Checks the provided password against the stored hash.
	 */
	public boolean checkPassword(String password) {
	}

	/**
	 * Gets the User's first name.
	 */
	public String getFirstName() {
	}

	/**
	 * Gets the User's last name.
	 */
	public String getLastName() {
	}

	/**
	 * Set the User's note.
	 */
	public void setNote(String note) {
	}

	/**
	 * Get the User's note.
	 */
	public String getNote() {
	}

	/**
	 * Adds a picture to the User record.
	 *
	 * @pre getPictures().contains(picture) == false
	 * @pre getPictures().contains(picture) == true
	 */
	public void addPicture(Picture picture) {
	}

	/**
	 * Removes a picture associated with the User record. picture.
	 *
	 * @pre getPictures().contains(picture) == true
	 * @pre getPictures().contains(picture) == false
	 */
	public void removePicture(Picture picture) {
	}

	/**
	 * Gets the pictures associated with the User record.
	 */
	public ArrayList<Picture> getPictures(Picture picture) {
	}

	/**
	 * Gets the dates associated with this transaction.
	 */
	public Dates getDates() {
	}
}

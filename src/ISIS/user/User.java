package ISIS.user;

import ISIS.database.DB;
import ISIS.database.DB.TableName;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.database.RecordNotFoundException;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Picture;
import ISIS.session.Session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The class representing the person who is employed by the client and is using
 * IRS. It consists of information about the user, such as his or her username,
 * employee id, password, and etcetera. Invariants: The Customer references
 * exactly one set of associated dates.
 * 
 * @dates != null The transaction has a primary key field that is always set.
 * @pkey > 0
 */
public final class User extends Record {
	/**
	 * The static variable returned of getTableName(). This is the table name
	 * used by the database.
	 */
	public static TableName	tableName	= TableName.user;
	
	/**
	 * The static boolean that tells whether this type of record has the
	 * mod_date, creation_date, and et cetera of some records.
	 */
	public static boolean	hasDates_	= true;
	
	/**
	 * Converts byte array to hex.
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		
		return result.toString();
	}
	
	private static byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	/**
	 * Checks if a user exists.
	 */
	public static boolean userExists(String username) throws SQLException {
		try {
			new User(username, "asdf");
		} catch (AuthenticationException e) {
			if (e.type == AuthenticationException.exceptionType.USERNAME) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Public constructor. Takes a User database key, and has the option to
	 * populate the fields from the database.
	 */
	public User(Integer pkey, boolean populate) throws SQLException,
			RecordNotFoundException {
		super();
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Public constructor. A User starts with all fields populated.
	 * 
	 * @post this.password == hash_function(password)
	 */
	public User(String username, boolean active, String password, String fname,
			String lname, String note) {
		super();
		
		this.setFieldValue("username", username);
		this.setFieldValue("active", active);
		this.setFieldValue("fname", fname);
		this.setFieldValue("lname", lname);
		this.setFieldValue("note", note);
		this.setPassword(password);
	}
	
	/**
	 * Tries to log a user in.
	 */
	public User(String username, String password) throws SQLException,
			AuthenticationException {
		super();
		
		PreparedStatement stmt = Session.getDB().prepareStatement(
				"SELECT * FROM USER WHERE username=?");
		stmt.setString(1, username);
		
		ArrayList<HashMap<String, Field>> users = DB.mapResultSet(stmt
				.executeQuery());
		if (users.size() != 1) {
			throw new AuthenticationException(
					"Username or password is incorrect.",
					AuthenticationException.exceptionType.USERNAME);
		}
		this.initializeFields(users.get(0));
		
		if (!this.checkPassword(password)) {
			throw new AuthenticationException(
					"Username or password is incorrect.",
					AuthenticationException.exceptionType.PASSWORD);
		}
	}
	
	/**
	 * Adds a picture to the User record.
	 * 
	 * @pre getPictures().contains(picture) == false
	 * @pre getPictures().contains(picture) == true
	 */
	public void addPicture(Picture picture) {}
	
	/**
	 * Checks the provided password against the stored hash.
	 */
	public boolean checkPassword(String password) {
		// get salt from DB, hash it with the password given
		if (((String) this.getFieldValue("password")).length() < 4) {
			return false;
		}
		return this.hashPassword(
				password.getBytes(),
				hexToBytes(((String) this.getFieldValue("password")).substring(
						0, 4))).equals((this.getFieldValue("password")));
	}
	
	/**
	 * Gets the User's active status.
	 */
	public boolean getActive() {
		return ((Integer) this.getFieldValue("active") == 1);
	}
	
	/**
	 * Get the employee's ID.
	 */
	public int getEmployeeID() {
		return (Integer) this.getFieldValue("pkey");
	}
	
	/**
	 * Gets the User's first name.
	 */
	public String getFirstName() {
		return (String) this.getFieldValue("fname");
	}
	
	/**
	 * Gets the User's last name.
	 */
	public String getLastName() {
		return (String) this.getFieldValue("lname");
	}
	
	/**
	 * Get the User's note.
	 */
	public String getNote() {
		return (String) this.getFieldValue("note");
	}
	
	/**
	 * Gets the password for the purposes of updating the record.
	 */
	public String getPassword() {
		return (String) this.getFieldValue("password");
	}
	
	/**
	 * Gets the pictures associated with the User record.
	 */
	public ArrayList<Picture> getPictures(Picture picture) {
		return null;
	}
	
	@Override
	protected TableName getTableName() {
		return User.tableName;
	}
	
	/**
	 * Gets the User's username.
	 */
	public String getUsername() {
		return (String) this.getFieldValue("username");
	}
	
	@Override
	protected boolean hasDates() {
		return User.hasDates_;
	}
	
	/**
	 * Gets sha1 of salt + password, returns salt + hash (for reversibility)
	 */
	private String hashPassword(byte[] password, byte[] salt) {
		// append password to salt
		byte[] saltedpass = new byte[salt.length + password.length];
		System.arraycopy(salt, 0, saltedpass, 0, salt.length);
		System.arraycopy(password, 0, saltedpass, salt.length, password.length);
		
		// hash
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] hash = md.digest(saltedpass);
			return bytesToHex(salt) + bytesToHex(hash);
		} catch (NoSuchAlgorithmException ex) {
			ErrorLogger.error("SHA-1 not implemented..", true, true);
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Removes a picture associated with the User record. picture.
	 * 
	 * @pre getPictures().contains(picture) == true
	 * @pre getPictures().contains(picture) == false
	 */
	public void removePicture(Picture picture) {}
	
	/**
	 * Sets the User's active status.
	 */
	public void setActive(boolean active) {
		this.setFieldValue("active", active);
	}
	
	/**
	 * Set the User's note.
	 */
	public void setNote(String note) {
		this.setFieldValue("note", note);
	}
	
	/**
	 * Sets the password for this user.
	 * 
	 * @post this.password == hash_function(password)
	 */
	public void setPassword(String password) {
		// generate a salt
		Random r = new SecureRandom();
		byte[] salt = new byte[2];
		r.nextBytes(salt);
		byte[] pass = password.getBytes();
		
		// hash salt + password
		this.setFieldValue("password", this.hashPassword(pass, salt));
	}
}

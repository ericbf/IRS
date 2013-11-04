package ISIS.user;

import ISIS.database.DB;
import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.database.RecordNotFoundException;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Dates;
import ISIS.misc.Picture;
import ISIS.session.Session;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	 * Public constructor. A User starts with all fields populated.
	 * 
	 * @post this.password == hash_function(password)
	 */
	public User(String username, boolean active, String password, String fname,
			String lname, String note) {
		super("user", true);
		this.initializeFields(getFields());
		
		this.setFieldValue("username", username);
		this.setFieldValue("active", active);
		this.setFieldValue("fname", fname);
		this.setFieldValue("lname", lname);
		this.setFieldValue("note", note);
		this.setPassword(password);
	}
	
	/**
	 * Public constructor. Takes a User database key, and has the option to
	 * populate the fields from the database.
	 */
	public User(Integer pkey, boolean populate) throws SQLException,
			RecordNotFoundException {
		super("user", true);
		this.initializeFields(getFields());
		
		this.setPkey(pkey);
		if (populate) {
			this.fetch();
		}
	}
	
	/**
	 * Tries to log a user in.
	 */
	public User(String username, String password) throws SQLException,
			AuthenticationException {
		super("user", true);
		this.initializeFields(getFields());
		
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
	
	private HashMap<String, Field> getFields() {
		HashMap<String, Field> fields = new HashMap<>(7);
		fields.put("pkey", new Field(false));
		fields.put("active", new Field(true));
		fields.put("username", new Field(false));
		fields.put("password", new Field(true));
		fields.put("fname", new Field(false));
		fields.put("lname", new Field(false));
		fields.put("note", new Field(true));
		return fields;
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
	 * Get the employee's ID.
	 */
	public int getEmployeeID() {
		return (Integer) this.getFieldValue("pkey");
	}
	
	/**
	 * Sets the User's active status.
	 */
	public void setActive(boolean active) {
		this.setFieldValue("active", active);
	}
	
	/**
	 * Gets the User's active status.
	 */
	public boolean getActive() {
		return ((int) this.getFieldValue("active") == 1) ? true : false;
	}
	
	/**
	 * Gets the User's username.
	 */
	public String getUsername() {
		return (String) this.getFieldValue("username");
	}
	
	/**
	 * Converts byte array to hex.
	 */
	private static String bytesToHex(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		
		return result;
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
	 * Gets sha1 of salt + password, returns salt + hash (for reversibility)
	 */
	private String hashPassword(byte[] password, byte[] salt) {
		// append password to salt
		byte[] saltedpass = new byte[salt.length + password.length];
		System.arraycopy(salt, 0, saltedpass, 0, salt.length);
		System.arraycopy(password, 0, saltedpass, salt.length, password.length);
		
		// hash
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			ErrorLogger.error("SHA-1 not implemented..", true, true);
		}
		byte[] hash = md.digest(saltedpass);
		return bytesToHex(salt) + bytesToHex(hash);
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
		this.setFieldValue("password", hashPassword(pass, salt));
	}
	
	/**
	 * Gets the password for the purposes of updating the record.
	 */
	public String getPassword() {
		return (String) this.getFieldValue("password");
	}
	
	/**
	 * Checks the provided password against the stored hash.
	 */
	public boolean checkPassword(String password) {
		// get salt from DB, hash it with the password given
		if (((String) this.getFieldValue("password")).length() < 4) {
			return false;
		}
		return hashPassword(
				password.getBytes(),
				hexToBytes(((String) this.getFieldValue("password")).substring(
						0, 4))).equals(
				((String) this.getFieldValue("password")));
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
	 * Set the User's note.
	 */
	public void setNote(String note) {
		this.setFieldValue("note", note);
	}
	
	/**
	 * Get the User's note.
	 */
	public String getNote() {
		return (String) this.getFieldValue("note");
	}
	
	/**
	 * Adds a picture to the User record.
	 * 
	 * @pre getPictures().contains(picture) == false
	 * @pre getPictures().contains(picture) == true
	 */
	public void addPicture(Picture picture) {}
	
	/**
	 * Removes a picture associated with the User record. picture.
	 * 
	 * @pre getPictures().contains(picture) == true
	 * @pre getPictures().contains(picture) == false
	 */
	public void removePicture(Picture picture) {}
	
	/**
	 * Gets the pictures associated with the User record.
	 */
	public ArrayList<Picture> getPictures(Picture picture) {
		return null;
	}
}

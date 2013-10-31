package ISIS.user;

import ISIS.database.Record;
import ISIS.database.RecordEmptyFieldException;
import ISIS.database.RecordNotFoundException;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Dates;
import ISIS.misc.Picture;
import ISIS.session.Session;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class representing the person who is employed by the client and is using IRS. It consists of information about
 * the user, such as his or her username, employee id, password, and etcetera.
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
public final class User extends Record {

    private Integer pkey;
    private String username = null;
    private Boolean active = null;
    private String password = null;
    private String fname = null;
    private String lname = null;
    private String note = null;

    /**
     * Public constructor. A User starts with all fields populated.
     *
     * @post this.password == hash_function(password)
     */
    public User(String username, boolean active, String password, String fname, String lname, String note) {
        this.username = username;
        this.active = active;
        this.setPassword(password);
        this.fname = fname;
        this.lname = lname;
        this.note = note;
    }

    /**
     * Public constructor. Takes a User database key, and has the option to populate the fields from the database.
     */
    public User(int pkey, boolean populate) throws SQLException, RecordNotFoundException {
        this.pkey = pkey;
        if (populate) {
            this.fetch();
        }
    }

    /**
     * Get the employee's ID.
     */
    public int getEmployeeID() {
        if (this.pkey == null) {
            this.pkey = (Integer) this.getField("pkey");
        }
        return this.pkey;
    }

    /**
     * Sets the User's active status.
     */
    public void setActive(boolean active) {
        this.active = true;
    }

    /**
     * Gets the User's active status.
     */
    public boolean getActive() {
        if (this.active == null) {
            if (this.getField("active") != null) { // if it's null, leave it alone (shouldn't be, though).
                this.active = ((int) this.getField("active") == 1) ? true : false;
            }
        }
        return this.active;
    }

    /**
     * Gets the User's username.
     */
    public String getUsername() {
        if (this.username == null) {
            this.username = (String) this.getField("username");
        }
        return this.username;
    }

    /**
     * Converts byte array to hex.
     */
    private static String bytesToHex(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Gets sha1 of salt + password, returns salt + hash (for reversibility)
     */
    private String hashPassword(byte[] password, byte[] salt) {
        //append password to salt
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
        //generate a salt
        Random r = new SecureRandom();
        byte[] salt = new byte[2];
        r.nextBytes(salt);
        byte[] pass = password.getBytes();

        //hash salt + password
        this.password = hashPassword(pass, salt);
    }

    /**
     * Gets the password for the purposes of updating the record.
     */
    public String getPassword() {
        if (this.password == null) {
            this.password = (String) this.getField("password");
        }
        return this.password;
    }

    /**
     * Checks the provided password against the stored hash.
     */
    public boolean checkPassword(String password) {
        if (this.password == null) {
            this.password = (String) this.getField("password");
        }
        //get salt from DB, hash it with the password given
        return hashPassword(password.getBytes(), hexToBytes(this.password.substring(0, 4))).equals(this.password);
    }

    /**
     * Gets the User's first name.
     */
    public String getFirstName() {
        if (this.fname == null) {
            this.fname = (String) this.getField("fname");
        }
        return this.fname;
    }

    /**
     * Gets the User's last name.
     */
    public String getLastName() {
        if (this.lname == null) {
            this.lname = (String) this.getField("lname");
        }
        return this.lname;
    }

    /**
     * Set the User's note.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Get the User's note.
     */
    public String getNote() {
        if (this.note == null) {
            this.note = (String) this.getField("note");
        }
        return this.note;
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
        return null;
    }

    /**
     * Gets the dates associated with this transaction.
     */
    public Dates getDates() {
        return null;
    }

    @Override
    public void fetch() throws SQLException, RecordNotFoundException {
        PreparedStatement stmt = Session.getDB().prepareStatement("SELECT * FROM USER WHERE pkey=?");
        stmt.setInt(1, this.pkey);
        fields = Record.mapResultSet(stmt.executeQuery());
    }

    @Override
    public void save() throws SQLException {
        if (this.pkey == null) { // This record doesn't yet exist; create it.
            PreparedStatement stmt = Session.getDB().prepareStatement("INSERT INTO USER (username, active, password, fname, lname, note) VALUES (?, ?, ?, ?, ?, ?)");
            int idx = 0;
            stmt.setString(++idx, username);
            stmt.setBoolean(++idx, active);
            stmt.setString(++idx, password);
            stmt.setString(++idx, fname);
            stmt.setString(++idx, lname);
            stmt.setString(++idx, note);
            stmt.executeUpdate();
        } else { // Record needs update.
            PreparedStatement stmt = Session.getDB().prepareStatement("UPDATE user SET active=?, password=?, note=? WHERE pkey=?");
            int idx = 0;
            stmt.setBoolean(++idx, getActive());
            stmt.setString(++idx, getPassword());
            stmt.setString(++idx, getNote());
            stmt.setInt(++idx, getPkey());
            stmt.executeUpdate();
        }
    }
}

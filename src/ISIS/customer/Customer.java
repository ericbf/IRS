package ISIS.customer;

import ISIS.database.*;
import ISIS.gui.ErrorLogger;
import ISIS.misc.Address;
import ISIS.misc.Phone;
import ISIS.misc.Picture;
import ISIS.session.Session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Customer is the entity that intends to purchase products from the client. A
 * customer record consists of a status field, an email address, a password, an
 * address, a note, a phone number, a first and last name, an initiating user,
 * and, optionally, a picture. Invariants: The Customer record references
 * exactly one set of associated dates.
 *
 * @dates != null The Customer record references a customer status field.
 * @active == true || active == false The customer record has a primary key
 * field that is always set.
 * @pkey > 0 The customer record name fields are always set.
 * @fname != null, lname != null, mname != null
 */
public class Customer extends Record {

    // private ArrayList<Address> addresses;
    private ArrayList<Phone> numbers = null;
    private ArrayList<Phone> numbersToRemove = new ArrayList<>();

    /**
     * Public constructor. Take a Customer database key, and has the option to
     * populate the fields from the database.
     */
    public Customer(int pkey, boolean populate) throws SQLException {
        super("customer", true);
        this.initializeFields(this.getFields());

        this.setPkey(pkey);
        if (populate) {
            this.fetch();
        }
    }

    public Customer(HashMap<String, Field> map) {
        super("customer", true);
        this.initializeFields(map);
    }

    /**
     * Public constructor. A Customer starts with a name and a status.
     */
    public Customer(String fname, String lname, String email, String note, String password, boolean active) {
        super("customer", true);
        this.initializeFields(this.getFields());

        this.setFieldValue("fname", fname);
        this.setFieldValue("lname", lname);
        this.setFieldValue("email", email);
        this.setFieldValue("note", note);
        this.setFieldValue("password", password);
        this.setFieldValue("active", active);
    }

    /**
     * This table's fields.
     */
    private HashMap<String, Field> getFields() {
        HashMap<String, Field> fields = new HashMap<>(7);
        fields.put("pkey", new Field(false));
        fields.put("active", new Field(true));
        fields.put("password", new Field(true));
        fields.put("fname", new Field(false));
        fields.put("lname", new Field(false));
        fields.put("email", new Field(true));
        fields.put("note", new Field(true));
        return fields;
    }

    @Override
    protected void postSave() throws SQLException {
        //save any new phone numbers
        if (this.numbers == null) {
            return;
        }
        ArrayList<Integer> keys = new ArrayList<>(this.numbers.size());
        StringBuilder args = new StringBuilder();
        for (Phone number : this.numbers) {
            try {
                number.save();
                if (args.length() == 0) {
                    args.append("(?, ?)");
                } else {
                    args.append(", (?, ?)");
                }
                keys.add(number.getPkey());
            } catch (SQLException e) {
                ErrorLogger.error(e, "Saving a phone number failed.", true, true);
                throw e;
            }
        } if (keys.size() < 1) {
            return;
        }
        String sql = "INSERT INTO customer_phone (customer, phone) VALUES " + args.toString();
        try {
            PreparedStatement stmt = Session.getDB().prepareStatement(sql);
            int i = 1;
            for (Integer key : keys) {
                stmt.setInt(i++, this.getPkey());
                stmt.setInt(i++, key);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            ErrorLogger.error(e, "Could not save phone numbers.", true, true);
            throw e;
        }
    }

    /**
     * Adds an address to the customer record.
     */
    public void addAddress(Address address) {
    }

    /**
     * Adds a phone number to the customer record.
     *
     * @pre getPhoneNums().contains(phone) == false
     * @post getPhoneNums().contains(phone) == true
     */
    public void addPhoneNum(Phone phone) {
//        if(this.getPkey())
        this.getPhoneNums();
        if (this.numbers == null) {
            throw new RecordSaveException("Couldn't add new phone number: couldn't fetch phone numbers.");
        }
        this.numbers.add(phone);
    }

    /**
     * Adds a picture to the customer record.
     *
     * @pre getPictures().contains(picture) == false
     * @pre getPictures().contains(picture) == true
     */
    public void addPicture(Picture picture) {
    }

    /**
     * Gets all addresses associated with the customer record.
     */
    public ArrayList<Address> getAddresses() {
        return null;
    }

    /**
     * Gets the Customer's email address.
     */
    public String getEmail() {
        return (String) this.getFieldValue("email");
    }

    /**
     * Sets the Customer's email address.
     */
    public void setEmail(String email) {
        this.setFieldValue("email", email);
    }

    /**
     * Gets the Customer's first name.
     */
    public String getFirstName() {
        return (String) this.getFieldValue("fname");
    }

    /**
     * Gets the Customer's last name.
     */
    public String getLastName() {
        return (String) this.getFieldValue("lname");
    }

    /**
     * Gets the Customer's note field.
     */
    public String getNote() {
        return (String) this.getFieldValue("note");
    }

    /**
     * Sets the Customer's note field.
     */
    public void setNote(String note) {
        this.setFieldValue("note", note);
    }

    /**
     * Gets the Customer's password.
     */
    public String getPassword() {
        return (String) this.getFieldValue("password");
    }

    /**
     * Sets the Customer's password.
     */
    public void setPassword(String password) {
        this.setFieldValue("password", password);
    }

    /**
     * Gets all phone numbers and information associated with the numbers from
     * the customer record. Returns empty list on failure.
     */
    public ArrayList<Phone> getPhoneNums() {
        if (this.numbers != null) {
            return this.numbers;
        }
        try {
            this.getPkey(); //check if the record has ever been saved.
        } catch (UninitializedFieldException e) {
            this.numbers = new ArrayList<>();
            return this.numbers;
        }
        String sql = "SELECT p.* FROM customer_phone AS cp LEFT JOIN phone AS p ON cp.phone=p.pkey " + "WHERE cp.customer=?";
        try {
            PreparedStatement stmt = Session.getDB().prepareStatement(sql);
            stmt.setInt(1, this.getPkey());
            ArrayList<HashMap<String, Field>> results = DB.mapResultSet(stmt.executeQuery());
            ArrayList<Phone> numbers = new ArrayList<>(results.size());
            for (HashMap<String, Field> result : results) {
                numbers.add(new Phone(result));
            }
            this.numbers = numbers;
            return this.numbers;
        } catch (SQLException e) {
            ErrorLogger.error(e, "Failed to retrieve phone numbers.", true, true);
            return new ArrayList<>();
        }
    }

    /**
     * Gets the primary phone number. Returns null if there are no numbers, or an arbitrary number if there is no primary.
     */
    public Phone getPrimaryNum() {
        for (Phone number : this.getPhoneNums()) {
            if (number.getPrimary()) {
                return number;
            }
        }
        if (this.getPhoneNums().size() == 0) {
            return null;
        } else {
            return (Phone) this.getPhoneNums().get(0);
        }
    }

    /**
     * Gets the pictures associated with the customer record.
     */
    public ArrayList<Picture> getPictures(Picture picture) {
        return null;
    }

    /**
     * Gets the active status of the Customer.
     */
    public boolean isActive() {
        return (((Integer) this.getFieldValue("active")) == 1 ? true : false);
    }

    /**
     * Allows you to set the active status of the Customer.
     */
    public void setActive(boolean active) {
        this.setFieldValue("active", ((active) ? 1 : 0));
    }

    /**
     * Remove an address from the customer record.
     *
     * @pre getAddresses().contains(address) == true
     * @post getAddresses().contains(address) == false
     */
    public void removeAddress(Address address) {
    }

    /**
     * Removes a phone number from the customer record.
     *
     * @pre getPhoneNums().contains(phone) == true
     * @post getPhoneNums().contains(phone) == false
     */
    public void removePhoneNum(Phone phone) {
        getPhoneNums();
        if (this.numbers == null) {
            throw new RecordSaveException("Could not remove phone number: couldn't fetch phone numbers.");
        } else {
            if (this.numbers.contains(phone)) {
                this.numbersToRemove.add(phone);
            }
            throw new RecordSaveException("Could not remove phone number: couldn't find phone number to remove.");
        }
    }

    /**
     * Removes a picture from the Customer record.
     *
     * @pre getPictures().contains(picture) == true
     * @pre getPictures().contains(picture) == false
     */
    public void removePicture(Picture picture) {
    }
}

package ISIS.misc;

import ISIS.database.Field;
import ISIS.database.Record;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Phone is a generic class for phone numbers. Invariants: The Transaction
 * references exactly one number, one primary status, and one type.
 *
 * @number != null, primary == true || primary == false, PhoneType != null
 */
public class Phone extends Record {

    /**
     * Public constructor. A Phone record starts with a number, primary status,
     * and type (mobile/home/etc).
     */
    public Phone(String number, boolean primary, PhoneType type) {
        super("phone", true);
        this.setFieldValue("number", number);
        this.setFieldValue("primary_num", (primary ? 1 : 0));
        this.setFieldValue("type", type.toString());
    }

    /**
     * Public constructor. Take a Phone database key, and has the option to
     * populate the fields from the database.
     */
    public Phone(int pkey, boolean populate) throws SQLException {
        super("phone", true);

        this.setPkey(pkey);
        if (populate) {
            this.fetch();
        }
    }

    public Phone(HashMap<String, Field> map) {
        super("phone", true, map);
    }

    /**
     * Gets the phone number.
     */
    public String getNumber() {
        return (String) this.getFieldValue("number");
    }

    /**
     * Returns the phone number's primary status.
     */
    public boolean getPrimary() {
        return ((Integer) this.getFieldValue("primary_num")) == 1;
    }

    /**
     * Sets the phone number's primary status.
     */
    public void setPrimary(Boolean primary) {
        this.setFieldValue("primary_num", (primary ? 1 : 0));
    }

    /**
     * Returns the type of the phone number.
     */
    public PhoneType getType() {
        return PhoneType.valueOf((String) this.getFieldValue("type"));
    }

    /**
     * The type of the phone number.
     */
    public static enum PhoneType {

        HOME, CELL, FAX
    }
}

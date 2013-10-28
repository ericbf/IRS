package ISIS.misc;

/**
 * Phone is a generic class for phone numbers.
 *
 * Invariants:
 *
 * The Transaction references exactly one number, one primary status, and one
 * type.
 *
 * @number != null, primary == true || primary == false, PhoneType != null
 */
public class Phone {
    /* Fields omitted */

    /**
     * The type of the phone number.
     */
    public static enum PhoneType {

        HOME, CELL, FAX
    }

    /**
     * Public constructor. A Phone record starts with a number, primary status,
     * and type (mobile/home/etc).
     */
    public Phone(String number, boolean primary, PhoneType type) {
    }

    /**
     * Gets the phone number.
     */
    public String getNumber() {
    }

    /**
     * Returns the phone number's primary status.
     */
    public boolean getPrimary() {
    }

    /**
     * Returns the type of the phone number.
     */
    public PhoneType getType() {
    }
}

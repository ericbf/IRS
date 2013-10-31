package ISIS.misc;

import ISIS.database.Record;

/**
 * A picture class for storing a picture and various metadata. This metadata includes a name, a note, and dates
 * associated with the picture. A picture instance is always referenced from another record.
 */
public class Picture extends Record {
    /* Fields omitted */

    /**
     * Public constructor. A picture starts with a name, binary representation, a note, and dates associated with its
     * creation.
     */
    public Picture(String name, byte[] picture, String note, Dates dates) {
    }

    /**
     * Public constructor. Takes a picture database key, and has the option to populate fields from the database.
     */
    public Picture(int pkey, boolean populate) {
    }

    /**
     * Gets the name associated with the picture.
     */
    public String getName() {
	return null;
    }

    /**
     * Gets the note associated with the picture.
     */
    public String getNote() {
	return null;
    }

    /**
     * Gets the dates associated with the picture.
     */
    public Dates getDates() {
	return null;
    }

    /**
     * Gets the picture associated with the picture record.
     */
    public byte[] getPicture() {
	return null;
    }
}

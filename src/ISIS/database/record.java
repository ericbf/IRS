package ISIS.database;

/**
 * Base class for all record classes.
 */
public abstract class Record {
    /* Fields omitted */

    /**
     * Gets the primary key associated with this record.
     */
    protected int getPkey() {
    }

    /**
     * Fetches the record in the database associated with getPkey(), using the
     * fetchMode specified. fetchMode is an enum specifying what type of column
     * should be retrieved.
     */
    public void fetch(fetchMode mode) {
    }

    /**
     * Saves the record in the database. If the record is not in the database it
     * is inserted, otherwise it is updated.
     */
    public void save(saveMode mode) {
    }
}

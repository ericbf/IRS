/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ISIS.database;

/**
 *
 * @author michaelm
 */
public final class Field {

    private Object field = null;
    private Boolean fetched = false;
    private Boolean changed = false;
    private Boolean modifiable;
    private Boolean initialized = false;

    public Field(boolean modifiable) {
        this.modifiable = modifiable;
    }

    /**
     * To make a field unmodifiable.
     */
    public void setUnmodifiable() {
        this.modifiable = false;
    }

    /**
     * Check if a field can be modified.
     */
    public Boolean isUnmodifiable() {
        return !modifiable;
    }

    /**
     * For when value is retrieved from database.
     */
    public void initField(Object value) {
        initialized = true;
        fetched = true;
        this.field = value;
    }

    /**
     * For use in record classes.
     */
    public void setValue(Object value) {
        if (this.initialized && !this.modifiable) {
            throw new UnmodifiableFieldException();
        }
        this.field = value;
        this.changed = true;
    }

    /**
     * Gets the field.
     */
    public Object getValue() {
        this.initialized = true;
        if (!this.changed && !this.fetched) {
            throw new UninitializedFieldException();
        }
        return field;
    }

    /**
     * Checks if this record was initialized.
     */
    public Boolean getWasInitialized() {
        return this.initialized;
    }

    /**
     * Checks if this field was populated from the database (at some point).
     */
    public Boolean getWasFetched() {
        return this.fetched;
    }

    /**
     * Checks if this field was changed in a record class.
     */
    public Boolean getWasChanged() {
        return this.changed;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ISIS.database;

public final class Field {

    private Object field = null; // the value
    private Boolean changed = false; // whether it has been modified or not
    private Boolean modifiable; // whether it can be modified
    private Boolean initialized = false; // whether anything has been put in it (to tell null apart from uninitialzied)

    public Field(boolean modifiable) {
        this.modifiable = modifiable;
    }

    /**
     * For when value is retrieved from database.
     */
    public void initField(Object value) {
        initialized = true;
        this.field = value;
    }

    /**
     * For use in record classes.
     */
    public void setValue(Object value) {
        if (this.initialized && !this.modifiable) {
            throw new UnmodifiableFieldException();
        }
        this.initialized = true;
        this.field = value;
        this.changed = true;
    }

    /**
     * Gets the field.
     */
    public Object getValue() {
        if (!this.initialized) {
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
     * Makes a field unmodifiable.
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
     * Checks if this field was changed in a record class.
     */
    public Boolean getWasChanged() {
        return this.changed;
    }
}

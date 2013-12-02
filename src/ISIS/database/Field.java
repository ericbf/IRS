package ISIS.database;

public final class Field {
	
	private Object	field		= null;	// the value
	private Boolean	changed		= false;	// whether it has been modified or
											// not
	private Boolean	initialized	= false;	// whether anything has been put in
											// it (to tell null apart from
											// uninitialzied)
											
	public Field() {}
	
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
	 * Checks if this field was changed in a record class.
         * 
         * @pre isOpen == true
         * @post - return bool
	 */
	public Boolean getWasChanged() {
		return this.changed;
	}
	
	/**
	 * Checks if this record was initialized.
         * 
         * @pre isOpen == true
         * returns bool
	 */
	public Boolean getWasInitialized() {
		return this.initialized;
	}
	
	/**
	 * For when value is retrieved from database.
         * @pre isOpen == true
         * @post - set initialized value
         * 
	 */
	public void initField(Object value) {
		initialized = true;
		this.field = value;
	}
	
	void save() {
		this.changed = false;
	}
	
	/**
	 * For use in record classes.
         * 
         * @pre isOpen == true
         * @post - sets field value
	 */
	public void setValue(Object value) {
		if (value.equals(this.field)) {
			return; // old value == new value, do nothing.
		}
		this.initialized = true;
		this.field = value;
		this.changed = true;
	}
}

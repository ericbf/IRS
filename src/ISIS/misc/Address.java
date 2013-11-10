package ISIS.misc;

import ISIS.database.Field;
import ISIS.database.Record;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * General purpose class for representing postal addresses.
 */
public class Address extends Record {

    /**
     * Public constructor.
     */
    public Address(boolean active, boolean primary, String country, String title, String zip, String state, String city, String county,
                   String streetAddress) {
        super("address", true);
        this.initializeFields(this.getFields());
        this.setFieldValue("active", (active ? 1 : 0));
        this.setFieldValue("primary_status", (primary ? 1 : 0));
        this.setFieldValue("title", title);
        this.setFieldValue("country", country);
        this.setFieldValue("county", county);
        this.setFieldValue("zip", zip);
        this.setFieldValue("state", state);
        this.setFieldValue("city", city);
        this.setFieldValue("st_address", streetAddress);
    }

    /**
     * Public constructor. Take an address database key, and has the option to
     * populate the fields from the database.
     */
    public Address(int pkey, boolean populate) throws SQLException {
        super("address", true);
        this.initializeFields(this.getFields());

        this.setPkey(pkey);
        if (populate) {
            this.fetch();
        }
    }

    public Address(HashMap<String, Field> map) {
        super("address", true, map);
    }

    /**
     * gets the record's primary status
     */
    public boolean getPrimary() {
        return ((Integer) this.getFieldValue("primary_status")) == 1;

    }

    /**
     * Sets the record's primary status
     */
    public void setPrimary(boolean primary) {
        this.setFieldValue("primary", (primary ? 1 : 0));

    }

    /**
     * This table's fields.
     */
    private HashMap<String, Field> getFields() {
        HashMap<String, Field> fields = new HashMap<>(4);
        fields.put("pkey", new Field(false));
        fields.put("active", new Field(true));
        fields.put("primary_status", new Field(true));
        fields.put("title", new Field(false));
        fields.put("city", new Field(false));
        fields.put("state", new Field(false));
        fields.put("zip", new Field(false));
        fields.put("county", new Field(false));
        fields.put("country", new Field(false));
        fields.put("st_address", new Field(false));
        return fields;
    }

    /**
     * Gets the record's active status.
     */
    public Boolean getActive() {
        return ((Integer) this.getFieldValue("active")) == 1;
    }

    /**
     * Sets the record's active status.
     */
    public void setActive(Boolean active) {
        this.setFieldValue("active", (active ? 1 : 0));
    }

    /**
     * Gets the country.
     */
    public String getCountry() {
        return (String) this.getFieldValue("country");
    }

    /**
     * Gets the ZIP.
     */
    public String getZIP() {
        return (String) this.getFieldValue("zip");
    }

    /**
     * Gets the state.
     */
    public String getState() {
        return (String) this.getFieldValue("state");
    }

    /**
     * Gets the city.
     */
    public String getCity() {
        return (String) this.getFieldValue("city");
    }

    /**
     * Gets the county.
     */
    public String getCounty() {
        return (String) this.getFieldValue("county");
    }

    /**
     * Gets the street address.
     */
    public String getStreetAddress() {
        return (String) this.getFieldValue("st_address");
    }
}

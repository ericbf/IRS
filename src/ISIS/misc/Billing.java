package ISIS.misc;

import ISIS.database.Field;
import ISIS.database.Record;
import ISIS.gui.ErrorLogger;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * General purpose class for representing billing information.
 */
public class Billing extends Record {
    Address address = null;

    /**
     * Gets a set of billing info from the DB using the given key.
     */
    public Billing(int pkey, boolean populate) throws SQLException {
        super("billing", true);

        this.setPkey(pkey);
        if (populate) {
            this.fetch();
        }
    }

    /**
     * Public constructor for CREDIT billing.
     */
    public Billing(Address address, String cardNumber, Date expDate, String CCV) {
        super("billing", true);

        this.setFieldValue("type", BillingType.CREDIT.toString());
        this.address = address;
        if (address != null) {
            this.setFieldValue("address", address.getPkey());
        }

        this.setFieldValue("number", cardNumber);
        SimpleDateFormat df = new SimpleDateFormat("MM/yy");
        this.setFieldValue("expiration", df.format(expDate));
        this.setFieldValue("CCV", CCV);
    }

    /**
     * Public constructor for CASH or OTHER billing.
     */
    public Billing(Address address, BillingType billingType) {
        super("billing", true);

        if (billingType.equals(BillingType.CREDIT)) {
            throw new RuntimeException("COMMENTS.");
        }
        this.setFieldValue("type", billingType.toString());

        if (address != null) {
            this.setFieldValue("address", address.getPkey());
        }
    }

    public Billing(HashMap<String, Field> map) {
        super("billing", true, map);
    }

    /**
     * Gets the billing type associated with the record.
     */
    public BillingType getBillingType() {
        return BillingType.valueOf(((String) this.getFieldValue("type")));
    }

    /**
     * Gets the billing address.
     */
    public Address getAddress() throws SQLException {
        if (this.address == null) {
            return new Address((int) this.getFieldValue("address"), false);
        } else {
            return this.address;
        }
    }

    /**
     * Gets the credit card number.
     *
     * @pre getBillingType == BillingType.CREDIT
     */
    public String getCardNumber() {
        if (this.getBillingType().equals(BillingType.CREDIT)) {
            return (String) this.getFieldValue("number");
        } else {
            throw new RuntimeException("Cannot get card number unless the billing info is a card!");
        }
    }

    /**
     * Gets the credit card number.
     *
     * @pre getBillingType == BillingType.CREDIT
     */
    public String getCCV() {
        if (this.getBillingType().equals(BillingType.CREDIT)) {
            return (String) this.getFieldValue("CCV");
        } else {
            throw new RuntimeException("Cannot get CCV unless the billing info is a card!");
        }
    }

    public Date getExpiration() {
        try {
            Date date = new SimpleDateFormat("MM/yy", Locale.ENGLISH).parse((String) this.getFieldValue("expiration"));
            return date;
        } catch (ParseException e) {
            ErrorLogger.error(e, "Failed to parse expiration date.", true, false);
            throw new RuntimeException("Failed to retrieve date");
        }
    }

    public static enum BillingType {

        CREDIT, CASH, OTHER
    }
}

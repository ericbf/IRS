package ISIS.transaction;

import ISIS.customer.Customer;
import ISIS.database.Record;
import ISIS.item.Item;
import ISIS.misc.Address;
import ISIS.misc.Billing;
import ISIS.misc.Dates;
import ISIS.user.User;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * A Transaction is the exchange of goods, legal tender, or rendering of services between the client and a customer. A
 * Transaction consists of a customer, an intiating user, a set of items and information pertaining to the items,
 * billing information, various date/time information, and shipping information.
 *
 * Invariants:
 *
 * The Transaction references exactly one customer record.
 *
 * @customer != null
 *
 * The Transaction references exactly one user record.
 * @user != null
 *
 * The Transaction references exactly one set of associated dates.
 * @dates != null
 *
 * The transaction has a primary key field that is always set.
 * @pkey > 0
 */
public class Transaction extends Record {

    @Override
    public void fetch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /* Fields omitted */

    /**
     * TransactionStatus is a enumeration describing the status of a transaction.
     */
    public static enum TransactionStatus {

	ACTIVE, CLOSED, BILLED, UNDER_REVIEW, ABORTED
    }

    /**
     * Public constructor. A Transaction starts with a user and a customer. These attributes cannot be changed.
     */
    public Transaction(User user, Customer customer) {
    }

    /**
     * Public constructor. A return Transaction starts with a modifying user and a transaction. This attributes cannot
     * be changed.
     *
     * @post getModified() == true
     * @post getType() == type.Sale
     * @post getStatus() == status.Finalized
     * @post getAddress() != null
     * @post getBilling() != null
     * @post getItems().size() > 0
     */
    public Transaction(User user, Transaction originalSale) {
    }

    /**
     * Public constructor. Takes a transaction database key, and has the option to populate the fields from the
     * database.
     */
    public Transaction(int pkey, boolean populate) {
    }

    /**
     * Gets a list of returns that have been made on the transaction, or the transaction that this return is being made
     * on as well as returns associated with it. If the transaction is not associated with a return, an empty list is
     * returned.
     */
    public ArrayList<Transaction> getRelatedTransactions() {
	return null;
    }

    /**
     * Adds an item to this transaction.
     *
     * @pre getItems().contains(item) == false
     * @post getItems().contains(item) == true
     */
    public void addItem(Item item, BigDecimal adjustment, BigDecimal quantity, String note) {
    }

    /**
     * Removes an item from this transaction.
     *
     * @pre getItems().contains(item) == true
     * @post getItems().contains(item) == false
     */
    public void removeItem(Item item) {
    }

    /**
     * Modifies attributes of an item associated with this transaction.
     *
     * @pre getItems().contains(item) == true
     */
    public void modItem(Item item, BigDecimal adjustment, BigDecimal quantity, String note) {
    }

    /**
     * Gets information associated with all involved items in this transaction.
     */
    public ArrayList<TransactionLineItem> getItems() {
	return null;
    }

    /**
     * Sets the billing information for this transaction.
     */
    public void setBilling(Billing billing) {
    }

    /**
     * Gets the billing information associated with this transaction.
     */
    public Billing getBilling() {
	return null;
    }

    /**
     * Sets the address for this transaction.
     */
    public void setAddress(Address address) {
    }

    /**
     * Gets the address associated with this transaction.
     */
    public Address getAddress() {
	return null;
    }

    /**
     * Gets status associated with this transaction.
     */
    public TransactionStatus getStatus() {
	return null;
    }

    /**
     * Checks whether any returns have been made that reference this transaction.
     *
     * @pre getStatus() == status.Finalized
     */
    public boolean isModified() {
	return false;
    }

    /**
     * Gets user (employee) associated with this transaction.
     */
    public User getUser() {
	return null;
    }

    /**
     * Gets customer associated with this transaction.
     */
    public Customer getCustomer() {
	return null;
    }

    /**
     * Gets the dates associated with this transaction.
     */
    public Dates getDates() {
	return null;
    }

    /**
     * Completes this transaction. No finalized transaction can be modified.
     *
     * @pre getStatus() != status.Finalized
     * @pre getAddress() != null
     * @pre getBilling() != null
     * @pre getItems().size() > 0
     * @post getStatus() == status.Finalized
     */
    public void finalize() {
    }
}

package ISIS.gui.customer;

import ISIS.gui.View;

/**
 * View for adding and editing customers.
 */
public class AddEditCustomer extends View {

    /**
     * Public constructor: returns new instance of add/edit customer view.
     */
    public AddEditCustomer() {
    }

    /**
     * This view needs to be saved.
     */
    @Override
    public boolean needsSave() {
        return true;
    }

    /**
     * Saves the customer.
     */
    @Override
    public void save() {
    }

    /**
     * Discards any modifications.
     */
    @Override
    public void cancel() {
    }
}

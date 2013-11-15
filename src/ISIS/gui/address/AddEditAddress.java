package ISIS.gui.address;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.BadInputException;
import ISIS.gui.SplitPane;
import ISIS.misc.Address;

public class AddEditAddress extends AddEditView {

    private Address address;

    public AddEditAddress(SplitPane splitPane) {
        super(splitPane);
        this.populateElements();
        this.address = null;
    }

    private void populateElements() {
        //TODO: THIS
    }

    @Override
    public void cancel() {
    }

    @Override
    public Record getCurrentRecord() throws BadInputException {
        return null;
    }

    @Override
    public Boolean isAnyFieldDifferentFromDefault() {
        return null;
    }
}

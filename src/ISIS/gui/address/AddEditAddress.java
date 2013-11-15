package ISIS.gui.address;

import ISIS.database.Record;
import ISIS.gui.AddEditView;
import ISIS.gui.BadInputException;
import ISIS.gui.SplitPane;
import ISIS.misc.Address;

public class AddEditAddress extends AddEditView {
	private static final long	serialVersionUID	= 1L;
	@SuppressWarnings("unused")
	private Address				address;
	
	public AddEditAddress(SplitPane splitPane) {
		super(splitPane);
		this.populateElements();
		this.address = null;
	}
	
	@Override
	public void cancel() {}
	
	@Override
	public Record getCurrentRecord() throws BadInputException {
		return null;
	}
	
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		return null;
	}
	
	private void populateElements() {
		// TODO: THIS
	}
}

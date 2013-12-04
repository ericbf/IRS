package ISIS.database;

// TODO: we need to catch all exceptions and runtime exceptions at the window
// level, and exit the view on catching (since we didn't
// explicitly catch them, we don't know if they're recoverable)

// @pre - isOpen == True
// * @post - exception thrown
public class RecordSaveException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;
	
	public RecordSaveException() {
		super();
	}
	
	public RecordSaveException(String message) {
		super(message);
	}
}

package ISIS.database;

/**
 * @author michaelm
 * @pre - isOpen == True
 * @post - exception thrown
 */
public class RecordNotFoundException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;
	
	public RecordNotFoundException() {
		super();
	}
	
	public RecordNotFoundException(String message) {
		super(message);
	}
}

package ISIS.database;

/**
 * @author michaelm
 */
public class RecordNotFoundException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;

    public RecordNotFoundException(String message) {
        super(message);
    }
	
	public RecordNotFoundException() {
		super();
	}
}

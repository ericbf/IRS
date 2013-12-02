package ISIS.database;

/**
 * @author michaelm
 * 
 * @pre - isOpen == True
 * @post - exception thrown
 */
public class UninitializedFieldException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;
	
	public UninitializedFieldException() {
		super();
	}
}

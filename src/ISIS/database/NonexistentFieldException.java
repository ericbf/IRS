/**
 * 
 */
package ISIS.database;

/**
 * @author eric
 */
public class NonexistentFieldException extends RuntimeException {
	private static final long	serialVersionUID	= 1L;
	
	public NonexistentFieldException() {
		super();
	}
	
	public NonexistentFieldException(String message) {
		super(message);
	}
}
package ISIS.user;

/**
 * @author michaelm
 */
public class AuthenticationException extends Exception {
	public enum exceptionType {
		
		USERNAME, PASSWORD, ACTIVE, OTHER
	}
	
	private static final long	serialVersionUID	= 1L;
	
	public exceptionType		type;
	
	/**
	 * Public constructor. Creates new instance of AuthenticationException
	 * 
	 * @param message
	 * @param type
	 */
	public AuthenticationException(String message, exceptionType type) {
		super(message);
		this.type = type;
	}
}

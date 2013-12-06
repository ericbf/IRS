package ISIS.gui;

public class BadInputException extends Exception {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * @param message
	 * @pre - none
	 * @post - none - abstract for override
	 */
	public BadInputException(String message) {
		super(message);
	}
}

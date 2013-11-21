/**
 * 
 */
package ISIS.html.objects;

/**
 * @author eric
 */
public class Header extends HTMLTextContainer<Header> {
	private final int	level;
	
	/**
	 * @param type
	 */
	public Header(int level) {
		super(Type.HEADER);
		
		if (level < 1) {
			throw new IndexOutOfBoundsException("Level can't be below 1");
		}
		this.level = level;
	}
	
	@Override
	public String toString() {
		return "h" + this.level;
	}
}

package ISIS.html;

import org.w3c.dom.ranges.RangeException;

public class StringBuilder {
	private java.lang.StringBuilder	b;
	private int						tabs;
	
	/**
	 * Public constructor: new StringBuilder instance
	 */
	public StringBuilder() {
		this.b = new java.lang.StringBuilder();
		this.tabs = 0;
	}
	
	/**
	 * @param string
	 * @pre - none
	 * @post - append string param to b
	 */
	public void append(String string) {
		this.b.append(string);
	}
	
	/**
	 * @pre - tabs > 0
	 * @post - tabs decremented
	 */
	public void closeBlock() {
		if (this.tabs == 0) {
			throw new RangeException((short) 1, "Tabs can't be negative");
		}
		this.tabs--;
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - returns b
	 */
	public StringBuilder indents() {
		for (int i = 0; i < this.tabs; i++) {
			this.b.append("\t");
		}
		return this;
	}
	
	/**
	 * @param string
	 * @pre - none
	 * @post - \n is appended to b
	 */
	public void ln(String string) {
		String[] strings = string.split("\n");
		for (String str : strings) {
			this.indents();
			this.append(str);
			this.b.append("\n");
		}
	}
	
	/**
	 * @pre - none
	 * @post - tabs incremented
	 */
	public void startBlock() {
		this.tabs++;
	}
	
	@Override
	public String toString() {
		return this.b.toString();
	}
}
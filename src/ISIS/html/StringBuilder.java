package ISIS.html;

import org.w3c.dom.ranges.RangeException;

public class StringBuilder {
	private java.lang.StringBuilder	b;
	private int						tabs;
	
        /*
         * Public constructor: new StringBuilder instance
         */
	public StringBuilder() {
		b = new java.lang.StringBuilder();
		tabs = 0;
	}
	
        /*
         * @pre - none
         * @post - append string param to b
         */
	public void append(String string) {
		b.append(string);
	}
	
        /*
         * @pre - tabs > 0
         * @post - tabs decremented
         */
	public void closeBlock() {
		if (tabs == 0) {
			throw new RangeException((short) 1, "Tabs can't be negative");
		}
		tabs--;
	}
	
        /*
         * @pre - none
         * @post - returns b
         */
	public StringBuilder indents() {
		for (int i = 0; i < tabs; i++) {
			b.append("\t");
		}
		return this;
	}
	
        /*
         * @pre - none
         * @post - \n is appended to b
         */
	public void ln(String string) {
		String[] strings = string.split("\n");
		for (String str : strings) {
			indents();
			append(str);
			b.append("\n");
		}
	}
	
        /*
         * @pre - none
         * @post - tabs incremented
         */
	public void startBlock() {
		tabs++;
	}
	
	@Override
	public String toString() {
		return b.toString();
	}
}
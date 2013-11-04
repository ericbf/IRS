package ISIS.html;

import org.w3c.dom.ranges.RangeException;

public class StringBuilder {
	private java.lang.StringBuilder	b;
	private int						tabs;
	
	public StringBuilder() {
		b = new java.lang.StringBuilder();
		tabs = 0;
	}
	
	public void append(String string) {
		b.append(string);
	}
	
	public void closeBlock() {
		if (tabs == 0)
			throw new RangeException((short) 1, "Tabs can't be negative");
		tabs--;
	}
	
	public StringBuilder indents() {
		for (int i = 0; i < tabs; i++)
			b.append("\t");
		return this;
	}
	
	public void ln(String string) {
		String[] strings = string.split("\n");
		for (String str : strings) {
			indents();
			append(str);
			b.append("\n");
		}
	}
	
	public void startBlock() {
		tabs++;
	}
	
	@Override
	public String toString() {
		return b.toString();
	}
}
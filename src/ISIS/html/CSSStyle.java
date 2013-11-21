package ISIS.html;

import java.util.ArrayList;

public class CSSStyle {
	private final ArrayList<String>	attributes;
	private String					designator;
	
	public CSSStyle(String designator) {
		this.designator = designator;
		this.attributes = new ArrayList<String>();
	}
	
	public CSSStyle addAttribute(String key, String value) {
		if (key == null || value == null || key.trim().isEmpty()
				|| value.trim().isEmpty())
			throw new NullPointerException(
					"Neither the key nor the value can be empty or only whitespace.");
		this.attributes.add(key + ":" + value);
		return this;
	}
	
	public String build() {
		StringBuilder b = new StringBuilder();
		b.append(this.designator);
		b.append(" { ");
		int size;
		if ((size = this.attributes.size()) > 0) {
			b.append(this.attributes.get(0) + (size > 1 ? "" : " "));
			for (int i = 1; i < size; i++)
				b.append("; " + this.attributes.get(i) + " ");
		}
		b.append("}");
		return b.toString();
	}
	
	public String getDesignator() {
		return this.designator;
	}
	
	public CSSStyle pasteAttributes(String str) {
		this.attributes.add(str);
		return this;
	}
}

package ISIS.html;

import java.util.ArrayList;

public class CSSStyle {
	private final ArrayList<String>	attributes;
	private String					designator;
	
	public CSSStyle(String designator) {
		this.designator = designator;
		attributes = new ArrayList<String>();
	}
	
	public CSSStyle addAttribute(String key, String value) {
		if (key == null || value == null || key.trim().isEmpty()
				|| value.trim().isEmpty())
			throw new NullPointerException(
					"Neither the key nor the value can be empty or only whitespace.");
		attributes.add(key + ": " + value + ";");
		return this;
	}
	
	public String build() {
		StringBuilder b = new StringBuilder();
		b.append(designator);
		b.append(" { ");
		for (String s : attributes)
			b.append(s + " ");
		b.append("}");
		return b.toString();
	}
	
	public String getDesignator() {
		return designator;
	}
}

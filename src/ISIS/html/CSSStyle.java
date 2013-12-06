package ISIS.html;

import java.util.ArrayList;

public class CSSStyle {
	private final ArrayList<String>	attributes;
	private String					designator;
	
	/**
	 * Public constructor: creates new CSSStyle instance
	 * 
	 * @param designator
	 */
	public CSSStyle(String designator) {
		this.designator = designator;
		this.attributes = new ArrayList<String>();
	}
	
	/**
	 * @param key
	 * @param value
	 * @return
	 * @pre - key & value == true
	 * @post - returns key + value
	 */
	public CSSStyle addAttribute(String key, String value) {
		if (key == null || value == null || key.trim().isEmpty()
				|| value.trim().isEmpty()) {
			throw new NullPointerException(
					"Neither the key nor the value can be empty or only whitespace.");
		}
		this.attributes.add(key + ":" + value);
		return this;
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - returns newly built CSSStyle string
	 */
	public String build() {
		StringBuilder b = new StringBuilder();
		b.append(this.designator);
		b.append(" { ");
		int size;
		if ((size = this.attributes.size()) > 0) {
			b.append(this.attributes.get(0) + (size > 1 ? "" : " "));
			for (int i = 1; i < size; i++) {
				b.append("; " + this.attributes.get(i) + " ");
			}
		}
		b.append("}");
		return b.toString();
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - returns designator
	 */
	public String getDesignator() {
		return this.designator;
	}
	
	/**
	 * @param str
	 * @return
	 * @pre - none
	 * @post - returns attributes with string param appended
	 */
	public CSSStyle pasteAttributes(String str) {
		this.attributes.add(str);
		return this;
	}
}

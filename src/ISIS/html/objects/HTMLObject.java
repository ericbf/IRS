package ISIS.html.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ISIS.html.StringBuilder;

public class HTMLObject {
	protected enum Type {
		TABLE, TABLE_ROW, TABLE_CELL, TABLE_HEADER, DIVISION, ITALICS, BOLD,
		BREAK, QUOTE, PARAGRAPH, SEPARATOR
	};
	
	private static final Map<Type, String>	types		= new HashMap<>();
	static {
		types.put(Type.TABLE, "table");
		types.put(Type.TABLE_ROW, "tr");
		types.put(Type.TABLE_CELL, "td");
		types.put(Type.TABLE_HEADER, "th");
		types.put(Type.DIVISION, "div");
		types.put(Type.ITALICS, "i");
		types.put(Type.BOLD, "b");
		types.put(Type.QUOTE, "blockquote");
		types.put(Type.PARAGRAPH, "p");
		types.put(Type.BREAK, "br");
		types.put(Type.SEPARATOR, "hr");
	}
	
	private final boolean					container;
	protected final ArrayList<HTMLObject>	objects;
	protected String						type;
	private String							text		= "";
	private String							id			= "";
	private String							HTMLclass	= "";
	private String							attributes	= "";
	
	protected HTMLObject(String text) {
		type = "plain text";
		if (text == null || text.isEmpty())
			throw new NullPointerException(
					"This HTML object cannot be empty of text: " + type);
		this.text = text;
		container = false;
		objects = null;
	}
	
	protected HTMLObject(Type type) {
		this.type = types.get(type);
		container = isContainer(type);
		objects = new ArrayList<>();
	}
	
	protected HTMLObject add(HTMLObject object) {
		objects.add(object);
		return this;
	}
	
	public HTMLObject addAttribute(String key, String value) {
		if (key == null || value == null || key.trim().isEmpty()
				|| value.trim().isEmpty())
			throw new NullPointerException(
					"Neither the key nor the value can be empty or only whitespace.");
		if (attributes.isEmpty()) attributes = key + "=\"" + value + "\"";
		else attributes += " " + key + "=\"" + value + "\"";
		return this;
	}
	
	public HTMLObject addClass(String HTMLclass) {
		if (this.HTMLclass.isEmpty()) this.HTMLclass = HTMLclass;
		else this.HTMLclass += String.format(" %s", HTMLclass);
		return this;
	}
	
	public String build() {
		StringBuilder b = new StringBuilder();
		if (container) {
			b.ln("<" + openTag() + ">");
			b.startBlock();
			if (this instanceof Cell) {
				int i = 0;
				if (i < objects.size())
					b.indents().append(
							objects.get(i++).build()
									+ (objects.size() == 1 ? "\n" : ""));
				for (; i < objects.size() - 1; i++)
					b.append(objects.get(i).build());
				if (i < objects.size())
					b.append(objects.get(i).build() + "\n");
			} else for (HTMLObject o : objects)
				b.ln(o.build());
			b.closeBlock();
			b.ln("</" + type + ">");
		} else {
			if (type.equals("plain text")) b.append(text);
			else {
				if (!(this instanceof HTMLTextContainer)) {
					b.ln("<" + openTag() + "/>");
				} else {
					b.append("<" + openTag() + ">");
					for (HTMLObject o : objects) {
						b.append(o.build());
					}
					b.append("</" + type + ">");
				}
			}
		}
		return b.toString();
	}
	
	public boolean isContainer() {
		return container;
	}
	
	private boolean isContainer(Type type) {
		return Arrays.asList(
				new Type[] { Type.TABLE, Type.TABLE_CELL, Type.DIVISION,
						Type.TABLE_ROW }).contains(type);
	}
	
	protected String openTag() {
		return type + (id.isEmpty() ? "" : " id=\"" + id + "\"")
				+ (HTMLclass.isEmpty() ? "" : " class=\"" + HTMLclass + "\"")
				+ (attributes.isEmpty() ? "" : " " + attributes);
	}
	
	public HTMLObject setID(String id) {
		this.id = id;
		return this;
	}
}

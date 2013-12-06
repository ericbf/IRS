package ISIS.html.objects;

import java.util.ArrayList;

import ISIS.html.StringBuilder;

public class HTMLObject<E> {
	protected enum Type {
		TABLE {
			@Override
			public String toString() {
				return "table";
			}
		},
		TABLE_ROW {
			@Override
			public String toString() {
				return "tr";
			}
		},
		TABLE_CELL {
			@Override
			public String toString() {
				return "td";
			}
		},
		TABLE_HEADER {
			@Override
			public String toString() {
				return "th";
			}
		},
		DIVISION {
			@Override
			public String toString() {
				return "div";
			}
		},
		ITALICS {
			@Override
			public String toString() {
				return "i";
			}
		},
		BOLD {
			@Override
			public String toString() {
				return "b";
			}
		},
		BREAK {
			@Override
			public String toString() {
				return "br";
			}
		},
		QUOTE {
			@Override
			public String toString() {
				return "blockquote";
			}
		},
		PARAGRAPH {
			@Override
			public String toString() {
				return "p";
			}
		},
		SEPARATOR {
			@Override
			public String toString() {
				return "hr";
			}
		},
		HEADER {
			@Override
			public String toString() {
				return "h";
			}
		},
		PLAIN_TEXT;
	};
	
	private final boolean						container;
	protected final ArrayList<HTMLObject<?>>	objects;
	protected Type								type;
	private String								text		= "";
	private String								id			= "";
	private String								HTMLclass	= "";
	private String								attributes	= "";
	
	/**
	 * Protected constructor: new HTMLObject instance
	 */
	protected HTMLObject(String text) {
		this.type = Type.PLAIN_TEXT;
		if (text == null || text.isEmpty()) {
			throw new NullPointerException(
					"This HTML object cannot be empty of text: " + this.type);
		}
		this.text = text;
		this.container = false;
		this.objects = null;
	}
	
	/**
	 * Protected constructor: new HTMLObject instance
	 */
	protected HTMLObject(Type type) {
		this.objects = new ArrayList<HTMLObject<?>>();
		this.container = this.isContainer(type);
		this.type = type;
	}
	
	/**
	 * @pre - none
	 * @post - param add to objects
	 */
	protected E add(HTMLObject<?> object) {
		this.objects.add(object);
		@SuppressWarnings("unchecked")
		E e = (E) this;
		return e;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return
	 * @pre - key & value are not empty
	 * @post - key & value are added to attributes
	 */
	public E addAttribute(String key, String value) {
		if (key == null || value == null || key.trim().isEmpty()
				|| value.trim().isEmpty()) {
			throw new NullPointerException(
					"Neither the key nor the value can be empty or only whitespace.");
		}
		if (this.attributes.isEmpty()) {
			this.attributes = key + "=\"" + value + "\"";
		} else {
			this.attributes += " " + key + "=\"" + value + "\"";
		}
		@SuppressWarnings("unchecked")
		E e = (E) this;
		return e;
	}
	
	/**
	 * @param HTMLclass
	 * @return
	 * @pre - none
	 * @post - param appended to HTMLclass
	 */
	public E addClass(String HTMLclass) {
		if (this.HTMLclass.isEmpty()) {
			this.HTMLclass = HTMLclass;
		} else {
			this.HTMLclass += String.format(" %s", HTMLclass);
		}
		@SuppressWarnings("unchecked")
		E e = (E) this;
		return e;
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - builds b
	 */
	public String build() {
		StringBuilder b = new StringBuilder();
		if (this.container) {
			b.ln("<" + this.openTag() + ">");
			b.startBlock();
			if (this instanceof Cell) {
				int i = 0;
				if (i < this.objects.size()) {
					b.indents().append(
							this.objects.get(i++).build()
									+ (this.objects.size() == 1 ? "\n" : ""));
				}
				for (; i < this.objects.size() - 1; i++) {
					b.append(this.objects.get(i).build());
				}
				if (i < this.objects.size()) {
					b.append(this.objects.get(i).build() + "\n");
				}
			} else {
				for (HTMLObject<?> o : this.objects) {
					b.ln(o.build());
				}
			}
			b.closeBlock();
			b.ln("</" + this + ">");
		} else {
			if (this.type == Type.PLAIN_TEXT) {
				b.append(this.text);
			} else {
				if (!(this instanceof HTMLTextContainer)) {
					b.ln("<" + this.openTag() + "/>");
				} else {
					b.append("<" + this.openTag() + ">");
					for (HTMLObject<?> o : this.objects) {
						b.append(o.build());
					}
					b.append("</" + this + ">");
				}
			}
		}
		return b.toString();
	}
	
	/**
	 * Returns an ArrayList with all of the objects of this HTMLBuilder
	 * 
	 * @return
	 */
	public ArrayList<HTMLObject<?>> getObjects() {
		return this.objects;
	}
	
	public boolean isContainer() {
		return this.container;
	}
	
	private boolean isContainer(Type type) {
		switch (type) {
			case TABLE:
			case TABLE_CELL:
			case DIVISION:
			case TABLE_ROW:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * @pre - none
	 * @post - String list of this HTMLbuilder
	 */
	protected String openTag() {
		return this
				+ (this.id.isEmpty() ? "" : " id=\"" + this.id + "\"")
				+ (this.HTMLclass.isEmpty() ? "" : " class=\"" + this.HTMLclass
						+ "\"")
				+ (this.attributes.isEmpty() ? "" : " " + this.attributes);
	}
	
	/**
	 * @param id
	 * @return
	 * @pre - none
	 * @post - id == param
	 */
	public E setID(String id) {
		this.id = id;
		@SuppressWarnings("unchecked")
		E e = (E) this;
		return e;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}

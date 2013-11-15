package ISIS.html;

import java.util.ArrayList;

import ISIS.html.objects.HTMLObject;

public class HTMLBuilder {
	private final ArrayList<JSScript>	scripts;
	private final ArrayList<CSSStyle>	styles;
	private final ArrayList<HTMLObject>	objects;
	
	private String						title;
	
	public HTMLBuilder(String title) {
		setTitle(title);
		scripts = new ArrayList<JSScript>();
		styles = new ArrayList<CSSStyle>();
		objects = new ArrayList<HTMLObject>();
	}
	
	public void add(CSSStyle style) {
		styles.add(style);
	}
	
	public void add(HTMLObject object) {
		objects.add(object);
	}
	
	public void add(JSScript script) {
		scripts.add(script);
	}
	
	public String build() throws HTMLFormatException {
		if (title.isEmpty())
			throw new HTMLFormatException("HTML title cannot be empty.");
		StringBuilder out = new StringBuilder();
		out.ln("<!DOCTYPE html>");
		out.ln("<html>");
		out.startBlock();
		{
			out.ln("<head>");
			out.startBlock();
			{
				out.ln("<title>" + title + "</title>");
				if (!styles.isEmpty()) {
					out.ln("<style type=\"text/css\">");
					out.startBlock();
					{
						for (CSSStyle s : styles) {
							out.ln(s.build());
						}
					}
					out.closeBlock();
					out.ln("</style>");
				}
				if (!scripts.isEmpty()) {
					out.ln("<script>");
					out.startBlock();
					{
						for (JSScript s : scripts) {
							out.ln(s.build());
						}
					}
					out.closeBlock();
					out.ln("</script>");
				}
			}
			out.closeBlock();
			out.ln("</head>");
			out.ln("<body>");
			out.startBlock();
			{
				if (!objects.isEmpty()) for (HTMLObject o : objects)
					out.ln(o.build());
			}
			out.closeBlock();
			out.ln("</body>");
		}
		out.closeBlock();
		out.ln("</html>");
		return out.toString();
	}
	
	/**
	 * Returns null if the designator doesn't match an existing style.
	 * 
	 * @param designator
	 * @return
	 */
	public CSSStyle getStyle(String designator) {
		for (CSSStyle s : styles)
			if (designator.equals(s.getDesignator())) return s;
		return null;
	}
	
	public final void setTitle(String title) {
		this.title = title;
	}
}

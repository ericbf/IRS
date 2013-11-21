package ISIS.html;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import ISIS.html.objects.Break;
import ISIS.html.objects.Division;
import ISIS.html.objects.HTMLContainObjects;
import ISIS.html.objects.HTMLObject;
import ISIS.html.objects.Header;
import ISIS.html.objects.Paragraph;
import ISIS.html.objects.Separator;
import ISIS.html.objects.Table;

public class HTMLBuilder implements HTMLContainObjects<HTMLBuilder> {
	public static HTMLBuilder getReportTemplate(String title) {
		HTMLBuilder temp = new HTMLBuilder(title);
		
		temp.add(new CSSStyle("body").addAttribute("padding", "2em"));
		temp.add(new CSSStyle(".header *").addAttribute("margin", "0"));
		temp.add(new CSSStyle("table").addAttribute("margin", "auto"));
		
		Division header = new Division().addClass("header");
		
		Division div1 = new Division().addAttribute("style", "clear:both");
		div1.add(new Header(1).addAttribute("style", "float:left").add("IRS"));
		Header rightH = new Header(1);
		rightH.addAttribute("style", "float:right");
		rightH.add(title);
		div1.add(rightH);
		header.add(div1);
		
		Division div2 = new Division().addAttribute("style", "clear:both");
		div2.add(new Paragraph().addAttribute("style",
				"float:right; text-align:right").add(
				DateFormat.getDateInstance().format(new Date())));
		header.add(div2);
		
		temp.add(header);
		temp.add(new Paragraph().add(new Break()).add(new Separator()));
		
		return temp;
	}
	
	private final ArrayList<JSScript>		scripts;
	private final ArrayList<CSSStyle>		styles;
	
	private final ArrayList<HTMLObject<?>>	objects;
	
	private String							title;
	
	public HTMLBuilder(String title) {
		this.setTitle(title);
		this.scripts = new ArrayList<JSScript>();
		this.styles = new ArrayList<CSSStyle>();
		this.objects = new ArrayList<HTMLObject<?>>();
	}
	
	public void add(CSSStyle style) {
		this.styles.add(style);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Division)
	 */
	@Override
	public HTMLBuilder add(Division division) {
		return this.add((HTMLObject<?>) division);
	}
	
	@Override
	public HTMLBuilder add(Header header) {
		return this.add((HTMLObject<?>) header);
	}
	
	private HTMLBuilder add(HTMLObject<?> object) {
		this.objects.add(object);
		return this;
	}/*
	 * (non-Javadoc)
	 * @see
	 * ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Paragraph)
	 */
	
	public void add(JSScript script) {
		this.scripts.add(script);
	}/*
	 * (non-Javadoc)
	 * @see ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Table)
	 */
	
	@Override
	public HTMLBuilder add(Paragraph paragraph) {
		return this.add((HTMLObject<?>) paragraph);
	}
	
	@Override
	public HTMLBuilder add(Table table) {
		return this.add((HTMLObject<?>) table);
	}
	
	public String build() throws HTMLFormatException {
		if (this.title.isEmpty())
			throw new HTMLFormatException("HTML title cannot be empty.");
		StringBuilder out = new StringBuilder();
		out.ln("<!DOCTYPE html>");
		out.ln("<html>");
		out.startBlock();
		{
			out.ln("<head>");
			out.startBlock();
			{
				out.ln("<title>" + this.title + "</title>");
				if (!this.styles.isEmpty()) {
					out.ln("<style type=\"text/css\">");
					out.startBlock();
					{
						for (CSSStyle s : this.styles) {
							out.ln(s.build());
						}
					}
					out.closeBlock();
					out.ln("</style>");
				}
				if (!this.scripts.isEmpty()) {
					out.ln("<script>");
					out.startBlock();
					{
						for (JSScript s : this.scripts) {
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
				if (!this.objects.isEmpty())
					for (HTMLObject<?> o : this.objects)
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
	 * @return the objects
	 */
	public ArrayList<HTMLObject<?>> getObjects() {
		return this.objects;
	}
	
	/**
	 * @return the scripts
	 */
	public ArrayList<JSScript> getScripts() {
		return this.scripts;
	}
	
	/**
	 * Returns null if the designator doesn't match an existing style.
	 * 
	 * @param designator
	 * @return
	 */
	public CSSStyle getStyle(String designator) {
		for (CSSStyle s : this.styles)
			if (designator.equals(s.getDesignator())) return s;
		return null;
	}
	
	/**
	 * @return the styles
	 */
	public ArrayList<CSSStyle> getStyles() {
		return this.styles;
	}
	
	public final void setTitle(String title) {
		this.title = title;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.html.objects.HTMLContainObjects#add(ISIS.html.objects.Header)
	 */
	@Override
	public String toString() {
		try {
			return this.build();
		} catch (HTMLFormatException e) {
			e.printStackTrace();
			return "build failed: " + super.toString();
		}
	}
}

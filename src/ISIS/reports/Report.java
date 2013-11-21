package ISIS.reports;

/**
 * Abstract class for all reports. Reports are HTML based, and can be exported
 * to be displayed in the interface or printed.
 * 
 * @author michaelm
 */
public abstract class Report {
	
	public static enum HeadingType {
		
		H1, H2, H3
	}
	
	/**
	 * Appends a heading to the report.
	 */
	public void appendHeading(String id, String class_,
			HeadingType headingType, String headingContents) {}
	
	/**
	 * Appends HTML to the report. Caveat: XSS attacks are possible if input is
	 * not sanitized.
	 */
	protected final void appendHTML(String html) {}
	
	/**
	 * Appends a paragraph to the report.
	 */
	public void appendParagraph(String id, String class_,
			String paragraphContents) {}
	
	/**
	 * Escapes text for insertion in HTML, to prevent XSS.
	 */
	protected final String escapeText(String input) {
		return null;
	}
	
	/**
	 * Exports the report as a HTML string.
	 */
	protected final String export() {
		return null;
	}
	
	/**
	 * Sets the report's secondary stylesheet (user styles).
	 */
	protected final void setSecondaryStyles(String id, String class_,
			String stylesheet) {}
	
	/**
	 * Sets the report's main CSS stylesheet.
	 */
	protected final void setStyles(String stylesheet) {}
}

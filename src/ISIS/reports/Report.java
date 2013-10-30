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
	 * Sets the report's main CSS stylesheet.
	 */
	protected void setStyles(String stylesheet) {
	}

	/**
	 * Sets the report's secondary stylesheet (user styles).
	 */
	protected void setSecondaryStyles(String id, String class_, String stylesheet) {
	}

	/**
	 * Appends a table to the report.
	 */
	public void appendTable(String id, String class_, Table table) {
	}

	/**
	 * Appends a heading to the report.
	 */
	public void appendHeading(String id, String class_, HeadingType headingType, String headingContents) {
	}

	/**
	 * Appends a paragraph to the report.
	 */
	public void appendParagraph(String id, String class_, String paragraphContents) {
	}

	/**
	 * Escapes text for insertion in HTML, to prevent XSS.
	 */
	protected String escapeText(String input) {
	}

	/**
	 * Appends HTML to the report.
	 *
	 * Caveat: XSS attacks are possible if input is not sanitized.
	 */
	protected void appendHTML(String html) {
	}

	/**
	 * Exports the report as a HTML string.
	 */
	protected String export() {
	}
}

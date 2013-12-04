package ISIS.reports;

import ISIS.html.HTMLBuilder;

/**
 * Abstract class for all reports. Reports are HTML based, and can be exported
 * to be displayed in the interface or printed.
 * 
 * @author michaelm
 */
public abstract class Report {
	protected HTMLBuilder	b;
	protected String		title;
	
	/**
	 * Protected constructor. Creates new instance of report
	 * 
	 * @param title
	 */
	protected Report(String title) {
		this.b = HTMLBuilder.getReportTemplate(title);
		this.title = title;
	}
	
	/**
	 * Gets HTML builder.
	 * 
	 * @return
	 */
	public final HTMLBuilder getBuilder() {
		return this.b;
	}
	
	/**
	 * Gets title.
	 * 
	 * @return
	 */
	public final String getTitle() {
		return this.title;
	}
	
	/**
	 * Suggested way to populate the builder
	 */
	public void populateBuilder() {}
}

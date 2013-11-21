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
	
	protected Report(String title) {
		this.b = HTMLBuilder.getReportTemplate(title);
		this.title = title;
		this.populateBuilder();
	}
	
	public final HTMLBuilder getBuilder() {
		return this.b;
	}
	
	public final String getTitle() {
		return this.title;
	}
	
	public abstract void populateBuilder();
}

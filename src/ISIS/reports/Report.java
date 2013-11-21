package ISIS.reports;

import ISIS.html.HTMLBuilder;

/**
 * Abstract class for all reports. Reports are HTML based, and can be exported
 * to be displayed in the interface or printed.
 * 
 * @author michaelm
 */
public abstract class Report {
	HTMLBuilder	b;
	
	protected Report(String title) {
		this.b = HTMLBuilder.getReportTemplate(title);
	}
	
	public HTMLBuilder getBuilder() {
		return this.b;
	}
}

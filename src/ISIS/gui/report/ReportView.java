package ISIS.gui.report;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.JScrollPane;

import org.xhtmlrenderer.simple.XHTMLPanel;

import ISIS.database.Record;
import ISIS.gui.BadInputException;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.html.HTMLFormatException;
import ISIS.reports.Report;

/**
 * Abstract class for the report GUIs.
 */
public class ReportView extends View {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Public constructor.
	 */
	public ReportView(Report report, SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c;
		this.setPadding();
		
		XHTMLPanel p = new XHTMLPanel();
		try {
			p.setDocument(new ByteArrayInputStream(report.getBuilder().build()
					.getBytes("UTF-8")), "");
		} catch (HTMLFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JScrollPane(p), c);
	}
	
	/**
	 * Not supported.
	 */
	@Override
	public void cancel() {
		throw new UnsupportedOperationException("Not supported.");
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 */
	@Override
	public Record getCurrentRecord() throws BadInputException {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		return null;
	}
	
	/**
	 * Reports are not saved, but regenerated as necessary.
	 */
	@Override
	public boolean needsSave() {
		return false;
	}
	
	/**
	 * Not supported.
	 */
	@Override
	public void save() {
		throw new UnsupportedOperationException("Not supported.");
	}
}

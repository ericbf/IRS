package ISIS.gui.report;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JScrollPane;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.simple.XHTMLPanel;

import ISIS.database.Record;
import ISIS.gui.ErrorLogger;
import ISIS.gui.SplitPane;
import ISIS.gui.View;
import ISIS.html.HTMLFormatException;
import ISIS.reports.Report;

import com.lowagie.text.DocumentException;

/**
 * Abstract class for the report GUIs.
 */
public class ReportView extends View {
	private static final long	serialVersionUID	= 1L;
	boolean						isSaved;
	private String				html;
	Report						report;
	
	// static {
	// Session.getCurrentSession().setDefaultSetting("report_dir",
	// System.getProperty("user.home"));
	// }
	
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
			p.setDocument(
					new ByteArrayInputStream(
							(this.html = (this.report = report).getBuilder()
									.build()).getBytes("UTF-8")), null);
		} catch (HTMLFormatException e) {
			ErrorLogger.error(
					"Malformatted HTML, fix the <class extends Report>", true,
					true);
			e.printStackTrace();
		} catch (Exception e) {
			ErrorLogger.error("Error setting HTML to view", true, true);
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
	public Record getCurrentRecord() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public Boolean isAnyFieldDifferentFromDefault() {
		return !this.isSaved;
	}
	
	/**
	 * Reports are not saved exactly, but exported (regenerated as necessary).
	 */
	@Override
	public boolean needsSave() {
		return true;
	}
	
	/**
	 * Export to PDF file
	 */
	@Override
	public void save() {
		this.isSaved = true;
		
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(this.html);
		
		renderer.layout();
		
		FileDialog d = new FileDialog((Frame) null, "Pick a location",
				FileDialog.SAVE);
		d.setLocationRelativeTo(this);
		d.setFile(this.report.getTitle() + ".pdf");
		// System.out.println(Session.getCurrentSession().getSetting("report_dir")
		// .toString());
		// d.setDirectory(Session.getCurrentSession().getSetting("report_dir")
		// .toString());
		d.setVisible(true);
		// if (d.getDirectory() != null) {
		// Session.getCurrentSession().setSetting("report_dir",
		// d.getDirectory());
		// }
		try {
			FileOutputStream fos = new FileOutputStream(d.getDirectory()
					+ d.getFile());
			renderer.createPDF(fos);
			fos.close();
		} catch (DocumentException | IOException e) {
			ErrorLogger.error("Failed to write file", false, true);
			e.printStackTrace();
		}
	}
}

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
public class ReportViewer extends View {
	private static final long	serialVersionUID	= 1L;
	private String				html;
	Report						report;
	
        /**
	 * Public constructor. User is presented with a Report View.
         * 
         * @pre - receives all required parameters for a report and Split Pane.
         * @post - returns new instance of the view.
	 */
	public ReportViewer(Report report, SplitPane splitPane) {
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
	public boolean isAnyFieldDifferentFromDefault() {
		return false;
	}
	
	/**
	 * Reports are not saved exactly, but exported (regenerated as necessary).
	 */
	@Override
	public boolean needsSave() {
		return true;
	}
	
	/**
	 * @pre - Save is clicked.
         * @post - Exports to PDF file
	 */
	@Override
	public void save() {
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(this.html);
		
		renderer.layout();
		
		FileDialog d = new FileDialog((Frame) null, "Pick a location",
				FileDialog.SAVE);
		d.setLocationRelativeTo(this);
		d.setFile(this.report.getTitle() + ".pdf");
		d.setVisible(true);
		if (d.getDirectory() == null || d.getFile() == null) {
			return;
		}
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

/**
 * 
 */
package ISIS.gui.report;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;

import ISIS.database.Record;
import ISIS.gui.SplitPane;
import ISIS.gui.SplitPane.LayoutType;
import ISIS.gui.View;
import ISIS.reports.BlankReport;

/**
 * @author eric
 */

/**
 * Public class for the Report Select View
 * 
 * @pre - User is presented with a view and buttons.
 * @post - Buttons respond according to user's interactions.
 */
public class ReportSelectorView extends View {
	private static class JButton2 extends JButton {
		private static final long	serialVersionUID	= 1L;
		
		public JButton2(String text) {
			super(text);
		}
		
		public JButton addActionListenero(ActionListener l) {
			super.addActionListener(l);
			return this;
		}
	}
	
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Public constructor. User is presented with a Report Select View.
	 * 
	 * @pre - receives all required parameters for a Split Pane.
	 * @post - returns new instance created.
	 */
	public ReportSelectorView(SplitPane splitPane) {
		super(splitPane);
		this.setLayout(new GridBagLayout());
		this.setPadding();
		GridBagConstraints c;
		int y, x = y = 0;
		
		c = new GridBagConstraints();
		c.gridx = x++;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JButton2("Test").addActionListenero(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportSelectorView.this.splitPane.push(new ReportViewer(
						new BlankReport(), ReportSelectorView.this.splitPane),
						LayoutType.HORIZONTAL, ReportSelectorView.this);
			}
		}), c);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#cancel()
	 */
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#getCurrentRecord()
	 */
	@Override
	public Record getCurrentRecord() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#isAnyFieldDifferentFromDefault()
	 */
	@Override
	public boolean isAnyFieldDifferentFromDefault() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#needsSave()
	 */
	@Override
	public boolean needsSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#save()
	 */
	@Override
	public void save() throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}

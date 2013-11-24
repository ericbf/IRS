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
				ReportSelectorView.this.splitPane.push(new ReportView(
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
	public Boolean isAnyFieldDifferentFromDefault() {
		// TODO Auto-generated method stub
		return null;
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
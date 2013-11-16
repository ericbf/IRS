/**
 * 
 */
package ISIS.gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * @author eric
 */
public class ListButtonListener implements ActionListener {
	private final String		tab;
	private final CardLayout	cl;
	private final JPanel		cardHost;
	
	public ListButtonListener(CardLayout cl, JPanel cardHost, String tab) {
		this.cl = cl;
		this.tab = tab;
		this.cardHost = cardHost;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.cl.show(this.cardHost, this.tab);
	}
}
/**
 * 
 */
package ISIS.gui;

import java.awt.Component;

import javax.swing.JScrollPane;

/**
 * @author eric
 */
public class Scroller extends JScrollPane {
	private static final long	serialVersionUID	= 1L;
	
	public Scroller(Component c) {
		super(c);
		
		this.setBorder(null);
	}
}

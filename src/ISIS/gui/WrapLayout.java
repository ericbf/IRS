/**
 * 
 */
package ISIS.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * @author Rob Camick http://tips4java.wordpress.com/2008/11/06/wrap-layout/
 */
public class WrapLayout extends FlowLayout {
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Adjust the preferred height to fit rows
	 * 
	 * @param dim
	 * @param rowWidth
	 * @param rowHeight
	 */
	private void addRow(Dimension dim, int rowWidth, int rowHeight) {
		dim.width = Math.max(dim.width, rowWidth);
		
		if (dim.height > 0) {
			dim.height += this.getVgap();
		}
		
		dim.height += rowHeight;
	}
	
	/**
	 * Returns the minimum or preferred dimension needed to layout the target
	 * container.
	 * 
	 * @param target
	 * @param preferred
	 * @return the dimension to layout the target container
	 */
	private Dimension layoutSize(Container target, boolean preferred) {
		synchronized (target.getTreeLock()) {
			// Each row must fit with the width allocated to the
			// containter.
			// When the container width = 0, the preferred width of the
			// container
			// has not yet been calculated so lets ask for the maximum.
			
			int targetWidth = target.getSize().width;
			
			if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
			
			int hgap = this.getHgap();
			int vgap = this.getVgap();
			Insets insets = target.getInsets();
			int horizontalInsetsAndGap = insets.left + insets.right
					+ (hgap * 2);
			int maxWidth = targetWidth - horizontalInsetsAndGap;
			
			// Fit components into the allowed width
			Dimension dim = new Dimension(0, 0);
			int rowWidth = 0;
			int rowHeight = 0;
			
			int nmembers = target.getComponentCount();
			
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				
				if (m.isVisible()) {
					Dimension d = preferred ? m.getPreferredSize() : m
							.getMinimumSize();
					
					// To wide; start new row
					if (rowWidth + d.width > maxWidth) {
						this.addRow(dim, rowWidth, rowHeight);
						rowWidth = 0;
						rowHeight = 0;
					}
					
					// Add a hgap for all components after the first
					if (rowWidth != 0) {
						rowWidth += hgap;
					}
					
					rowWidth += d.width;
					rowHeight = Math.max(rowHeight, d.height);
				}
			}
			
			this.addRow(dim, rowWidth, rowHeight);
			
			if (!preferred) {
				dim.width = 0;
				for (int i = 0; i < nmembers; i++) {
					Component m = target.getComponent(i);
					
					if (m.isVisible()) {
						dim.width = Math.max(m.getMinimumSize().width,
								dim.width);
					}
				}
			}
			dim.width += horizontalInsetsAndGap;
			dim.height += insets.top + insets.bottom + vgap * 2;
			
			// Fix for when using JScrollPane
			Container scrollPane = SwingUtilities.getAncestorOfClass(
					JScrollPane.class, target);
			
			if (scrollPane != null && target.isValid()) {
				dim.width -= (hgap + 1);
			}
			
			return dim;
		}
	}
	
	@Override
	public Dimension minimumLayoutSize(Container target) {
		Dimension minimum = this.layoutSize(target, false);
		minimum.width -= (this.getHgap() + 1);
		return minimum;
	}
	
	@Override
	public Dimension preferredLayoutSize(Container target) {
		return this.layoutSize(target, true);
	}
}

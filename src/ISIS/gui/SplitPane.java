package ISIS.gui;

import java.util.ArrayList;

import javax.swing.JSplitPane;

/**
 *
 */
public final class SplitPane extends JSplitPane {
	private static final long	serialVersionUID	= 1L;
	private int					stackPointer;
	ArrayList<View>				stack;
	
	SplitPane() {
		stack = new ArrayList<>();
		stackPointer = -1;
	}
	
	/**
	 * The ways of laying out the views.
	 */
	public static enum LayoutType {
		VERTICAL, HORIZONTAL
	}
	
	/**
	 * Pushes a view onto the stack with a given layout and makes it visible.
	 * 
	 * @post views.size() > 0 == true
	 */
	protected final void push(View view, LayoutType layout) {
		if (stack.size() == 0) {
			setLeftComponent(view);
			setRightComponent(null);
			setDividerSize(0);
		} else {
			setLeftComponent(stack.get(stack.size() - 1));
			setRightComponent(view);
		}
		stackPointer++;
		stack.add(view);
	}
	
	/**
	 * Completely removes a view from the stack.
	 * 
	 * @pre views.size() > 0 == true
	 */
	protected final void pop() {
		if (stack.size() == 0) System.out.println("You stupid");
		else {
			
		}
	}
	
	/**
	 * Hides the current view by shifting the two views backwards in the stack.
	 * 
	 * @pre previousViews() == true
	 * @post hiddenViews() == true
	 */
	protected final void backward() {}
	
	/**
	 * Shows hidden views by shifting forwards in the stack.
	 * 
	 * @pre hiddenViews() == true
	 * @post previousViews() == true
	 */
	protected final void forward() {}
}

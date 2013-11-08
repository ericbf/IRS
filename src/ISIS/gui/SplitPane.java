package ISIS.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 */
public final class SplitPane extends JSplitPane {
	private static final long	serialVersionUID	= 1L;
	private final int			defaultDividerSize;
	private int					stackPointer;
	ArrayList<View>				stack;
	
	SplitPane() {
		this.stack = new ArrayList<>();
		this.stackPointer = 0;
		this.setBorder(null);
		this.defaultDividerSize = this.getDividerSize();
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
	public final void push(View view, LayoutType layout) {
		if (this.stack.isEmpty()) {
			this.setLeftComponent(view);
			this.setRightComponent(null);
		} else {
			this.setLeftComponent(this.stack.get(this.stack.size() - 1));
			this.setRightComponent(view);
		}
		this.stackPointer++;
		this.stack.add(view);
	}
	
	/**
	 * Completely removes a view from the stack.
	 * 
	 * @pre views.size() > 0 == true
	 */
    public final void pop() {}
	
	/**
	 * Hides the current view by shifting the one view backwards in the stack.
	 * 
	 * @pre previousViews() == true
	 * @post hiddenViews() == true
	 */
	public final void backward() {
		if (this.stackPointer == 1) {
			this.setLeftComponent(this.stack.get(--this.stackPointer));
			this.setRightComponent(null);
		} else {
			this.setLeftComponent(this.stack.get(--this.stackPointer - 1));
			this.setRightComponent(this.stack.get(this.stackPointer));
		}
	}
	
	/**
	 * Shows hidden views by shifting forwards in the stack.
	 * 
	 * @pre hiddenViews() == true
	 * @post previousViews() == true
	 */
	protected final void forward() {
		this.setRightComponent(this.stack.get(++this.stackPointer));
		this.setLeftComponent(this.stack.get(this.stackPointer - 1));
	}
	
	/**
	 * When setting this SplitPane to single view mode, hide the divider line,
	 * else set it to the default size
	 */
	@Override
	public void setRightComponent(Component comp) {
		if (comp != null){ this.setDividerSize(this.defaultDividerSize);}
		else this.setDividerSize(0);
		super.setRightComponent(comp);
	}
}

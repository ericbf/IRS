package ISIS.gui;

import javax.swing.JSplitPane;

/**
 *
 */
public abstract class SplitPane extends JSplitPane {

    /**
     * The ways of laying out the views.
     */
    public static enum LayoutType {

        VERTICAL, HORIZONTAL
    }
    
    /**
     * Pushes a view onto the stack with a given layout and makes it visible. 
     * @post views.size() > 0 == true
     */
    protected void push(View view, LayoutType layout) {
    }
    
    /**
     * Completely removes a view from the stack. 
     * @pre views.size() > 0 == true
     */    
    protected void pop() {
    }

    /**
     * Hides the current view by shifting the two views backwards in the stack.
     * @pre previousViews() == true
     * @post hiddenViews() == true
     */    
    protected void backward() {
    }
    
    /**
     * Shows hidden views by shifting forwards in the stack.
     * @pre hiddenViews() == true
     * @post previousViews() == true
     */
    protected void forward() {
    }

    
    
}

package ISIS.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 */
public final class SplitPane extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int defaultDividerSize;
    ArrayList<View> stack;
    private int stackPointer;
    private JSplitPane splitPane;
    private int columns;

    SplitPane() {
        this.columns = 20;
        this.stack = new ArrayList<>();
        this.stackPointer = 0;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.gridwidth = columns;
        c.weightx = 1;
        c.weighty = 1;
        this.splitPane = new JSplitPane();
        this.add(this.splitPane, c);
        this.setBorder(null);
        this.defaultDividerSize = this.splitPane.getDividerSize();
    }

    /**
     * Adds back, forwards, save, etc.
     */
    public final void addButtons() {
        GridBagConstraints c;
        if (this.stackPointer > 2) {
            c = new GridBagConstraints();
            c.gridy = 0;
            c.gridx = 0;
            c.anchor = GridBagConstraints.WEST;
            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SplitPane.this.backward();
                }
            });
            this.add(backButton, c);
        }
        if (this.stackPointer < this.stack.size()) {
            c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1;
            JButton forwardsButton = new JButton("Forwards");
            forwardsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SplitPane.this.forward();
                }
            });
            this.add(forwardsButton, c);
        }
        if (this.stack.get(stackPointer - 1).needsSave()) {
            c = new GridBagConstraints();
            c.gridy = 2;
            JButton save = new JButton("Save");
            JButton cancel = new JButton("Cancel");
            // push buttons to right
            c.gridx = this.columns - 3;
            c.weightx = 1;
            this.add(Box.createHorizontalGlue(), c);

            //add buttons
            c.weightx = 0;
            c.gridx = this.columns - 2;
            this.add(cancel, c);
            c.gridx = this.columns - 1;
            this.add(save, c);
        }
        this.validate();
    }

    /**
     * Pushes a view onto the stack with a given layout and makes it visible.
     *
     * @post views.size() > 0 == true
     */
    public final void push(View view, LayoutType layout) {
        if (this.stackPointer < this.stack.size()) { // we're trying to pop with views higher than us on the stack.
            int lastToPop = this.stackPointer;
            this.stackPointer = this.stack.size();  //move to the last view in the stack
            for (int i = this.stack.size() - 1; i >= lastToPop; --i) {
                try {
                    this.pop();
                } catch (CloseCanceledException e) {
                    return;  // close canceled; stop here.
                }
            }
            return;
        }
        if (this.stack.isEmpty()) {
            this.setLeftComponent(view);
            this.setRightComponent(null);
        } else {
            this.setLeftComponent(this.stack.get(this.stack.size() - 1));
            this.setRightComponent(view);
        }
        this.stackPointer++;
        this.stack.add(view);
        this.addButtons();
    }

    /**
     * Completely removes a view from the stack.
     *
     * @pre views.size() > 0 == true
     */
    public final void pop() throws CloseCanceledException {
        this.stack.get(this.stack.size() - 1).close();
        this.stack.remove(this.stack.size() - 1);
        this.stackPointer--;
        if (this.stack.size() > 1) {
            this.setRightComponent(this.stack.get(this.stack.size() - 1));
            this.setLeftComponent(this.stack.get(this.stack.size() - 2));
        } else if (this.stack.size() == 1) {
            this.setLeftComponent(this.stack.get(0));
            this.setRightComponent(null);
        } else {
            throw new RuntimeException("Popped the entire stack!");
        }
        this.addButtons();
    }

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
        this.addButtons();
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
        this.addButtons();
    }

    /**
     * When setting this SplitPane to single view mode, hide the divider line,
     * else set it to the default size
     */
    public void setRightComponent(Component comp) {
        if (comp != null) {
            this.splitPane.setDividerSize(this.defaultDividerSize);
        } else this.splitPane.setDividerSize(0);
        this.splitPane.setRightComponent(comp);
    }

    /**
     * Alias to JSplitPane.setLeftComponent
     */
    public void setLeftComponent(Component comp) {
        this.splitPane.setLeftComponent(comp);
    }

    /**
     * The ways of laying out the views.
     */
    public static enum LayoutType {
        VERTICAL, HORIZONTAL
    }
}

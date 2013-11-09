package ISIS.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
    private JButton back, forwards, cancel, save, closeButton;

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
            this.back = new JButton("Back");
            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SplitPane.this.backward();
                }
            });
            this.add(back, c);
            this.add(back, c);
        } else {
            if (this.back != null) {
                this.remove(back);
            }
        }
        if (this.stackPointer < this.stack.size()) {
            c = new GridBagConstraints();
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1;
            this.forwards = new JButton("Forwards");
            forwards.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SplitPane.this.forward();
                }
            });
            this.add(forwards, c);
        } else {
            if (this.forwards != null) {
                this.remove(this.forwards);
            }
        }
        if (this.stack.get(stackPointer - 1).needsSave()) {
            c = new GridBagConstraints();
            c.gridy = 2;
            this.closeButton = new JButton("Close");
            this.save = new JButton("Save");
            //            this.cancel = new JButton("Cancel");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //                        SplitPane.this.stack.get(stackPointer - 1).save();
                    SplitPane.this.pop(SplitPane.this.stack.get(stackPointer - 2));
                }
            });
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        SplitPane.this.stack.get(stackPointer - 1).save();
                    } catch (SQLException e1) {
                        ErrorLogger.error(e1, "Could not save the current view.", true, true);
                    }
                }
            });
            //            cancel.addActionListener(new ActionListener() {
            //                @Override
            //                public void actionPerformed(ActionEvent e) {
            //                    SplitPane.this.stack.get(stackPointer - 1).cancel();
            //                    SplitPane.this.pop(true);
            //                }
            //            });
            int i = 0;
            // push buttons to right
            c.gridx = i++;
            c.weightx = 1;
            this.add(Box.createHorizontalGlue(), c);

            //add buttons
            c.weightx = 0;
            c.gridx = i++;
            //            this.add(saveClose, c);
            c.gridx = i++;
            this.add(save, c);
            c.gridx = i++;
            this.add(closeButton, c);
        } else {
            //check if we no longer need save buttons
            if (this.cancel != null) {
                //                this.remove(this.cancel);
                this.remove(this.save);
                this.remove(this.closeButton);
            }
        }
        this.validate();
    }

    /**
     * Pushes a view onto the stack with a given layout and makes it visible.
     *
     * @post views.size() > 0 == true
     */
    public final void push(View view, LayoutType layout, View pusher) {
        if (pusher != null) {
            int origin = this.stack.indexOf(pusher);
            if (origin < this.stack.size() - 1) {
                this.pop(pusher);
                this.push(view, layout, null);
                return;
            }
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
     * Completely removes a closed view from the stack. DO NOT call it with false.
     *
     * @pre views.size() > 0 == true
     */
    public final void pop(View popper) {
        int origin = this.stack.indexOf(popper);
        if (origin < this.stack.size() - 1) {
            this.stackPointer = this.stack.size();  //move to the last view in the stack
            for (int i = this.stack.size() - 1; i > origin; --i) {
                try {
                    //move to the view we're closing
                    this.setRightComponent(this.stack.get(this.stack.size() - 1));
                    this.setLeftComponent(this.stack.get(this.stack.size() - 2));
                    this.stack.get(i).close();
                    this.stack.remove(i);
                    this.stackPointer--;
                } catch (CloseCanceledException e) {
                    return;  // close canceled; stop here.
                }
            }
            if (this.stack.size() > 1) {
                this.setRightComponent(this.stack.get(this.stack.size() - 1));
                this.setLeftComponent(this.stack.get(this.stack.size() - 2));
            } else if (this.stack.size() == 1) {
                this.setLeftComponent(this.stack.get(0));
                this.setRightComponent(null);
            } else {
                throw new RuntimeException("Popped the entire stack!");
            }
            return;
        }
        if (origin == this.stack.size() - 1) {
            try {
                this.stack.get(origin).close();
            } catch (CloseCanceledException e) {
                //the pop was cancelled
                return;
            }
        }
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

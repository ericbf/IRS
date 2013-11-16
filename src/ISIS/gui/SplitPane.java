package ISIS.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 */
public final class SplitPane extends JPanel {
	/**
	 * The ways of laying out the views.
	 */
	public static enum LayoutType {
		VERTICAL, HORIZONTAL
	}
	
	private static final long	serialVersionUID	= 1L;
	private final int			defaultDividerSize;
	ArrayList<View>				stack;
	private int					stackPointer;
	private JSplitPane			splitPane;
	private double				dividerLocationRatio;
	
	JPanel						buttons;
	
	SplitPane() {
		super(new BorderLayout());
		this.stack = new ArrayList<View>();
		this.stackPointer = 0;
		this.add(this.splitPane = new JSplitPane());
		this.setOpaque(false);
		this.splitPane.setOpaque(false);
		this.splitPane.setBorder(null);
		this.defaultDividerSize = 9;
		GridBagLayout buttonLayout;
		this.buttons = new JPanel(buttonLayout = new GridBagLayout());
		buttonLayout.columnWidths = new int[] { 0, 94, 94, 94, 94 };
		this.buttons.setOpaque(false);
		this.add(this.buttons, BorderLayout.NORTH);
		this.dividerLocationRatio = 0.5;
	}
	
	/**
	 * Adds back, forwards, save, etc.
	 */
	public final void addButtons() {
		GridBagConstraints c;
		this.buttons.removeAll();
		boolean hasButtons = false;
		
		if (this.stackPointer > 0) {
			hasButtons = true;
			c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 1;
			c.fill = GridBagConstraints.BOTH;
			JButton backButton = new JButton("Back");
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SplitPane.this.backward();
				}
			});
			this.buttons.add(backButton, c);
		}
		if (this.stackPointer < this.stack.size() - 1) {
			hasButtons = true;
			c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 2;
			c.fill = GridBagConstraints.BOTH;
			JButton forwardsButton = new JButton("Forward");
			forwardsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SplitPane.this.forward();
				}
			});
			this.buttons.add(forwardsButton, c);
		}
		if (this.stack.get(this.stackPointer).needsSave()) {
			hasButtons = true;
			JButton save = new JButton("Save");
			JButton cancel = new JButton("Close");
			
			c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 3;
			c.fill = GridBagConstraints.BOTH;
			this.buttons.add(cancel, c);
			
			c = new GridBagConstraints();
			c.gridy = 0;
			c.gridx = 4;
			c.fill = GridBagConstraints.BOTH;
			this.buttons.add(save, c);
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						SplitPane.this.stack.get(SplitPane.this.stackPointer)
								.save();
					} catch (SQLException e1) {
						ErrorLogger.error(e1,
								"Could not save the current view.", true, true);
					}
				}
			});
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						SplitPane.this.popAllAbovePointer();
						// SplitPane.this.stack.get(
						// SplitPane.this.stackPointer - 1).cancel();
						SplitPane.this.pop();
					} catch (CloseCanceledException e1) {
						return;
					}
				}
			});
		}
		
		if (hasButtons) {
			c = new GridBagConstraints();
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			JPanel spacer;
			this.buttons.add(spacer = new JPanel(), c);
			spacer.setOpaque(false);
		}
		this.buttons.repaint();
	}
	
	/**
	 * Hides the current view by shifting the one view backwards in the stack.
	 * 
	 * @pre previousViews() == true
	 * @post hiddenViews() == true
	 */
	public final void backward() {
		this.dividerLocationRatio = ((double) this.splitPane
				.getDividerLocation()) / this.getWidth();
		if (this.stackPointer == 1) {
			this.setLeftComponent(this.stack.get(--this.stackPointer));
			this.setRightComponent(null);
		} else {
			this.setLeftComponent(this.stack.get(--this.stackPointer - 1));
			this.setRightComponent(this.stack.get(this.stackPointer));
			// Try to set the divider to the same location as before, but no
			// less than the left component's minimum width
			this.splitPane.setDividerLocation(Math.max(
					(this.splitPane.getLeftComponent().getMinimumSize().width)
							/ (2.0 * this.getWidth()),
					this.dividerLocationRatio));
		}
		this.addButtons();
		this.validate();
	}
	
	/**
	 * Shows hidden views by shifting forwards in the stack.
	 * 
	 * @pre hiddenViews() == true
	 * @post previousViews() == true
	 */
	protected final void forward() {
		if (this.stackPointer > 0)
			this.dividerLocationRatio = ((double) this.splitPane
					.getDividerLocation()) / this.getWidth();
		this.setRightComponent(this.stack.get(++this.stackPointer));
		this.setLeftComponent(this.stack.get(this.stackPointer - 1));
		// Try to set the divider to the same location as before, but no less
		// than the left component's minimum width
		this.splitPane
				.setDividerLocation(Math.max((this.splitPane.getLeftComponent()
						.getMinimumSize().width) / ((double) this.getWidth()),
						this.dividerLocationRatio));
		this.addButtons();
	}
	
	/**
	 * Completely removes the top view from the stack. If you call this method
	 * from the left pane, the left pane will not be removed.
	 * 
	 * @throws CloseCanceledException
	 * @pre views.size() > 0 == true
	 */
	public final void pop() throws CloseCanceledException {
		int diff = this.stack.size() - this.stackPointer - 1;
		for (int i = 0; i < diff; i++)
			this.forward();
		this.stack.get(this.stack.size() - 1).close();
		this.stack.remove(this.stack.size() - 1);
		if (this.stackPointer == this.stack.size()) {
			if (this.stack.size() == 0) {
				throw new RuntimeException("Popped the entire stack!");
			} else {
				this.backward();
			}
		}
	}
	
	public final void popAllAbovePointer() throws CloseCanceledException {
		for (int i = this.stack.size(); i > this.stackPointer + 1; i--)
			this.pop();
	}
	
	/**
	 * Pushes a view onto the stack with a given layout and makes it visible.
	 * 
	 * @post views.size() > 0 == true
	 */
	public final void push(View view, LayoutType layout, View pusher) {
		if (pusher != null) {
			int origin = this.stack.indexOf(pusher);
			if (origin == -1) {
				// pusher.. didn't.. exist..
				throw new RuntimeException("WAT. This should never happen.");
			}
			if (origin < this.stackPointer) {
				try {
					this.popAllAbovePointer();
					this.pop();
				} catch (CloseCanceledException e) {
					return;
				}
				this.push(view, layout, null);
				return;
			}
		}
		// we're trying to push with views higher than us on the stack.
		if (this.stackPointer < this.stack.size() - 1) {
			try {
				this.popAllAbovePointer();
			} catch (CloseCanceledException e) {
				return; // Close cancelled
			}
		}
		if (layout.equals(LayoutType.VERTICAL)) {
			this.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		} else this.splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		if (this.stack.isEmpty()) {
			this.setLeftComponent(view);
			this.setRightComponent(null);
			this.stack.add(view);
		} else {
			this.stack.add(view);
			this.forward();
		}
	}
	
	/**
	 * Alias to JSplitPane.setLeftComponent
	 */
	public void setLeftComponent(Component comp) {
		if (comp != null) {
			// this.splitPane.setLeftComponent(this.getWrappedComponent(comp));
			this.splitPane.setLeftComponent(comp);
		} else this.splitPane.setLeftComponent(null);
	}
	
	// /**
	// * Wraps the component in a JScrollPane and sets its settings
	// *
	// * @param comp
	// * @return
	// */
	// private JScrollPane getWrappedComponent(Component comp) {
	// JScrollPane sp = new JScrollPane(comp);
	// sp.setOpaque(false);
	// sp.getViewport().setOpaque(false);
	// sp.setBorder(new EmptyBorder(4, 0, 10, 5));
	// return sp;
	// }
	
	/**
	 * When setting this SplitPane to single view mode, hide the divider line,
	 * else set it to the default size
	 */
	public void setRightComponent(Component comp) {
		if (comp == null) {
			this.splitPane.setDividerSize(0);
			this.splitPane.setRightComponent(null);
		} else {
			this.splitPane.setDividerSize(this.defaultDividerSize);
			// this.splitPane.setRightComponent(this.getWrappedComponent(comp));
			this.splitPane.setRightComponent(comp);
		}
		
	}
	
	// /**
	// * Completely removes a closed view from the stack. DO NOT call it with
	// * false.
	// *
	// * @pre views.size() > 0 == true
	// */
	// public final void pop(View popper) {
	// int origin = this.stack.indexOf(popper);
	// if (origin < this.stack.size() - 1) {
	// this.stackPointer = this.stack.size(); // move to the last view in
	// // the stack
	// for (int i = this.stack.size() - 1; i > origin; --i) {
	// try {
	// // move to the view we're closing
	// this.setRightComponent(this.stack.get(this.stack.size() - 1));
	// this.setLeftComponent(this.stack.get(this.stack.size() - 2));
	// this.stack.get(i).close();
	// this.stack.remove(i);
	// this.stackPointer--;
	// } catch (CloseCanceledException e) {
	// return; // close canceled; stop here.
	// }
	// }
	// if (this.stack.size() > 1) {
	// this.setRightComponent(this.stack.get(this.stack.size() - 1));
	// this.setLeftComponent(this.stack.get(this.stack.size() - 2));
	// } else if (this.stack.size() == 1) {
	// this.setLeftComponent(this.stack.get(0));
	// this.setRightComponent(null);
	// } else {
	// throw new RuntimeException("Popped the entire stack!");
	// }
	// return;
	// }
	// if (origin == this.stack.size() - 1) {
	// try {
	// this.stack.get(origin).close();
	// } catch (CloseCanceledException e) {
	// // the pop was cancelled
	// return;
	// }
	// }
	// this.stack.remove(this.stack.size() - 1);
	// this.stackPointer--;
	// if (this.stack.size() > 1) {
	// this.setRightComponent(this.stack.get(this.stack.size() - 1));
	// this.setLeftComponent(this.stack.get(this.stack.size() - 2));
	// } else if (this.stack.size() == 1) {
	// this.setLeftComponent(this.stack.get(0));
	// this.setRightComponent(null);
	// } else {
	// throw new RuntimeException("Popped the entire stack!");
	// }
	// this.addButtons();
	// }
}

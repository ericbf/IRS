package ISIS.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 */
public final class SplitPane extends JPanel {
	private static final long	serialVersionUID	= 1L;
	private final int			defaultDividerSize;
	ArrayList<View>				stack;
	private int					stackPointer;
	private JSplitPane			splitPane;
	JPanel						buttons;
	
	SplitPane() {
		super(new BorderLayout());
		this.stack = new ArrayList<>();
		this.stackPointer = 0;
		this.add(this.splitPane = new JSplitPane());
		this.setOpaque(false);
		this.splitPane.setOpaque(false);
		this.splitPane.setBorder(null);
		this.defaultDividerSize = 9;
		this.buttons = new JPanel(new GridBagLayout());
		this.buttons.setOpaque(false);
		this.add(this.buttons, BorderLayout.NORTH);
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
			JButton cancel = new JButton("Cancel");
			
			c = new GridBagConstraints();
			c.gridy = 1;
			c.gridx = 1;
			this.buttons.add(cancel, c);
			
			c = new GridBagConstraints();
			c.gridy = 1;
			c.gridx = 2;
			this.buttons.add(save, c);
		}
		
		if (hasButtons) {
			c = new GridBagConstraints();
			c.gridheight = 2;
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			JPanel spacer;
			this.buttons.add(spacer = new JPanel(), c);
			spacer.setOpaque(false);
		}
	}
	
	/**
	 * Pushes a view onto the stack with a given layout and makes it visible.
	 * 
	 * @post views.size() > 0 == true
	 */
	public final void push(View view, LayoutType layout) {
		// we're trying to push with views higher than us on the stack.
		if (this.stackPointer < this.stack.size() - 1) {
			for (int i = this.stackPointer; i < this.stack.size() - 1; i++) {
				try {
					this.pop();
				} catch (CloseCanceledException e) {
					return; // close canceled; stop here.
				}
			}
			// return;
		}
		if (this.stack.isEmpty()) {
			this.setLeftComponent(view);
			this.setRightComponent(null);
		} else {
			this.setLeftComponent(this.stack.get(this.stack.size() - 1));
			this.setRightComponent(view);
			this.stackPointer++;
		}
		this.stack.add(view);
		this.addButtons();
	}
	
	/**
	 * Completely removes the top view from the stack.
	 * 
	 * @pre views.size() > 0 == true
	 */
	public final void pop() throws CloseCanceledException {
		this.stack.get(this.stack.size() - 1).close();
		this.stack.remove(this.stack.size() - 1);
		if (this.stackPointer == this.stack.size()) {
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
		this.setLeftComponent(this.stack.get(this.stackPointer));
		this.setRightComponent(this.stack.get(++this.stackPointer));
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

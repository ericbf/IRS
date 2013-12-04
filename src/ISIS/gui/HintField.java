/**
 * 
 */
package ISIS.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * @author eric
 */
public class HintField extends JTextField {
	private class UI extends BasicTextFieldUI {
		private String	hint;
		private boolean	hintEnabled;
		
		public UI(String hint) {
			super();
			
			this.hint = hint;
			this.hintEnabled = true;
		}
		
		/*
		 * @pre - receives graphics object
		 * @post - object drawn
		 */
		@Override
		protected void paintSafely(Graphics g) {
			super.paintSafely(g);
			HintField comp = (HintField) this.getComponent();
			if (this.hintEnabled && this.hint != null && comp.isEmpty()) {
				g.setColor(Color.gray);
				g.setFont(new Font(comp.getFont().getName(), Font.ITALIC, comp
						.getFont().getSize()));
				int padding = (comp.getHeight() - comp.getFont().getSize()) / 2;
				g.drawString(this.hint, 7, comp.getHeight() - padding - 2);
			}
		}
		
		/*
		 * @pre - none
		 * @post - object redrawn
		 */
		public void repaint() {
			if (this.getComponent() != null) {
				this.getComponent().repaint();
			}
		}
	}
	
	private static final long	serialVersionUID	= 1L;
	
	private boolean				selectAll;
	private String				hint;
	private UI					ui;
	
	/*
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	public HintField() {
		this("");
	}
	
	/*
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	public HintField(String hint) {
		this(hint, "");
	}
	
	/*
	 * @pre - none, constructor
	 * @post - object instantiated and returned
	 */
	public HintField(String hint, String initialText) {
		super(initialText);
		this.hint = hint;
		this.setUI(this.ui = new UI(this.hint));
		this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");
		this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
						java.awt.event.InputEvent.META_DOWN_MASK), "none");
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
						java.awt.event.InputEvent.ALT_DOWN_MASK), "none");
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP,
						java.awt.event.InputEvent.META_DOWN_MASK), "none");
		this.getInputMap().put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP,
						java.awt.event.InputEvent.ALT_DOWN_MASK), "none");
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (HintField.this.selectAll) {
					HintField.this.selectAll();
				}
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				if (HintField.this.selectAll) {
					HintField.this.setCaretPosition(0);
				}
			}
		});
		this.addKeyListener(new KeyListener() {
			private final int	BACKSPACE	= 8;
			private HintField	me			= HintField.this;
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case BACKSPACE:
						if (meta) {
							int caretPosition = this.me.getCaretPosition();
							if (caretPosition < this.me.getText().length()) {
								this.me.setText(this.me.getText().substring(
										caretPosition));
								this.me.setCaretPosition(0);
							} else {
								this.me.setText("");
							}
						}
						break;
				}
				HintField.this.ui.repaint();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
	
	/*
	 * @pre - none
	 * @post - returns true if no text
	 */
	public boolean isEmpty() {
		return super.getText().isEmpty();
	}
	
	/*
	 * @pre - receives a bool
	 * @post - sets selectAll to bool passed
	 */
	public HintField setSelectAll(boolean b) {
		this.selectAll = b;
		return this;
	}
}
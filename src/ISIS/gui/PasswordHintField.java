/**
 * 
 */
package ISIS.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.plaf.LayerUI;

/**
 * @author eric
 */
public class PasswordHintField extends JPasswordField {
	private class PasswordHintFieldLayer extends LayerUI<PasswordHintField> {
		private static final long	serialVersionUID	= 1L;
		
		/*
		 * (non-Javadoc)
		 * @see javax.swing.plaf.LayerUI#paint(java.awt.Graphics,
		 * javax.swing.JComponent)
		 */
		@Override
		public void paint(Graphics g, JComponent c) {
			super.paint(g, c);
			
			Graphics2D g2 = (Graphics2D) g.create();
			
			if (PasswordHintField.this.hintEnabled
					&& PasswordHintField.this.hint != null
					&& PasswordHintField.this.isEmpty()) {
				g.setColor(Color.gray);
				g.setFont(new Font(PasswordHintField.this.getFont().getName(),
						Font.ITALIC, PasswordHintField.this.getFont().getSize()));
				int padding = (PasswordHintField.this.getHeight() - PasswordHintField.this
						.getFont().getSize()) / 2;
				g.drawString(PasswordHintField.this.hint, 7,
						PasswordHintField.this.getHeight() - padding - 2);
			}
			
			g2.dispose();
		}
	}
	
	private static final long				serialVersionUID	= 1L;
	
	private boolean							made;
	
	private String							hint;
	private boolean							selectAll;
	private boolean							hintEnabled;
	private final JLayer<PasswordHintField>	layer;
	
	public PasswordHintField() {
		this(null);
	}
	
	public PasswordHintField(String hint) {
		super();
		this.hint = hint;
		this.hintEnabled = true;
		
		LayerUI<PasswordHintField> layerUI = new PasswordHintFieldLayer();
		this.layer = new JLayer<>(this, layerUI);
		
		this.setSettings();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JTextField#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		if (!PasswordHintField.this.made) {
			throw new IllegalAccessError(
					"This hint field was not made before being shown!");
		}
		return super.getPreferredSize();
	}
	
	/**
	 * @pre - none
	 * @post - returns true if no text
	 */
	public boolean isEmpty() {
		return super.getPassword().length == 0;
	}
	
	public JLayer<PasswordHintField> make() {
		this.made = true;
		return this.layer;
	}
	
	/**
	 * @pre - receives a bool
	 * @post - sets selectAll to bool passed
	 */
	public PasswordHintField setSelectAll(boolean b) {
		this.selectAll = b;
		return this;
	}
	
	private void setSettings() {
		
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
				if (PasswordHintField.this.selectAll) {
					PasswordHintField.this.selectAll();
				}
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				if (PasswordHintField.this.selectAll) {
					PasswordHintField.this.setCaretPosition(0);
				}
			}
		});
		this.addKeyListener(new KeyListener() {
			private final int			BACKSPACE	= 8;
			private PasswordHintField	me			= PasswordHintField.this;
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case BACKSPACE:
						if (meta) {
							int caretPosition = this.me.getCaretPosition();
							if (caretPosition < this.me.getPassword().length) {
								String text = "";
								for (int i = caretPosition; i < this.me
										.getPassword().length; i++) {
									text += this.me.getPassword()[i];
								}
								this.me.setText(text);
								this.me.setCaretPosition(0);
							} else {
								this.me.setText("");
							}
						}
						break;
				}
				PasswordHintField.this.layer.repaint();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
}

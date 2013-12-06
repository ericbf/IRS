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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.plaf.LayerUI;

/**
 * @author eric
 */
public class HintField extends JTextField {
	private class HintFieldLayer extends LayerUI<HintField> {
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
			
			if (HintField.this.hintEnabled && HintField.this.hint != null
					&& HintField.this.isEmpty()) {
				g.setColor(Color.gray);
				g.setFont(new Font(HintField.this.getFont().getName(),
						Font.ITALIC, HintField.this.getFont().getSize()));
				int padding = (HintField.this.getHeight() - HintField.this
						.getFont().getSize()) / 2;
				g.drawString(HintField.this.hint, 7, HintField.this.getHeight()
						- padding - 2);
			}
			
			g2.dispose();
		}
	}
	
	private static final long		serialVersionUID	= 1L;
	
	private boolean					made;
	
	private String					hint;
	private boolean					selectAll;
	private boolean					hintEnabled;
	private final JLayer<HintField>	layer;
	
	public HintField() {
		this(null);
	}
	
	public HintField(String hint) {
		super();
		this.hint = hint;
		this.hintEnabled = true;
		
		LayerUI<HintField> layerUI = new HintFieldLayer();
		this.layer = new JLayer<>(this, layerUI);
		
		this.setSettings();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JTextField#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		if (!HintField.this.made) {
			throw new IllegalAccessError(
					"This hint field was not made before being shown!");
		}
		return super.getPreferredSize();
	}
	
	/**
	 * @return
	 * @pre - none
	 * @post - returns true if no text
	 */
	public boolean isEmpty() {
		return super.getText().isEmpty();
	}
	
	public JLayer<HintField> make() {
		this.made = true;
		return this.layer;
	}
	
	/**
	 * @param b
	 * @return
	 * @pre - receives a bool
	 * @post - sets selectAll to bool passed
	 */
	public HintField setSelectAll(boolean b) {
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
				HintField.this.layer.repaint();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
}

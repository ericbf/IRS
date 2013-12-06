/**
 * 
 */
package ISIS.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.plaf.LayerUI;

/**
 * @author eric
 */
public class HintArea extends JTextArea {
	private class HintAreaLayer extends LayerUI<HintArea> {
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
			
			if (HintArea.this.hintEnabled && HintArea.this.hint != null
					&& HintArea.this.isEmpty()) {
				g.setColor(Color.gray);
				g.setFont(new Font(HintArea.this.getFont().getName(),
						Font.ITALIC, HintArea.this.getFont().getSize()));
				g.drawString(HintArea.this.hint, 7, HintArea.this.getFont()
						.getSize() + 4);
			}
			
			g2.dispose();
		}
	}
	
	private static final long		serialVersionUID	= 1L;
	
	private boolean					made;
	
	private String					hint;
	private boolean					selectAll;
	private boolean					hintEnabled;
	private final JLayer<HintArea>	layer;
	
	public HintArea() {
		this(null);
	}
	
	public HintArea(String hint) {
		super();
		this.hint = hint;
		this.hintEnabled = true;
		
		LayerUI<HintArea> layerUI = new HintAreaLayer();
		this.layer = new JLayer<>(this, layerUI);
		
		this.setSettings();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JTextField#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		if (!HintArea.this.made) {
			throw new IllegalAccessError(
					"This hint area was not made before being shown!");
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
	
	public JLayer<HintArea> make() {
		this.made = true;
		return this.layer;
	}
	
	/**
	 * @param b
	 * @return
	 * @pre - receives a bool
	 * @post - sets selectAll to bool passed
	 */
	public HintArea setSelectAll(boolean b) {
		this.selectAll = b;
		return this;
	}
	
	private void setSettings() {
		this.setBorder(new Border() {
			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(5, 7, 5, 7);
			}
			
			@Override
			public boolean isBorderOpaque() {
				return false;
			}
			
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				if (HintArea.this.isFocusOwner()) {
					g.setColor(new Color(0x6d96be));
					g.drawRect(x, y, width--, height--);
				} else {
					g.setColor(new Color(0xa2a2a2));
					g.drawRect(x, y, width--, height--);
				}
				g.setColor(new Color(0xd0d0d0));
				g.drawRect(++x, ++y, --width, --height);
				
			}
		});
		this.setWrapStyleWord(true);
		this.setLineWrap(true);
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (HintArea.this.selectAll) {
					HintArea.this.selectAll();
				}
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				if (HintArea.this.selectAll) {
					HintArea.this.setCaretPosition(0);
				}
			}
		});
		this.addKeyListener(new KeyListener() {
			private final int	BACKSPACE	= 8;
			private HintArea	me			= HintArea.this;
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean meta = (e.getModifiers() & ActionEvent.META_MASK) == ActionEvent.META_MASK
						|| (e.getModifiers() & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK;
				switch (e.getKeyCode()) {
					case BACKSPACE:
						if (meta) {
							int caretPosition = this.me.getCaretPosition();
							int startPos;
							String text = this.me.getText();
							char c;
							for (startPos = caretPosition - 1; startPos > 0; startPos--) {
								if ((c = text.charAt(startPos)) == '\r'
										|| c == '\n') {
									break;
								}
							}
							this.me.setText(text.substring(0, startPos)
									+ text.substring(caretPosition,
											text.length()));
							this.me.setCaretPosition(startPos);
						}
						break;
				}
				HintArea.this.layer.repaint();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
}

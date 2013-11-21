/**
 * 
 */
package ISIS.gui;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * @author eric
 */
public class HintField extends JTextField {
	private static final long	serialVersionUID	= 1L;
	private boolean				showingHint;
	private boolean				hintEnabled;
	private boolean				selectAll;
	private String				hint;
	
	public HintField() {
		this("");
	}
	
	public HintField(String hint) {
		this(hint, "");
	}
	
	public HintField(String hint, String initialText) {
		super();
		super.setText(initialText.isEmpty() ? hint : initialText);
		this.hint = hint;
		this.showingHint = initialText.isEmpty();
		this.hintEnabled = true;
		// this.selectAll = true;
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
				if (HintField.this.showingHint && HintField.this.hintEnabled) {
					HintField.super.setText("");
				}
				HintField.this.showingHint = false;
			}
			
			@Override
			public void focusLost(FocusEvent fe) {
				if (HintField.this.selectAll) {
					HintField.this.setCaretPosition(0);
				}
				if (HintField.this.getText().isEmpty()) {
					HintField.this.showingHint = true;
					if (HintField.this.hintEnabled) {
						HintField.super.setText(HintField.this.hint);
					}
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
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyTyped(KeyEvent e) {}
		});
	}
	
	@Override
	public String getText() {
		if (this.showingHint) {
			return "";
		} else {
			return super.getText();
		}
	}
	
	public HintField setHintEnabled(boolean b) {
		this.hintEnabled = b;
		return this;
	}
	
	public HintField setSelectAll(boolean b) {
		this.selectAll = b;
		return this;
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		if (this.showingHint = text.isEmpty() && HintField.this.hintEnabled
				&& !this.isFocusOwner()) {
			HintField.super.setText(HintField.this.hint);
		}
	}
}
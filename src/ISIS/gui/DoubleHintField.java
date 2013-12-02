/**
 * 
 */
package ISIS.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * @author eric
 */
public class DoubleHintField extends HintField {
	private static final long	serialVersionUID	= 1L;
	private String				toReturn;
	
	public DoubleHintField() {
		this(null, null);
	}
	
	public DoubleHintField(String hint) {
		this(hint, null);
	}
	
	public DoubleHintField(String hint, String initialText) {
		super(hint, initialText);
		this.toReturn = "0.00";
		((PlainDocument) this.getDocument())
				.setDocumentFilter(new DocumentFilter() {
					private boolean check(String str) {
						return str
								.matches("([1-9][0-9]*)?[0-9]?(\\.[0-9]?[0-9]?)?");
					}
					
					@Override
					public void insertString(FilterBypass fb, int offset,
							String string, AttributeSet attr)
							throws BadLocationException {
						
						Document doc = fb.getDocument();
						StringBuilder sb = new StringBuilder();
						sb.append(doc.getText(0, doc.getLength()));
						sb.insert(offset, string);
						
						if (this.check(sb.toString())) {
							super.insertString(fb, offset, string, attr);
						}
					}
					
					@Override
					public void remove(FilterBypass fb, int offset, int length)
							throws BadLocationException {
						Document doc = fb.getDocument();
						StringBuilder sb = new StringBuilder();
						sb.append(doc.getText(0, doc.getLength()));
						sb.delete(offset, offset + length);
						
						if (this.check(sb.toString())
								|| sb.toString().isEmpty()) {
							super.remove(fb, offset, length);
						}
					}
					
					@Override
					public void replace(FilterBypass fb, int offset,
							int length, String text, AttributeSet attrs)
							throws BadLocationException {
						Document doc = fb.getDocument();
						StringBuilder sb = new StringBuilder();
						sb.append(doc.getText(0, doc.getLength()));
						sb.replace(offset, offset + length, text);
						
						if (this.check(sb.toString())) {
							super.replace(fb, offset, length, text, attrs);
						}
					}
				});
		this.addFocusListener(new FocusListener() {
			
			@Override
			public void focusGained(FocusEvent e) {}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (DoubleHintField.this.getText().matches("[0-9]*\\.")) {
					DoubleHintField.this.setText(DoubleHintField.this.getText()
							+ "0");
				}
				if (DoubleHintField.this.getText().matches("\\.[0-9]*")) {
					DoubleHintField.this.setText("0"
							+ DoubleHintField.this.getText());
				}
				if (DoubleHintField.this.getText().matches("[0-9]*\\.[0-9]")) {
					DoubleHintField.this.setText(DoubleHintField.this.getText()
							+ "0");
				}
				DoubleHintField.this.setCaretPosition(0);
			}
		});
	}
	
	@Override
	public String getText() {
		if (super.getText().isEmpty()) {
			return this.toReturn;
		} else {
			return super.getText();
		}
	}
	
	public void setToReturnIfEmpty(String str) {
		this.toReturn = str;
	}
}

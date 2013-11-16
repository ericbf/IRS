/**
 * 
 */
package ISIS.gui;

import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.SQLException;

/**
 * @author eric
 */
public abstract class AddEditView extends View {
	
	private static final long			serialVersionUID	= 1L;
	public static final DocumentFilter	numberFilter;
	static {
		numberFilter = new DocumentFilter() {
			private boolean check(String str) {
				return str.matches("([1-9][0-9]*)?[0-9]?(\\.[0-9]?[0-9]?)?");
			}
			
			@Override
			public void insertString(FilterBypass fb, int offset,
					String string, AttributeSet attr)
					throws BadLocationException {
				
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);
				
				if (this.check(sb.toString()))
					super.insertString(fb, offset, string, attr);
			}
			
			@Override
			public void remove(FilterBypass fb, int offset, int length)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);
				
				if (this.check(sb.toString()) || sb.toString().isEmpty())
					super.remove(fb, offset, length);
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length,
					String text, AttributeSet attrs)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);
				
				if (this.check(sb.toString()))
					super.replace(fb, offset, length, text, attrs);
			}
		};
	}
	protected boolean					wasSavedOrAlreadySetUp;
	
	/**
	 * Link to super
	 */
	public AddEditView(SplitPane splitPane) {
		super(splitPane);
		this.setBorder(new EmptyBorder(4, 0, 10, 5));
	}
	
	/**
	 * Used to disable any unchangeable fields
	 * 
	 * @param fields
	 */
	protected final void disableFields(HintField... fields) {
		this.wasSavedOrAlreadySetUp = true;
		for (HintField field : fields) {
			field.setEditable(false);
			field.setForeground(Color.gray);
		}
	}
	
	/**
	 * Use this to activate the necessary fields after the first save. Override
	 * to give action.
	 */
	protected void doSaveRecordAction() {}
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#needsSave()
	 */
	@Override
	public final boolean needsSave() {
		return true;
	};
	
	/*
	 * (non-Javadoc)
	 * @see ISIS.gui.View#save()
	 */
	@Override
	public void save() throws SQLException {
		try {
			if (this.isAnyFieldDifferentFromDefault() != null
					&& this.isAnyFieldDifferentFromDefault())
				this.getCurrentRecord().save();
			if (!this.wasSavedOrAlreadySetUp) {
				this.doSaveRecordAction();
				this.wasSavedOrAlreadySetUp = true;
			}
		} catch (BadInputException e) {
			ErrorLogger.error(e, "", false, true);
		}
	}
}

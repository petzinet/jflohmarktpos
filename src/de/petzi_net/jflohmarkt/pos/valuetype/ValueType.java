/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.valuetype;

import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import de.petzi_net.jflohmarkt.pos.JFlohmarktPOS;

/**
 * @author axel
 *
 */
public abstract class ValueType<T> extends AbstractFormatterFactory {

	private final Class<T> type;
	private final InputVerifier inputVerifier = new InputVerifier() {
		
		@Override
		public boolean verify(JComponent input) {
			boolean result;
			if (input instanceof JTextComponent) {
				result = ValueType.this.verify(((JTextComponent) input).getText(), true);
			} else {
				result = false;
			}
			if (!result) {
				JFlohmarktPOS.flashFrame(input);
			}
			return result;
		}
		
	};
	private final DocumentFilter documentFilter = new DocumentFilter() {

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			String newText = fb.getDocument().getText(0, offset) + string + fb.getDocument().getText(offset, fb.getDocument().getLength() - offset);
			if (verify(newText, false)) {
				super.insertString(fb, offset, string, attr);
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String newText = fb.getDocument().getText(0, offset) + text + fb.getDocument().getText(offset + length, fb.getDocument().getLength() - offset - length);
			if (verify(newText, false)) {
				super.replace(fb, offset, length, text, attrs);
			}
		}

		
	};
	
	public ValueType(Class<T> type) {
		this.type = type;
	}
	
	@Override
	public AbstractFormatter getFormatter(JFormattedTextField tf) {
		return new Formatter();
	}
	
	public InputVerifier getInputVerifier() {
		return inputVerifier;
	}
	
	public abstract int getHorizontalAlignment();
	
	public abstract int getMaxLength();
	
	protected abstract boolean verify(String text, boolean committing);
	
	protected abstract T stringToValue(String text) throws ParseException;
	
	protected abstract String valueToString(T value) throws ParseException;
	
	@SuppressWarnings("serial")
	private class Formatter extends AbstractFormatter {
		
		@Override
		public String valueToString(Object value) throws ParseException {
			return ValueType.this.valueToString(type.cast(value));
		}
		
		@Override
		public Object stringToValue(String text) throws ParseException {
			return ValueType.this.stringToValue(text);
		}
		
		protected DocumentFilter getDocumentFilter() {
			return ValueType.this.documentFilter;
		};
		
	}

}

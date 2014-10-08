/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.valuetype;

import java.text.ParseException;

import javax.swing.SwingConstants;

/**
 * @author axel
 *
 */
public class StringValueType extends ValueType<String> {

	private final int length;
	
	public StringValueType(int length) {
		super(String.class);
		this.length = length;
	}
	
	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.LEFT;
	}

	@Override
	public int getMaxLength() {
		return length;
	}

	@Override
	protected boolean verify(String text, boolean committing) {
		if (text == null || text.isEmpty())
			return true;
		if (text.length() > length)
			return false;
		return true;
	}

	@Override
	protected String stringToValue(String text) throws ParseException {
		return text;
	}

	@Override
	protected String valueToString(String value) throws ParseException {
		return value;
	}

}

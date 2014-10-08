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
public class IntegerValueType extends ValueType<Integer> {

	private final int digits;
	
	public IntegerValueType(int digits) {
		super(Integer.class);
		this.digits = digits;
	}
	
	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.RIGHT;
	}

	@Override
	public int getMaxLength() {
		return digits;
	}

	@Override
	protected boolean verify(String text, boolean committing) {
		if (text == null || text.isEmpty())
			return true;
		if (!text.matches("\\d*"))
			return false;
		if (text.length() > digits)
			return false;
		return true;
	}

	@Override
	protected Integer stringToValue(String text) throws ParseException {
		if (text == null || text.trim().isEmpty())
			return null;
		try {
			return Integer.valueOf(text);
		} catch (NumberFormatException e) {
			throw new ParseException("not an integer", 0);
		}
	}

	@Override
	protected String valueToString(Integer value) throws ParseException {
		return value == null ? "" : value.toString();
	}

}

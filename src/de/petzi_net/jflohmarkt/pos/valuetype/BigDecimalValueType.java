/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.valuetype;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import javax.swing.SwingConstants;

/**
 * @author axel
 *
 */
public class BigDecimalValueType extends ValueType<BigDecimal> {

	private final int precision;
	private final int scale;
	private final boolean negative;
	
	public BigDecimalValueType(int precision, int scale, boolean negative) {
		super(BigDecimal.class);
		this.precision = precision;
		this.scale = scale;
		this.negative = negative;
	}
	
	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.RIGHT;
	}

	@Override
	public int getMaxLength() {
		return precision + 1 + (negative ? 1 : 0);
	}
	
	@Override
	protected boolean verify(String text, boolean committing) {
		if (text == null || text.isEmpty())
			return true;
		if (text.charAt(0) == '-') {
			if (negative) {
				text = text.substring(1);
			} else {
				return false;
			}
		}
		if (!text.matches("\\d*(,\\d*)?"))
			return false;
		int pos = text.indexOf(',');
		if (pos < 0) {
			if (text.length() > precision - scale)
				return false;
		} else {
			if (pos > precision - scale)
				return false;
			if (text.length() - pos - 1 > scale)
				return false;
		}
		return true;
	}

	@Override
	protected BigDecimal stringToValue(String text) throws ParseException {
		if (text == null || text.trim().isEmpty())
			return null;
		try {
			return new BigDecimal(text.replace(',', '.')).setScale(scale, RoundingMode.HALF_UP);
		} catch (NumberFormatException e) {
			throw new ParseException("not a decimal", 0);
		}
	}

	@Override
	protected String valueToString(BigDecimal value) throws ParseException {
		return value == null ? "" : value.toPlainString().replace('.', ',');
	}

}

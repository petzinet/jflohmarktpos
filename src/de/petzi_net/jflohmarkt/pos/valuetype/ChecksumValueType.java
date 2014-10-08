/**
 * 
 */
package de.petzi_net.jflohmarkt.pos.valuetype;

import java.text.ParseException;

/**
 * @author axel
 *
 */
public class ChecksumValueType extends IntegerValueType {
	
	private final int digits;
	
	public ChecksumValueType(int digits) {
		super(digits);
		this.digits = digits;
	}
	
	@Override
	protected boolean verify(String text, boolean committing) {
		boolean result = super.verify(text, committing);
		if (committing && text != null && text.length() > 0) {
			if (result) {
				result = text.length() == digits;
			}
			if (result) {
				try {
					Integer value = stringToValue(text);
					result = checkNumber(value);
				} catch (ParseException e) {
					result = false;
				}
			}
		}
		return result;
	}
	
	private static boolean checkNumber(int number) {
		int checksum = 0;
		boolean oddDigit = false;
		while (number > 0) {
			int digit = number % 10;
			number = number / 10;
			checksum += digit * (oddDigit ? 3 : 1);
			oddDigit = !oddDigit;
		}
		return (checksum % 10) == 0;
	}

}

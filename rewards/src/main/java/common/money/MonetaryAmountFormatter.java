package common.money;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import common.money.MonetaryAmount;

public class MonetaryAmountFormatter implements Formatter<MonetaryAmount> {
	// Note that in some cases the Locale might be important here -
	// conceivably a monetary amount could be in UK Pounds Sterling, Dollars or
	// Euros, for example. However, to keep things simple, we're just going to
	// presume it's in dollars only.

	/**
	 * Convert the given MonetaryAmount to a String
	 * 
	 * @param amount
	 *            The amount in question
	 * @param locale
	 *            The currently active locale
	 * 
	 * @return The converted amount
	 */
	public String print(MonetaryAmount amount, Locale locale) {
		return amount.toString();
	}

	/**
	 * Converts the given string to a monetary amount
	 * 
	 * @param text
	 *            The string to convert
	 * @param locale
	 *            The currently active locale
	 * 
	 * @return The converted amount
	 * 
	 * @throws ParseException
	 *             if the amount was incorrectly defined
	 */
	public MonetaryAmount parse(String text, Locale locale)
			throws ParseException {
		return MonetaryAmount.valueOf(text);
	}
}

package common.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.format.Formatter;

import common.datetime.SimpleDate;

public class SimpleDateFormatter implements Formatter<SimpleDate> {
	// The date could conceivably be locale-dependent, 
	// but we're hardcoding it in one format for simplicity.
	private static final SimpleDateFormat formatter 
		= new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Convert the given SimpleDate to a String
	 * 
	 * @param date		The date in question
	 * @param locale	The currently active locale
	 * 
	 * @return	The converted date
	 */
	public String print(SimpleDate date, Locale locale) {
		return formatter.format(date.asDate());
	}

	/**
	 * Converts the given string to a simple date
	 * 
	 * @param text		The string to convert
	 * @param locale	The currently active locale
	 * 
	 * @return	The converted date
	 * 
	 * @throws	ParseException if the date was incorrectly defined
	 */
	public SimpleDate parse(String text, Locale locale)
			throws ParseException {
		return SimpleDate.valueOf(formatter.parse(text));
	}
}

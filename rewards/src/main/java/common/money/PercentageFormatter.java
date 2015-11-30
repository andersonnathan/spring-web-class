package common.money;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

/**
 * A formatter for Percentage properties. Converts object values to well-formatted strings and strings back to values.
 * Usable by a data binding framework for binding user input to the model.
 */
public class PercentageFormatter implements Formatter<Percentage> {

	@Override
	public String print(Percentage percentage, Locale locale) {
		if (percentage == null) {
			return "";
		} else {
			return percentage.toString();
		}
	}

	@Override
	public Percentage parse(String text, Locale locale) throws ParseException {
		if (StringUtils.hasText(text)) {
			return Percentage.valueOf(text);
		} else {
		 return null;
		}
	}

}

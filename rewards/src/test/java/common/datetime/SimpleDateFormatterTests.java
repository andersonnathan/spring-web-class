package common.datetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;

public class SimpleDateFormatterTests {

	private SimpleDateFormatter formatter = new SimpleDateFormatter();

	@Test
	public void testPrint() {
		SimpleDate date = new SimpleDate(12, 29, 1977);
		assertEquals("1977-12-29",
				formatter.print(date, Locale.getDefault()));
	}

	@Test
	public void testParse() throws ParseException {
		assertEquals(new SimpleDate(12, 29, 1977),
				formatter.parse("1977-12-29", Locale.getDefault()));
	}

	@Test
	public void testParseBogus() {
		try {
			formatter.parse("1977-12-abc", Locale.getDefault());
			fail("Should have failed - parse exception");
		} catch (ParseException e) {

		}
	}
}

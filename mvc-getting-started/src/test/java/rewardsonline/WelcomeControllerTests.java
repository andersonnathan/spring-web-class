package rewardsonline;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for the WelcomeController implementation.
 */
public class WelcomeControllerTests {

	private WelcomeController controller;

	@Before
	public void setUp() throws Exception {
		controller = new WelcomeController();
	}

	// TODO-12: Remove the @Ignore and use JUnit to run the test.
	@Test @Ignore
	public void testHome() throws Exception {
		String view = controller.welcome();
		assertEquals("welcome", view);
	}

}
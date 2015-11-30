package rewardsonline;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
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

	@Test
	public void testHome() throws Exception {
		String view = controller.welcome();
		assertEquals("welcome", view);
	}

}
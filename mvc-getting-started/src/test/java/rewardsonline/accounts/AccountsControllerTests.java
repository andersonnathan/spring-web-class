package rewardsonline.accounts;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import rewardsonline.accounts.test.AccountTestConstants;
import rewardsonline.accounts.test.StubAccountManagerWithOneAccount;

/**
 * Unit test for the AccountController implementation.
 */
@SuppressWarnings("unused")
public class AccountsControllerTests {

	// The account number you can use in your tests.
	protected final static String ACC_123456789 = AccountTestConstants.ACC_123456789;

	// The account name you can use in your tests.
	protected final static String KEITH_AND_KERI = AccountTestConstants.KEITH_AND_KERI;

	private AccountsController controller;

	private AccountManager accountManager;

	@Before
	public void setUp() throws Exception {
		// Stub class is already defined in rewards project
		accountManager = new StubAccountManagerWithOneAccount();

		// TODO-13: Create a controller to test
	}

	@Test
	public void testList() throws Exception {
		Model model = new ExtendedModelMap();
		// TODO-14: Implement the test
		// Check the view-name returned and the contents of the Model
	}

	@Test
	public void testShow() throws Exception {
		// TODO-15: Implement the test
		// Check the view-name returned and the contents of the Model
	}

}
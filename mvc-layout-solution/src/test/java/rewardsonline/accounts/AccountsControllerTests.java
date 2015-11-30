package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import rewardsonline.accounts.test.AccountTestConstants;
import rewardsonline.accounts.test.StubAccountManagerWithOneAccount;

/**
 * Unit test for the AccountController implementation.
 */
public class AccountsControllerTests implements AccountTestConstants {

	private AccountsController controller;

	private AccountManager accountManager;

	@Before
	public void setUp() throws Exception {
		// Stub class is already defined in rewards project
		accountManager = new StubAccountManagerWithOneAccount();
		
		// The controller to test
		controller = new AccountsController(accountManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testList() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.list(model);
		assertEquals("accounts/list", view);
		assertEquals(1, ((List<Account>) model.get("accountList")).size());
	}

	@Test
	public void testShow() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.show(ACC_123456789, model);
		assertEquals("accounts/show", view);
		assertEquals(ACC_123456789, ((Account) model.get("account")).getNumber());
	}

}
package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import rewardsonline.accounts.test.StubAccountManagerWithOneAccount;

/**
 * A JUnit test case testing the AccountController.
 */
public class AccountsControllerTests {

	protected static final String TEST_ACCOUNT = StubAccountManagerWithOneAccount.ACC_123456789;
	protected static final String NEW_ACCOUNT = "112233445";
	private AccountsController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountsController(new StubAccountManagerWithOneAccount());
	}

	@Test
	public void testShow() throws Exception {
		Account account = (Account) controller.findAccount(TEST_ACCOUNT);
		String view = controller.show();
		assertNotNull(view);
		assertEquals("accounts/show", view);
		assertNotNull(account);
		assertEquals(TEST_ACCOUNT, account.getNumber());
	}

	@Test
	public void testEdit() throws Exception {
		String view = controller.edit();
		assertEquals("accounts/edit", view);
	}

	@Test
	public void testSave() {
		final Account account = new Account(NEW_ACCOUNT, "Fred Smith");
		final String successView = "redirect:" + account.getNumber();
		final String errorView = "accounts/edit";
		BindingResult result = new BeanPropertyBindingResult(account, "account");
		Model model = new ExtendedModelMap();

		String view1 = controller.save(account, result, model);
		assertEquals(successView, view1);

		result.addError(new ObjectError("account", "broken"));
		String view2 = controller.save(account, result, model);
		assertEquals(errorView, view2);
	}

	public void testAccountDetails() {
		String accountNumber = TEST_ACCOUNT;
		Account account = controller.accountDetails(accountNumber);
		assertNotNull(account);
	}
}

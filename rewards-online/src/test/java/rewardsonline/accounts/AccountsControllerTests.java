package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import rewardsonline.accounts.test.AccountTestConstants;
import rewardsonline.accounts.test.StubAccountManagerWithTwoAccounts;

public class AccountsControllerTests {

	protected static final String NEW_ACCOUNT_NUMBER = "112233445";
	protected static final String NEW_ACCOUNT_NAME = "Joe";
	
	private AccountsController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountsController(new StubAccountManagerWithTwoAccounts());
	}

	@Test
	public void testSaveEdit() {
		Account account = new Account(NEW_ACCOUNT_NUMBER, NEW_ACCOUNT_NAME);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				account, "account");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.save(account, result, model);
		assertEquals("redirect:" + account.getNumber(), view);
	}

	@Test
	public void testModelAttributeFactoryMethodFindAccount() throws Exception {
		Account account = controller.findAccount(AccountTestConstants.ACC_100000001);
		assertNotNull(account);
	}

}
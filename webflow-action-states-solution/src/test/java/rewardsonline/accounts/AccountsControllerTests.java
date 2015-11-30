package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import rewardsonline.accounts.test.StubAccountManagerWithTwoAccounts;

public class AccountsControllerTests {

	private AccountsController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountsController(
				new StubAccountManagerWithTwoAccounts());
	}

	@Test
	public void testSaveEdit() {
		Account account = new Account("1", "joe");
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				account, "account");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.save(account, result, model);
		assertEquals("redirect:" + account.getNumber(), view);
	}

	@Test
	public void testModelAttributeFactoryMethodFindAccount() throws Exception {
		Account account = controller
				.findAccount(StubAccountManagerWithTwoAccounts.ACC_100000001);
		assertNotNull(account);
	}

}
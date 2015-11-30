package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import rewardsonline.accounts.test.AccountTestConstants;
import rewardsonline.accounts.test.StubAccountManagerWithOneAccount;

public class AccountsControllerTests implements AccountTestConstants {

	private AccountsController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountsController(new StubAccountManagerWithOneAccount());
	}

	@Test
	public void testSaveEdit() {
		Account account = new Account(ACC_123456789, KEITH_AND_KERI);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				account, "account");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.save(account, result, model);
		assertEquals("redirect:" + account.getNumber(), view);
	}

	@Test
	public void testModelAttributeFactoryMethodFindAccount() throws Exception {
		Account account = controller.findAccount(ACC_123456789);
		assertNotNull(account);
	}

}
package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static rewardsonline.accounts.test.AccountTestConstants.GLENDA_SMITH;
import static rewardsonline.accounts.test.AccountTestConstants.ACC_100000001;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import rewardsonline.accounts.test.StubAccountManagerWithTwoAccounts;

public class AccountsControllerTests {

	private AccountsController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountsController(
				new StubAccountManagerWithTwoAccounts());
	}

	@Test
	public void testShowAccount() throws Exception {
		Model model = new ExtendedModelMap();

		String view = controller.show(ACC_100000001, model);
		assertEquals("accounts/show", view);

		Account account = (Account) model.asMap().get("account");
		assertNotNull(account);
		assertEquals(ACC_100000001, account.getNumber());
		assertEquals(GLENDA_SMITH, account.getName());
		assertEquals(2, account.getBeneficiaries().size());
	}

	// TODO-05: Remove @Ignore once the edit() controller method exists
	@Ignore
	@Test
	public void testShowEditForm() throws Exception {
		Model model = new ExtendedModelMap();

		String view = null;

		// TODO-06: Uncomment the controller.edit() line.
		//          Save the result of this call in the view variable.
		//          Ensure the test runs.

		// controller.edit(ACC_100000001, model);
		assertEquals("accounts/edit", view);

		Account account = (Account) model.asMap().get("account");
		assertNotNull(account);
		assertEquals(ACC_100000001, account.getNumber());
		assertEquals(GLENDA_SMITH, account.getName());
		assertEquals(2, account.getBeneficiaries().size());
	}

	/**
	 * The save method is a placeholder that will be implemented properly in the
	 * next lab.
	 */
	@Test
	public void testSaveEdit() {
		Account account = new Account("1", "joe");
		String view = controller.save("1", account);
		assertEquals("showData", view);
	}

}
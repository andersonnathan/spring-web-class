package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import rewardsonline.accounts.test.StubAccountManagerWithOneAccount;

/**
 * A JUnit test case testing the AccountController.
 */
public class AccountSearchControllerTests {

	private AccountSearchController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountSearchController(new StubAccountManagerWithOneAccount());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testList() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.list(model);
		assertEquals("accounts/list", view);
		assertEquals(1, ((List<Account>) model.get("accountList")).size());
	}

	@Test
	public void testListData() throws Exception {
		List<Account> accounts = controller.listData(null);
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
	}

}

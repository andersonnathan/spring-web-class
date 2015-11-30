package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

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

	@Test
	@SuppressWarnings("unchecked")
	public void testList() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.list(model);
		assertEquals("accounts/list", view);
		assertEquals(1, ((List<Account>) model.get("accountList")).size());
	}

	@Test
	public void testGetSearchForm() throws Exception {
		String view = controller.search(new AccountSearchCriteria());
		assertEquals("accounts/search", view);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSearchOk() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("keith");
		criteria.setMaximumResults(5);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				criteria, "accountSearchCriteria");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.processSubmit(criteria, result, model);
		assertEquals("redirect:accounts/123456789", view);
		List<Account> accounts = (List<Account>)model.get("accountList");
		assertNull(accounts);
	}

	@Test
	public void testRestfulSearchOk() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("keith");
		criteria.setMaximumResults(5);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				criteria, "accountSearchCriteria");
		List<Account> accounts = controller.search2(criteria, result);
		
		assertNotNull(accounts);
		assertEquals(1, accounts.size());
	}
	
	@Test
	public void testSearchFail() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("fred");
		criteria.setMaximumResults(5);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				criteria, "accountSearchCriteria");
		List<Account> accounts = controller.search2(criteria, result);
		
		assertNotNull(accounts);
		assertEquals(0, accounts.size());
	}

}

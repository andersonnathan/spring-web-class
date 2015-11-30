package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import rewardsonline.accounts.test.AccountTestConstants;
import rewardsonline.accounts.test.StubAccountManagerWithTwoAccounts;

public class AccountSearchControllerTests {

	protected static final String GLENDA_SMITH = AccountTestConstants.GLENDA_SMITH;

	protected static final String ACC_100000001 = AccountTestConstants.ACC_100000001;

	private AccountSearchController controller;

	@Before
	public void setUp() throws Exception {
		controller = new AccountSearchController(new StubAccountManagerWithTwoAccounts());
	}

	@Test
	public void testSearchMultipleAccounts() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("a");
		criteria.setMaximumResults(5);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				criteria, "accountSearchCriteria");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.processSubmit(criteria, result, model);
		assertEquals("accounts/list", view);
		List<?> accountList = (List<?>) model.get("accountList");
		assertTrue(accountList.size() > 1);
	}

	@Test
	public void testSearchSingleAccount() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setMaximumResults(5);
		criteria.setSearchString(GLENDA_SMITH);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(
				criteria, "accountSearchCriteria");
		ExtendedModelMap model = new ExtendedModelMap();
		String view = controller.processSubmit(criteria, result, model);
		assertEquals("redirect:accounts/" + ACC_100000001, view);
	}

}
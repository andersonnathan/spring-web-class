package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static rewardsonline.accounts.test.AccountTestConstants.GLENDA_SMITH;
import static rewardsonline.accounts.test.AccountTestConstants.ACC_100000001;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import rewardsonline.accounts.test.StubAccountManagerWithTwoAccounts;

public class AccountSearchControllerTests {

	private AccountSearchController controller;

	@Before
	public void setUp() throws Exception {
		AccountManager accountManager = new StubAccountManagerWithTwoAccounts();
		controller = new AccountSearchController(accountManager);
	}

	@Test
	public void testSearchMultipleAccounts() throws Exception {

		// TODO-08: (Optional Bonus 3) Create search criteria looking for "a" and fetching five
		//            accounts at a time.
		//          Add it to the result (see commented code below).
		//          Invoke the controller's processSubmit method.
		//          Assert that: the returned view is "accounts/list" and
		//            that the model contains an accoubtList of 1

		// BeanPropertyBindingResult result = new BeanPropertyBindingResult(
		// criteria, "accountSearchCriteria");

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchSingleAccount() throws Exception {
		String accountName = GLENDA_SMITH;
		String expectedAccountNumber = ACC_100000001;
		
		// TODO-09: (Optional Bonus 4) This time search explicitly for accountName.  Assert that one
		// account is returned with the right account-number. Check that
		// the view is a redirect to that account.
		
	}

}
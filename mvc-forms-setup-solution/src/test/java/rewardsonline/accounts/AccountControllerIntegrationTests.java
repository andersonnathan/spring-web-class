package rewardsonline.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import rewardsonline.accounts.test.AbstractAccountControllerIntegrationTests;

/**
 * This runs a full MVC integration test that exercises a Controller as if it is
 * running in a JEE servlet-system and being called from the Dispatcher Servlet
 * (see {@link AbstractAccountControllerIntegrationTests} for details.
 * <p>
 * This type of test is covered in the web-test section much later in the
 * course.
 * <p>
 * It is included here as a sanity check for the course developers.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:rewardsonline/accounts/test/mvc-integration-test-config.xml",
		"file:src/main/webapp/WEB-INF/spring/mvc-config.xml" })
@WebAppConfiguration
@ActiveProfiles({"one-account-stub","search"})
public class AccountControllerIntegrationTests extends
		AbstractAccountControllerIntegrationTests {

	// AccountsController
	@Test
	public void postAnAccount() throws Exception {
		String modelAttributeName = "account";
		String testAccountName = KEITH_AND_KERI;
		String testAccountNumber = ACC_123456789;
		Account a = new Account(testAccountNumber, testAccountName);

		// Fake a form submission
		testBuilder.url("/accounts/{acctId}", testAccountNumber)
				.requestMethod(RequestMethod.POST)
				.param("name", testAccountName)
				.modelAttribute(modelAttributeName, a)
				.contentType("text/html").viewName("showData")
				.run();
	}
	
	// AccountSearchController
	@Test
	public void initSearch() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("k");

		testBuilder.url("/accounts/search")
				.param("searchString", "k")
				.modelAttribute("accountSearchCriteria", criteria)
				.contentType("text/html").viewName("accounts/search")
				.run();
	}
	
	// AccountSearchController
	@Test
	public void doSearch() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("k");

		// Still not implemented yet - next lab
		testBuilder.url("/accounts")
				.param("searchString", "k")
				.modelAttribute("accountSearchCriteria", criteria)
				.contentType("text/html").viewName("showData")
				.run();
	}
}

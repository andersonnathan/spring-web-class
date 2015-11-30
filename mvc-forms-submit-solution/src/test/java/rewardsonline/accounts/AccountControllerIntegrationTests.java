package rewardsonline.accounts;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import rewardsonline.accounts.test.AbstractAccountControllerIntegrationTests;

import common.datetime.SimpleDate;

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
@ActiveProfiles({ "one-account-stub", "search" })
public class AccountControllerIntegrationTests extends
		AbstractAccountControllerIntegrationTests {

	// AccountsController
	@Test
	public void editAnAccount() throws Exception {
		String modelAttributeName = "account";
		String testAccountName = KEITH_AND_KERI;
		String testAccountNumber = ACC_123456789;
		Account a = new Account(testAccountNumber, testAccountName);

		// Fake an account edit request
		testBuilder.url("/accounts/{acctId}/edit", testAccountNumber)
				.param("name", testAccountName)
				.modelAttribute(modelAttributeName, a).contentType("text/html")
				.viewName("accounts/edit").run();
	}

	@Test
	public void postAnAccountAndFail() throws Exception {
		String modelAttributeName = "account";
		String testAccountName = KEITH_AND_KERI;
		String testAccountNumber = ACC_123456789;
		Account a = new Account(testAccountNumber, testAccountName);

		// Fake a form submission - should fail because mandatory fields are
		// missing
		testBuilder.url("/accounts/{acctId}", testAccountNumber)
				.requestMethod(RequestMethod.POST)
				.param("name", testAccountName).hasErrors()
				.modelAttribute(modelAttributeName, a).contentType("text/html")
				.viewName("accounts/edit").run();
	}

	@Test
	@DirtiesContext
	public void postAnAccount() throws Exception {
		String modelAttributeName = "account";
		String testAccountName = KEITH_AND_KERI;
		String testAccountNumber = ACC_123456789;
		String testEmail = "keith@somewhere.com";
		String testCC = "1234123412341234";
		String testDOB = "1990-01-20";

		Account a = new Account(testAccountNumber, testAccountName);
		a.setEmail(testEmail);
		a.setCreditCardNumber(testCC);
		a.setDateOfBirth(new SimpleDate(1, 20, 1990).asDate());

		// Fake a form submission - should succeed and redirect
		testBuilder
				.url("/accounts/{acctId}", testAccountNumber)
				.requestMethod(RequestMethod.POST)
				.responseStatus(HttpStatus.FOUND.value())
				// Redirect
				.param("name", testAccountName).param("email", testEmail)
				.param("creditCardNumber", testCC)
				.param("dateOfBirth", testDOB)
				.modelAttribute(modelAttributeName, a).contentType("text/html")
				.viewName("redirect:" + a.getNumber()).run();
	}

	// AccountSearchController
	@Test
	public void initSearch() throws Exception {
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("k");

		testBuilder.url("/accounts/search").param("searchString", "k")
				.modelAttribute("accountSearchCriteria", criteria)
				.contentType("text/html").viewName("accounts/search").run();
	}

	@Test
	public void badSearch() throws Exception {
		// Search form contents
		AccountSearchCriteria criteria = new AccountSearchCriteria();

		testBuilder.url("/accounts").param("searchString", "")
				.modelAttribute("accountSearchCriteria", criteria)
				.contentType("text/html")
				.viewName("accounts/search").run();
	}

	@Test
	public void doSearch() throws Exception {
		// Search form contents
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("k");

		testBuilder.url("/accounts").param("searchString", "k")
				.modelAttribute("accountSearchCriteria", criteria)
				.responseStatus(HttpStatus.FOUND.value())
					// Redirect when only one account found
				.contentType("text/html")
				.viewName("redirect:accounts/123456789").run();
	}

	// AccountSearchController
	@Test
	public void doSearch2() throws Exception {
		// Search form contents
		AccountSearchCriteria criteria = new AccountSearchCriteria();
		criteria.setSearchString("k");
		criteria.setPage(1);

		// Search result
		Account a = new Account(ACC_123456789, KEITH_AND_KERI);
		List<Account> accounts = new ArrayList<Account>(1);
		accounts.add(a);

		testBuilder.url("/accounts").param("searchString", "k")
				.param("page", "1")
				.modelAttribute("accountSearchCriteria", criteria)
				.modelAttribute("accountList", accounts)
				.contentType("text/html").viewName("accounts/list").run();
	}
}

package rewardsonline.accounts;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
@ActiveProfiles("one-account-stub")
public class AccountControllerIntegrationTests extends
		AbstractAccountControllerIntegrationTests {

	@Test
	public void fetchAccountAsHtml() throws Exception {
		String modelAttributeName = "account";
		String testAcountNumber = ACC_123456789;
		Account a = new Account(testAcountNumber, KEITH_AND_KERI);

		testBuilder.url("/accounts/{acctId}", testAcountNumber)
				.modelAttribute(modelAttributeName, a)
				.contentType("text/html").viewName("accounts/show")
				.run();
	}

	@Test
	public void fetchAccountAsJson() throws Exception {
		String modelAttributeName = "account";
		String testAcountNumber = ACC_123456789;
		Account a = new Account(testAcountNumber, KEITH_AND_KERI);

		// Convert to JSON format
		String accountAsJson = toJsonFormat(modelAttributeName, a);

		System.out.println(accountAsJson);

		testBuilder.url("/accounts/{acctId}.json", testAcountNumber)
				.modelAttribute(modelAttributeName, a)
				.viewName("accounts/show")
				.content("application/json", accountAsJson).run();
	}

	@Test
	public void fetchAccountListAsJson() throws Exception {
		String modelAttributeName = "accountList";
		Account a = new Account(ACC_123456789, KEITH_AND_KERI);
		List<Account> accounts = new ArrayList<Account>(1);
		accounts.add(a);

		// Convert to JSON format
		String accountListAsJson = toJsonFormat(modelAttributeName, accounts);

		testBuilder.url("/accounts.json")
				.modelAttribute(modelAttributeName, accounts)
				.viewName("accounts/list")
				.content("application/json", accountListAsJson).run();
	}

	@Test
	public void fetchAccountListAsXls() throws Exception {
		Account a = new Account(ACC_123456789, KEITH_AND_KERI);
		List<Account> accounts = new ArrayList<Account>(1);
		accounts.add(a);

		testBuilder.url("/accounts.xls")
				.modelAttribute("accountList", accounts)
				.viewName("accounts/list")
				.contentType("application/vnd.ms-excel").run();
	}

	public String toJsonFormat(String modelAttributeName, Object obj)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter jsonData = new StringWriter();
		mapper.writeValue(jsonData, obj);
		return "{\"" + modelAttributeName + "\":" + jsonData.toString() + "}";
	}

}

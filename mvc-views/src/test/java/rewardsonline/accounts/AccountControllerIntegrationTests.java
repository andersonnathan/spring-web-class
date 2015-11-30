package rewardsonline.accounts;

import java.util.ArrayList;
import java.util.List;

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
		String modelAttributeName = "accountList";
		String testAcountNumber = ACC_123456789;
		Account a = new Account(testAcountNumber, KEITH_AND_KERI);
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(a);

		testBuilder.url("/accounts.html")
				.modelAttribute(modelAttributeName, accounts)
				.contentType("text/html").viewName("accounts/list")
				.run();
	}

}

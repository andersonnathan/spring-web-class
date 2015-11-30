package rewardsonline.accounts;

// TODO-01: Set up required static imports for using the Web Test framework

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;


// TODO-02: Add appropriate annotations to create a Spring Integration test with a WebApplicationContext
//          Import the configuration files using @ContextConfiguration - we are not running in a container
//          so you will need to refer to them as file resources like this:
//                 file:src/main/webapp/WEB-INF/...
//          Specify the same Spring configuration files that are specified in web.xml.  Ignore the
//          security configuration, it is not needed for testing (yet)
public class AccountControllerIntegrationTests {
	// TODO-03: Autowire in necessary dependencies such as WebApplicationContext and AccountManager
	
	// TODO-04: Declare MockMvc object and initialize before every test is run
	
	// TODO-05: Write a test to perform a get on account details for account '123456001'. Assert some things like
	//         correct status is returned and the right forwarded URL is provided. Add various assertions one at
	//         a time to make sure each one works before adding another. Potential assertions include:
	//         - Verifying status code 200 was returned
	//         - A model object was returned
	//         - The correct forwarded URL was returned - since we are using Tiles it will always be the standard
	//             layout: /WEB-INF/layouts/standard.jsp
	
	// TODO-06: Write a test to simulate POSTing data from the account edit form. Use the formatDate() method to simulate
	//         the correct format of a date string sent from the form - or just hard code the value. Assert correct status (302) 
	//         is returned (expect status().isFound()) and correct redirected URL is provided (to the account).
	
	// TODO-07: Write a test to assert that validation is working correctly. Use a bad email format for example and assert that
	//         the correct errors are identified and the right view or forwarded URL is sent. Some things to assert include:
	//         - Verifying response from controller (is it forward or redirect?)
	//         - Verifying the correct model errors

	// TODO-08: Write a test to exercise a RESTful method on the AccountsController. Invoke the appropriate URI with a 
	//          requested type of JSON and verify that you get a response back as a content type of JSON. Also, use JsonPath
	//          expressions to verify the results contain expected values for such attributes as creditCardNumber
	
	/**
	 * This method ensures that given a date, a string in the format yyyy-MM-dd (ex 2013-04-30) is returned.
	 * 
	 * @param date An initialized Date instance
	 * @return A string in the format yyyy-MM-dd
	 */
	protected String formatDate(Date date) {
		String fmtDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		System.out.println("Formatted date: " + fmtDate);
		return fmtDate;
	}
}

package rewardsonline.accounts;

import org.springframework.test.context.ContextConfiguration;

// TODO-09: Add the appropriate static imports to use the Web Test framework RequestBuilders and ResultMatchers

// TODO-10: Add annotations to define this as a Spring Integration test with a WebApplicationContext
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-config.xml")
public class AccountControllerSecurityTests {
	// TODO-11: Autowire in dependencies including the FilterChainProxy Spring bean 
		
	// TODO-12: Declare and initialize the MockMvc object with the FilterChainProxy instance
	
	// TODO-13: Write a test to attempt to perform a GET on /accounts. Assert that the request is redirected to the 
	//          login page.
}

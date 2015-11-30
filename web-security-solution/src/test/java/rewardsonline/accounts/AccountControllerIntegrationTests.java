package rewardsonline.accounts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static rewardsonline.accounts.SecurityRequestPostProcessors.user;
import static rewardsonline.accounts.SecurityRequestPostProcessors.userDetailsService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-config.xml")
@WebAppConfiguration

// FIXME Tests fail with duplicate /** protection error
@Ignore
public class AccountControllerIntegrationTests {
	@Autowired
	WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	AccountManager accountManager;
	
	MockMvc mockMvc;
	
	@Before
	public void setup() {
		mockMvc = webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
	}
	
	@Test
	public void requiresAuthentication() throws Exception {
		mockMvc.perform(get("/user"))
			.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void accessGranted() throws Exception {
		// Uses the userDetailsService static method to look up a user with username='joe'
		this.mockMvc.perform(get("/").with(userDetailsService("joe")))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/WEB-INF/layouts/standard.jsp"));
	}	
	
	@Test
	public void testAccountDetailsWithSecurity() throws Exception {
		// Creates a user with one custom authority (USER) and bypasses the userDetailsService
		mockMvc.perform(get("/accounts/{acctId}", "123456001").with(user("joe").rolePrefix(null).roles("ROLE_USER")))
		.andExpect(status().isOk())
		.andExpect(model().size(1))
		.andExpect(view().name("accounts/show"))
		.andExpect(forwardedUrl("/WEB-INF/layouts/standard.jsp"));
	}
	
}

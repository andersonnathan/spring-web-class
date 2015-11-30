package rewardsonline.accounts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/*-config.xml")
@WebAppConfiguration
public class AccountControllerSecurityTests {
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
		mockMvc.perform(get("/accounts")).andDo(print())
		    // Will re-direct to this URL based on configuration of
			// <login-form> security config element
			.andExpect(redirectedUrl("http://localhost/login"));
	}
	
}

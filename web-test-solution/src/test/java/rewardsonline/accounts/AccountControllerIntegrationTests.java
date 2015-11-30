package rewardsonline.accounts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
// Ignore the security configuration, not needed for functionality testing
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/app-config.xml",
		"file:src/main/webapp/WEB-INF/spring/mvc-config.xml" })
@WebAppConfiguration
public class AccountControllerIntegrationTests {
	@Autowired
	WebApplicationContext context;

	@Autowired
	AccountManager accountManager;

	MockMvc mockMvc;

	@Before
	public void setup() {
		// Initialize the MockMvc with the Spring WebApplicationContext
		// Note: This is one of many examples of using the static imports
		mockMvc = webAppContextSetup(context).build();
	}

	@Test
	public void testAccountDetails() throws Exception {
		mockMvc.perform(get("/accounts/{acctId}", "123456001"))
				.andExpect(status().isOk()) // Expect status code 200
				.andExpect(model().size(1)) // Expect one model attribute
				.andExpect(view().name("accounts/show")) // Expect this view
				.andExpect(forwardedUrl("/WEB-INF/layouts/standard.jsp"));
					// Resulting in this forward URL
	}

	@Test
	public void testPostAccountSuccess() throws Exception {
		Account a = accountManager.findAccount("123456001");
		a.setEmail("testEmail@someaddress.com"); // Update the email
		mockMvc.perform(
				post("/accounts/{acctId}", "123456001")
						.param("name", a.getName())
						// Providing all the params that would have been sent in
						// the form
						.param("dateOfBirth", formatDate(a.getDateOfBirth()))
						.param("email", a.getEmail())
						.param("receiveNewsletter",
								(a.isReceiveNewsletter() ? "1" : "0"))
						.param("receiveMonthlyEmailUpdate",
								(a.isReceiveMonthlyEmailUpdate() ? "1" : "0")))
				.andExpect(status().isFound()) // Because this is a redirect
				.andExpect(redirectedUrl("123456001")); // The redirect URL
														// provided by the
														// controller
	}

	@Test
	public void testPostAccountFailValidation() throws Exception {
		Account a = accountManager.findAccount("123456001");
		a.setEmail("bogusemail"); // Update the email with bad email address
		mockMvc.perform(
				post("/accounts/{acctId}", "123456001")
						.param("name", a.getName())
						// Providing all the params that would have been sent in
						// the form
						.param("dateOfBirth", formatDate(a.getDateOfBirth()))
						.param("email", a.getEmail())
						.param("receiveNewsletter",
								(a.isReceiveNewsletter() ? "1" : "0"))
						.param("receiveMonthlyEmailUpdate",
								(a.isReceiveMonthlyEmailUpdate() ? "1" : "0")))
				.andExpect(model().attributeHasErrors("account"))
				.andExpect(model().attributeErrorCount("account", 1))
				.andExpect(model().attributeHasFieldErrors("account", "email"))
				.andExpect(status().isOk()) // Because this is a forward to the
											// same view
				.andExpect(view().name("accounts/edit")); 

	}

	@Test
	public void testRestGetRequest() throws Exception {
		mockMvc.perform(
				get("/accounts/{acctId}", "123456001").accept(
						MediaType.APPLICATION_JSON))
				.andDo(print())
				// Assert status code 200 returned
				.andExpect(status().isOk())
				// Assert the correct content type was returned
				.andExpect(content().contentType("application/json"))
				// Assert the correct values for number and creditCardNumber
				// elements
				.andExpect(jsonPath("$.number").value("123456001"))
				.andExpect(
						jsonPath("$.creditCardNumber")
								.value("1234123412340001"));

	}

	/**
	 * This method ensures that given a date, a string in the format yyyy-MM-dd
	 * (ex 2013-04-30) is returned.
	 * 
	 * @param date
	 *            An initialized Date instance
	 * @return A string in the format yyyy-MM-dd
	 */
	private String formatDate(Date date) {
		String fmtDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		System.out.println("Formatted date: " + fmtDate);
		return fmtDate;
	}
}

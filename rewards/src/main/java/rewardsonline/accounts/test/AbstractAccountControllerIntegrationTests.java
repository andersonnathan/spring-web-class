package rewardsonline.accounts.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import rewardsonline.accounts.Account;

/**
 * This is a full MVC integration test that exercises a Controller as if it is
 * running in a servlet-system. This type of test is covered in the web-test
 * section much later in the course.
 * <p>
 * It is used here as a sanity check for the course developers.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class AbstractAccountControllerIntegrationTests implements AccountTestConstants {

	@Autowired
	protected WebApplicationContext context;

	protected MockMvc mockMvc;

	protected AccountControllerTestBuilder testBuilder;

	@Before
	public void setup() {
		// Initialize the MockMvc with the Spring WebApplicationContext
		// Note: This is one of many examples of using the static imports
		mockMvc = webAppContextSetup(context).build();
		testBuilder = new AccountControllerTestBuilder();
	}

	@Test
	public void testAccountList() throws Exception {
		Account a = new Account(ACC_123456789, KEITH_AND_KERI);
		List<Account> accounts = new ArrayList<Account>(1);
		accounts.add(a);

		testBuilder.url("/accounts").modelAttribute("accountList", accounts)
				.contentType("text/html").viewName("accounts/list")
				.forwardTo("/WEB-INF/accounts/list.jsp").run();
	}

	@Test
	public void testAccountDetails() throws Exception {
		final String testAccountNumber = ACC_123456789;

		Account a = new Account(testAccountNumber, KEITH_AND_KERI);

		testBuilder.url("/accounts/{acctId}", testAccountNumber)
				.modelAttribute("account", a).contentType("text/html")
				.viewName("accounts/show")
				.forwardTo("/WEB-INF/accounts/show.jsp").run();
	}

	/**
	 * Builds and runs tests for the AccountControllers used in these labs. By
	 * default requests are assumed to return HTML by running Tiles.
	 */
	protected class AccountControllerTestBuilder implements ResultMatcher {
		private boolean usingTiles = true;

		// The Request
		private String url;
		private RequestMethod requestMethod = RequestMethod.GET;
		private Map<String, String> parameters = new HashMap<String, String>();

		// The response
		private int responseStatus = HttpStatus.OK.value();
		private boolean errorsExpected = false;
		private List<String> errors = new ArrayList<String>();
		private String expectedContentType = "text/html";
		private Map<String, Object> expectedModel = new HashMap<String, Object>();
		private String viewName;
		private String forwardTo;
		private Object expectedContent;
		private String tilesTemplate = "/WEB-INF/layouts/standard.jsp";
		private Object[] urlVariables;

		// THE REQEUST

		/**
		 * Specify the servlet-relative URL to invoke, with any (optional) URI
		 * Template parameters.
		 * 
		 * @param url
		 *            The URL to test.
		 * @param urlVariables
		 *            Any URI Template parameters (specified as {x} in the URL).
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder url(String url,
				Object... urlVariables) {
			this.url = url;
			this.urlVariables = urlVariables;
			return this;
		}

		/**
		 * Set the request method to use - HTTP GET by default.
		 * 
		 * @param status
		 *            Expected status.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder requestMethod(RequestMethod method) {
			requestMethod = method;
			return this;
		}

		/**
		 * Define an attribute that is expected in the model.
		 * 
		 * @param attributeName
		 *            The model-attribute name.
		 * @param expectedValue
		 *            The model-attribur value.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder param(String paramName,
				String paramValue) {
			parameters.put(paramName, paramValue);
			return this;
		}

		// THE RESPONSE

		/**
		 * Set the expected response status. HTTP OK or 200 by default.
		 * 
		 * @param status
		 *            Expected status.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder responseStatus(int status) {
			responseStatus = status;
			return this;
		}

		/**
		 * Indicates that a form submission is expected to fail with errors.
		 * 
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder hasErrors() {
			errorsExpected = true;
			return this;
		}

		/**
		 * Indicates that a form submission is expected to fail with errors.
		 * 
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder error(String field) {
			errorsExpected = true;
			errors.add(field);
			return this;
		}

		/**
		 * Most labs uses Tiles internally. However some do not. Tiles is
		 * assumed by default.
		 * 
		 * @param usingTiles
		 *            Is the lab using Tiles internally?
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder usingTiles(boolean usingTiles) {
			this.usingTiles = usingTiles;
			return this;
		}

		/**
		 * Define an attribute that is expected in the model.
		 * 
		 * @param attributeName
		 *            The model-attribute name.
		 * @param expectedValue
		 *            The model-attribur value.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder modelAttribute(
				String attributeName, Object expectedValue) {
			expectedModel.put(attributeName, expectedValue);
			return this;
		}

		/**
		 * Expected content type of HTTP response. "text/html" is assumed by
		 * default.
		 * 
		 * @param expectedContentType
		 *            The expected mime-type of the HTTP response.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder contentType(
				String expectedContentType) {
			this.expectedContentType = expectedContentType;
			return this;
		}

		/**
		 * Specify both the expected content-type of the HTTP response and the
		 * expected contents of the HTTP response body.
		 * 
		 * @param expectedContentType
		 *            The expected mime-type of the HTTP response.
		 * @param expectedContent
		 *            The expected content (as a string).
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder content(String expectedContentType,
				Object expectedContent) {
			this.expectedContentType = expectedContentType;
			this.expectedContent = expectedContent;
			return this;
		}

		/**
		 * The logical view name returned from the controller method (makes no
		 * sense if @ResponseBody is being used)
		 * 
		 * @param viewName
		 *            Logical view name.
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder viewName(String viewName) {
			this.viewName = viewName;
			return this;
		}

		/**
		 * What page to forward to? Only relevant when JSP is being used
		 * directly (no Tiles).
		 * 
		 * @param forwardTo
		 *            The location of a JSP w.r.t. document-root (typically a
		 *            path like "/WEB-INF/views/xxx.jsp".
		 * @return This object (fluent builder pattern).
		 */
		public AccountControllerTestBuilder forwardTo(String forwardTo) {
			this.forwardTo = forwardTo;
			return this;
		}

		/**
		 * Run the actual test. Uses a Mock MVC setup to invoke the URL through
		 * the Dispatcher Servlet. Checks all specified properties of the
		 * response.
		 * 
		 * @throws Exception
		 */
		public void run() throws Exception {
			assertNotNull("A (servlet-releative) URL to test is mandatory", url);

			// Emulate an interaction
			ResultActions results = null;

			try {
				results = mockMvc.perform(request());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			}

			// Model contains the right number of attributes
			results.andDo(print()).andExpect(status().is(responseStatus));

			results.andDo(printModel());

			if (responseStatus != HttpStatus.OK.value()
					&& responseStatus != HttpStatus.FOUND.value())
				return; // Nothing else to check

			if (errorsExpected) {
				results.andExpect(model().hasErrors());

				if (errors.size() > 0)
					results.andExpect(model().errorCount(errors.size()));

				for (String field : errors) {
					results.andExpect(model().attributeHasErrors(field));
				}
			}

			// Size ignores Error (form-binding) objects
			results.andExpect(model().size(expectedModel.size()));

			// Check model contents
			for (Map.Entry<String, Object> attribute : expectedModel.entrySet()) {
				// Check the attribute exists and its value is correct.
				// Will work ONLY if equals is defined properly on objects in
				// the model
				String attributeName = attribute.getKey();
				results.andExpect(model().attributeExists(attributeName));
				results.andExpect(model().attribute(attributeName,
						attribute.getValue()));
			}

			// org.springframework.validation.BindingResult.account=org.springframework.validation.BeanPropertyBindingResult

			// Check view name
			if (viewName != null)
				results.andExpect(view().name(viewName));

			// Check the response
			results.andExpect(this);
		}

		/**
		 * Setup the HTTP Request by configuring the builder that will generate
		 * it.
		 * 
		 * @return One of Spring's Mock HTTP Request builders.
		 */
		protected MockHttpServletRequestBuilder request() {
			MockHttpServletRequestBuilder builder = null;

			if (requestMethod == RequestMethod.GET) {
				builder = MockMvcRequestBuilders.get(url, urlVariables);
			} else if (requestMethod == RequestMethod.POST) {
				builder = MockMvcRequestBuilders.post(url, urlVariables);
			} else if (requestMethod == RequestMethod.PUT) {
				builder = MockMvcRequestBuilders.put(url, urlVariables);
			} else if (requestMethod == RequestMethod.DELETE) {
				builder = MockMvcRequestBuilders.delete(url, urlVariables);
			} else {
				fail("Unsupported HTTP method " + requestMethod);
			}

			// Copy any parameters
			for (Map.Entry<String, String> param : parameters.entrySet()) {
				builder.param(param.getKey(), param.getValue());
			}

			return builder;
		}

		protected ResultHandler printModel() {
			return new ResultHandler() {

				@Override
				public void handle(MvcResult result) throws Exception {
					ModelAndView mav = result.getModelAndView();
					if (mav == null)
						return;

					System.out.println();
					System.out.println("               Model = "
							+ mav.getModel());

					// Find the errors object(s) - if there are any
					for (Object value : mav.getModel().values()) {
						if (value instanceof Errors) {
							Errors errors = (Errors) value;
							System.out.println("  Total errors found = "
									+ errors.getErrorCount());
							System.out.println("          All errors = "
									+ errors.getAllErrors());
						}
					}

					System.out.println();
				}
			};
		}

		/**
		 * This class is also a {@link ResultMatcher} so it can check its own
		 * responses. This matcher is used to check the content of the HTTP
		 * response generated by the invocation (when {@link #run()} is called).
		 */
		@Override
		public void match(MvcResult result) throws Exception {
			System.out.println(result);
			ModelAndView mav = result.getModelAndView();
			assertNotNull("Model expected", mav);

			System.out.println("Model = " + mav.getModel());
			MockHttpServletResponse response = result.getResponse();
			String contentType = response.getContentType();

			// HTML is generated by JSP forwarding (either directly or via
			// Tiles) so there is no content. Handle as a special case.
			if (contentType == null) {
				contentType = "text/html";
				System.out.println("Using tiles = " + usingTiles);
				if (responseStatus == HttpStatus.FOUND.value())
					assertNull("JSP forwarding", response.getForwardedUrl()); // ignore
																				// redirect
				else if (usingTiles)
					assertEquals("JSP forwarding", response.getForwardedUrl(),
							tilesTemplate);
				else {
					assertNotNull("No JSP specified to check", forwardTo);
					assertEquals("JSP forwarding", response.getForwardedUrl(),
							forwardTo);
				}

			} else if (expectedContent != null) {
				// Non-HTML content such as JSON, XLS ...
				assertTrue("Content",
						response.getContentAsString().equals(expectedContent));
			}

			// Check the content-type is correct
			assertEquals(expectedContentType, contentType);
		}
	}

}

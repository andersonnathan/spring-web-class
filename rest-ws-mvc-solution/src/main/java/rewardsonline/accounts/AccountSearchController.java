package rewardsonline.accounts;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.validation.Valid;

import java.util.logging.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controls the execution of the user operation to list and search for accounts.
 */
@Controller
@RequestMapping(value = "/accounts")
public class AccountSearchController {

	private AccountManager accountManager;

	@Autowired
	public AccountSearchController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Handles requests to list all accounts. In previous labs this method was
	 * on the {@link AccountsController}.
	 * <p>
	 * <b>Note:</b> In a real system a method like this is not realistic as it
	 * is unbounded (there could by hundreds of thousands of accounts). Fetching
	 * accounts in batches is far more realistic - which is what the
	 * {@link #search(AccountSearchCriteria)} method does.
	 * 
	 * @param model
	 *            The data to pass to the resulting view - holds a collection of
	 *            all accounts.
	 * @return The name of the view that will be used to show the list of
	 *         results.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("accountList", accountManager.findAllAccounts());
		return "accounts/list";
	}

	/**
	 * Displays a search form to the user.
	 * 
	 * @param criteria
	 *            The search criteria to pre-populate the form with
	 * @return The name of the view that will be used to show the search form.
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(AccountSearchCriteria criteria) {
		return "accounts/search";
	}

	/**
	 * Executes an account search. Selects a search results view if multiple
	 * accounts are returned. Redirects to a URL to show account details if
	 * exactly one account is returned.
	 * 
	 * @param criteria
	 *            The search criteria
	 * @param model
	 *            The model exported to the selected view - holds a collection
	 *            of matching accounts (which may be empty).
	 * @return The name of the view that will be used to show the search
	 *         results.
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "searchString" })
	public String processSubmit(@Valid AccountSearchCriteria criteria,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "accounts/search";
		}

		List<Account> accounts = accountManager.findAccounts(criteria);

		// If there is one result and we are on the first page (so it is the
		// one and only result) display that account directly, otherwise
		// output a list of results for the current page.
		if (accounts.size() == 1 && criteria.getPage() == 0) {
			return "redirect:accounts/" + accounts.get(0).getNumber();
		} else {
			model.addAttribute("accountList", accounts);
			return "accounts/list";
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// REST Methods
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Handles RESTful requests for accounts.
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Account> listData() {
		return accountManager.findAllAccounts();
	}

	/**
	 * Creates a new Account, setting its URL as the Location header on the response.
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HttpEntity<?> createAccount(@RequestBody Account newAccount, 
							  @Value("#{request.requestURL}") StringBuffer url) {
		Account account = accountManager.save(newAccount);
		HttpHeaders headers = new HttpHeaders();
		String location = AccountsController.getLocationForChildResource(url, account.getNumber());
		headers.add("Location", location);
		return new HttpEntity<String>(headers);
	}

	/**
	 * Maps either a DataIntegrityViolationException (raised by Spring) or a
	 * ConstraintViolationException (from Hibernate) to a 409 Conflict HTTP
	 * status code.
	 */
	@ResponseStatus(HttpStatus.CONFLICT)  // 409
	@ExceptionHandler({ DataIntegrityViolationException.class,
			ConstraintViolationException.class })
	public void handleAlreadyExists() {
		// just return empty 409
		Logger.getLogger(getClass().getName()).info(
				"Duplicate account id: handleAlreadyExists() invoked.");
	}

}

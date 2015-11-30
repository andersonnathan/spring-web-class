package rewardsonline.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	 * accounts in batches is far more realistic - which is what the search
	 * method will do (optional part of lab).
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
	 * Displays a search form to the suer.
	 * 
	 * @param criteria
	 *            the search criteria to pre-populate the form with
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(AccountSearchCriteria criteria) {
		// TODO-07: Optional Bonus: Setup the search form. Replace this line
		//          to display the search page instead
		// TODO-08: Optional Bonus: Convert the search.jsp to use Spring
		//          form-tags - see the TO DO in that file.
		return "notsupported";
	}

	/**
	 * Show the result of submitting the form. A do-nothing controller method
	 * that will be replaced in the next lab.
	 * 
	 * @param criteria
	 *            The search criteria entered by the user in the search form.
	 * 
	 * @return a String indicating a view name
	 */
	@RequestMapping(method = RequestMethod.GET, params = { "searchString" })
	public String processSubmit(AccountSearchCriteria criteria) {
		return "showData";
	}

}

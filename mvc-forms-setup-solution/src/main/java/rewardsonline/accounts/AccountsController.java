package rewardsonline.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A controller handling requests for showing and updating an Account.
 */
@Controller
@RequestMapping("/accounts")
public class AccountsController {

	private AccountManager accountManager;

	@Autowired
	public AccountsController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Handles requests to shows detail about one account.
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public String show(@PathVariable String number, Model model) {
		model.addAttribute("account", accountManager.findAccount(number));
		return "accounts/show";
	}

	/**
	 * A request handling method for showing an account's details in edit mode.
	 * This method does not need to find the account because the findAccount
	 * method has already done that.
	 */
	@RequestMapping(value = "/{number}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable String number, Model model) {
		model.addAttribute("account", accountManager.findAccount(number));
		return "accounts/edit";
	}

	/**
	 * Show the result of submitting the form. A do-nothing controller method
	 * that will be replaced in the next lab.
	 * 
	 * @param number
	 *            the account number
	 * @param account
	 *            the account retrieved from the DB and updated with the user
	 *            values
	 * @return a String indicating a view name
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.POST)
	public String save(@PathVariable String number, Account account) {
		// We need to process the submitted data here - next lab!
		return "showData";
	}

}
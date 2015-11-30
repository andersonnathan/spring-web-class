package rewardsonline.accounts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Controls the execution of user operations on individual Account entities.
 */
@Controller
@RequestMapping("/accounts/{number}")
public class AccountsController {

	private AccountManager accountManager;
	
	@Autowired
	public AccountsController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Finds the account to manipulate and exports it in the model.
	 * <p>
	 * Note: because this method is annotated with @ModelAttribute, Spring MVC
	 * will invoke it before every other request handling method. After
	 * invocation, the method return value will be placed in the model with name
	 * <code>account</code>.
	 * 
	 * @param number
	 *            the account number
	 * @return the account to export in the model
	 */
	@ModelAttribute
	protected Account findAccount(@PathVariable String number) {
		return accountManager.findAccount(number);
	}

	/**
	 * A request handling method for showing an account's details. This method
	 * does not need to find the account because the findAccount method has
	 * already done that.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show() {
		return "accounts/show";
	}

	/**
	 * A request handling method for showing an account's details in edit mode.
	 * This method does not need to find the account because the findAccount
	 * method has already done that.
	 */
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public String edit() {
		return "accounts/edit";
	}

	/**
	 * A request handling method for applying changes to an Account. Note that
	 * Spring MVC will invoke the findAccount method first to get the account,
	 * apply data binding and then call this method to persist the changes.
	 * 
	 * @param request
	 *            the current request
	 * @param account
	 *            the account retrieved from the DB and updated with the user
	 *            values
	 * @param result
	 *            a data structure containing results from the data binding
	 * @param model
	 *            the model to add attributes to
	 * @return a String indicating a view name
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public String save(@Valid Account account, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "accounts/edit";
		}
		accountManager.update(account);
		return "redirect:" + account.getNumber();
	}
	
}
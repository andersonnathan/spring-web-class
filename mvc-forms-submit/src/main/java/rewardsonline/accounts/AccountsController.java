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

// TODO-01: Deploy this application to the server.
//          Access the home page at http://localhost:8080/mvc-forms-submit.
//          Remove any previously deployed applications from your server
//            to allow for faster startup.
//
//          Click on Accounts link, pick the first account in the list and
//            show its details. Click the edit link to see an input form.
//          Click submit and you will get a 405. We will implement the submit
//            feature in this lab.

@Controller
@RequestMapping("/accounts")
public class AccountsController {

	private AccountManager accountManager;

	@Autowired
	public AccountsController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	// TODO-03: Add a method to pre-retrieve the Account being modified
	// and add it to the implicit model. Remove the (now redundant) lines in
	// show() and edit() that add the account to the model.
	// Save all work, restart the server and attempt the form submit.
	// It should be successful.
	//
	// OPTIONAL: You might like to change the class-level request mapping
	//   to be @RequestMapping("/accounts/{number}") and refactor all the
	//   @RequestMapping annotations to remove "/{number}".

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

	// TODO-02: Provide a method to process the edit-form submission.
	// It should respond to an HTTP POST to the path "/{number}"
	// and return a redirect to /accounts/{number}".
	//
	// Declare a BindingResult parameter right after the Account parameter
	//
	// If there are any binding/validation errors () return the user to
	// "accounts/edit". If no errors, use the account manager to update
	// the modified account, using accountManager.update(account), and
	// redirect to show the updated details for the account using a
	// redirect like this:
	//       return "redirect:/accounts/" + account.getNumber();
	//
	// Save all work, restart the server, and attempt to submit.
	// You should encounter an exception. See if you can understand
	// the problem then go on to the next step to fix it.
	
}
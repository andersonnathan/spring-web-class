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
	@RequestMapping(value="/{number}", method = RequestMethod.GET)
	public String show(@PathVariable String number, Model model) {
		model.addAttribute("account", accountManager.findAccount(number));
		return "accounts/show";
	}

	// TODO-01: Deploy this application to the server and check it runs OK.
    //          Access the home page at http://localhost:8080/mvc-forms-setup.
	//          Remove any previously deployed applications from your server
	//            to allow for faster startup.
	//
	//          Click on the edit link - you should get a 404 error.
	//
	//          Write a method to get the details of an account for editing
	//          Will respond to the path "/{number}/edit" and use the
	//            view "accounts/edit".
	//          Find and save the Account in the Model and remember it's
	//            attribute name - it will be used as the form-object.
	//          Call the method edit().
	
	// TODO-02: Check edit.jsp - follow the TO DO step inside
	
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
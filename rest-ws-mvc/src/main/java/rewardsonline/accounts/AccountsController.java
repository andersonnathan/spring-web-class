package rewardsonline.accounts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

/**
 * Controls the execution of user operations on individual Account entities.
 */
@Controller
@RequestMapping("/accounts/{accountNumber}")
public class AccountsController {

	private AccountManager accountManager;

	@Autowired
	public AccountsController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	// HTML Methods
	
	/**
	 * Finds the account to manipulate and exports it in the model.
	 * <p>
	 * Note: because this method is annotated with @ModelAttribute, Spring MVC
	 * will invoke it before every other request handling method. After
	 * invocation, the method return value will be placed in the model with name
	 * <code>account</code>.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the account to export in the model
	 */
	@ModelAttribute
	protected Account findAccount(@PathVariable("accountNumber") String accountNumber) {
		return accountManager.findAccount(accountNumber);
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
	

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// REST Methods
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	// TODO-02: Modify to return the details of an account in JSON format. Note
	// that currently the show() method also returns details for an account and
	// responds to GET. Make sure that that they do _not_ have the same
	// @RequestMapping - this one is called to produce JSON data.

	/**
	 * Provide the details of an account with the given number.
	 */
	public Account accountDetails(String accountNumber) {
		Account account = retrieveAccount(accountNumber);
		return account;
	}

	// TODO-04: Modify addBeneficiary() for POSTs to "/accounts/{accountNumber}/beneficiaries"
	// which adds a beneficiary. You will need to use @RequestBody, but the body is just a String
	// (the beneficiary name).  You should not need to use HttpServletRequest or HttpServletResponse
	// as parameters to this method.

	/**
	 * Adds a named Beneficiary to the Account with the given number,
	 * setting its URL as the Location header on the response. 
	 */
	public HttpEntity<String> addBeneficiary( /* Add extra parameters here */ ) {
		return null;   // replace this line
	}
	
	/**
	 * Returns the named Beneficiary for the Account with the given number.   
	 */
	@RequestMapping(value="beneficiaries/{beneficiaryName}", method=RequestMethod.GET)
	public @ResponseBody Beneficiary getBeneficiary(@PathVariable("accountNumber") String accountNumber, 
			@PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountNumber).getBeneficiary(beneficiaryName);
	}
	
	// TODO-05: Implement for DELETEs of "/accounts/{accountNumber}/beneficiaries/{beneficiaryName}"
	// which will remove a beneficiary.
	
	/**
	 * Removes the Beneficiary with the given name from the Account with the given name. 
	 */
	public void removeBeneficiary() {
	}
	
	// TODO-06 Generate a HTTP 404 when an account cannot be found (in which case
	// the retrieveAccount() method throws an IllegalArgumentException
	/**
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	public void handleNotFound() {
		// just return empty 404
	}

	/**
	 * Finds the Account with the given number, throwing an IllegalArgumentException
	 * if there is no such Account. 
	 */
	private Account retrieveAccount(String accountNumber) throws IllegalArgumentException {
		Account account = accountManager.findAccount(accountNumber);
		if (account == null) {
			throw new IllegalArgumentException("No such account with number " + accountNumber);
		}
		return account;
	}

	/**
	 * Determines URL of child resource based on the full URL of the given request,
	 * appending the path info with the given childIdentifier using a UriTemplate.
	 */ 
	protected static String getLocationForChildResource(StringBuffer url, Object childIdentifier) {
		UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
		return template.expand(childIdentifier).toASCIIString();
	}
	
}
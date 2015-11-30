package rewardsonline.accounts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	/**
	 * Provide the details of an account with the given id.
	 */
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Account accountDetails(@PathVariable("accountNumber") String accountNumber) {
		Account account = retrieveAccount(accountNumber);
		return account;
	}
	
	/**
	 * Adds a Beneficiary with the given name to the Account with the given id,
	 * setting its URL as the Location header on the response. 
	 */
	@RequestMapping(value="beneficiaries", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public HttpEntity<?> addBeneficiary(@PathVariable("accountNumber") String accountNumber, 
							   @RequestBody String beneficiaryName, 
							   @Value("#{request.requestURL}") StringBuffer url) {
		accountManager.addBeneficiary(accountNumber, beneficiaryName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", getLocationForChildResource(url, beneficiaryName));
		return new HttpEntity<String>(headers);
	}

	/**
	 * Returns the Beneficiary with the given name for the Account with the given id.   
	 */
	@RequestMapping(value="beneficiaries/{beneficiaryName}", method=RequestMethod.GET)
	public @ResponseBody Beneficiary getBeneficiary(@PathVariable("accountNumber") String accountNumber, 
			@PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountNumber).getBeneficiary(beneficiaryName);
	}

	/**
	 * Removes the Beneficiary with the given name from the Account with the given id. 
	 */
	@RequestMapping(value="beneficiaries/{beneficiaryName}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeBeneficiary(@PathVariable("accountNumber") String accountNumber, 
			                      @PathVariable("beneficiaryName") String beneficiaryName) {
		accountManager.removeBeneficiary(accountNumber, beneficiaryName);
	}
	
	/**
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)  // 404
	@ExceptionHandler({IllegalArgumentException.class})
	public void handleNotFound() {
		// just return empty 404
	}

	/**
	 * Finds the Account with the given id, throwing an IllegalArgumentException
	 * if there is no such Account. 
	 */
	private Account retrieveAccount(String accountNumber) throws IllegalArgumentException {
		Account account = accountManager.findAccount(accountNumber);
		if (account == null) {
			throw new IllegalArgumentException("No such account with id " + accountNumber);
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
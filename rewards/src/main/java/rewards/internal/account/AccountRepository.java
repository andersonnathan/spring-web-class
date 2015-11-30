package rewards.internal.account;

import java.util.List;

import rewards.InvalidCreditCardException;

/**
 * Loads account aggregates. Used to find and reconstitute Account entities from
 * an external form such as a set of RDMS rows.
 * 
 * Objects returned by this repository are guaranteed to be fully-initialized
 * and ready to use.
 */
public interface AccountRepository {

	/**
	 * Return a list of all accounts.
	 * 
	 * @return the account listing
	 */
	List<Account> findAllAccounts();

	/**
	 * Return a list of all accounts with the given name (this will be treated
	 * as a wildcard) between the specified limits.
	 * 
	 * @param name
	 *            The account name to search for. Can be a partial string. A
	 *            LIKE query will be used.
	 * @param first
	 *            First result to return - allows just some of the result-set to
	 *            be returned
	 * @param nResults
	 *            The number of results to return.
	 * @return the account list
	 */
	List<Account> findAccounts(String name, int first, int nResults);

	/**
	 * Load an account by its credit card.
	 * 
	 * @param creditCardNumber
	 *            the credit card number
	 * @return the account object
	 */
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException;

	/**
	 * Load an account by its account number.
	 * 
	 * @param accountNumber
	 *            the account number
	 * @return the account object
	 */
	public Account findByAccountNumber(String accountNumber);

	/**
	 * Update account details. Can also be used to create a new account.
	 * 
	 * @param account
	 *            The account to be created or updated.
	 */
	public void update(Account account);

	/**
	 * Updates the beneficiary information for this account. First scenario is
	 * when a beneficiary is added or removed from the account. Second scenario
	 * is to update the 'savings' of each account beneficiary to include the
	 * amounts distributed for a contribution made during a reward transaction.
	 * The name or allocation percentage of a beneficiary cannot be changed
	 * directly.
	 * 
	 * @param account
	 *            the account whose beneficiary savings have changed
	 */
	public void updateBeneficiaries(Account account);

}
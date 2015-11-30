package rewardsonline.accounts;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import rewards.InvalidCreditCardException;

/**
 * Manages access to member account information.
 */
@Transactional
public interface AccountManager {

	/**
	 * Return a listing of all accounts.
	 * 
	 * @return the account listing
	 */
	@Transactional(readOnly = true)
	List<Account> findAllAccounts();

	/**
	 * Return the list of accounts that match the criteria provided.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the list of accounts that match
	 */
	@Transactional(readOnly = true)
	List<Account> findAccounts(AccountSearchCriteria searchCriteria);

	/**
	 * Find the account with the provided account number.
	 * 
	 * @param number
	 *            the account number
	 * @return the account or <code>null</code> if no account exists with that
	 *         number
	 * @throws EmptyResultDataAccessException
	 *             the account number does not exist.
	 */
	@Transactional(readOnly = true)
	Account findAccount(String number);

	/**
	 * Load an account by its credit card.
	 * 
	 * @param creditCardNumber
	 *            the credit card number
	 * @return the account object
	 * @throws InvalidCreditCardException
	 *             the credit-card number does not exist.
	 */
	@Transactional(readOnly = true)
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException;

	/**
	 * Save a new account. The account's number should not exist already. To
	 * modify an existing account use {@link #update(Account)}.
	 * 
	 * @param account
	 *            the new account
	 * @return the account after it has been saved - this need not be the same
	 *         object, but it will now have an entity id.
	 * @throws Exception
	 *             if an account already exists with the given account number.
	 */
	public Account save(Account account);

	/**
	 * Mark an account entity as changed and needing an update on flush. The
	 * account must exist - to create a new account use {@link #save(Account)}.
	 * 
	 * @param account
	 *            the account object with changes
	 * @throws EmptyResultDataAccessException
	 *             the account number does not exist.
	 */
	void update(Account account);

	/**
	 * Add a new beneficiary to the specified account. If it is the first
	 * beneficiary it will be given an allocation-percentage of 100%. Otherwise
	 * it will be set to zero.
	 * 
	 * @param accountNumber
	 *            an existing account number
	 * @param beneficiaryName
	 *            the name of the new beneficiary
	 * @throws EmptyResultDataAccessException
	 *             the account number does not exist.
	 */
	public void addBeneficiary(String accountNumber, String beneficiaryName);

	/**
	 * Remove an existing beneficiary from the specified account. The
	 * allocation-percentages are not rebalanced (but should be in a real
	 * application).
	 * 
	 * @param accountNumber
	 *            an existing account number
	 * @param beneficiaryName
	 *            the name of the new beneficiary
	 * @throws EmptyResultDataAccessException
	 *             the account number does not exist.
	 */
	public void removeBeneficiary(String accountNumber, String beneficiaryName);

}
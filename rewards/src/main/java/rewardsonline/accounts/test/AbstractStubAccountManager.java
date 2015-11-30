package rewardsonline.accounts.test;

import java.util.List;
import java.util.Map;

import rewards.InvalidCreditCardException;
import rewardsonline.accounts.Account;
import rewardsonline.accounts.AccountManager;
import rewardsonline.accounts.AccountSearchCriteria;

import common.money.Percentage;

/**
 * A dummy implementation of an account manager that does nothing. All methods
 * throw an IllegalStateException if called. Intended as a base class for
 * testing - overload just the methods you need in the test.
 */
public class AbstractStubAccountManager implements AccountManager {

	@Override
	public List<Account> findAllAccounts() {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public List<Account> findAccounts(AccountSearchCriteria searchCriteria) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public Account findAccount(String number) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public void update(Account account) {
		throw new IllegalStateException("Should not be called for test");
	}

	public void updateBeneficiaryAllocationPercentages(String accountNumber,
			Map<String, Percentage> allocationPercentages) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public void addBeneficiary(String accountNumber, String beneficiaryName) {
		throw new IllegalStateException("Should not be called for test");
	}

	public void removeBeneficiary(String accountNumber, String beneficiaryName,
			Map<String, Percentage> allocationPercentages) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public void removeBeneficiary(String accountNumber, String beneficiaryName) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public Account save(Account newAccount) {
		throw new IllegalStateException("Should not be called for test");
	}

	@Override
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException {
		throw new IllegalStateException("Should not be called for test");
	}

}

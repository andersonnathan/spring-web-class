package rewardsonline.accounts.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rewards.InvalidCreditCardException;
import rewardsonline.accounts.Account;
import rewardsonline.accounts.AccountSearchCriteria;
import rewardsonline.accounts.Beneficiary;

import common.money.Percentage;

/**
 * Static stub used to support unit testing objects that depend on the
 * AccountManager interface. Defines two accounts: "100000001" for user
 * "Glenda Smith" and "100000002"for user "Julia Weber".
 */
public class StubAccountManagerWithTwoAccounts extends
		AbstractStubAccountManager implements AccountTestConstants {

	private Map<String, Account> accountsByNumber = new HashMap<String, Account>();

	public StubAccountManagerWithTwoAccounts() {
		Set<Beneficiary> beneficiaries = new LinkedHashSet<Beneficiary>();
		beneficiaries.add(new Beneficiary("Leslie", Percentage.valueOf("50%")));
		beneficiaries.add(new Beneficiary("Weslie", Percentage.valueOf("50%")));

		Account account = new Account(ACC_100000001, GLENDA_SMITH);
		account.setBeneficiaries(beneficiaries);
		account.setCreditCardNumber(ACCOUNT_1_CC_NUMBER);
		accountsByNumber.put(ACC_100000001, account);

		account = new Account(ACC_100000002, JULIA_WEBER);
		account.setCreditCardNumber(ACCOUNT_2_CC_NUMBER);
		accountsByNumber.put(ACC_100000002, account);
	}

	@Override
	public List<Account> findAllAccounts() {
		return new ArrayList<Account>(accountsByNumber.values());
	}

	@Override
	public List<Account> findAccounts(AccountSearchCriteria criteria) {
		int count = 0;
		List<Account> accounts = new ArrayList<Account>();
		Iterator<Account> it = accountsByNumber.values().iterator();
		while (it.hasNext()) {
			Account account = it.next();
			if (account.getName().toLowerCase()
					.contains(criteria.getSearchString().toLowerCase())) {
				accounts.add(account);
				count++;
			}
			if (count == criteria.getMaximumResults()) {
				break;
			}
		}
		return accounts;
	}

	@Override
	public Account findAccount(String number) {
		return accountsByNumber.get(number);
	}

	@Override
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException {
		if (ACCOUNT_1_CC_NUMBER.equals(creditCardNumber))
			return accountsByNumber.get(ACC_100000001);
		else if (ACCOUNT_2_CC_NUMBER.equals(creditCardNumber))
			return accountsByNumber.get(ACC_100000002);
		else
			throw new InvalidCreditCardException(creditCardNumber);
	}

	@Override
	public void update(Account account) {
		accountsByNumber.put(account.getNumber(), account);
	}

}

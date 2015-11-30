package rewardsonline.accounts.test;

import java.util.ArrayList;
import java.util.List;

import rewardsonline.accounts.Account;
import rewardsonline.accounts.AccountSearchCriteria;

/**
 * Static stub used to support unit testing objects that depend on the
 * AccountManager interface. Defines one account ACC_123456789 for user
 * KEITH_AND_KERI.
 */
public class StubAccountManagerWithOneAccount extends
		AbstractStubAccountManager implements AccountTestConstants {

	protected Account keith = new Account(ACC_123456789, KEITH_AND_KERI);

	@Override
	public Account findAccount(String number) {
		return ACC_123456789.equals(number) ? keith : null;
	}

	@Override
	public List<Account> findAllAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(keith);
		return accounts;
	}

	@Override
	public List<Account> findAccounts(AccountSearchCriteria searchCriteria) {
		List<Account> accounts = new ArrayList<Account>();

		if (KEITH_AND_KERI.toUpperCase().indexOf(
				searchCriteria.getSearchString().toUpperCase()) != -1)
			accounts.add(keith);

		return accounts;
	}

	@Override
	public void update(Account account) {
		if (ACC_123456789.equals(account.getNumber()))
			keith = account;
	}

}

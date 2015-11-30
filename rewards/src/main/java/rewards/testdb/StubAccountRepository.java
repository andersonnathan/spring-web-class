package rewards.testdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import rewards.internal.account.Account;
import rewards.internal.account.AccountRepository;
import rewardsonline.accounts.test.AccountTestConstants;

import common.money.Percentage;

/**
 * A dummy account repository implementation. Has a single Account
 * "Keith and Keri Donald" with two beneficiaries "Annabelle" (50% allocation)
 * and "Corgan" (50% allocation) associated with credit card "1234123412341234".
 * 
 * Stubs facilitate unit testing. An object needing an AccountRepository can
 * work with this stub and not have to bring in expensive and/or complex
 * dependencies such as a Database. Simple unit tests can then verify object
 * behavior by considering the state of this stub.
 */
public class StubAccountRepository implements AccountRepository, AccountTestConstants {

	private Account account;

	public StubAccountRepository() {
		Account account = new Account(ACC_123456789, "Keith and Keri Donald");
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));
		this.account = account;
	}

	@Override
	public List<Account> findAllAccounts() {
		List<Account> accounts = new ArrayList<Account>(1);
		accounts.add(account);
		return accounts;
	}

	@Override
	public List<Account> findAccounts(String name, int first, int nResults) {
		List<Account> accounts = new ArrayList<Account>(1);
	
		if (account.getName().toUpperCase().contains(name.toUpperCase()))
			accounts.add(account);

		return accounts;
	}

	@Override
	public Account findByCreditCard(String creditCardNumber) {
		if ("1234123412341234".equals(creditCardNumber)) {
			return this.account;
		}
		throw new EmptyResultDataAccessException(1);
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		if (ACC_123456789.equals(accountNumber)) {
			return this.account;
		}
		throw new EmptyResultDataAccessException(1);
	}

	@Override
	public void update(Account account) {
		// nothing to do, everything is in memory
	}

	@Override
	public void updateBeneficiaries(Account account) {
		// nothing to do, everything is in memory
	}
}
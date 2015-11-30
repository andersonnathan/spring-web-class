package rewardsonline.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import rewardsonline.accounts.test.AccountTestConstants;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Unit test for the account manager implementation. Tests application data
 * access behavior to verify the Account and Beneficiary DTOs are being created
 * correctly from the underlying entities correctly.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:rewardsonline/accounts/account-manager-test-config.xml")
@Transactional
public class AccountManagerTests implements AccountTestConstants {

	public static final int ACCOUNTS_EXPECTED = 3;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private DataSource dataSource;

	// FIXME - Test fails under maven (unable to rollback Hibernate transaction)
	// but works fine in STS.
	@Test
	@Ignore
	public void testFindAllAccounts() {
		List<Account> accounts = accountManager.findAllAccounts();
		assertNotNull("accounts should never be null", accounts);
		assertEquals(ACCOUNTS_EXPECTED, accounts.size());
		assertEquals("Keith and Keri Donald", accounts.get(0).getName());
		assertEquals("Riley Hollyhand", accounts.get(1).getName());
		assertEquals("Zach Braff", accounts.get(2).getName());

		assertEquals(ACCOUNTS_EXPECTED,
				(int) new JdbcTemplate(dataSource).queryForObject(
						"SELECT count(*) FROM T_ACCOUNT", Integer.class));

	}

	@Test
	public void testFindAccount() {
		Account account = accountManager.findAccount(ACC_123456789);
		// assert the returned account contains what you expect given the state
		// of the database
		// and the Account Hibernate mapping configuration
		assertNotNull("account should never be null", account);
		assertEquals("wrong account number", ACC_123456789, account.getNumber());
		assertEquals("wrong name", "Keith and Keri Donald", account.getName());
		assertEquals("wrong beneficiary collection size", 2, account
				.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary("Annabelle");
		assertNotNull("Annabelle should be a beneficiary", b1);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"),
				b1.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"),
				b1.getAllocationPercentage());

		Beneficiary b2 = account.getBeneficiary("Corgan");
		assertNotNull("Corgan should be a beneficiary", b2);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"),
				b2.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("50%"),
				b2.getAllocationPercentage());
	}

	@Test
	public void testFindMissingAccount() {
		try {
			accountManager.findAccount(ACC_BOGUS_NUMBER);
			fail("EmptyResultDataAccessException expected when looking up"
					+ " non-existant account " + ACC_BOGUS_NUMBER);
		} catch (EmptyResultDataAccessException e) {
			// As expected
		}
	}

	@Test
	public void testSaveAccount() throws ParseException {
		Account account = new Account(ACC_NEW_NUMBER, "John Doe");
		account.setCreditCardNumber("1111222233334444");
		account.setDateOfBirth(new SimpleDateFormat("dd MMM yyyy")
				.parse("22 May 1980"));
		account.setEmail("jdoe@somewhere.or.other.net");
		account.addBeneficiary("Jane Doe");

		account = accountManager.save(account);
		validateNewAccount(account);

		// There should now be one more account
		assertEquals("wrong number of accounts", ACCOUNTS_EXPECTED + 1,
				accountManager.findAllAccounts().size());
		
		// Fetch the persistent account and check it
		validateNewAccount(accountManager.findAccount(ACC_NEW_NUMBER));
	}

	private void validateNewAccount(Account account) {
		assertNotNull("account should never be null", account);
		assertNotNull("account should now have an entity id", account.getEntityId());
		assertEquals("wrong entity id", ACCOUNTS_EXPECTED, (int)account.getEntityId());
		assertEquals("wrong account number", ACC_NEW_NUMBER, account.getNumber());
		assertEquals("wrong name", "John Doe", account.getName());
		assertEquals("wrong beneficiary collection size", 1, account
				.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary("Jane Doe");
		assertNotNull("Jane Doe should be a beneficiary", b1);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"),
				b1.getSavings());
		assertEquals("wrong allocation percentage", Percentage.valueOf("100%"),
				b1.getAllocationPercentage());
	}

	@Test
	public void testSaveDuplicateAccount() throws ParseException {
		Account account = new Account(ACC_123456789, "John Doe");
		account.setCreditCardNumber("1111222233334444");
		account.setDateOfBirth(new SimpleDateFormat("dd MMM yyyy")
				.parse("22 May 1980"));
		account.setEmail("jdoe@somewhere.or.other.net");
		account.addBeneficiary("Jane Doe");

		try {
			accountManager.save(account);
			fail("DataIntegrityViolationException expected when creating account"
					+ " duplicate number " + ACC_123456789);
		} catch (DataIntegrityViolationException e) {
			// As expected
		}
	}

	@Test
	@DirtiesContext
	public void forceClearUp() {
		// Do nothing - this test forces Spring to throw away the cached
		// application context once all the tests are run.
	}

}
package rewards.internal.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import rewards.InvalidCreditCardException;
import rewardsonline.accounts.test.AccountTestConstants;

import common.money.MonetaryAmount;
import common.money.Percentage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:rewards/internal/account/accounts-test-config.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AccountRepositoryTests implements AccountTestConstants {

	public static final Percentage FIFTY_PERCENT = Percentage.valueOf("50%");
	public static final Percentage ONE_HUNDRED_PERCENT = Percentage.valueOf("100%");

	public static final String ACC_123456789 = AccountTestConstants.ACC_123456789;
	public static final String KEITH_AND_KERI = AccountTestConstants.KEITH_AND_KERI;
	public static final String CORGAN = "Corgan";
	public static final String ANNABELLE = "Annabelle";

	public static final String JOHN_DOE = "John Doe";
	public static final String JANE_DOE = "Jane Doe";

	public static final String TEST_CREDIT_CARD = AccountTestConstants.CC_1234123412341234;
	public static final String TEST_BAD_CREDIT_CARD = "0000000000000000";

	public static final int ACCOUNTS_EXPECTED = 21;

	//protected final Logger logger;
	protected final Log logger; 

	
	@Autowired
	protected ApplicationContext context;

	@Autowired
	protected AccountRepository repository;

	@Autowired
	protected PlatformTransactionManager txnMgr;

	public AccountRepositoryTests() {
		try {
			LogManager.getLogManager().readConfiguration(new ClassPathResource("logging.properties").getInputStream());
		} catch (Exception e) {
			// ignore
		}

		logger = LogFactory.getLog(getClass());
		//java.util.logging.Logger.getLogger("org.springframework").setLevel(Level.WARNING);
	}

	@Test
	public void verifyProfile() {
		String[] profiles = context.getEnvironment().getActiveProfiles();

		for (String profile : profiles) {
			if (profile.equals("jdbc"))
				assertTrue(repository instanceof JdbcAccountRepository);
			else if (profile.equals("jpa"))
				assertTrue(repository instanceof JpaAccountRepository);
			else if (profile.equals("hibernate"))
				assertTrue(repository instanceof HibernateAccountRepository);
			else
				fail("Unexpected profile " + profile);
		}
	}

	@Test
	@Transactional
	public void verifyFindAllAccounts() {
		logger.warn("TEST: verifyFindAllAccounts");
		List<Account> accounts = repository.findAllAccounts();
		assertEquals(ACCOUNTS_EXPECTED, accounts.size());
		Account account = accounts.get(0);
		assertEquals(KEITH_AND_KERI, account.getName());
		assertEquals(ACC_123456789, account.getNumber());
	}

	@Test
	@Transactional
	public void verifyFindAccountByNumber() {
		logger.warn("TEST: verifyFindAccountByNumber");
		Account account = repository.findByAccountNumber(ACC_123456789);
		assertEquals(KEITH_AND_KERI, account.getName());
		assertEquals(ACC_123456789, account.getNumber());
		assertEquals(2, account.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary(ANNABELLE);
		assertEquals(FIFTY_PERCENT, b1.getAllocationPercentage());
		assertEquals(MonetaryAmount.valueOf("0.0"), b1.getSavings());
		Beneficiary b2 = account.getBeneficiary(CORGAN);
		assertEquals(FIFTY_PERCENT, b2.getAllocationPercentage());
		assertEquals(MonetaryAmount.valueOf("0.0"), b2.getSavings());
	}

	@Test
	@Transactional
	public void verifyFindByCreditCard1() throws InvalidCreditCardException {
		logger.warn("TEST: verifyFindByCreditCard1");
		Account account = repository.findByCreditCard(TEST_CREDIT_CARD);
		assertEquals(KEITH_AND_KERI, account.getName());
		assertEquals(ACC_123456789, account.getNumber());
		assertEquals(2, account.getBeneficiaries().size());
	}

	@Test
	@Transactional
	public void verifyFindByCreditCard2() {
		logger.warn("TEST: verifyFindByCreditCard2");
		try {
			repository.findByCreditCard(TEST_BAD_CREDIT_CARD);
			fail("Exception expected looking for unknown card: "
					+ TEST_BAD_CREDIT_CARD);
		} catch (InvalidCreditCardException e) {
			// Success
		}
	}

	@Test @DirtiesContext
	public void verifyUpdate() {
		logger.warn("TEST: verifyUpdate");
		String newEmail = "k2@springville.us";
		int entityId = -1;
	
		// Modify account in txn 1
		TransactionStatus txn = txnMgr.getTransaction(null);
		Account account = repository.findByAccountNumber(ACC_123456789);
		assertNotNull("account should not be null", account);
		entityId = account.getEntityId();
		account.setEmail(newEmail);
		Beneficiary b1 = account.getBeneficiary(CORGAN);
		b1.credit(MonetaryAmount.valueOf("30.00"));

		repository.update(account);
		txnMgr.commit(txn);

		txn = txnMgr.getTransaction(null);
		assertNotNull("account should not be null", account);
		assertEquals("account should have same entity id", entityId, (int)account.getEntityId());
		account = repository.findByAccountNumber(ACC_123456789);
		assertEquals("Email should have changed", newEmail, account.getEmail());
		b1 = account.getBeneficiary(ANNABELLE);
		assertEquals(FIFTY_PERCENT, b1.getAllocationPercentage());
		assertEquals(MonetaryAmount.zero(), b1.getSavings());
		b1 = account.getBeneficiary(CORGAN);
		assertEquals(FIFTY_PERCENT, b1.getAllocationPercentage());
		assertEquals(MonetaryAmount.valueOf("30.0"), b1.getSavings());
		txnMgr.rollback(txn);
	}

	@Test @DirtiesContext
	public void verifyCreate() throws ParseException {
		logger.warn("TEST: verifyCreate");
		// Create new account in txn 1
		TransactionStatus txn = txnMgr.getTransaction(null);
		Account account = new Account(ACC_NEW_NUMBER, JOHN_DOE);
		account.setCreditCardNumber("1111222233334444");
		account.setDateOfBirth(new SimpleDateFormat("dd MMM yyyy")
				.parse("22 May 1980"));
		account.setEmail("jdoe@somewhere.or.other.net");
		account.addBeneficiary(JANE_DOE);

		repository.update(account);
		txnMgr.commit(txn);

		// Check account exists in txn 2
		txn = txnMgr.getTransaction(null);
		account = repository.findByAccountNumber(ACC_NEW_NUMBER);
		assertNotNull("account should never be null", account);
		assertNotNull("account should now have an entity id", account.getEntityId());
		assertEquals("wrong entity id", ACCOUNTS_EXPECTED, (int)account.getEntityId());
		assertEquals("wrong account number", ACC_NEW_NUMBER, account.getNumber());
		assertEquals("wrong name", JOHN_DOE, account.getName());
		assertEquals("wrong beneficiary collection size", 1, account
				.getBeneficiaries().size());

		Beneficiary b1 = account.getBeneficiary(JANE_DOE);
		assertNotNull("Jane Doe should be a beneficiary", b1);
		assertEquals("wrong savings", MonetaryAmount.valueOf("0.00"),
				b1.getSavings());
		assertEquals("wrong allocation percentage", ONE_HUNDRED_PERCENT,
				b1.getAllocationPercentage());
		txnMgr.rollback(txn);
	}

	@Test @DirtiesContext
	public void zzClearUp() {
		// Do nothing - this test forces Spring to throw away the cached
		// application context once all the tests are run.
	}

}

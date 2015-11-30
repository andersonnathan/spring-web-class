package rewards.internal.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import rewards.InvalidCreditCardException;
import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Loads accounts from a data source using the JDBC API.
 */
@Profile("jdbc")
@Repository("accountRespository")
public class JdbcAccountRepository implements AccountRepository {

	public final static String ACCOUNT_QUERY = //
	/* Query */"select a.ID as ID, a.NUMBER as ACCOUNT_NUMBER, a.NAME as ACCOUNT_NAME,"
			+ " a.EMAIL as EMAIL, a.CREDIT_CARD as CREDIT_CARD_NUMBER,"
			+ " a.DATE_OF_BIRTH as DATE_OF_BIRTH, a.REWARDS_NEWSLETTER as REWARDS_NEWSLETTER,"
			+ " a.MONTHLY_EMAIL_UPDATE as MONTHLY_EMAIL_UPDATE,"
			+ " b.ID AS BENEFICIARY_ID, b.NAME as BENEFICIARY_NAME,"
			+ " b.ALLOCATION_PERCENTAGE as BENEFICIARY_ALLOCATION_PERCENTAGE, b.SAVINGS as BENEFICIARY_SAVINGS "
			+ "from T_ACCOUNT a LEFT JOIN T_ACCOUNT_BENEFICIARY b ON a.ID = b.ACCOUNT_ID";

	private static Logger logger = Logger.getLogger(JdbcAccountRepository.class
			.getName());

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Extracts one Account object from rows returned from a join of T_ACCOUNT
	 * and T_ACCOUNT_BENEFICIARY.
	 */
	private ResultSetExtractor<Account> accountExtractor = new AccountExtractor();

	/**
	 * Extracts multiple Account objects from rows returned from a join of
	 * T_ACCOUNT and T_ACCOUNT_BENEFICIARY.
	 */
	private ResultSetExtractor<List<Account>> accountsExtractor = new AccountsExtractor();

	@Override
	public List<Account> findAllAccounts() {
		return jdbcTemplate.query(ACCOUNT_QUERY, accountsExtractor);
	}

	@Override
	public List<Account> findAccounts(String name, int first, int nResults) {
		StringBuilder searchString = new StringBuilder("%").append(
				name.toUpperCase()).append("%");

		return jdbcTemplate.query(ACCOUNT_QUERY
				+ " WHERE name LIKE ? LIMIT(?, ?)", accountsExtractor,
				searchString.toString(), first, first + nResults);
	}

	@Override
	public Account findByCreditCard(String creditCardNumber)
			throws InvalidCreditCardException {
		String sql = ACCOUNT_QUERY + " WHERE a.CREDIT_CARD = ?";
		try {
			return jdbcTemplate.query(sql, accountExtractor, creditCardNumber);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidCreditCardException(creditCardNumber);
		}
	}

	@Override
	public Account findByAccountNumber(String accountNumber) {
		String sql = ACCOUNT_QUERY + " WHERE a.NUMBER = ?";
		return jdbcTemplate.query(sql, accountExtractor, accountNumber);

	}

	@Override
	public void update(Account account) {
		logger.info("UPDATE ...");
		String updateSql = "update T_ACCOUNT SET NAME = ?, NUMBER = ?, DATE_OF_BIRTH = ?, "
				+ "EMAIL = ?, REWARDS_NEWSLETTER = ?, MONTHLY_EMAIL_UPDATE = ? where ID = ?";
		String createSql = "insert into T_ACCOUNT (NAME, NUMBER, DATE_OF_BIRTH, "
				+ "EMAIL, REWARDS_NEWSLETTER, MONTHLY_EMAIL_UPDATE) VALUES (?, ?, ?, ?, ?, ?)";
		String createBeneficiariesSql = "insert into T_ACCOUNT_BENEFICIARY "
				+ "(ACCOUNT_ID, NAME, ALLOCATION_PERCENTAGE, SAVINGS) VALUES(?, ?, ?, ?)";

		account.isValid(); // Ensure account is valid first

		if (account.getEntityId() == null) {
			jdbcTemplate.update(createSql, account.getName(),
					account.getNumber(), account.getDateOfBirth(),
					account.getEmail(), account.isReceiveNewsletter(),
					account.isReceiveMonthlyEmailUpdate());

		} else {
			logger.info("Account: " + account);
			jdbcTemplate.update(updateSql, account.getName(),
					account.getNumber(), account.getDateOfBirth(),
					account.getEmail(), account.isReceiveNewsletter(),
					account.isReceiveMonthlyEmailUpdate(),
					account.getEntityId());

			// Update beneficiaries by first removing any old ones
			jdbcTemplate.update(
					"DELETE FROM T_ACCOUNT_BENEFICIARY WHERE ACCOUNT_ID = ? ",
					account.getEntityId());
		}

		int entityId = jdbcTemplate.queryForObject(
				"SELECT ID FROM T_ACCOUNT WHERE NUMBER = ?", Integer.class,
				account.getNumber());
		account.setEntityId(entityId);

		// Create or restore beneficiaries
		for (Beneficiary b : account.getBeneficiaries()) {
			logger.info("Add beneficiary " + b);
			jdbcTemplate.update(createBeneficiariesSql, account.getEntityId(),
					b.getName(), b.getAllocationPercentage().asBigDecimal(), b
							.getSavings().asBigDecimal());
		}

		logger.info("Beneficiaries: "
				+ jdbcTemplate
						.queryForList("SELECT ID, ACCOUNT_ID, NAME FROM T_ACCOUNT_BENEFICIARY"));
	}

	@Override
	public void updateBeneficiaries(Account account) {
		String sql = "update T_ACCOUNT_BENEFICIARY SET SAVINGS = ? "
				+ "where ACCOUNT_ID = ? and NAME = ?";

		account.isValid(); // Ensure account is valid first

		for (Beneficiary b : account.getBeneficiaries()) {
			jdbcTemplate.update(sql, b.getSavings().asBigDecimal(),
					account.getEntityId(), b.getName());
		}
	}

	/**
	 * Extract multiple accounts from a join query against the account and
	 * beneficiary tables.
	 */
	private static class AccountsExtractor implements
			ResultSetExtractor<List<Account>> {
		Account current = null;
		Set<Beneficiary> beneficiaries = null;

		/**
		 * Map the rows returned from a join of tables: T_ACCOUNT and
		 * T_ACCOUNT_BENEFICIARY to a list of fully-reconstituted Account
		 * aggregates.
		 * 
		 * @param rs
		 *            the set of rows returned from the query
		 * @return A list of Account aggregates - never null, may be empty
		 * @throws SQLException
		 *             an exception occurred extracting data from the result set
		 * 
		 * @throws DataAccessException
		 *             if zero or multiple accounts are found.
		 */
		@Override
		public List<Account> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<Account> results = new ArrayList<Account>();

			while (rs.next()) {
				String number = rs.getString("ACCOUNT_NUMBER");

				if (current == null || !number.equals(current.getNumber())) {
					// New account?
					beneficiaries = new HashSet<Beneficiary>();
					Account account = mapAccount(rs);
					results.add(account);
					current = account;
				}

				mapBeneficiary(beneficiaries, rs);
			}

			return results;
		}
	}

	/**
	 * Extract a single account from a query against the account and beneficiary
	 * tables.
	 */
	private static class AccountExtractor implements
			ResultSetExtractor<Account> {
		/**
		 * Map the rows returned from the join of T_ACCOUNT and
		 * T_ACCOUNT_BENEFICIARY to an fully-reconstituted Account aggregate.
		 * 
		 * @param rs
		 *            the set of rows returned from the query
		 * @return the mapped Account aggregate
		 * @throws SQLException
		 *             an exception occurred extracting data from the result set
		 * 
		 * @throws DataAccessException
		 *             if zero or multiple accounts are found.
		 */
		public Account extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			if (!rs.next()) {
				throw new EmptyResultDataAccessException(1);
			}

			// build out the account object graph from the returned rows
			Set<Beneficiary> beneficiaries = new HashSet<Beneficiary>();
			Account account = mapAccount(rs);

			do {
				mapBeneficiary(beneficiaries, rs);
			} while (rs.next());

			account.setBeneficiaries(beneficiaries);
			return account;
		}
	}

	/**
	 * Maps the account columns in a single row to newly allocated Account
	 * object.
	 * 
	 * @param rs
	 *            the result set with its cursor positioned at the current row
	 * @return an allocated account
	 * @throws SQLException
	 *             an exception occurred extracting data from the result set
	 */
	private static Account mapAccount(ResultSet rs) throws SQLException {
		String number = rs.getString("ACCOUNT_NUMBER");
		String name = rs.getString("ACCOUNT_NAME");
		Date dob = new Date(rs.getDate("DATE_OF_BIRTH").getTime());
		String email = rs.getString("EMAIL");

		Account account = new Account(number, name);
		account.setDateOfBirth(dob);
		account.setEmail(email);
		account.setCreditCardNumber(rs.getString("CREDIT_CARD_NUMBER"));
		account.setReceiveNewsletter(rs.getString("REWARDS_NEWSLETTER").equals(
				"T"));
		account.setReceiveMonthlyEmailUpdate(rs.getString(
				"MONTHLY_EMAIL_UPDATE").equals("T"));

		// Set internal entity identifier (primary key)
		account.setEntityId(rs.getInt("ID"));
		return account;
	}

	/**
	 * Maps the beneficiary columns in a single row to a newly allocated
	 * Beneficiary object.
	 * 
	 * @param rs
	 *            the result set with its cursor positioned at the current row
	 * @return an allocated beneficiary
	 * @throws SQLException
	 *             an exception occurred extracting data from the result set
	 */
	private static boolean mapBeneficiary(Set<Beneficiary> beneficiaries,
			ResultSet rs) throws SQLException {
		String name = rs.getString("BENEFICIARY_NAME");

		if (name == null) {
			logger.info("No beneficiaries for account "
					+ rs.getString("ACCOUNT_NUMBER") + " "
					+ rs.getString("ACCOUNT_NAME"));
			return false; // No beneficiary for this account
		}

		MonetaryAmount savings = MonetaryAmount.valueOf(rs
				.getString("BENEFICIARY_SAVINGS"));
		Percentage allocationPercentage = Percentage.valueOf(rs
				.getString("BENEFICIARY_ALLOCATION_PERCENTAGE"));
		Beneficiary b = new Beneficiary(name, allocationPercentage, savings);

		// Set internal entity identifier (primary key)
		b.setEntityId(rs.getInt("BENEFICIARY_ID"));

		beneficiaries.add(b);
		return false;
	}

}
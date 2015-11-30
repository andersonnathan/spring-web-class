package rewards.internal.reward;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;
import rewards.internal.account.Account;

import common.money.MonetaryAmount;
import common.money.Percentage;

/**
 * Tests the JDBC reward repository with a test data source to verify data access and relational-to-object mapping
 * behavior works as expected.
 */
public class JdbcRewardRepositoryTests {

	private JdbcRewardRepository repository;

	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		dataSource = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.setName("rewards")
			.addScript("datasource/db-schema.sql")
			.addScript("datasource/db-test-data.sql")
			.build();
		repository = new JdbcRewardRepository(dataSource);
	}

	@Test
	public void testCreateReward() throws SQLException {
		Dining dining = Dining.createDining("100.00", "1234123412341234", "0123456789");

		Account account = new Account("1", "Keith and Keri Donald");

		try {
			//account.setEntityId(0);
			Method setEntityId = ReflectUtils.findDeclaredMethod(Account.class, "setEntityId", new Class[] {Integer.class});
			setEntityId.setAccessible(true);
			setEntityId.invoke(account, 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		
		account.addBeneficiary("Annabelle", Percentage.valueOf("50%"));
		account.addBeneficiary("Corgan", Percentage.valueOf("50%"));

		AccountContribution contribution = account.makeContribution(MonetaryAmount.valueOf("8.00"));
		RewardConfirmation confirmation = repository.confirmReward(contribution, dining);
		assertNotNull("confirmation should not be null", confirmation);
		assertNotNull("confirmation number should not be null", confirmation.getConfirmationNumber());
		assertEquals("wrong contribution object", contribution, confirmation.getAccountContribution());
		verifyRewardInserted(confirmation, dining);
	}

	private void verifyRewardInserted(RewardConfirmation confirmation, Dining dining) throws SQLException {
		assertEquals(1, getRewardCount());
		Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("select REWARD_AMOUNT from T_REWARD where CONFIRMATION_NUMBER = '"
				+ confirmation.getConfirmationNumber() + "'");
		rs.next();
		assertEquals(confirmation.getAccountContribution().getAmount(), MonetaryAmount.valueOf(rs.getString(1)));
	}

	private int getRewardCount() throws SQLException {
		Statement stmt = dataSource.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("select count(*) from T_REWARD");
		rs.next();
		return rs.getInt(1);
	}

}
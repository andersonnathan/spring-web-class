package rewards.internal.reward;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import rewards.AccountContribution;
import rewards.Dining;
import rewards.RewardConfirmation;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

/**
 * JDBC implementation of a reward repository that records the result of a
 * reward transaction by inserting a reward confirmation record.
 */
@Repository("rewardRepository")
public class JdbcRewardRepository implements RewardRepository {

	private JdbcTemplate jdbcTemplate;

	private RewardMapper rewardMapper = new RewardMapper();

	@Autowired
	public JdbcRewardRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public RewardConfirmation confirmReward(AccountContribution contribution,
			Dining dining) {
		String sql = "insert into T_REWARD (CONFIRMATION_NUMBER, REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?)";
		String confirmationNumber = nextConfirmationNumber();
		jdbcTemplate.update(sql, confirmationNumber, contribution.getAmount()
				.asBigDecimal(), SimpleDate.today().asDate(), contribution
				.getAccountNumber(), dining.getMerchantNumber(), dining
				.getDate().asDate(), dining.getAmount().asBigDecimal());
		return new RewardConfirmation(confirmationNumber, contribution);
	}

	@Override
	public Reward findReward(String confirmationNumber) {
		return jdbcTemplate.queryForObject(
				"select * from T_REWARD where CONFIRMATION_NUMBER = ?",
				rewardMapper, confirmationNumber);
	}

	/**
	 * Get the next confirmation number from a sequence.
	 * 
	 * @return The next reward number.
	 */
	private String nextConfirmationNumber() {
		String sql = "select next value for S_REWARD_CONFIRMATION_NUMBER from DUAL_REWARD_CONFIRMATION_NUMBER";
		return jdbcTemplate.queryForObject(sql, String.class);
	}

	/**
	 * Encapsulates the logic to map a row in a ResultSet to a Reward object.
	 */
	private static class RewardMapper implements RowMapper<Reward> {
		public Reward mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Reward(rs.getString("CONFIRMATION_NUMBER"),
					rs.getString("ACCOUNT_NUMBER"),
					rs.getString("DINING_MERCHANT_NUMBER"),
					rs.getDate("DINING_DATE"), new MonetaryAmount(
							rs.getDouble("REWARD_AMOUNT")));
		}
	}

}
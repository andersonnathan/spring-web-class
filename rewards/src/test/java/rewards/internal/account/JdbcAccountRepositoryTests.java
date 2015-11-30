package rewards.internal.account;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jdbc")
public class JdbcAccountRepositoryTests extends AccountRepositoryTests {

	public JdbcAccountRepositoryTests() {
		logger.info("Running JDBC tests");
	}

}

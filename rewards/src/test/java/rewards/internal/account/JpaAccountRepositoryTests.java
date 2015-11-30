package rewards.internal.account;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("jpa")
public class JpaAccountRepositoryTests extends AccountRepositoryTests {

	public JpaAccountRepositoryTests() {
		logger.info("Running JPA tests");
	}

}

package rewards.internal.account;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("hibernate")
public class HibernateAccountRepositoryTests extends AccountRepositoryTests {

	public HibernateAccountRepositoryTests() {
		logger.info("Running Hibernate tests");
	}

}

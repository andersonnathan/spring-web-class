package rewardsonline.rewards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import rewardsonline.accounts.test.AccountTestConstants;

/**
 * Tests the JDBC reward repository with a test data source to verify data
 * access and relational-to-object mapping behavior works as expected.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class RewardsControllerTests implements AccountTestConstants {

	@Autowired private RewardsController controller;

	@Test
	public void testShow() {
		String confirmationNumber = "1";
		Model model = new ExtendedModelMap();
		String viewName = controller.show(confirmationNumber, model);
		assertEquals("rewards/show", viewName);
		Reward reward = (Reward)model.asMap().get("reward");
		assertNotNull("rewards should not be null", reward);
		assertEquals(ACC_123456789, reward.getAccountNumber());
	}

}
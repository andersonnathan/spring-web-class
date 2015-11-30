package rewardsonline.rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rewards.internal.reward.RewardRepository;

/**
 * Implements the RewardManager service using the {@link RewardRepository}
 * underneath to fetch and manage data. Uses the DTO pattern to hide domain
 * objects from the view layer. This is good practice and, in particular, avoids
 * lazy loading problems when domain objects are loaded via an ORM.
 */
@Service("rewardService")
public class RewardManagerImpl implements RewardManager {

	private RewardRepository rewardRepository;

	@Autowired
	public RewardManagerImpl(RewardRepository rewardRepository) {
		this.rewardRepository = rewardRepository;
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc RewardManager#findReward(String)}
	 */
	@Transactional(readOnly = true)
	public Reward findReward(String confirmationNumber) {
		rewards.internal.reward.Reward reward = rewardRepository
				.findReward(confirmationNumber);

		if (reward == null)
			return null;

		// Copy domain object into a DTO and return it
		Reward dto = new Reward(reward.getConfirmationNumber(),
				reward.getAccountNumber(), reward.getMerchantNumber(),
				reward.getDate(), reward.getAmount());

		return dto;
	}

}

package rewardsonline.rewards;

import org.springframework.transaction.annotation.Transactional;

import rewardsonline.rewards.Reward;

/**
 * Manages access to member reward information.
 */
@Transactional
public interface RewardManager {
	/**
	 * Lookup an existing reward by its confirmation-number.
	 * 
	 * @param confirmationNumber
	 *            Reward confirmation number - this reward should exist.
	 * @return  The reward if found.
	 */
	public Reward findReward(String confirmationNumber);
}

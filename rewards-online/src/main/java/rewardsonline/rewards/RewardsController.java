package rewardsonline.rewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rewardsonline.rewards.RewardManager;

/**
 * Controls the execution of operations on Reward entities.
 */
@Controller
public class RewardsController {

	private RewardManager rewardManager;

	/**
	 * Creates a new RewardController
	 * 
	 * @param dataSource
	 *            the factory used to acquire database connections
	 */
	@Autowired
	public RewardsController(RewardManager rewardService) {
		this.rewardManager = rewardService;
	}

	/**
	 * Controls showing details of a reward to the user.
	 * 
	 * @param confirmationNumber
	 *            the confirmation number of the reward to display
	 * @return the Reward to show
	 */
	@RequestMapping(value="/rewards/{confirmationNumber}", method = RequestMethod.GET)
	public String show(@PathVariable String confirmationNumber, Model model) {
		model.addAttribute("reward", rewardManager.findReward(confirmationNumber));
		return "rewards/show";
	}

}
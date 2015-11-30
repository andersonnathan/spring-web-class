package rewardsonline.rewards;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controls the execution of operations on individual Reward entities.
 */
@Controller
public class RewardsController {

	/**
	 * Controls showing details of a reward to the user.
	 * 
	 * @param confirmationNumber
	 *            the confirmation number of the reward to display
	 * @return the logical view name
	 */
	@RequestMapping(value="/rewards/{confirmationNumber}", method = RequestMethod.GET)
	public String show(@PathVariable String confirmationNumber, Model model) {
		return "rewards/show";
	}

}

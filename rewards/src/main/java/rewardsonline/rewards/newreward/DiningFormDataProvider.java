package rewardsonline.rewards.newreward;

import java.util.Map;

/**
 * Fetches the data necessary to render out the enterDiningInformation form of
 * the newReward flow.
 */
public interface DiningFormDataProvider {

	/**
	 * Find all restaurants in the database.
	 * 
	 * @return a map, where each entry's key is a restaurant merchant number and
	 *         the value is a restaurant name
	 */
	Map<String, String> findAllRestaurants();
}
